package com.grafica.controller;

import com.grafica.dao.ClienteDAO;
import com.grafica.dao.OrcamentoDAO;
import com.grafica.model.Cliente;
import com.grafica.model.Orcamento;
import com.grafica.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML
    private Label tituloLabel;

    @FXML
    private Label subtituloLabel;

    @FXML
    private Label vendasTotaisLabel;

    @FXML
    private Label orcamentosPendentesLabel;

    @FXML
    private Label taxaConversaoLabel;

    @FXML
    private Label novosClientesLabel;

    @FXML
    private TableView<OrcamentoRow> tabelaUltimosOrcamentos;

    @FXML
    private TableColumn<OrcamentoRow, String> colId;

    @FXML
    private TableColumn<OrcamentoRow, String> colCliente;

    @FXML
    private TableColumn<OrcamentoRow, String> colData;

    @FXML
    private TableColumn<OrcamentoRow, String> colValor;

    @FXML
    private TableColumn<OrcamentoRow, String> colStatus;

    @FXML
    private TableColumn<OrcamentoRow, Void> colAcoes;

    @FXML
    private VBox alertasContainer;

    @FXML
    private Label metaDiariaLabel;

    @FXML
    private Button btnNovoOrcamento;

    @FXML
    private Button btnClientes;

    @FXML
    private Button btnMateriais;

    @FXML
    private Button btnRelatorios;

    @FXML
    private Button btnHistoricoCompleto;

    @FXML
    private Button btnConfiguracoes;

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final OrcamentoDAO orcamentoDAO = new OrcamentoDAO();
    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private static final DateTimeFormatter DATA_LABEL_FORMATTER = DateTimeFormatter.ofPattern("dd/MM");
    private static final int META_DIARIA_ORCAMENTOS = 5;

    private MainController mainController;
    private Usuario usuarioLogado;

    @FXML
    private void initialize() {
        configurarTabela();
        atualizarPainel();
    }

    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnDetalhes = new Button("Detalhes");
            {
                btnDetalhes.getStyleClass().add("dashboard-link");
                btnDetalhes.setOnAction(event -> navegarParaOrcamentos());
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnDetalhes);
                }
            }
        });

        tabelaUltimosOrcamentos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        configurarAcoesRapidas();
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        atualizarCabecalho();
        atualizarPainel();
    }

    private void configurarAcoesRapidas() {
        if (btnNovoOrcamento != null) {
            btnNovoOrcamento.setOnAction(event -> navegarParaOrcamentos());
        }
        if (btnClientes != null) {
            btnClientes.setOnAction(event -> navegarParaClientes());
        }
        if (btnMateriais != null) {
            btnMateriais.setOnAction(event -> navegarParaMateriais());
        }
        if (btnRelatorios != null) {
            btnRelatorios.setOnAction(event -> navegarParaRelatorios());
        }
        if (btnHistoricoCompleto != null) {
            btnHistoricoCompleto.setOnAction(event -> navegarParaRelatorios());
        }
        if (btnConfiguracoes != null) {
            btnConfiguracoes.setOnAction(event -> navegarParaAjustes());
        }
    }

    private void atualizarCabecalho() {
        if (tituloLabel != null) {
            String nome = usuarioLogado != null && usuarioLogado.getNome() != null && !usuarioLogado.getNome().isBlank()
                ? usuarioLogado.getNome()
                : "Gráfica";
            tituloLabel.setText("Olá, " + nome + "!");
        }
        if (subtituloLabel != null) {
            subtituloLabel.setText("Aqui está o que está acontecendo com seus orçamentos hoje.");
        }
    }

    private void atualizarPainel() {
        try {
            List<Orcamento> orcamentos = orcamentoDAO.listarTodos();
            List<Cliente> clientes = clienteDAO.listarTodos();
            Map<Integer, String> nomesClientes = clientes.stream()
                .filter(cliente -> cliente.getId() != null)
                .collect(Collectors.toMap(Cliente::getId, Cliente::getNome, (atual, substituto) -> atual));

            BigDecimal totalVendas = orcamentos.stream()
                .map(orcamento -> orcamento.getValorFinal() != null ? orcamento.getValorFinal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            long pendentes = orcamentos.stream()
                .filter(orcamento -> "PENDENTE".equalsIgnoreCase(orcamento.getStatus()))
                .count();

            long concluidos = orcamentos.stream()
                .filter(orcamento -> isConcluido(orcamento.getStatus()))
                .count();

            long clientesNovos = clientes.stream()
                .filter(cliente -> cliente.getDataCadastro() != null && cliente.getDataCadastro().isAfter(java.time.LocalDateTime.now().minusDays(30)))
                .count();

            if (vendasTotaisLabel != null) {
                vendasTotaisLabel.setText(moeda.format(totalVendas));
            }
            if (orcamentosPendentesLabel != null) {
                orcamentosPendentesLabel.setText(String.valueOf(pendentes));
            }
            if (taxaConversaoLabel != null) {
                BigDecimal taxa = orcamentos.isEmpty()
                    ? BigDecimal.ZERO
                    : BigDecimal.valueOf(concluidos)
                        .multiply(BigDecimal.valueOf(100))
                        .divide(BigDecimal.valueOf(orcamentos.size()), 1, RoundingMode.HALF_UP);
                taxaConversaoLabel.setText(taxa.stripTrailingZeros().toPlainString() + "%");
            }
            if (novosClientesLabel != null) {
                novosClientesLabel.setText(String.valueOf(clientesNovos));
            }

            atualizarUltimosOrcamentos(orcamentos, nomesClientes);
            atualizarAlertasCriticos(orcamentos, nomesClientes);
            atualizarMetaDiaria(orcamentos);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar dashboard: " + e.getMessage());
        }
    }

    private void atualizarUltimosOrcamentos(List<Orcamento> orcamentos, Map<Integer, String> nomesClientes) {
        if (tabelaUltimosOrcamentos == null) {
            return;
        }

        List<Orcamento> ultimos = orcamentos.stream()
            .sorted(Comparator
                .comparing(Orcamento::getDataEmissao, Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(Orcamento::getId, Comparator.nullsLast(Comparator.reverseOrder())))
            .limit(5)
            .toList();

        ObservableList<OrcamentoRow> rows = FXCollections.observableArrayList();
        for (Orcamento o : ultimos) {
            String cliente = nomesClientes.getOrDefault(o.getIdCliente(), "Cliente não identificado");
            String id = formatarCodigoOrcamento(o.getId());
            String data = formatarDataResumo(o.getDataEmissao());
            String valor = moeda.format(o.getValorFinal() != null ? o.getValorFinal() : BigDecimal.ZERO);
            String status = formatarStatus(o.getStatus());
            rows.add(new OrcamentoRow(id, cliente, data, valor, status));
        }

        tabelaUltimosOrcamentos.setItems(rows);
    }

    private void atualizarAlertasCriticos(List<Orcamento> orcamentos, Map<Integer, String> nomesClientes) {
        if (alertasContainer == null) {
            return;
        }

        alertasContainer.getChildren().clear();
        LocalDate hoje = LocalDate.now();

        List<Orcamento> alertas = orcamentos.stream()
            .filter(orcamento -> "PENDENTE".equalsIgnoreCase(orcamento.getStatus()))
            .sorted(Comparator.comparing(Orcamento::getDataValidade, Comparator.nullsLast(Comparator.naturalOrder())))
            .limit(3)
            .toList();

        if (alertas.isEmpty()) {
            alertasContainer.getChildren().add(criarAlerta("OK", "Nenhum alerta crítico no momento.", "Atualizado agora"));
            return;
        }

        for (Orcamento orcamento : alertas) {
            LocalDate validade = orcamento.getDataValidade();
            String cliente = nomesClientes.getOrDefault(orcamento.getIdCliente(), "cliente sem nome");
            String tag;
            String tempo;

            if (validade == null) {
                tag = "ATENÇÃO";
                tempo = "Sem data de validade";
            } else if (validade.isBefore(hoje)) {
                tag = "URGENTE";
                tempo = "Vencido há " + (hoje.toEpochDay() - validade.toEpochDay()) + " dia(s)";
            } else if (!validade.isAfter(hoje.plusDays(2))) {
                tag = "REVISÃO NECESSÁRIA";
                tempo = "Vence em " + (validade.toEpochDay() - hoje.toEpochDay()) + " dia(s)";
            } else {
                tag = "LOGÍSTICA";
                tempo = "Validade: " + validade.format(DATA_LABEL_FORMATTER);
            }

            String texto = "Orçamento " + formatarCodigoOrcamento(orcamento.getId()) + " de " + cliente + " está pendente.";
            alertasContainer.getChildren().add(criarAlerta(tag, texto, tempo));
        }
    }

    private VBox criarAlerta(String tag, String texto, String tempo) {
        VBox alerta = new VBox(8.0);
        alerta.getStyleClass().add("dashboard-alert");

        Label tagLabel = new Label(tag);
        tagLabel.getStyleClass().add("dashboard-alert-tag");

        Label textoLabel = new Label(texto);
        textoLabel.getStyleClass().add("dashboard-alert-text");
        textoLabel.setWrapText(true);

        Label tempoLabel = new Label(tempo);
        tempoLabel.getStyleClass().add("dashboard-alert-time");

        alerta.getChildren().addAll(tagLabel, textoLabel, tempoLabel);
        return alerta;
    }

    private void atualizarMetaDiaria(List<Orcamento> orcamentos) {
        if (metaDiariaLabel == null) {
            return;
        }

        long emitidosHoje = orcamentos.stream()
            .filter(orcamento -> LocalDate.now().equals(orcamento.getDataEmissao()))
            .count();

        int percentual = (int) Math.min(100, Math.round((emitidosHoje * 100.0) / META_DIARIA_ORCAMENTOS));
        if (emitidosHoje == 0) {
            metaDiariaLabel.setText("Você ainda não tem orçamentos emitidos hoje.");
            return;
        }

        if (percentual >= 100) {
            metaDiariaLabel.setText("Meta diária atingida! " + emitidosHoje + " orçamento(s) emitido(s) hoje.");
            return;
        }

        metaDiariaLabel.setText("Você atingiu " + percentual + "% da sua meta diária de orçamentos hoje.");
    }

    private String formatarCodigoOrcamento(Integer id) {
        return id == null ? "ORC-S/N" : String.format("ORC-%04d", id);
    }

    private String formatarStatus(String status) {
        if (status == null || status.isBlank()) {
            return "Sem Status";
        }
        String normalizado = status.trim().toUpperCase();
        return switch (normalizado) {
            case "PENDENTE" -> "Pendente";
            case "APROVADO" -> "Aprovado";
            case "VENDIDO" -> "Vendido";
            case "FECHADO" -> "Fechado";
            case "CANCELADO" -> "Cancelado";
            default -> status;
        };
    }

    private String formatarDataResumo(LocalDate data) {
        if (data == null) {
            return "-";
        }
        if (LocalDate.now().equals(data)) {
            return "Hoje";
        }
        if (LocalDate.now().minusDays(1).equals(data)) {
            return "Ontem";
        }
        return data.format(DATA_LABEL_FORMATTER);
    }

    private boolean isCancelado(String status) {
        return status != null && "CANCELADO".equalsIgnoreCase(status.trim());
    }

    private boolean isConcluido(String status) {
        if (status == null) {
            return false;
        }
        String normalized = status.trim().toUpperCase();
        return normalized.equals("APROVADO") || normalized.equals("VENDIDO") || normalized.equals("FECHADO");
    }

    public void navegarParaDashboard() {
        if (mainController != null) {
            mainController.abrirDashboardTela();
        }
    }

    public void navegarParaClientes() {
        if (mainController != null) {
            mainController.abrirClientesTela();
        }
    }

    public void navegarParaMateriais() {
        if (mainController != null) {
            mainController.abrirMateriaisTela();
        }
    }

    public void navegarParaOrcamentos() {
        if (mainController != null) {
            mainController.abrirOrcamentosTela();
        }
    }

    public void navegarParaRelatorios() {
        if (mainController != null) {
            mainController.abrirRelatoriosTela();
        }
    }

    public void navegarParaAjustes() {
        if (mainController != null) {
            mainController.abrirAjustesTela();
        }
    }

    // Inner class for table data
    public static class OrcamentoRow {
        private final String id;
        private final String cliente;
        private final String data;
        private final String valor;
        private final String status;

        public OrcamentoRow(String id, String cliente, String data, String valor, String status) {
            this.id = id;
            this.cliente = cliente;
            this.data = data;
            this.valor = valor;
            this.status = status;
        }

        public String getId() { return id; }
        public String getCliente() { return cliente; }
        public String getData() { return data; }
        public String getValor() { return valor; }
        public String getStatus() { return status; }
    }
}