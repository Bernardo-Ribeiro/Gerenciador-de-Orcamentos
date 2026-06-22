package com.grafica.controller;

import com.grafica.dao.CategoriaLucroDAO;
import com.grafica.dao.MaterialDAO;
import com.grafica.model.CategoriaLucro;
import com.grafica.model.Material;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class CustosInsumosController {

    @FXML
    private TableView<Material> tableInsumos;

    @FXML
    private TableColumn<Material, String> colMaterial;

    @FXML
    private TableColumn<Material, String> colUnidade;

    @FXML
    private TableColumn<Material, BigDecimal> colCustoBase;

    @FXML
    private TableColumn<Material, String> colUltimaAtualizacao;

    @FXML
    private TableColumn<Material, String> colStatus;

    @FXML
    private TextField txtBusca;

    @FXML
    private CheckBox chkMostrarInativos;

    @FXML
    private TextField txtMaterial;

    @FXML
    private ComboBox<String> cmbCategoria;

    @FXML
    private ComboBox<String> cmbTipoItem;

    @FXML
    private ComboBox<CategoriaLucro> cmbCategoriaLucro;

    @FXML
    private ComboBox<String> cmbUnidade;

    @FXML
    private TextField txtCustoBase;

    @FXML
    private TextField txtCustoProducao;

    @FXML
    private ComboBox<String> cmbStatus;

    private MaterialDAO materialDAO;
    private CategoriaLucroDAO categoriaLucroDAO;
    private ObservableList<Material> materiais;
    private ObservableList<CategoriaLucro> categoriasLucro;
    private Material selecionado;

    @FXML
    private void initialize() {
        materialDAO = new MaterialDAO();
        categoriaLucroDAO = new CategoriaLucroDAO();

        cmbCategoria.setItems(FXCollections.observableArrayList("COMUNICACAO_VISUAL", "IMPRESSOS"));
        cmbTipoItem.setItems(FXCollections.observableArrayList("BASE", "ACABAMENTO"));
        cmbUnidade.setItems(FXCollections.observableArrayList("AREA", "UNIDADE", "TIRAGEM", "FOLHA", "PACOTE", "SERVICO", "CHAPA"));
        cmbStatus.setItems(FXCollections.observableArrayList("ATIVO", "INATIVO"));

        configurarTabela();
        configurarCombos();
        carregarInsumos();

        tableInsumos.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            selecionado = newV;
            if (selecionado != null) {
                preencherFormulario(selecionado);
            }
        });
    }

    private void configurarTabela() {
        colMaterial.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNome()));
        colUnidade.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getTipoCobranca()));
        colCustoBase.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getCustoBase()));
        colUltimaAtualizacao.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty("-"));
        colStatus.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(normalizarTexto(cell.getValue().getStatus())));

        tableInsumos.setColumnResizePolicy(
            TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );
    }

    private void configurarCombos() {
        categoriasLucro = FXCollections.observableArrayList(categoriaLucroDAO.listarTodos());
        if (cmbCategoriaLucro != null) cmbCategoriaLucro.setItems(categoriasLucro);
        cmbCategoriaLucro.setConverter(new StringConverter<>() {
            @Override
            public String toString(CategoriaLucro categoriaLucro) {
                return categoriaLucro == null ? "Nenhuma" : categoriaLucro.getNome();
            }

            @Override
            public CategoriaLucro fromString(String s) {
                return categoriasLucro.stream()
                    .filter(c -> c.getNome().equalsIgnoreCase(s))
                    .findFirst().orElse(null);
            }
        });
    }

    private void carregarInsumos() {
        List<Material> lista = materialDAO.listarTodos(true);
        materiais = FXCollections.observableArrayList(lista);
        aplicarFiltro();
    }

    private void preencherFormulario(Material m) {
        txtMaterial.setText(m.getNome());
        cmbCategoria.setValue(m.getCategoria());
        cmbTipoItem.setValue(m.getTipoItem());
        cmbUnidade.setValue(m.getTipoCobranca());
        txtCustoBase.setText(m.getCustoBase() != null ? m.getCustoBase().toString() : "");
        txtCustoProducao.setText(m.getCustoProducao() != null ? m.getCustoProducao().toString() : "");
        cmbStatus.setValue(normalizarTexto(m.getStatus()).toUpperCase());

        if (m.getIdCategoriaLucro() != null) {
            Optional<CategoriaLucro> cat = categoriasLucro.stream().filter(c -> c.getId() == m.getIdCategoriaLucro()).findFirst();
            cat.ifPresent(categoriaLucro -> cmbCategoriaLucro.setValue(categoriaLucro));
        } else {
            cmbCategoriaLucro.setValue(null);
        }
    }

    @FXML
    private void abrirHistoricoGeral() {
        System.out.println("Abrir histórico geral (no implementado)");
    }

    @FXML
    private void novoInsumo() {
        selecionado = null;
        txtMaterial.clear();
        cmbCategoria.setValue(null);
        cmbTipoItem.setValue(null);
        cmbUnidade.setValue(null);
        txtCustoBase.clear();
        cmbCategoriaLucro.setValue(null);
        txtCustoProducao.clear();
        cmbStatus.setValue("ATIVO");
        tableInsumos.getSelectionModel().clearSelection();
    }

    @FXML
    private void filtrarInsumos(KeyEvent event) {
        aplicarFiltro();
    }

    @FXML
    private void alternarMostrarInativos() {
        aplicarFiltro();
    }

    private void aplicarFiltro() {
        String q = txtBusca.getText() == null ? "" : txtBusca.getText().trim().toLowerCase();
        boolean mostrarInativos = chkMostrarInativos != null && chkMostrarInativos.isSelected();
        ObservableList<Material> filtrados = FXCollections.observableArrayList();

        for (Material m : materiais) {
            if (!mostrarInativos && "INATIVO".equalsIgnoreCase(m.getStatus())) {
                continue;
            }
            String nome = m.getNome() != null ? m.getNome().toLowerCase() : "";
            String categoria = m.getCategoria() != null ? m.getCategoria().toLowerCase() : "";
            String unidade = m.getTipoCobranca() != null ? m.getTipoCobranca().toLowerCase() : "";
            if (!q.isEmpty() && !(nome.contains(q) || categoria.contains(q) || unidade.contains(q))) {
                continue;
            }
            filtrados.add(m);
        }

        tableInsumos.setItems(filtrados);
    }

    @FXML
    private void abrirFiltros() {
        System.out.println("Abrir filtros (no implementado)");
    }

    @FXML
    private void exportarCsv() {
        System.out.println("Exportar CSV (no implementado)");
    }

    @FXML
    private void excluirSelecionado() {
        Material m = tableInsumos.getSelectionModel().getSelectedItem();
        if (m == null) return;
        materialDAO.deletar(m.getId());
        carregarInsumos();
        novoInsumo();
    }

    @FXML
    private void salvarInsumo() {
        try {
            if (txtMaterial.getText() == null || txtMaterial.getText().trim().isEmpty() || txtCustoBase.getText() == null || txtCustoBase.getText().trim().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Nome e custo base são obrigatórios.", ButtonType.OK);
                a.showAndWait();
                return;
            }

            if (cmbCategoria.getValue() == null || cmbTipoItem.getValue() == null || cmbUnidade.getValue() == null || cmbStatus.getValue() == null) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Categoria, tipo, unidade e status são obrigatórios", ButtonType.OK);
                a.showAndWait();
                return;
            }

            BigDecimal custoBase = new BigDecimal(txtCustoBase.getText().trim().replace(',', '.'));
            BigDecimal custoProducao = parseDecimalOpcional(txtCustoProducao.getText());
            CategoriaLucro categoriaLucro = cmbCategoriaLucro.getValue();

            if (selecionado == null) {
                Material novo = new Material(
                    txtMaterial.getText().trim(),
                    cmbCategoria.getValue(),
                    cmbTipoItem.getValue(),
                    cmbUnidade.getValue(),
                    custoBase
                );
                novo.setCustoProducao(custoProducao);
                novo.setStatus(cmbStatus.getValue().toUpperCase());
                if (categoriaLucro != null) {
                    novo.setIdCategoriaLucro(categoriaLucro.getId());
                }
                materialDAO.criar(novo);
            } else {
                selecionado.setNome(txtMaterial.getText().trim());
                selecionado.setCategoria(cmbCategoria.getValue());
                selecionado.setTipoItem(cmbTipoItem.getValue());
                selecionado.setTipoCobranca(cmbUnidade.getValue());
                selecionado.setCustoBase(custoBase);
                selecionado.setCustoProducao(custoProducao);
                selecionado.setStatus(cmbStatus.getValue().toUpperCase());
                if (categoriaLucro != null) {
                    selecionado.setIdCategoriaLucro(categoriaLucro.getId());
                } else {
                    selecionado.setIdCategoriaLucro(null);
                }
                materialDAO.atualizar(selecionado);
            }
            carregarInsumos();
            novoInsumo();
        } catch (Exception e) {
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR, "Erro ao salvar insumo: " + e.getMessage(), ButtonType.OK);
            a.showAndWait();
        }
    }

    private BigDecimal parseDecimalOpcional(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(valor.trim().replace(',', '.'));
    }

    private String normalizarTexto(String valor) {
        return valor == null ? "" : valor;
    }
}
