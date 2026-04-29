package com.grafica.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Locale;

public class CustosInsumosController {
    private static final DecimalFormat MONEY_FORMAT = (DecimalFormat) DecimalFormat.getCurrencyInstance(new Locale("pt", "BR"));

    @FXML
    private TableView<InsumoRegistro> tableInsumos;

    @FXML
    private TableColumn<InsumoRegistro, String> colMaterial;

    @FXML
    private TableColumn<InsumoRegistro, String> colUnidade;

    @FXML
    private TableColumn<InsumoRegistro, String> colCustoBase;

    @FXML
    private TableColumn<InsumoRegistro, String> colUltimaAtualizacao;

    @FXML
    private TableColumn<InsumoRegistro, String> colStatus;

    @FXML
    private TextField txtBusca;

    @FXML
    private TextField txtMaterial;

    @FXML
    private ComboBox<String> cmbCategoria;

    @FXML
    private ComboBox<String> cmbUnidade;

    @FXML
    private ComboBox<String> cmbStatus;

    @FXML
    private TextField txtCustoBase;

    @FXML
    private TextField txtCustoProducao;

    @FXML
    private Button btnSalvarInsumo;

    private final ObservableList<InsumoRegistro> registros = FXCollections.observableArrayList();
    private FilteredList<InsumoRegistro> registrosFiltrados;
    private InsumoRegistro registroSelecionado;

    @FXML
    private void initialize() {
        configurarTabela();
        configurarCombos();
        carregarDadosExemplo();
        configurarSelecaoDaTabela();
        novoInsumo();
    }

