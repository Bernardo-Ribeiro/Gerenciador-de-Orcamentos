package com.grafica.controller;

import com.grafica.dao.MaterialDAO;
import com.grafica.model.Material;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.math.BigDecimal;
import java.util.List;

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
    private TextField txtMaterial;

    @FXML
    private ComboBox<String> cmbCategoria;

    @FXML
    private ComboBox<String> cmbUnidade;

    @FXML
    private TextField txtCustoBase;

    @FXML
    private TextField txtCustoProducao;

    @FXML
    private ComboBox<String> cmbStatus;

    private MaterialDAO materialDAO;
    private ObservableList<Material> materiais;
    private Material selecionado;

    @FXML
    private void initialize() {
        materialDAO = new MaterialDAO();

        cmbCategoria.setItems(FXCollections.observableArrayList("COMUNICACAO_VISUAL", "IMPRESSOS"));
        cmbUnidade.setItems(FXCollections.observableArrayList("AREA", "UNIDADE", "TIRAGEM", "FOLHA", "PACOTE", "SERVICO", "CHAPA"));
        cmbStatus.setItems(FXCollections.observableArrayList("ATIVO", "INATIVO"));

        configurarTabela();
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
        colStatus.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getStatus()));
    }

    private void carregarInsumos() {
        List<Material> lista = materialDAO.listarTodos();
        materiais = FXCollections.observableArrayList(lista);
        tableInsumos.setItems(materiais);
    }

    private void preencherFormulario(Material m) {
        txtMaterial.setText(m.getNome());
        cmbCategoria.setValue(m.getCategoria());
        cmbUnidade.setValue(m.getTipoCobranca());
        txtCustoBase.setText(m.getCustoBase() != null ? m.getCustoBase().toString() : "");
        txtCustoProducao.setText(m.getCustoProducao() != null ? m.getCustoProducao().toString() : "");
        cmbStatus.setValue(m.getStatus());
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
        cmbUnidade.setValue(null);
        txtCustoBase.clear();
        txtCustoProducao.clear();
        cmbStatus.setValue("ATIVO");
    }

    @FXML
    private void filtrarInsumos(KeyEvent event) {
        String q = txtBusca.getText().toLowerCase();
        if (q.isEmpty()) {
            tableInsumos.setItems(materiais);
            return;
        }
        ObservableList<Material> filtered = FXCollections.observableArrayList();
        for (Material m : materiais) {
            if (m.getNome().toLowerCase().contains(q)) filtered.add(m);
        }
        tableInsumos.setItems(filtered);
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
            if (txtMaterial.getText().isEmpty() || txtCustoBase.getText().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Nombre y costo base son obligatorios", ButtonType.OK);
                a.showAndWait();
                return;
            }

            if (selecionado == null) {
                Material novo = new Material(
                    txtMaterial.getText(),
                    cmbCategoria.getValue(),
                    "BASE",
                    cmbUnidade.getValue(),
                    new BigDecimal(txtCustoBase.getText())
                );
                if (!txtCustoProducao.getText().isEmpty()) novo.setCustoProducao(new BigDecimal(txtCustoProducao.getText()));
                novo.setStatus(cmbStatus.getValue());
                materialDAO.criar(novo);
            } else {
                selecionado.setNome(txtMaterial.getText());
                selecionado.setCategoria(cmbCategoria.getValue());
                selecionado.setTipoCobranca(cmbUnidade.getValue());
                selecionado.setCustoBase(new BigDecimal(txtCustoBase.getText()));
                if (!txtCustoProducao.getText().isEmpty()) selecionado.setCustoProducao(new BigDecimal(txtCustoProducao.getText()));
                selecionado.setStatus(cmbStatus.getValue());
                materialDAO.atualizar(selecionado);
            }
            carregarInsumos();
            novoInsumo();
        } catch (Exception e) {
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR, "Error al guardar insumo: " + e.getMessage(), ButtonType.OK);
            a.showAndWait();
        }
    }
}
