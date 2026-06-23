package com.grafica.controller;

import com.grafica.dao.MaterialDAO;
import com.grafica.dao.ProdutoDAO;
import com.grafica.dao.ProdutoAcabamentoDAO;
import com.grafica.dao.ProdutoMaterialDAO;
import com.grafica.model.Material;
import com.grafica.model.Produto;
import com.grafica.model.ProdutoAcabamento;
import com.grafica.model.ProdutoMaterial;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProdutoController {
    @FXML
    private TextField nomeField;
    @FXML
    private ComboBox<String> categoriaCombo;
    @FXML
    private ComboBox<String> filtroCategoria;
    @FXML
    private TableView<Produto> produtosTable;
    @FXML
    private TableColumn<Produto, Integer> idColumn;
    @FXML
    private TableColumn<Produto, String> nomeColumn;
    @FXML
    private TableColumn<Produto, String> categoriaColumn;

    // Tabelas de associação
    @FXML private TableView<ProdutoMaterial> materiaisBaseTable;
    @FXML private TableColumn<ProdutoMaterial, String> materialBaseNomeColumn;
    @FXML private TableColumn<ProdutoMaterial, Boolean> materialBasePadraoColumn;
    @FXML private TableColumn<ProdutoMaterial, Void> materialBaseAcaoColumn;

    @FXML private TableView<ProdutoAcabamento> acabamentosTable;
    @FXML private TableColumn<ProdutoAcabamento, String> acabamentoNomeColumn;
    @FXML private TableColumn<ProdutoAcabamento, Boolean> acabamentoObrigatorioColumn;
    @FXML private TableColumn<ProdutoAcabamento, Void> acabamentoAcaoColumn;

    // Combos para adicionar
    @FXML private ComboBox<Material> comboAddMaterialBase;
    @FXML private ComboBox<Material> comboAddAcabamento;

    private ProdutoDAO produtoDAO;
    private MaterialDAO materialDAO;
    private ProdutoMaterialDAO produtoMaterialDAO;
    private ProdutoAcabamentoDAO produtoAcabamentoDAO;

    private ObservableList<Produto> todosProdutos = FXCollections.observableArrayList();
    private Produto produtoSelecionado;

    private List<Material> todosMateriais;
    private ObservableList<ProdutoMaterial> materiaisBaseAssociados = FXCollections.observableArrayList();
    private ObservableList<ProdutoAcabamento> acabamentosAssociados = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        produtoDAO = new ProdutoDAO();
        materialDAO = new MaterialDAO();
        produtoMaterialDAO = new ProdutoMaterialDAO();
        produtoAcabamentoDAO = new ProdutoAcabamentoDAO();

        categoriaCombo.setItems(FXCollections.observableArrayList(
            "IMPRESSOS", "COMUNICACAO_VISUAL", "RIGIDOS"
        ));

        filtroCategoria.setItems(FXCollections.observableArrayList(
            "TODOS", "IMPRESSOS", "COMUNICACAO_VISUAL", "RIGIDOS"
        ));
        filtroCategoria.setValue("TODOS");
        filtroCategoria.valueProperty().addListener((obs, oldVal, newVal) -> filtrarProdutos());

        // Configurar colunas da tabela de produtos
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        categoriaColumn.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        configurarControles();
        carregarDados();
    }

    private void configurarControles() {
        // Seleção na tabela de produtos
        produtosTable.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            produtoSelecionado = newV;
            preencherFormulario(newV);
            carregarAssociacoes(newV);
        });

        // Combos para adicionar materiais
        StringConverter<Material> materialConverter = new StringConverter<>() {
            @Override public String toString(Material m) { return m == null ? "" : m.getNome(); }
            @Override public Material fromString(String s) { return null; }
        };
        comboAddMaterialBase.setConverter(materialConverter);
        comboAddAcabamento.setConverter(materialConverter);

        // Tabela de materiais base
        materialBaseNomeColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(getMaterialNome(cell.getValue().getIdMaterial())));
        materialBasePadraoColumn.setCellValueFactory(new PropertyValueFactory<>("materialPadrao"));
        materialBasePadraoColumn.setCellFactory(col -> createRadioButtonCell(true));
        materialBaseAcaoColumn.setCellFactory(col -> createButtonCell("Remover", this::removerMaterialBase));

        // Tabela de acabamentos
        acabamentoNomeColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(getMaterialNome(cell.getValue().getIdMaterial())));
        acabamentoObrigatorioColumn.setCellValueFactory(new PropertyValueFactory<>("obrigatorio"));
        acabamentoObrigatorioColumn.setCellFactory(col -> createCheckBoxCell());
        acabamentoAcaoColumn.setCellFactory(col -> createButtonCell("Remover", this::removerAcabamento));

        materiaisBaseTable.setItems(materiaisBaseAssociados);
        acabamentosTable.setItems(acabamentosAssociados);
    }

    private void carregarDados() {
        todosProdutos.setAll(produtoDAO.listarTodos());
        produtosTable.setItems(todosProdutos);

        todosMateriais = materialDAO.listarTodos();
        comboAddMaterialBase.setItems(FXCollections.observableArrayList(
            todosMateriais.stream().filter(m -> "BASE".equals(m.getTipoItem())).collect(Collectors.toList())
        ));
        comboAddAcabamento.setItems(FXCollections.observableArrayList(
            todosMateriais.stream().filter(m -> "ACABAMENTO".equals(m.getTipoItem())).collect(Collectors.toList())
        ));
    }

    private void preencherFormulario(Produto produto) {
        if (produto == null) {
            limparFormulario();
            return;
        }
        nomeField.setText(produto.getNome());
        categoriaCombo.setValue(produto.getCategoria());
    }

    private void limparFormulario() {
        nomeField.clear();
        categoriaCombo.setValue(null);
        produtoSelecionado = null;
        produtosTable.getSelectionModel().clearSelection();
        materiaisBaseAssociados.clear();
        acabamentosAssociados.clear();
    }

    @FXML
    private void limpar() {
        limparFormulario();
    }

    @FXML
    private void salvarProduto() {
        if (nomeField.getText().isEmpty() || categoriaCombo.getValue() == null) {
            mostrarAlerta("Nome e Categoria são obrigatórios.");
            return;
        }

        if (produtoSelecionado == null) { // Criar
            Produto novo = new Produto();
            novo.setNome(nomeField.getText());
            novo.setCategoria(categoriaCombo.getValue());
            produtoDAO.criar(novo);
        } else { // Atualizar
            produtoSelecionado.setNome(nomeField.getText());
            produtoSelecionado.setCategoria(categoriaCombo.getValue());
            produtoDAO.atualizar(produtoSelecionado);
        }
        carregarDados();
        limparFormulario();
    }

    @FXML
    private void deletarProduto() {
        if (produtoSelecionado == null) {
            mostrarAlerta("Selecione um produto para excluir.");
            return;
        }
        produtoDAO.deletar(produtoSelecionado.getId());
        carregarDados();
        limparFormulario();
    }

    @FXML
    private void filtrarProdutos() {
        String categoria = filtroCategoria.getValue();
        if (categoria == null || "TODOS".equals(categoria)) {
            produtosTable.setItems(todosProdutos);
        } else {
            produtosTable.setItems(todosProdutos.filtered(p -> categoria.equals(p.getCategoria())));
        }
    }

    // --- Lógica de Associação ---

    private void carregarAssociacoes(Produto produto) {
        if (produto == null) {
            materiaisBaseAssociados.clear();
            acabamentosAssociados.clear();
            return;
        }
        materiaisBaseAssociados.setAll(produtoMaterialDAO.listarPorProduto(produto.getId()));
        acabamentosAssociados.setAll(produtoAcabamentoDAO.listarPorProduto(produto.getId()));
    }

    @FXML
    private void adicionarMaterialBase() {
        if (produtoSelecionado == null || comboAddMaterialBase.getValue() == null) return;
        ProdutoMaterial pm = new ProdutoMaterial(produtoSelecionado.getId(), comboAddMaterialBase.getValue().getId());
        produtoMaterialDAO.criar(pm);
        carregarAssociacoes(produtoSelecionado);
        comboAddMaterialBase.setValue(null);
    }

    @FXML
    private void adicionarAcabamento() {
        if (produtoSelecionado == null || comboAddAcabamento.getValue() == null) return;
        ProdutoAcabamento pa = new ProdutoAcabamento(produtoSelecionado.getId(), comboAddAcabamento.getValue().getId());
        produtoAcabamentoDAO.criar(pa);
        carregarAssociacoes(produtoSelecionado);
        comboAddAcabamento.setValue(null);
    }

    private void removerMaterialBase(ProdutoMaterial pm) {
        produtoMaterialDAO.deletar(pm.getIdProduto(), pm.getIdMaterial());
        carregarAssociacoes(produtoSelecionado);
    }

    private void removerAcabamento(ProdutoAcabamento pa) {
        produtoAcabamentoDAO.deletar(pa.getIdProduto(), pa.getIdMaterial());
        carregarAssociacoes(produtoSelecionado);
    }

    private void setMaterialPadrao(ProdutoMaterial pm) {
        // Desmarca todos os outros
        materiaisBaseAssociados.forEach(item -> {
            if (!item.equals(pm) && Boolean.TRUE.equals(item.getMaterialPadrao())) {
                item.setMaterialPadrao(false);
                produtoMaterialDAO.atualizar(item);
            }
        });
        // Marca o selecionado
        pm.setMaterialPadrao(true);
        produtoMaterialDAO.atualizar(pm);
        materiaisBaseTable.refresh();
    }

    private void setAcabamentoObrigatorio(ProdutoAcabamento pa, boolean isObrigatorio) {
        pa.setObrigatorio(isObrigatorio);
        produtoAcabamentoDAO.atualizar(pa);
        acabamentosTable.refresh();
    }

    // --- Helpers ---

    private String getMaterialNome(int idMaterial) {
        return todosMateriais.stream()
            .filter(m -> m.getId() == idMaterial)
            .map(Material::getNome)
            .findFirst()
            .orElse("Desconhecido");
    }

    private <T> TableCell<T, Void> createButtonCell(String label, java.util.function.Consumer<T> action) {
        return new TableCell<>() {
            private final Button button = new Button(label);
            {
                button.getStyleClass().add("materials-link-button");
                button.setOnAction(evt -> action.accept(getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : button);
            }
        };
    }

    private TableCell<ProdutoMaterial, Boolean> createRadioButtonCell(boolean isMaterialBase) {
        return new TableCell<>() {
            private final RadioButton radio = new RadioButton();
            {
                radio.setOnAction(evt -> {
                    setMaterialPadrao(getTableView().getItems().get(getIndex()));
                });
            }
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    radio.setSelected(item);
                    setGraphic(radio);
                }
            }
        };
    }

    private TableCell<ProdutoAcabamento, Boolean> createCheckBoxCell() {
        return new TableCell<>() {
            private final CheckBox check = new CheckBox();
            {
                check.setOnAction(evt -> {
                    setAcabamentoObrigatorio(getTableView().getItems().get(getIndex()), check.isSelected());
                });
            }
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    check.setSelected(item);
                    setGraphic(check);
                }
            }
        };
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}