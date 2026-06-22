package com.grafica.controller;

import com.grafica.dao.ProdutoDAO;
import com.grafica.model.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.List;

public class ProdutoController {
    @FXML
    private TextField nomeField;
    @FXML
    private ComboBox<String> categoriaCombo;
    @FXML
    private TableView<Produto> produtosTable;
    @FXML
    private TableColumn<Produto, Integer> idColumn;
    @FXML
    private TableColumn<Produto, String> nomeColumn;
    @FXML
    private TableColumn<Produto, String> categoriaColumn;
    @FXML
    private ComboBox<String> filtroCategoria;

    private ProdutoDAO produtoDAO;
    private ObservableList<Produto> produtosObservable;
    private Produto produtoSelecionado;

    @FXML
    public void initialize() {
        produtoDAO = new ProdutoDAO();

        categoriaCombo.setItems(FXCollections.observableArrayList(
            "IMPRESSOS", "COMUNICACAO_VISUAL", "RIGIDOS"
        ));

        filtroCategoria.setItems(FXCollections.observableArrayList(
            "TODOS", "IMPRESSOS", "COMUNICACAO_VISUAL", "RIGIDOS"
        ));
        filtroCategoria.setValue("TODOS");

        configurarTabela();
        carregarProdutos();
    }

    private void configurarTabela() {
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        nomeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        categoriaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategoria()));

        produtosTable.setColumnResizePolicy(
            TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );

        produtosTable.setOnMouseClicked(event -> {
            produtoSelecionado = produtosTable.getSelectionModel().getSelectedItem();
            if (produtoSelecionado != null) {
                preencherFormulario(produtoSelecionado);
            }
        });
    }

    private void carregarProdutos() {
        String filtro = filtroCategoria.getValue();
        List<Produto> lista;

        if ("TODOS".equals(filtro)) {
            lista = produtoDAO.listarTodos();
        } else {
            lista = produtoDAO.listarPorCategoria(filtro);
        }

        produtosObservable = FXCollections.observableArrayList(lista);
        produtosTable.setItems(produtosObservable);
    }

    private void preencherFormulario(Produto produto) {
        nomeField.setText(produto.getNome());
        categoriaCombo.setValue(produto.getCategoria());
    }

    private void limparFormulario() {
        nomeField.clear();
        categoriaCombo.setValue(null);
        produtoSelecionado = null;
    }

    private boolean validarDados() {
        if (nomeField.getText().isEmpty() ||
            categoriaCombo.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validação");
            alert.setHeaderText("Dados Incompletos");
            alert.setContentText("Todos os campos obrigatórios devem ser preenchidos.");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    @FXML
    private void salvarProduto() {
        if (!validarDados()) {
            return;
        }

        if (produtoSelecionado == null) {
            Produto novoProduto = new Produto();
            novoProduto.setNome(nomeField.getText());
            novoProduto.setCategoria(categoriaCombo.getValue());
            produtoDAO.criar(novoProduto);
        } else {
            produtoSelecionado.setNome(nomeField.getText());
            produtoSelecionado.setCategoria(categoriaCombo.getValue());
            produtoDAO.atualizar(produtoSelecionado);
        }

        limparFormulario();
        carregarProdutos();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText("Produto guardado com sucesso!");
        alert.showAndWait();
    }

    @FXML
    private void deletarProduto() {
        if (produtoSelecionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Seleção");
            alert.setHeaderText("Nenhum Produto Selecionado");
            alert.setContentText("Selecione um produto para deletar.");
            alert.showAndWait();
            return;
        }

        Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
        confirmar.setTitle("Confirmação");
        confirmar.setHeaderText("Deletar Produto");
        confirmar.setContentText("Tem certeza que deseja deletar este produto?");

        if (confirmar.showAndWait().get() == ButtonType.OK) {
            produtoDAO.deletar(produtoSelecionado.getId());
            limparFormulario();
            carregarProdutos();
        }
    }

    @FXML
    private void limpar() {
        limparFormulario();
    }

    @FXML
    private void filtrar() {
        carregarProdutos();
    }
}