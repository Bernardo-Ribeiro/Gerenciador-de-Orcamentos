package com.grafica.controller;

import com.grafica.dao.ClienteDAO;
import com.grafica.dao.OrcamentoDAO;
import com.grafica.model.Cliente;
import com.grafica.model.Orcamento;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class RelatoriosController {
    @FXML
    private Label totalVendidoLabel;

    @FXML
    private Label orcamentosPendentesLabel;

    @FXML
    private Label conversaoMediaLabel;

    @FXML
    private Label clientesAtivosLabel;

    @FXML
    private TextField periodoField;

    @FXML
    private TextField statusField;

    @FXML
    private TextField clienteField;

    @FXML
    private Label resultadosSubtitleLabel;

    @FXML
    private VBox resultadosContainer;

    @FXML
    private VBox rankingClientesContainer;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button btnAplicarFiltros;

    @FXML
    private Button btnExportarCsv;

    @FXML
    private Button btnGerarPdf;

    private OrcamentoDAO orcamentoDAO;
    private ClienteDAO clienteDAO;
    private Map<Integer, String> nomesClientes;
    private List<Orcamento> orcamentosBase;
    private List<Orcamento> orcamentosFiltrados;

    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private static final DateTimeFormatter DATA_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final int LIMITE_LINHAS = 10;

    @FXML
    private void initialize() {
        orcamentoDAO = new OrcamentoDAO();
        clienteDAO = new ClienteDAO();
        carregarDadosBase();
        aplicarFiltros();
    }

    private void carregarDadosBase() {
        List<Cliente> clientes = clienteDAO.listarTodos();
        nomesClientes = clientes.stream()
            .filter(cliente -> cliente.getId() != null)
            .collect(Collectors.toMap(Cliente::getId, Cliente::getNome, (atual, substituto) -> atual));

        orcamentosBase = orcamentoDAO.listarTodos();
        orcamentosFiltrados = orcamentosBase;
    }

    @FXML
    private void atualizarRelatorios() {
        try {
            carregarDadosBase();
            aplicarFiltros();
        } catch (Exception e) {
            if (feedbackLabel != null) {
                feedbackLabel.setText("Erro ao carregar relatorios: " + e.getMessage());
            }
            System.err.println("Erro ao carregar relatorios: " + e.getMessage());
        }
    }

    @FXML
    private void aplicarFiltros() {
        int dias = parsePeriodoDias();
        LocalDate limiteData = LocalDate.now().minusDays(dias);
        String statusFiltro = valorNormalizado(statusField != null ? statusField.getText() : null);
        String clienteFiltro = valorNormalizado(clienteField != null ? clienteField.getText() : null);

        orcamentosFiltrados = orcamentosBase.stream()
            .filter(orcamento -> orcamento.getDataEmissao() == null || !orcamento.getDataEmissao().isBefore(limiteData))
            .filter(orcamento -> statusFiltro.isEmpty() || valorNormalizado(orcamento.getStatus()).contains(statusFiltro))
            .filter(orcamento -> {
                if (clienteFiltro.isEmpty()) {
                    return true;
                }
                String nomeCliente = nomesClientes.getOrDefault(orcamento.getIdCliente(), "");
                return valorNormalizado(nomeCliente).contains(clienteFiltro);
            })
            .sorted(Comparator
                .comparing(Orcamento::getDataEmissao, Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(Orcamento::getId, Comparator.nullsLast(Comparator.reverseOrder())))
            .toList();

        atualizarCardsResumo();
        atualizarResultados();
        atualizarRankingClientes();

        if (feedbackLabel != null) {
            feedbackLabel.setText("Filtros aplicados com sucesso.");
        }
    }

    @FXML
    private void exportarCsv() {
        try {
            Path arquivo = Path.of("sistema-orcamento", "target", "relatorios-export.csv");
            Files.createDirectories(arquivo.getParent());

            List<String> linhas = orcamentosFiltrados.stream()
                .map(orcamento -> String.join(";",
                    formatarCodigoOrcamento(orcamento.getId()),
                    nomesClientes.getOrDefault(orcamento.getIdCliente(), "Cliente nao identificado"),
                    orcamento.getDataEmissao() != null ? orcamento.getDataEmissao().format(DATA_FORMAT) : "-",
                    String.valueOf(valorSeguro(orcamento.getValorFinal())),
                    Objects.toString(orcamento.getStatus(), "-")))
                .toList();

            String conteudo = "codigo;cliente;data;valor_final;status\n" + String.join("\n", linhas);
            Files.writeString(arquivo, conteudo, StandardCharsets.UTF_8);

            if (feedbackLabel != null) {
                feedbackLabel.setText("CSV exportado em: " + arquivo);
            }
        } catch (IOException e) {
            if (feedbackLabel != null) {
                feedbackLabel.setText("Falha ao exportar CSV: " + e.getMessage());
            }
        }
    }

    @FXML
    private void gerarPdf() {
        if (feedbackLabel != null) {
            feedbackLabel.setText("Geracao de PDF sera implementada em seguida. Use Exportar CSV por enquanto.");
        }
    }

    private void atualizarCardsResumo() {
        BigDecimal totalVendido = orcamentosFiltrados.stream()
            .filter(orcamento -> isConcluido(orcamento.getStatus()))
            .map(orcamento -> valorSeguro(orcamento.getValorFinal()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        long pendentes = orcamentosFiltrados.stream()
            .filter(orcamento -> "PENDENTE".equalsIgnoreCase(orcamento.getStatus()))
            .count();

        long concluidos = orcamentosFiltrados.stream()
            .filter(orcamento -> isConcluido(orcamento.getStatus()))
            .count();

        int conversao = orcamentosFiltrados.isEmpty()
            ? 0
            : (int) Math.round((concluidos * 100.0) / orcamentosFiltrados.size());

        long clientesAtivos = orcamentosFiltrados.stream()
            .map(Orcamento::getIdCliente)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet())
            .size();

        if (totalVendidoLabel != null) {
            totalVendidoLabel.setText(moeda.format(totalVendido));
        }
        if (orcamentosPendentesLabel != null) {
            orcamentosPendentesLabel.setText(String.valueOf(pendentes));
        }
        if (conversaoMediaLabel != null) {
            conversaoMediaLabel.setText(conversao + "%");
        }
        if (clientesAtivosLabel != null) {
            clientesAtivosLabel.setText(String.valueOf(clientesAtivos));
        }
    }

    private void atualizarResultados() {
        if (resultadosContainer == null) {
            return;
        }

        resultadosContainer.getChildren().clear();
        int exibidos = Math.min(LIMITE_LINHAS, orcamentosFiltrados.size());

        if (resultadosSubtitleLabel != null) {
            resultadosSubtitleLabel.setText("Mostrando " + exibidos + " de " + orcamentosFiltrados.size() + " orcamentos encontrados.");
        }

        if (orcamentosFiltrados.isEmpty()) {
            Label vazio = new Label("Nenhum orcamento encontrado para os filtros selecionados.");
            vazio.getStyleClass().add("reports-card-subtitle");
            resultadosContainer.getChildren().add(vazio);
            return;
        }

        for (int i = 0; i < exibidos; i++) {
            Orcamento orcamento = orcamentosFiltrados.get(i);
            boolean ultimoItem = i == exibidos - 1;
            resultadosContainer.getChildren().add(criarLinhaResultado(orcamento, ultimoItem));
        }
    }

    private HBox criarLinhaResultado(Orcamento orcamento, boolean ultimoItem) {
        HBox linha = new HBox(16.0);
        linha.setAlignment(Pos.CENTER_LEFT);
        linha.getStyleClass().add("reports-table-row");
        if (ultimoItem) {
            linha.getStyleClass().add("reports-table-row-last");
        }

        Label codigo = criarCelula("reports-link", formatarCodigoOrcamento(orcamento.getId()), 140.0);
        Label cliente = criarCelula("reports-table-value", nomesClientes.getOrDefault(orcamento.getIdCliente(), "Cliente nao identificado"), 180.0);
        Label data = criarCelula("reports-table-value", formatarData(orcamento.getDataEmissao()), 120.0);
        Label valor = criarCelula("reports-table-value", moeda.format(valorSeguro(orcamento.getValorFinal())), 140.0);
        Label status = criarCelula("reports-pill", formatarStatus(orcamento.getStatus()), 120.0);

        HBox acoes = new HBox(6.0);
        acoes.setAlignment(Pos.CENTER_RIGHT);
        acoes.setPrefWidth(120.0);

        Button ver = new Button("Ver");
        ver.getStyleClass().add("reports-action");
        ver.setOnAction(event -> feedbackLabel.setText("Orcamento " + formatarCodigoOrcamento(orcamento.getId()) + " selecionado."));

        Button pdf = new Button("PDF");
        pdf.getStyleClass().add("reports-action");
        pdf.setOnAction(event -> feedbackLabel.setText("PDF do orcamento " + formatarCodigoOrcamento(orcamento.getId()) + " em implementacao."));

        Button aprovar = new Button("Aprovar");
        aprovar.getStyleClass().add("reports-action-approve");
        aprovar.setOnAction(event -> {
            aprovarOrcamento(orcamento);
        });

        Button reprovar = new Button("Reprovar");
        reprovar.getStyleClass().add("reports-action-reject");
        reprovar.setOnAction(event -> {
            reprovarOrcamento(orcamento);
        });

        acoes.getChildren().addAll(ver, pdf, aprovar, reprovar);
        linha.getChildren().addAll(codigo, cliente, data, valor, status, acoes);
        return linha;
    }

    private void aprovarOrcamento(Orcamento orcamento) {
        if (orcamento == null || orcamento.getId() == null) return;
        try {
            orcamento.setStatus("APROVADO");
            orcamentoDAO.atualizar(orcamento);
            feedbackLabel.setText("Orcamento " + formatarCodigoOrcamento(orcamento.getId()) + " aprovado.");
            // Atualiza dados locais e UI
            carregarDadosBase();
            aplicarFiltros();
        } catch (Exception e) {
            feedbackLabel.setText("Falha ao aprovar: " + e.getMessage());
        }
    }

    private void reprovarOrcamento(Orcamento orcamento) {
        if (orcamento == null || orcamento.getId() == null) return;
        try {
            orcamento.setStatus("REPROVADO");
            orcamentoDAO.atualizar(orcamento);
            feedbackLabel.setText("Orcamento " + formatarCodigoOrcamento(orcamento.getId()) + " reprovado.");
            // Atualiza dados locais e UI
            carregarDadosBase();
            aplicarFiltros();
        } catch (Exception e) {
            feedbackLabel.setText("Falha ao reprovar: " + e.getMessage());
        }
    }

    private Label criarCelula(String styleClass, String texto, double width) {
        Label label = new Label(texto);
        label.getStyleClass().add(styleClass);
        label.setPrefWidth(width);
        return label;
    }

    private void atualizarRankingClientes() {
        if (rankingClientesContainer == null) {
            return;
        }

        rankingClientesContainer.getChildren().clear();

        Map<Integer, BigDecimal> ranking = orcamentosFiltrados.stream()
            .filter(orcamento -> isConcluido(orcamento.getStatus()))
            .collect(Collectors.groupingBy(
                Orcamento::getIdCliente,
                Collectors.mapping(Orcamento::getValorFinal,
                    Collectors.reducing(BigDecimal.ZERO, this::valorSeguro, BigDecimal::add))));

        List<Map.Entry<Integer, BigDecimal>> topClientes = ranking.entrySet().stream()
            .sorted(Map.Entry.<Integer, BigDecimal>comparingByValue().reversed())
            .limit(4)
            .toList();

        if (topClientes.isEmpty()) {
            Label vazio = new Label("Sem vendas concluidas para montar ranking.");
            vazio.getStyleClass().add("reports-card-subtitle");
            rankingClientesContainer.getChildren().add(vazio);
            return;
        }

        for (Map.Entry<Integer, BigDecimal> entry : topClientes) {
            rankingClientesContainer.getChildren().add(criarLinhaRanking(entry.getKey(), entry.getValue()));
        }
    }

    private VBox criarLinhaRanking(Integer idCliente, BigDecimal valorTotal) {
        VBox bloco = new VBox(4.0);

        HBox cabecalho = new HBox(6.0);
        cabecalho.setAlignment(Pos.CENTER_LEFT);

        Label nome = new Label(nomesClientes.getOrDefault(idCliente, "Cliente nao identificado"));
        nome.getStyleClass().add("reports-table-value");

        Region espaco = new Region();
        HBox.setHgrow(espaco, Priority.ALWAYS);

        Label valor = new Label(moeda.format(valorTotal));
        valor.getStyleClass().add("reports-link");

        cabecalho.getChildren().addAll(nome, espaco, valor);

        Label barra = new Label();
        barra.getStyleClass().add("reports-progress");

        bloco.getChildren().addAll(cabecalho, barra);
        return bloco;
    }

    private int parsePeriodoDias() {
        if (periodoField == null || periodoField.getText() == null || periodoField.getText().isBlank()) {
            return 30;
        }
        String apenasNumeros = periodoField.getText().replaceAll("[^0-9]", "");
        if (apenasNumeros.isBlank()) {
            return 30;
        }
        try {
            int valor = Integer.parseInt(apenasNumeros);
            return Math.max(1, valor);
        } catch (NumberFormatException e) {
            return 30;
        }
    }

    private String valorNormalizado(String valor) {
        return valor == null ? "" : valor.trim().toUpperCase();
    }

    private BigDecimal valorSeguro(BigDecimal valor) {
        return valor == null ? BigDecimal.ZERO : valor;
    }

    private String formatarData(LocalDate data) {
        return data == null ? "-" : data.format(DATA_FORMAT);
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
            case "REPROVADO" -> "Reprovado";
            default -> status;
        };
    }

    private boolean isConcluido(String status) {
        return status != null && "APROVADO".equalsIgnoreCase(status.trim());
    }
}
