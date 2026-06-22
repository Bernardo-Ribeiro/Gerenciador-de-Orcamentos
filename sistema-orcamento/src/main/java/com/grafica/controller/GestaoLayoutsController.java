package com.grafica.controller;

import com.grafica.dao.LayoutProdutoDAO;
import com.grafica.dao.MaterialDAO;
import com.grafica.model.LayoutProduto;
import com.grafica.model.Material;
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
    private ComboBox<Material> materialCombo;
    @FXML
    private TextField nomeLayoutField;
    @FXML
    private TextField larguraField;
    @FXML
    private TextField alturaField;
    @FXML
    private TableView<LayoutProduto> layoutsTable;
    @FXML
    private TableColumn<LayoutProduto, Integer> materialColumn;
    @FXML
    private TableColumn<LayoutProduto, String> nomeColumn;
    @FXML
    private TableColumn<LayoutProduto, Integer> larguraColumn;
    @FXML
    private TableColumn<LayoutProduto, Integer> alturaColumn;
    @FXML
    private TableColumn<LayoutProduto, Void> acaoColumn;

    private LayoutProdutoDAO layoutDAO;
    private MaterialDAO materialDAO;
    private ObservableList<LayoutProduto> layoutsList = FXCollections.observableArrayList();
    private List<Material> materiais;
    private LayoutProduto layoutEmEdicao = null;

    @FXML
    public void initialize() {
        layoutDAO = new LayoutProdutoDAO();
        materialDAO = new MaterialDAO();

        configurarTabela();
        configurarComboMateriais();
        carregarDados();
    }

    private void configurarTabela() {
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nomeLayout"));
        larguraColumn.setCellValueFactory(new PropertyValueFactory<>("larguraMm"));
        alturaColumn.setCellValueFactory(new PropertyValueFactory<>("alturaMm"));

        materialColumn.setCellValueFactory(new PropertyValueFactory<>("idProduto"));
        materialColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer idProduto, boolean empty) {
                super.updateItem(idProduto, empty);
                if (empty || idProduto == null) {
                    setText(null);
                } else {
                    setText(materiais.stream()
                        .filter(m -> m.getId().equals(idProduto))
                        .map(Material::getNome)
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

    private void configurarComboMateriais() {
        materialCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Material material) {
                return material == null ? "" : material.getNome();
            }

            @Override
            public Material fromString(String string) {
                return null;
            }
        });
    }

    private void carregarDados() {
        try {
            materiais = materialDAO.listarTodos();
            materialCombo.setItems(FXCollections.observableArrayList(materiais));
            layoutsList.setAll(layoutDAO.listarTodos());
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar dados: " + e.getMessage());
        }
    }

    @FXML
    private void salvarLayout() {
        Material material = materialCombo.getValue();
        String nome = nomeLayoutField.getText();
        String larguraStr = larguraField.getText();
        String alturaStr = alturaField.getText();

        if (material == null || nome.isEmpty() || larguraStr.isEmpty() || alturaStr.isEmpty()) {
            mostrarErro("Preencha todos os campos obrigatórios.");
            return;
        }

        try {
            int largura = Integer.parseInt(larguraStr);
            int altura = Integer.parseInt(alturaStr);

            if (layoutEmEdicao == null) {
                LayoutProduto novo = new LayoutProduto(null, material.getId(), nome, largura, altura);
                layoutDAO.criar(novo);
                layoutsList.add(novo);
            } else {
                layoutEmEdicao.setIdProduto(material.getId());
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
        materialCombo.setValue(materiais.stream().filter(m -> m.getId().equals(layout.getIdProduto())).findFirst().orElse(null));
        nomeLayoutField.setText(layout.getNomeLayout());
        larguraField.setText(layout.getLarguraMm().toString());
        alturaField.setText(layout.getAlturaMm().toString());
    }

    @FXML
    private void limparCampos() {
        layoutEmEdicao = null;
        materialCombo.setValue(null);
        nomeLayoutField.clear();
        larguraField.clear();
        alturaField.clear();
        layoutsTable.getSelectionModel().clearSelection();
    }

    private void mostrarErro(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}
