package com.grafica.controller;

import com.grafica.dao.ClienteDAO;
import com.grafica.dao.OrcamentoDAO;
import com.grafica.model.Cliente;
import com.grafica.model.Orcamento;
import com.grafica.model.Usuario;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

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
    private VBox ultimosOrcamentosContainer;

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
        atualizarPainel();
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
                : "Grafica";
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
        if (ultimosOrcamentosContainer == null) {
            return;
        }

        ultimosOrcamentosContainer.getChildren().clear();

        List<Orcamento> ultimos = orcamentos.stream()
            .sorted(Comparator
                .comparing(Orcamento::getDataEmissao, Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(Orcamento::getId, Comparator.nullsLast(Comparator.reverseOrder())))
            .limit(5)
            .toList();

        if (ultimos.isEmpty()) {
            Label vazio = new Label("Nenhum orcamento registrado ainda.");
            vazio.getStyleClass().add("dashboard-card-subtitle");
            ultimosOrcamentosContainer.getChildren().add(vazio);
            return;
        }

        for (int i = 0; i < ultimos.size(); i++) {
            Orcamento orcamento = ultimos.get(i);
            boolean ultimoItem = i == ultimos.size() - 1;
            ultimosOrcamentosContainer.getChildren().add(criarLinhaOrcamento(orcamento, nomesClientes, ultimoItem));
        }
    }

    private HBox criarLinhaOrcamento(Orcamento orcamento, Map<Integer, String> nomesClientes, boolean ultimoItem) {
        HBox linha = new HBox(12.0);
        linha.setAlignment(Pos.CENTER_LEFT);
        linha.getStyleClass().add("dashboard-table-row");
        if (ultimoItem) {
            linha.getStyleClass().add("dashboard-table-row-last");
        }

        Label idLabel = new Label(formatarCodigoOrcamento(orcamento.getId()));
        idLabel.getStyleClass().add("dashboard-table-value");
        idLabel.setPrefWidth(110.0);

        Label clienteLabel = new Label(nomesClientes.getOrDefault(orcamento.getIdCliente(), "Cliente nao identificado"));
        clienteLabel.getStyleClass().add("dashboard-table-value");
        HBox.setHgrow(clienteLabel, Priority.ALWAYS);

        Label dataLabel = new Label(formatarDataResumo(orcamento.getDataEmissao()));
        dataLabel.getStyleClass().add("dashboard-table-value");
        dataLabel.setPrefWidth(90.0);

        BigDecimal valor = orcamento.getValorFinal() != null ? orcamento.getValorFinal() : BigDecimal.ZERO;
        Label valorLabel = new Label(moeda.format(valor));
        valorLabel.getStyleClass().add("dashboard-table-value");
        valorLabel.setPrefWidth(90.0);

        Label statusLabel = new Label(formatarStatus(orcamento.getStatus()));
        statusLabel.getStyleClass().add("dashboard-pill");
        if (isCancelado(orcamento.getStatus())) {
            statusLabel.getStyleClass().add("dashboard-pill-danger");
        }
        statusLabel.setPrefWidth(90.0);

        Button detalhesButton = new Button("Detalhes");
        detalhesButton.getStyleClass().add("dashboard-link");
        detalhesButton.setOnAction(event -> navegarParaOrcamentos());

        linha.getChildren().addAll(idLabel, clienteLabel, dataLabel, valorLabel, statusLabel, detalhesButton);
        return linha;
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
            alertasContainer.getChildren().add(criarAlerta("OK", "Nenhum alerta critico no momento.", "Atualizado agora"));
            return;
        }

        for (Orcamento orcamento : alertas) {
            LocalDate validade = orcamento.getDataValidade();
            String cliente = nomesClientes.getOrDefault(orcamento.getIdCliente(), "cliente sem nome");
            String tag;
            String tempo;

            if (validade == null) {
                tag = "ATENCAO";
                tempo = "Sem data de validade";
            } else if (validade.isBefore(hoje)) {
                tag = "URGENTE";
                tempo = "Vencido ha " + (hoje.toEpochDay() - validade.toEpochDay()) + " dia(s)";
            } else if (!validade.isAfter(hoje.plusDays(2))) {
                tag = "REVISAO NECESSARIA";
                tempo = "Vence em " + (validade.toEpochDay() - hoje.toEpochDay()) + " dia(s)";
            } else {
                tag = "LOGISTICA";
                tempo = "Validade: " + validade.format(DATA_LABEL_FORMATTER);
            }

            String texto = "Orcamento " + formatarCodigoOrcamento(orcamento.getId()) + " de " + cliente + " esta pendente.";
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
            metaDiariaLabel.setText("Voce ainda nao tem orcamentos emitidos hoje.");
            return;
        }

        if (percentual >= 100) {
            metaDiariaLabel.setText("Meta diaria atingida! " + emitidosHoje + " orcamento(s) emitido(s) hoje.");
            return;
        }

        metaDiariaLabel.setText("Voce atingiu " + percentual + "% da sua meta diaria de orcamentos hoje.");
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
}