package com.grafica.controller;

import com.grafica.dao.LayoutProdutoDAO;
import com.grafica.dao.ProdutoDAO;
import com.grafica.model.LayoutProduto;
import com.grafica.model.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.util.List;

public class GestaoLayoutsController {

    @FXML
    private ComboBox<Produto> produtoCombo;
    @FXML
    private TextField nomeLayoutField;
    @FXML
    private TextField larguraField;
    @FXML
    private TextField alturaField;
    @FXML
    private TableView<LayoutProduto> layoutsTable;
    @FXML
    private TableColumn<LayoutProduto, Integer> produtoColumn;
    @FXML
    private TableColumn<LayoutProduto, String> nomeColumn;
    @FXML
    private TableColumn<LayoutProduto, Integer> larguraColumn;
    @FXML
    private TableColumn<LayoutProduto, Integer> alturaColumn;
    @FXML
    private TableColumn<LayoutProduto, Void> acaoColumn;

    private LayoutProdutoDAO layoutDAO;
    private ProdutoDAO produtoDAO;
    private ObservableList<LayoutProduto> layoutsList = FXCollections.observableArrayList();
    private List<Produto> produtos;
    private LayoutProduto layoutEmEdicao = null;

    @FXML
    public void initialize() {
        layoutDAO = new LayoutProdutoDAO();
        produtoDAO = new ProdutoDAO();

        configurarTabela();
        configurarComboProdutos();
        carregarDados();
    }

    private void configurarTabela() {
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nomeLayout"));
        larguraColumn.setCellValueFactory(new PropertyValueFactory<>("larguraMm"));
        alturaColumn.setCellValueFactory(new PropertyValueFactory<>("alturaMm"));

        produtoColumn.setCellValueFactory(new PropertyValueFactory<>("idProduto")); // Mantém a busca pelo ID
        produtoColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer idProduto, boolean empty) {
                super.updateItem(idProduto, empty);
                if (empty || idProduto == null) {
                    setText(null);
                } else {
                    setText(produtos.stream() // Busca na lista de Produtos, não de Materiais
                        .filter(p -> p.getId().equals(idProduto))
                        .map(Produto::getNome)
                        .findFirst()
                        .orElse("Desconhecido"));
                }
            }
        });

        acaoColumn.setCellFactory(column -> new TableCell<>() {
            private final Button btnExcluir = new Button("Excluir");
            {
                btnExcluir.getStyleClass().add("settings-action-button");
                btnExcluir.getStyleClass().add("settings-danger-action");
                btnExcluir.setOnAction(event -> {
                    LayoutProduto layout = getTableView().getItems().get(getIndex());
                    excluirLayout(layout);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnExcluir);
            }
        });

        layoutsTable.setItems(layoutsList);
        layoutsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        
        layoutsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                preencherCamposParaEdicao(newVal);
            }
        });
    }

    private void configurarComboProdutos() {
        produtoCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Produto produto) {
                return produto == null ? "" : produto.getNome();
            }

            @Override
            public Produto fromString(String string) {
                return null;
            }
        });
    }

    private void carregarDados() {
        try {
            produtos = produtoDAO.listarTodos();
            produtoCombo.setItems(FXCollections.observableArrayList(produtos));
            layoutsList.setAll(layoutDAO.listarTodos());
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar dados: " + e.getMessage());
        }
    }

    @FXML
    private void salvarLayout() {
        Produto produto = produtoCombo.getValue();
        String nome = nomeLayoutField.getText();
        String larguraStr = larguraField.getText();
        String alturaStr = alturaField.getText();

        if (produto == null || nome.isEmpty() || larguraStr.isEmpty() || alturaStr.isEmpty()) {
            mostrarErro("Preencha todos os campos obrigatórios.");
            return;
        }

        try {
            int largura = Integer.parseInt(larguraStr);
            int altura = Integer.parseInt(alturaStr);

            if (layoutEmEdicao == null) {
                LayoutProduto novo = new LayoutProduto(null, produto.getId(), nome, largura, altura);
                layoutDAO.criar(novo);
                layoutsList.add(novo);
            } else {
                layoutEmEdicao.setIdProduto(produto.getId());
                layoutEmEdicao.setNomeLayout(nome);
                layoutEmEdicao.setLarguraMm(largura);
                layoutEmEdicao.setAlturaMm(altura);
                layoutDAO.atualizar(layoutEmEdicao);
                layoutsTable.refresh();
            }

            limparCampos();
        } catch (NumberFormatException e) {
            mostrarErro("Largura e Altura devem ser valores inteiros.");
        } catch (SQLException e) {
            mostrarErro("Erro ao salvar layout: " + e.getMessage());
        }
    }

    private void excluirLayout(LayoutProduto layout) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Deseja excluir este layout?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    layoutDAO.excluir(layout.getId());
                    layoutsList.remove(layout);
                    limparCampos();
                } catch (SQLException e) {
                    mostrarErro("Erro ao excluir layout: " + e.getMessage());
                }
            }
        });
    }

    private void preencherCamposParaEdicao(LayoutProduto layout) {
        layoutEmEdicao = layout;
        produtoCombo.setValue(produtos.stream().filter(p -> p.getId().equals(layout.getIdProduto())).findFirst().orElse(null));
        nomeLayoutField.setText(layout.getNomeLayout());
        larguraField.setText(layout.getLarguraMm().toString());
        alturaField.setText(layout.getAlturaMm().toString());
    }

    @FXML
    private void limparCampos() {
        layoutEmEdicao = null;
        produtoCombo.setValue(null);
        nomeLayoutField.clear();
        larguraField.clear();
        alturaField.clear();
        layoutsTable.getSelectionModel().clearSelection();
    }

    private void mostrarErro(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}