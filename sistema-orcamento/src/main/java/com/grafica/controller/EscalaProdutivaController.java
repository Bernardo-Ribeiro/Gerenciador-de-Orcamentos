package com.grafica.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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

    private final List<IntervaloRow> linhas = new ArrayList<>();

    @FXML
    private void initialize() {
        adicionarIntervaloComValores(1, 100, new BigDecimal("0"));
        adicionarIntervaloComValores(101, 500, new BigDecimal("5"));
        adicionarIntervaloComValores(501, 2000, new BigDecimal("12"));
        atualizarSimulacao();
    }

    @FXML
    private void adicionarIntervalo() {
        adicionarIntervaloComValores(null, null, null);
    }

    private void adicionarIntervaloComValores(Integer inicio, Integer fim, BigDecimal desconto) {
        TextField txtInicio = criarCampoNumero("Inicial", 110);
        TextField txtFim = criarCampoNumero("Final", 110);
        TextField txtPercent = criarCampoNumero("%", 70);

        if (inicio != null) {
            txtInicio.setText(String.valueOf(inicio));
        }
        if (fim != null) {
            txtFim.setText(String.valueOf(fim));
        }
        if (desconto != null) {
            txtPercent.setText(desconto.stripTrailingZeros().toPlainString());
        }

        Label lblPercent = new Label("%");
        lblPercent.getStyleClass().add("scale-suffix");

        HBox percentBox = new HBox(6, txtPercent, lblPercent);
        percentBox.getStyleClass().add("scale-percent-box");
        percentBox.setPrefWidth(130);

        Button btnExcluir = new Button("Excluir");
        btnExcluir.getStyleClass().add("scale-delete-button");
        btnExcluir.setPrefWidth(90);

        HBox row = new HBox(12, txtInicio, txtFim, percentBox, btnExcluir);
        row.getStyleClass().add("scale-row");

        IntervaloRow intervaloRow = new IntervaloRow(row, txtInicio, txtFim, txtPercent);
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
        List<Intervalo> intervalos = validarIntervalos();
        if (intervalos == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Escala salva");
        alert.setHeaderText(null);
        alert.setContentText("Intervalos validados com sucesso.");
        alert.showAndWait();
    }

    @FXML
    private void atualizarSimulacao() {
        int quantidade = parseIntSafe(txtQuantidadeSimulacao != null ? txtQuantidadeSimulacao.getText() : null, 0);
        BigDecimal valorBase = parseDecimalSafe(txtValorBaseSimulacao != null ? txtValorBaseSimulacao.getText() : null, BigDecimal.ZERO);

        BigDecimal descontoPercentual = encontrarDescontoParaQuantidade(quantidade);
        BigDecimal descontoValor = valorBase.multiply(descontoPercentual)
            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal totalFinal = valorBase.subtract(descontoValor).max(BigDecimal.ZERO);

        if (lblDescontoTitulo != null) {
            lblDescontoTitulo.setText("Desconto (" + descontoPercentual.stripTrailingZeros().toPlainString() + "%):");
        }
        if (lblDescontoValor != null) {
            lblDescontoValor.setText("- R$ " + formatarDecimal(descontoValor));
        }
        if (lblTotalValor != null) {
            lblTotalValor.setText("R$ " + formatarDecimal(totalFinal));
        }
    }

    private BigDecimal encontrarDescontoParaQuantidade(int quantidade) {
        List<Intervalo> intervalos = coletarIntervalosValidos();
        intervalos.sort(Comparator.comparingInt(intervalo -> intervalo.inicio));

        for (Intervalo intervalo : intervalos) {
            if (quantidade >= intervalo.inicio && quantidade <= intervalo.fim) {
                return intervalo.desconto;
            }
        }

        return BigDecimal.ZERO;
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

            if (isBlank(inicioText) || isBlank(fimText) || isBlank(descontoText)) {
                mostrarErro("Preencha todos os campos dos intervalos.");
                return null;
            }

            int inicio;
            int fim;
            BigDecimal desconto;
            try {
                inicio = Integer.parseInt(inicioText.trim());
                fim = Integer.parseInt(fimText.trim());
                desconto = parseDecimal(descontoText);
            } catch (NumberFormatException ex) {
                mostrarErro("Use apenas numeros nos intervalos e no desconto.");
                return null;
            }

            if (inicio < 0 || fim < 0 || fim < inicio) {
                mostrarErro("Informe intervalos validos (inicio <= fim)." );
                return null;
            }

            if (desconto.compareTo(BigDecimal.ZERO) < 0 || desconto.compareTo(new BigDecimal("100")) > 0) {
                mostrarErro("O desconto deve estar entre 0 e 100.");
                return null;
            }

            intervalos.add(new Intervalo(inicio, fim, desconto));
        }

        intervalos.sort(Comparator.comparingInt(intervalo -> intervalo.inicio));
        for (int i = 1; i < intervalos.size(); i++) {
            Intervalo anterior = intervalos.get(i - 1);
            Intervalo atual = intervalos.get(i);
            if (atual.inicio <= anterior.fim) {
                mostrarErro("Intervalos nao podem se sobrepor.");
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
                if (fim >= inicio) {
                    intervalos.add(new Intervalo(inicio, fim, desconto));
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

        private IntervaloRow(HBox container, TextField txtInicio, TextField txtFim, TextField txtPercent) {
            this.container = container;
            this.txtInicio = txtInicio;
            this.txtFim = txtFim;
            this.txtPercent = txtPercent;
        }
    }

    private static class Intervalo {
        private final int inicio;
        private final int fim;
        private final BigDecimal desconto;

        private Intervalo(int inicio, int fim, BigDecimal desconto) {
            this.inicio = inicio;
            this.fim = fim;
            this.desconto = desconto;
        }
    }
}
