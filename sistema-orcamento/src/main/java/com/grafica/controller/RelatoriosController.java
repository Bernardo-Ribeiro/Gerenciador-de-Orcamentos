package com.grafica.controller;

import com.grafica.dao.ClienteDAO;
import com.grafica.dao.OrcamentoDAO;
import com.grafica.model.Cliente;
import com.grafica.model.Orcamento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private TableView<RelatorioOrcamentoView> relatoriosTable;
    @FXML
    private TableColumn<RelatorioOrcamentoView, String> colCodigo;
    @FXML
    private TableColumn<RelatorioOrcamentoView, String> colCliente;
    @FXML
    private TableColumn<RelatorioOrcamentoView, String> colData;
    @FXML
    private TableColumn<RelatorioOrcamentoView, BigDecimal> colValor;
    @FXML
    private TableColumn<RelatorioOrcamentoView, String> colStatus;
    @FXML
    private TableColumn<RelatorioOrcamentoView, Void> colAcoes;

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

    @FXML
    private Label lblPaginacao;

    @FXML
    private Button btnAnterior;

    @FXML
    private Button btnProximo;

    @FXML
    private Button btnPage1;

    @FXML
    private Button btnPage2;

    private OrcamentoDAO orcamentoDAO;
    private ClienteDAO clienteDAO;
    private Map<Integer, String> nomesClientes;
    private List<Orcamento> orcamentosBase;
    private List<Orcamento> orcamentosFiltrados;
    private final ObservableList<RelatorioOrcamentoView> relatoriosVisiveis = FXCollections.observableArrayList();

    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private static final DateTimeFormatter DATA_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final int ITENS_POR_PAGINA = 10;

    private int paginaAtual = 1;
    private int totalPaginas = 1;

    @FXML
    private void initialize() {
        orcamentoDAO = new OrcamentoDAO();
        clienteDAO = new ClienteDAO();
        carregarDadosBase();
        configurarTabela();
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

        paginaAtual = 1;
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
        if (relatoriosTable == null) {
            return;
        }

        relatoriosVisiveis.clear();
        int totalRegistros = orcamentosFiltrados.size();
        totalPaginas = Math.max(1, (int) Math.ceil(totalRegistros / (double) ITENS_POR_PAGINA));
        paginaAtual = Math.min(Math.max(paginaAtual, 1), totalPaginas);

        int inicio = (paginaAtual - 1) * ITENS_POR_PAGINA;
        int fim = Math.min(inicio + ITENS_POR_PAGINA, totalRegistros);
        int exibidos = totalRegistros == 0 ? 0 : (fim - inicio);

        if (resultadosSubtitleLabel != null) {
            resultadosSubtitleLabel.setText("Mostrando " + exibidos + " de " + totalRegistros + " orçamentos encontrados.");
        }
        if (lblPaginacao != null) {
            lblPaginacao.setText("Página " + paginaAtual + " de " + totalPaginas);
        }

        Label placeholder = new Label("Nenhum orçamento encontrado para os filtros selecionados.");
        placeholder.getStyleClass().add("reports-card-subtitle");
        relatoriosTable.setPlaceholder(placeholder);

        if (totalRegistros == 0) {
            atualizarBotoesPaginacao();
            return;
        }

        for (int i = inicio; i < fim; i++) {
            Orcamento orcamento = orcamentosFiltrados.get(i);
            relatoriosVisiveis.add(new RelatorioOrcamentoView(orcamento));
        }

        atualizarBotoesPaginacao();
    }

    private void atualizarBotoesPaginacao() {
        if (btnAnterior != null) {
            btnAnterior.setDisable(paginaAtual <= 1);
        }
        if (btnProximo != null) {
            btnProximo.setDisable(paginaAtual >= totalPaginas);
        }
        configurarBotaoPagina(btnPage1, paginaAtual);
        configurarBotaoPagina(btnPage2, paginaAtual + 1);
    }

    private void configurarBotaoPagina(Button botao, int pagina) {
        if (botao == null) {
            return;
        }
        if (pagina > totalPaginas) {
            botao.setVisible(false);
            botao.setManaged(false);
            return;
        }
        botao.setVisible(true);
        botao.setManaged(true);
        botao.setText(String.valueOf(pagina));
        botao.getStyleClass().remove("reports-pagination-active");
        if (pagina == paginaAtual) {
            botao.getStyleClass().add("reports-pagination-active");
        }
    }

    @FXML
    private void paginaAnterior() {
        if (paginaAtual > 1) {
            paginaAtual--;
            atualizarResultados();
        }
    }

    @FXML
    private void paginaProxima() {
        if (paginaAtual < totalPaginas) {
            paginaAtual++;
            atualizarResultados();
        }
    }

    @FXML
    private void irParaPagina(javafx.event.ActionEvent event) {
        if (!(event.getSource() instanceof Button botao)) {
            return;
        }
        try {
            paginaAtual = Integer.parseInt(botao.getText());
            atualizarResultados();
        } catch (NumberFormatException ignored) {
        }
    }

    private void configurarTabela() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colValor.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : moeda.format(item));
            }
        });

        colStatus.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                Label statusLabel = new Label(status);
                statusLabel.getStyleClass().add("reports-pill");
                // TODO: Adicionar classes de estilo específicas para cada status (aprovado, pendente, etc)
                setGraphic(statusLabel);
                setText(null);
            }
        });

        colAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button ver = new Button("Ver");
            private final Button pdf = new Button("PDF");
            private final Button aprovar = new Button("Aprovar");
            private final Button reprovar = new Button("Reprovar");
            private final HBox acoesContainer = new HBox(6.0, ver, pdf, aprovar, reprovar);

            {
                acoesContainer.setAlignment(Pos.CENTER_RIGHT);
                ver.getStyleClass().add("reports-action");
                pdf.getStyleClass().add("reports-action");
                aprovar.getStyleClass().add("reports-action");
                aprovar.getStyleClass().add("reports-action-approve");
                reprovar.getStyleClass().add("reports-action");
                reprovar.getStyleClass().add("reports-action-reject");

                ver.setOnAction(event -> {
                    Orcamento orcamento = getTableView().getItems().get(getIndex()).getOrcamento();
                    feedbackLabel.setText("Orcamento " + formatarCodigoOrcamento(orcamento.getId()) + " selecionado.");
                });
                pdf.setOnAction(event -> {
                    Orcamento orcamento = getTableView().getItems().get(getIndex()).getOrcamento();
                    feedbackLabel.setText("PDF do orcamento " + formatarCodigoOrcamento(orcamento.getId()) + " em implementacao.");
                });
                aprovar.setOnAction(event -> aprovarOrcamento(getTableView().getItems().get(getIndex()).getOrcamento()));
                reprovar.setOnAction(event -> reprovarOrcamento(getTableView().getItems().get(getIndex()).getOrcamento()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : acoesContainer);
            }
        });

        relatoriosTable.setItems(relatoriosVisiveis);
        relatoriosTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
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

    public class RelatorioOrcamentoView {
        private final Orcamento orcamento;

        public RelatorioOrcamentoView(Orcamento orcamento) {
            this.orcamento = orcamento;
        }

        public Orcamento getOrcamento() {
            return orcamento;
        }

        public String getCodigo() {
            return formatarCodigoOrcamento(orcamento.getId());
        }

        public String getCliente() {
            return nomesClientes.getOrDefault(orcamento.getIdCliente(), "Cliente nao identificado");
        }

        public String getData() {
            return formatarData(orcamento.getDataEmissao());
        }

        public BigDecimal getValor() { return valorSeguro(orcamento.getValorFinal()); }

        public String getStatus() { return formatarStatus(orcamento.getStatus()); }
    }
}