    private void configurarTabela() {
        colMaterial.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaterial()));
        colUnidade.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUnidade()));
        colCustoBase.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCustoBase()));
        colUltimaAtualizacao.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUltimaAtualizacao()));
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

        registrosFiltrados = new FilteredList<>(registros, registro -> true);
        tableInsumos.setItems(registrosFiltrados);
    }

    private void configurarCombos() {
        cmbCategoria.setItems(FXCollections.observableArrayList(
                "Comunicação Visual",
                "Materiais Impressos"
        ));
        cmbUnidade.setItems(FXCollections.observableArrayList(
                "Milheiro",
                "m2",
                "Chapa",
                "Unidade"
        ));
        cmbStatus.setItems(FXCollections.observableArrayList(
                "Ativo",
                "Desatualizado",
                "Inativo"
        ));
    }

    private void carregarDadosExemplo() {
        registros.setAll(
                new InsumoRegistro("Papel Couché 150g", "Milheiro", formatarMoeda(145.50), "2024-05-10", "Ativo", "Comunicação Visual", formatarMoeda(12.00)),
                new InsumoRegistro("Papel Sulfite 90g", "Milheiro", formatarMoeda(89.20), "2023-12-15", "Desatualizado", "Materiais Impressos", formatarMoeda(7.90)),
                new InsumoRegistro("Adesivo Vinil Branco Brilho", "m2", formatarMoeda(320.00), "2024-04-20", "Ativo", "Comunicação Visual", formatarMoeda(35.00)),
                new InsumoRegistro("ACM", "Chapa", formatarMoeda(215.00), "2024-03-05", "Desatualizado", "Comunicação Visual", formatarMoeda(28.00)),
                new InsumoRegistro("Lona Front Light 440g", "m²", formatarMoeda(12.80), "2024-05-02", "Ativo", "Comunicação Visual", formatarMoeda(1.50))
        );
    }

    private void configurarSelecaoDaTabela() {
        tableInsumos.getSelectionModel().selectedItemProperty().addListener((observable, anterior, selecionado) -> {
            registroSelecionado = selecionado;
            preencherFormulario(selecionado);
        });
    }

    @FXML
    private void novoInsumo() {
        registroSelecionado = null;
        tableInsumos.getSelectionModel().clearSelection();
        txtMaterial.clear();
        cmbCategoria.getSelectionModel().clearSelection();
        cmbUnidade.getSelectionModel().clearSelection();
        cmbStatus.getSelectionModel().clearSelection();
        txtCustoBase.clear();
        txtCustoProducao.clear();
        btnSalvarInsumo.setText("Salvar");
    }

    @FXML
    private void salvarInsumo() {
        String material = textoLimpo(txtMaterial.getText());
        String categoria = cmbCategoria.getValue();
        String unidade = cmbUnidade.getValue();
        String status = cmbStatus.getValue();
        String custoBase = textoLimpo(txtCustoBase.getText());
        String custoProducao = textoLimpo(txtCustoProducao.getText());

        if (material.isEmpty() || categoria == null || unidade == null || status == null || custoBase.isEmpty()) {
            mostrarMensagem("Preencha material, categoria, unidade, status e custo base antes de salvar.");
            return;
        }

        if (registroSelecionado == null) {
            registros.add(new InsumoRegistro(
                    material,
                    unidade,
                    formatarMoeda(parseMoeda(custoBase)),
                    LocalDate.now().toString(),
                    status,
                    categoria,
                    custoProducao.isEmpty() ? formatarMoeda(0) : formatarMoeda(parseMoeda(custoProducao))
            ));
        } else {
            registroSelecionado.setMaterial(material);
            registroSelecionado.setCategoria(categoria);
            registroSelecionado.setUnidade(unidade);
            registroSelecionado.setStatus(status);
            registroSelecionado.setCustoBase(formatarMoeda(parseMoeda(custoBase)));
            registroSelecionado.setCustoProducao(custoProducao.isEmpty() ? formatarMoeda(0) : formatarMoeda(parseMoeda(custoProducao)));
            registroSelecionado.setUltimaAtualizacao(LocalDate.now().toString());
            tableInsumos.refresh();
        }

        aplicarFiltro(txtBusca.getText());
        novoInsumo();
    }

    @FXML
    private void excluirSelecionado() {
        if (registroSelecionado == null) {
            mostrarMensagem("Selecione um item na tabela para excluir.");
            return;
        }

        registros.remove(registroSelecionado);
        novoInsumo();
        aplicarFiltro(txtBusca.getText());
    }

    @FXML
    private void filtrarInsumos() {
        aplicarFiltro(txtBusca.getText());
    }

    @FXML
    private void abrirHistoricoGeral() {
        mostrarMensagem("Histórico geral será conectado ao módulo de auditoria.");
    }

    @FXML
    private void abrirFiltros() {
        mostrarMensagem("Filtros avançados serão adicionados na próxima etapa.");
    }

    @FXML
    private void exportarCsv() {
        mostrarMensagem("Exportação CSV será ligada ao DAO mais adiante.");
    }

    private void aplicarFiltro(String textoBusca) {
        String filtro = textoLimpo(textoBusca).toLowerCase();
        registrosFiltrados.setPredicate(registro -> filtro.isEmpty() || registro.getMaterial().toLowerCase().contains(filtro));
    }

    private void preencherFormulario(InsumoRegistro registro) {
        if (registro == null) {
            return;
        }

        txtMaterial.setText(registro.getMaterial());
        cmbCategoria.setValue(registro.getCategoria());
        cmbUnidade.setValue(registro.getUnidade());
        cmbStatus.setValue(registro.getStatus());
        txtCustoBase.setText(registro.getCustoBase());
        txtCustoProducao.setText(registro.getCustoProducao());
        btnSalvarInsumo.setText("Atualizar");
    }

    private void mostrarMensagem(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Custos de Insumos");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private String textoLimpo(String texto) {
        return texto == null ? "" : texto.trim();
    }

    private double parseMoeda(String valor) {
        String normalizado = valor.replace("R$", "").replace(".", "").replace(",", ".").trim();
        return Double.parseDouble(normalizado);
    }

    private String formatarMoeda(double valor) {
        return MONEY_FORMAT.format(valor);
    }

    public static class InsumoRegistro {
        private String material;
        private String unidade;
        private String custoBase;
        private String ultimaAtualizacao;
        private String status;
        private String categoria;
        private String custoProducao;

        public InsumoRegistro(String material, String unidade, String custoBase, String ultimaAtualizacao, String status, String categoria, String custoProducao) {
            this.material = material;
            this.unidade = unidade;
            this.custoBase = custoBase;
            this.ultimaAtualizacao = ultimaAtualizacao;
            this.status = status;
            this.categoria = categoria;
            this.custoProducao = custoProducao;
        }

        public String getMaterial() {
            return material;
        }

        public void setMaterial(String material) {
            this.material = material;
        }

        public String getUnidade() {
            return unidade;
        }

        public void setUnidade(String unidade) {
            this.unidade = unidade;
        }

        public String getCustoBase() {
            return custoBase;
        }

        public void setCustoBase(String custoBase) {
            this.custoBase = custoBase;
        }

        public String getUltimaAtualizacao() {
            return ultimaAtualizacao;
        }

        public void setUltimaAtualizacao(String ultimaAtualizacao) {
            this.ultimaAtualizacao = ultimaAtualizacao;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCategoria() {
            return categoria;
        }

        public void setCategoria(String categoria) {
            this.categoria = categoria;
        }

        public String getCustoProducao() {
            return custoProducao;
        }

        public void setCustoProducao(String custoProducao) {
            this.custoProducao = custoProducao;
        }
    }
}