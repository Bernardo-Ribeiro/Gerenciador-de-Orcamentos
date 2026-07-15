package com.grafica.controller;

import com.grafica.dao.EscalaProdutivaDAO;
import com.grafica.dao.ProdutoDAO;
import com.grafica.dao.MaterialDAO;
import com.grafica.dao.ProdutoMaterialDAO;
import com.grafica.model.EscalaProdutiva;
import com.grafica.model.Material;
import com.grafica.model.Produto;
import com.grafica.model.ProdutoMaterial;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class EscalaProdutivaController {

    @FXML
    private VBox rowsContainer;

    @FXML
    private TextField txtQuantidadeSimulacao;

    @FXML
    private TextField txtValorBaseSimulacao;

    @FXML
    private Label lblDescontoTitulo;

    @FXML
    private Label lblDescontoValor;

    @FXML
    private Label lblTotalValor;

    @FXML
    private ComboBox<Produto> produtoCombo;

    @FXML
    private ComboBox<Material> materialCombo;

    private final List<IntervaloRow> linhas = new ArrayList<>();
    private final DecimalFormat df = new DecimalFormat("0.00");
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final MaterialDAO materialDAO = new MaterialDAO();
    private final ProdutoMaterialDAO produtoMaterialDAO = new ProdutoMaterialDAO();
    private final EscalaProdutivaDAO escalaProdutivaDAO = new EscalaProdutivaDAO();

    private Integer materialSelecionadoId;

    @FXML
    private void initialize() {
        configurarComboProdutos();
        configurarComboMateriais();

        carregarProdutos();

        if (produtoCombo.getItems() != null && !produtoCombo.getItems().isEmpty()) {
            produtoCombo.getSelectionModel().selectFirst();
            carregarMateriaisPorProduto(produtoCombo.getSelectionModel().getSelectedItem().getId());
        }

        carregarEscalasDoMaterialSelecionado();
        atualizarSimulacao();
    }

    private void configurarComboProdutos() {
        produtoCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Produto p) { return p == null ? "" : p.getNome() + " (" + p.getCategoria() + ")"; }
            @Override public Produto fromString(String string) { return null; }
        });

        produtoCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                carregarMateriaisPorProduto(newVal.getId());
            } else {
                materialCombo.setItems(FXCollections.emptyObservableList());
                materialSelecionadoId = null;
                limparIntervalos();
            }
            atualizarSimulacao();
        });
    }

    private void configurarComboMateriais() {
        materialCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Material m) { return m == null ? "" : m.getNome(); }
            @Override public Material fromString(String string) { return null; }
        });

        materialCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            materialSelecionadoId = newVal != null ? newVal.getId() : null;
            carregarEscalasDoMaterialSelecionado();
            atualizarSimulacao();
        });
    }

    private void carregarProdutos() {
        try {
            List<Produto> produtos = produtoDAO.listarTodos();
            produtoCombo.setItems(FXCollections.observableArrayList(produtos));
        } catch (Exception e) {
            mostrarErro("Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private void carregarMateriaisPorProduto(Integer idProduto) {
        try {
            List<ProdutoMaterial> pms = produtoMaterialDAO.listarPorProduto(idProduto);
            List<Material> materiais = new ArrayList<>();
            for (ProdutoMaterial pm : pms) {
                Material m = materialDAO.obterPorId(pm.getIdMaterial());
                if (m != null) materiais.add(m);
            }
            materialCombo.setItems(FXCollections.observableArrayList(materiais));
            if (!materiais.isEmpty()) {
                materialCombo.getSelectionModel().selectFirst();
                materialSelecionadoId = materialCombo.getSelectionModel().getSelectedItem().getId();
            } else {
                materialSelecionadoId = null;
            }
            carregarEscalasDoMaterialSelecionado();
        } catch (Exception e) {
            mostrarErro("Erro ao carregar materiais: " + e.getMessage());
        }
    }

    private void carregarEscalasDoMaterialSelecionado() {
        limparIntervalos();
        if (materialSelecionadoId == null) {
            return;
        }
        try {
            List<EscalaProdutiva> escalas = escalaProdutivaDAO.listarPorMaterial(materialSelecionadoId);
            for (EscalaProdutiva escala : escalas) {
                BigDecimal desconto = escala.getDescontoPercentual() != null ? escala.getDescontoPercentual() : BigDecimal.ZERO;
                Double custoUnitario = escala.getCustoUnitario();
                adicionarIntervaloComValores(escala.getQtdMinima(), escala.getQtdMaxima(), desconto, custoUnitario);
            }
        } catch (Exception e) {
            mostrarErro("Erro ao carregar escalas: " + e.getMessage());
        }
    }

    @FXML
    private void adicionarIntervalo() {
        adicionarIntervaloComValores(null, null, null, null);
    }

    private void adicionarIntervaloComValores(Integer inicio, Integer fim, BigDecimal desconto, Double custoUnitario) {
        TextField txtInicio = criarCampoNumero("Inicial", 110);
        TextField txtFim = criarCampoNumero("Final", 110);
        TextField txtPercent = criarCampoNumero("%", 70);
        TextField txtCustoUnitario = criarCampoNumero("Custo Unit.", 100);

        if (inicio != null) {
            txtInicio.setText(String.valueOf(inicio));
        }
        if (fim != null) {
            txtFim.setText(String.valueOf(fim));
        }
        if (desconto != null) {
            txtPercent.setText(desconto.stripTrailingZeros().toPlainString());
        }
        if (custoUnitario != null) {
            txtCustoUnitario.setText(df.format(custoUnitario));
        }

        Label lblPercent = new Label("%");
        lblPercent.getStyleClass().add("scale-suffix");

        HBox percentBox = new HBox(6, txtPercent, lblPercent);
        percentBox.getStyleClass().add("scale-percent-box");
        percentBox.setPrefWidth(130);

        Label lblCusto = new Label("R$");
        lblCusto.getStyleClass().add("scale-suffix");

        HBox custoBox = new HBox(6, txtCustoUnitario, lblCusto);
        custoBox.getStyleClass().add("scale-percent-box");
        custoBox.setPrefWidth(110);

        Button btnExcluir = new Button("Excluir");
        btnExcluir.getStyleClass().add("scale-delete-button");
        btnExcluir.setPrefWidth(90);

        HBox row = new HBox(12, txtInicio, txtFim, percentBox, custoBox, btnExcluir);
        row.getStyleClass().add("scale-row");

        IntervaloRow intervaloRow = new IntervaloRow(row, txtInicio, txtFim, txtPercent, txtCustoUnitario);
        btnExcluir.setOnAction(event -> removerIntervalo(intervaloRow));

        rowsContainer.getChildren().add(row);
        linhas.add(intervaloRow);
        atualizarSeparadores();
        atualizarSimulacao();
    }

    private TextField criarCampoNumero(String prompt, double width) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setPrefWidth(width);
        field.getStyleClass().add("settings-input");
        field.getStyleClass().add("scale-input");
        return field;
    }

    private void removerIntervalo(IntervaloRow intervaloRow) {
        rowsContainer.getChildren().remove(intervaloRow.container);
        linhas.remove(intervaloRow);
        atualizarSeparadores();
        atualizarSimulacao();
    }

    private void atualizarSeparadores() {
        for (int i = 0; i < linhas.size(); i++) {
            HBox row = linhas.get(i).container;
            row.getStyleClass().remove("scale-row-last");
            if (i == linhas.size() - 1) {
                row.getStyleClass().add("scale-row-last");
            }
        }
    }

    @FXML
    private void salvarEscala() {
        if (materialSelecionadoId == null) {
            mostrarErro("Selecione um produto e um material para salvar as escalas.");
            return;
        }

        List<Intervalo> intervalos = validarIntervalos();
        if (intervalos == null) {
            return;
        }

        try {
            List<EscalaProdutiva> existentes = escalaProdutivaDAO.listarPorMaterial(materialSelecionadoId);
            List<Integer> idsExistentes = new ArrayList<>();
            for (EscalaProdutiva e : existentes) {
                idsExistentes.add(e.getId());
            }

            boolean primeiro = true;
            for (Intervalo intervalo : intervalos) {
                EscalaProdutiva escala = new EscalaProdutiva();
                escala.setIdMaterial(materialSelecionadoId);
                escala.setQtdMinima(intervalo.inicio);
                escala.setQtdMaxima(intervalo.fim);
                escala.setDescontoPercentual(intervalo.desconto);
                escala.setCustoUnitario(intervalo.custoUnitario);

                if (primeiro) {
                    escalaProdutivaDAO.deletarPorMaterial(materialSelecionadoId);
                    primeiro = false;
                }

                escalaProdutivaDAO.criar(escala);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Escala salva");
            alert.setHeaderText(null);
            alert.setContentText("Intervalos salvos com sucesso.");
            alert.showAndWait();

        } catch (Exception e) {
            mostrarErro("Erro ao salvar escalas: " + e.getMessage());
        }
    }

    @FXML
    private void atualizarSimulacao() {
        int quantidade = parseIntSafe(txtQuantidadeSimulacao != null ? txtQuantidadeSimulacao.getText() : null, 0);
        BigDecimal valorBase = parseDecimalSafe(txtValorBaseSimulacao != null ? txtValorBaseSimulacao.getText() : null, BigDecimal.ZERO);

        EscalaSelecionada escala = encontrarEscalaParaQuantidade(quantidade);

        BigDecimal descontoPercentual = escala.descontoPercentual != null ? escala.descontoPercentual : BigDecimal.ZERO;
        BigDecimal custoEfetivo;

        if (escala.custoUnitario != null) {
            custoEfetivo = BigDecimal.valueOf(escala.custoUnitario);
        } else {
            custoEfetivo = valorBase.subtract(
                valorBase.multiply(descontoPercentual)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
            );
        }

        BigDecimal totalFinal = custoEfetivo.max(BigDecimal.ZERO);

        if (lblDescontoTitulo != null) {
            lblDescontoTitulo.setText("Custo Efetivo (" + descontoPercentual.stripTrailingZeros().toPlainString() + "%):");
        }
        if (lblDescontoValor != null) {
            lblDescontoValor.setText("- R$ " + formatarDecimal(valorBase.subtract(custoEfetivo).max(BigDecimal.ZERO)));
        }
        if (lblTotalValor != null) {
            lblTotalValor.setText("R$ " + formatarDecimal(totalFinal));
        }
    }

    private EscalaSelecionada encontrarEscalaParaQuantidade(int quantidade) {
        List<Intervalo> intervalos = coletarIntervalosValidos();
        intervalos.sort(Comparator.comparingInt(intervalo -> intervalo.inicio));

        for (Intervalo intervalo : intervalos) {
            if (quantidade >= intervalo.inicio && quantidade <= intervalo.fim) {
                return new EscalaSelecionada(intervalo.desconto, intervalo.custoUnitario);
            }
        }

        return new EscalaSelecionada(BigDecimal.ZERO, null);
    }

    private void limparIntervalos() {
        rowsContainer.getChildren().clear();
        linhas.clear();
    }

    private List<Intervalo> validarIntervalos() {
        if (linhas.isEmpty()) {
            mostrarErro("Adicione pelo menos um intervalo.");
            return null;
        }

        List<Intervalo> intervalos = new ArrayList<>();
        for (IntervaloRow row : linhas) {
            String inicioText = row.txtInicio.getText();
            String fimText = row.txtFim.getText();
            String descontoText = row.txtPercent.getText();
            String custoUnitarioText = row.txtCustoUnitario.getText();

            if (isBlank(inicioText) || isBlank(fimText)) {
                mostrarErro("Preencha todos os campos dos intervalos.");
                return null;
            }

            int inicio;
            int fim;
            BigDecimal desconto;
            Double custoUnitario = null;
            try {
                inicio = Integer.parseInt(inicioText.trim());
                fim = Integer.parseInt(fimText.trim());
                desconto = parseDecimal(descontoText);
                if (!isBlank(custoUnitarioText)) {
                    custoUnitario = Double.parseDouble(custoUnitarioText.trim().replace(",", "."));
                }
            } catch (NumberFormatException ex) {
                mostrarErro("Use apenas numeros nos intervalos, desconto e custo unitario.");
                return null;
            }

            if (inicio <= 0 || fim < inicio) {
                mostrarErro("Informe intervalos validos (inicio > 0 e inicio <= fim).");
                return null;
            }

            if (desconto.compareTo(BigDecimal.ZERO) < 0 || desconto.compareTo(new BigDecimal("100")) > 0) {
                mostrarErro("O desconto deve estar entre 0 e 100.");
                return null;
            }

            if (custoUnitario != null && custoUnitario < 0) {
                mostrarErro("O custo unitario nao pode ser negativo.");
                return null;
            }

            if (desconto.compareTo(BigDecimal.ZERO) == 0 && custoUnitario == null) {
                mostrarErro("Informe pelo menos um desconto percentual ou um custo unitario.");
                return null;
            }

            intervalos.add(new Intervalo(inicio, fim, desconto, custoUnitario));
        }

        intervalos.sort(Comparator.comparingInt(intervalo -> intervalo.inicio));
        for (int i = 1; i < intervalos.size(); i++) {
            Intervalo anterior = intervalos.get(i - 1);
            Intervalo atual = intervalos.get(i);
            if (atual.inicio <= anterior.fim) {
                mostrarErro("Intervalos nao podem se sobrepor. Verifique os valores '" + anterior.fim + "' e '" + atual.inicio + "'.");
                return null;
            }
        }

        return intervalos;
    }

    private List<Intervalo> coletarIntervalosValidos() {
        List<Intervalo> intervalos = new ArrayList<>();
        for (IntervaloRow row : linhas) {
            try {
                int inicio = Integer.parseInt(row.txtInicio.getText().trim());
                int fim = Integer.parseInt(row.txtFim.getText().trim());
                BigDecimal desconto = parseDecimal(row.txtPercent.getText());
                Double custoUnitario = null;
                String custoText = row.txtCustoUnitario.getText();
                if (!isBlank(custoText)) {
                    custoUnitario = Double.parseDouble(custoText.trim().replace(",", "."));
                }
                if (fim >= inicio) {
                    intervalos.add(new Intervalo(inicio, fim, desconto, custoUnitario));
                }
            } catch (Exception ignored) {
            }
        }
        return intervalos;
    }

    private BigDecimal parseDecimal(String value) {
        if (value == null) {
            throw new NumberFormatException("empty");
        }
        String normalized = value.trim().replace(",", ".");
        return new BigDecimal(normalized);
    }

    private BigDecimal parseDecimalSafe(String value, BigDecimal fallback) {
        try {
            return parseDecimal(value);
        } catch (Exception ex) {
            return fallback;
        }
    }

    private int parseIntSafe(String value, int fallback) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception ex) {
            return fallback;
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String formatarDecimal(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private static class IntervaloRow {
        private final HBox container;
        private final TextField txtInicio;
        private final TextField txtFim;
        private final TextField txtPercent;
        private final TextField txtCustoUnitario;

        private IntervaloRow(HBox container, TextField txtInicio, TextField txtFim, TextField txtPercent, TextField txtCustoUnitario) {
            this.container = container;
            this.txtInicio = txtInicio;
            this.txtFim = txtFim;
            this.txtPercent = txtPercent;
            this.txtCustoUnitario = txtCustoUnitario;
        }
    }

    private static class Intervalo {
        private final int inicio;
        private final int fim;
        private final BigDecimal desconto;
        private final Double custoUnitario;

        private Intervalo(int inicio, int fim, BigDecimal desconto, Double custoUnitario) {
            this.inicio = inicio;
            this.fim = fim;
            this.desconto = desconto;
            this.custoUnitario = custoUnitario;
        }
    }

    private static class EscalaSelecionada {
        private final BigDecimal descontoPercentual;
        private final Double custoUnitario;

        private EscalaSelecionada(BigDecimal descontoPercentual, Double custoUnitario) {
            this.descontoPercentual = descontoPercentual;
            this.custoUnitario = custoUnitario;
        }
    }
}