package com.grafica.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

/**
 * Utility class for common UI operations
 */
public class UiUtils {
    
    private static final NumberFormat MOEDA_FORMAT = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private static final DateTimeFormatter DATA_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATA_RESUMO_FORMAT = DateTimeFormatter.ofPattern("dd/MM");
    
    private UiUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Shows an error dialog
     */
    public static void mostrarErro(Window owner, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        if (owner != null) {
            alert.initOwner(owner);
        }
        alert.showAndWait();
    }
    
    /**
     * Shows an information dialog
     */
    public static void mostrarInfo(Window owner, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        if (owner != null) {
            alert.initOwner(owner);
        }
        alert.showAndWait();
    }
    
    /**
     * Shows a confirmation dialog
     * @return true if user confirmed
     */
    public static boolean mostrarConfirmacao(Window owner, String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        if (owner != null) {
            alert.initOwner(owner);
        }
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    
    /**
     * Formats a BigDecimal value as Brazilian currency (R$)
     */
    public static String formatarMoeda(BigDecimal valor) {
        if (valor == null) {
            return "R$ 0,00";
        }
        return MOEDA_FORMAT.format(valor);
    }
    
    /**
     * Formats a LocalDate as dd/MM/yyyy
     */
    public static String formatarData(LocalDate data) {
        if (data == null) {
            return "-";
        }
        return data.format(DATA_FORMAT);
    }
    
    /**
     * Formats a LocalDate for summary views (dd/MM)
     */
    public static String formatarDataResumo(LocalDate data) {
        if (data == null) {
            return "-";
        }
        if (LocalDate.now().equals(data)) {
            return "Hoje";
        }
        if (LocalDate.now().minusDays(1).equals(data)) {
            return "Ontem";
        }
        return data.format(DATA_RESUMO_FORMAT);
    }
    
    /**
     * Parses a string to BigDecimal, handling comma as decimal separator
     */
    public static BigDecimal parsearDecimal(String valor, BigDecimal padrao) {
        if (valor == null || valor.trim().isEmpty()) {
            return padrao;
        }
        try {
            String normalized = valor.trim().replace(',', '.');
            return new BigDecimal(normalized);
        } catch (NumberFormatException e) {
            return padrao;
        }
    }
    
    /**
     * Parses a string to int with fallback value
     */
    public static int parsearInteiro(String valor, int padrao) {
        if (valor == null || valor.trim().isEmpty()) {
            return padrao;
        }
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            return padrao;
        }
    }
    
    /**
     * Normalizes status string to standard format
     */
    public static String normalizarStatus(String status) {
        if (status == null || status.isBlank()) {
            return "PENDENTE";
        }
        return status.trim().toUpperCase();
    }
    
    /**
     * Checks if status is considered completed
     */
    public static boolean isStatusConcluido(String status) {
        if (status == null) {
            return false;
        }
        String normalized = status.trim().toUpperCase();
        return normalized.equals("APROVADO") || 
               normalized.equals("VENDIDO") || 
               normalized.equals("FECHADO");
    }
    
    /**
     * Formats an order/budget code with leading zeros
     */
    public static String formatarCodigoOrcamento(Integer id) {
        if (id == null) {
            return "ORC-S/N";
        }
        return String.format("ORC-%04d", id);
    }
    
    /**
     * Strips null values from BigDecimal, returning ZERO instead
     */
    public static BigDecimal valorSeguro(BigDecimal valor) {
        return valor == null ? BigDecimal.ZERO : valor;
    }
    
    /**
     * Rounds a BigDecimal to 2 decimal places
     */
    public static BigDecimal arredondar(BigDecimal valor) {
        if (valor == null) {
            return BigDecimal.ZERO;
        }
        return valor.setScale(2, RoundingMode.HALF_UP);
    }
}