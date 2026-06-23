package com.grafica.controller;

import com.grafica.dao.CategoriaLucroDAO;
import com.grafica.model.CategoriaLucro;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

public class MargensLucroController implements Initializable {

    @FXML private VBox categoriasContainer;
    @FXML private Label lblCount;
    @FXML private HBox alertSuccess;
    @FXML private VBox novoCategoriaForm;
    @FXML private TextField txtNovaCategoriaNome;
    @FXML private TextField txtNovaCategoriaMargem;
    @FXML private TextField txtNovaCategoriaDescricao;

    private CategoriaLucroDAO categoriaDAO = new CategoriaLucroDAO();
    private Map<CategoriaLucro, TextField> fieldMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carregarCategorias();
    }

    private void carregarCategorias() {
        categoriasContainer.getChildren().clear();
        fieldMap.clear();
        List<CategoriaLucro> categorias = categoriaDAO.listarTodos();
        lblCount.setText(categorias.size() + " categorias listadas");

        for (int i = 0; i < categorias.size(); i++) {
            CategoriaLucro cat = categorias.get(i);
            boolean isLast = (i == categorias.size() - 1);
            HBox row = criarLinhaCategoria(cat, isLast);
            categoriasContainer.getChildren().add(row);
        }
    }

    private HBox criarLinhaCategoria(CategoriaLucro cat, boolean isLast) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("profit-row");
        if (isLast) {
            row.getStyleClass().add("profit-row-last");
        }

        CheckBox checkBox = new CheckBox();
        checkBox.getStyleClass().add("profit-checkbox");

        VBox infoVBox = new VBox(4);
        HBox.setHgrow(infoVBox, Priority.ALWAYS);

        HBox titleHBox = new HBox(6);
        titleHBox.setAlignment(Pos.CENTER_LEFT);
        Label lblNome = new Label(cat.getNome());
        lblNome.getStyleClass().add("profit-row-title");
        Label lblId = new Label("ID: " + String.format("%02d", cat.getId()));
        lblId.getStyleClass().addAll("profit-pill", "profit-pill-neutral");
        titleHBox.getChildren().addAll(lblNome, lblId);

        Label lblDesc = new Label(cat.getDescricao());
        lblDesc.getStyleClass().add("profit-row-subtitle");
        infoVBox.getChildren().addAll(titleHBox, lblDesc);

        HBox inputHBox = new HBox(6);
        inputHBox.setAlignment(Pos.CENTER_LEFT);
        TextField txtMargem = new TextField(cat.getMargemPadrao().toString());
        txtMargem.getStyleClass().add("settings-input");
        txtMargem.setPrefWidth(70);
        Label lblPercent = new Label("%");
        lblPercent.getStyleClass().add("profit-percent-suffix");
        inputHBox.getChildren().addAll(txtMargem, lblPercent);
        fieldMap.put(cat, txtMargem);

        HBox actionsHBox = new HBox(8);
        actionsHBox.setAlignment(Pos.CENTER_RIGHT);
        Button btnEditar = new Button("Editar");
        btnEditar.getStyleClass().add("profit-action-button");
        Button btnExcluir = new Button("Excluir");
        btnExcluir.getStyleClass().addAll("profit-action-button", "profit-action-danger");
        btnExcluir.setOnAction(e -> excluirCategoria(cat));
        actionsHBox.getChildren().addAll(btnEditar, btnExcluir);

        row.getChildren().addAll(checkBox, infoVBox, inputHBox, actionsHBox);
        return row;
    }

    @FXML
    private void novaCategoria() {
        txtNovaCategoriaNome.clear();
        txtNovaCategoriaMargem.setText("50,00");
        txtNovaCategoriaDescricao.clear();
        novoCategoriaForm.setVisible(true);
        novoCategoriaForm.setManaged(true);
    }

    @FXML
    private void cancelarNovaCategoria() {
        novoCategoriaForm.setVisible(false);
        novoCategoriaForm.setManaged(false);
    }

    @FXML
    private void salvarNovaCategoria() {
        String nome = txtNovaCategoriaNome.getText().trim();
        if (nome.isEmpty()) {
            mostrarErro("Informe o nome da categoria.");
            return;
        }

        String margemStr = txtNovaCategoriaMargem.getText().trim().replace(',', '.');
        BigDecimal margem;
        try {
            margem = new BigDecimal(margemStr);
        } catch (NumberFormatException e) {
            mostrarErro("Valor de margem inválido.");
            return;
        }

        String descricao = txtNovaCategoriaDescricao.getText().trim();
        if (descricao.isEmpty()) {
            descricao = "Nova categoria cadastrada";
        }

        CategoriaLucro nova = new CategoriaLucro();
        nova.setNome(nome);
        nova.setDescricao(descricao);
        nova.setMargemPadrao(margem);

        if (categoriaDAO.salvar(nova)) {
            novoCategoriaForm.setVisible(false);
            novoCategoriaForm.setManaged(false);
            carregarCategorias();
            mostrarSucesso();
        } else {
            mostrarErro("Erro ao salvar nova categoria.");
        }
    }

    @FXML
    private void salvarPadroes() {
        boolean sucesso = true;
        for (Map.Entry<CategoriaLucro, TextField> entry : fieldMap.entrySet()) {
            CategoriaLucro cat = entry.getKey();
            String valorStr = entry.getValue().getText().replace(',', '.');
            try {
                BigDecimal novaMargem = new BigDecimal(valorStr);
                cat.setMargemPadrao(novaMargem);
                if (!categoriaDAO.atualizar(cat)) {
                    sucesso = false;
                }
            } catch (NumberFormatException e) {
                mostrarErro("Valor inválido para a categoria: " + cat.getNome());
                return;
            }
        }

        if (sucesso) {
            mostrarSucesso();
        } else {
            mostrarErro("Algumas categorias não puderam ser atualizadas.");
        }
    }

    @FXML
    private void cancelarAlteracoes() {
        carregarCategorias();
        ocultarAlerta();
    }

    @FXML
    private void ocultarAlerta() {
        alertSuccess.setVisible(false);
        alertSuccess.setManaged(false);
    }

    private void mostrarSucesso() {
        alertSuccess.setVisible(true);
        alertSuccess.setManaged(true);
    }

    private void excluirCategoria(CategoriaLucro cat) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Excluir Categoria");
        alert.setHeaderText("Deseja realmente excluir a categoria?");
        alert.setContentText(cat.getNome());

        if (alert.showAndWait().get() == ButtonType.OK) {
            if (categoriaDAO.excluir(cat.getId())) {
                carregarCategorias();
                mostrarSucesso();
            } else {
                mostrarErro("Erro ao excluir categoria.");
            }
        }
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
