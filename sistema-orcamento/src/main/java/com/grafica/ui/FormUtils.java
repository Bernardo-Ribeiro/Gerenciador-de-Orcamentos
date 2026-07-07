package com.grafica.ui;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

/**
 * Utility class for form validation and field handling
 */
public class FormUtils {
    
    private FormUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Checks if a text field is empty or null
     */
    public static boolean isEmpty(TextField campo) {
        return campo == null || campo.getText() == null || campo.getText().trim().isEmpty();
    }
    
    /**
     * Checks if a combo box has no selection
     */
    public static boolean isEmpty(ComboBox<?> campo) {
        return campo == null || campo.getValue() == null;
    }
    
    /**
     * Validates required fields
     * @return error message if invalid, null if valid
     */
    public static String validarObrigatorios(String nomeCampo, TextField... campos) {
        for (TextField campo : campos) {
            if (isEmpty(campo)) {
                return nomeCampo + " é obrigatório.";
            }
        }
        return null;
    }
    
    /**
     * Validates if a combo box has a selection
     * @return error message if invalid, null if valid
     */
    public static String validarObrigatorios(String nomeCampo, ComboBox<?>... campos) {
        for (ComboBox<?> campo : campos) {
            if (isEmpty(campo)) {
                return nomeCampo + " é obrigatório.";
            }
        }
        return null;
    }
    
    /**
     * Configures a TextField to accept only numeric input
     */
    public static void configurarCampoNumerico(TextField campo, boolean permitirDecimal) {
        campo.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                return;
            }
            
            String texto = permitirDecimal 
                ? newVal.replaceAll("[^0-9.,]", "") 
                : newVal.replaceAll("[^0-9]", "");
            
            if (permitirDecimal) {
                // Ensure only one decimal separator
                int lastComma = texto.lastIndexOf(',');
                int lastDot = texto.lastIndexOf('.');
                int decimalPos = Math.max(lastComma, lastDot);
                
                if (decimalPos >= 0) {
                    String parteInteira = texto.substring(0, decimalPos);
                    String parteDecimal = texto.substring(decimalPos + 1).replaceAll("[^0-9]", "");
                    texto = parteInteira + "." + parteDecimal;
                }
            }
            
            if (!texto.equals(newVal)) {
                campo.setText(texto);
            }
        });
    }
    
    /**
     * Configures a TextField to accept only integer input
     */
    public static void configurarCampoInteiro(TextField campo) {
        configurarCampoNumerico(campo, false);
    }
    
    /**
     * Configures a TextField for currency input (R$)
     */
    public static void configurarCampoMoeda(TextField campo) {
        configurarCampoNumerico(campo, true);
    }
    
    /**
     * Configures a StringConverter for a ComboBox
     */
    public static <T> void configurarConverter(ComboBox<T> combo, 
                                                java.util.function.Function<T, String> toStringFunc) {
        combo.setConverter(new StringConverter<>() {
            @Override
            public String toString(T object) {
                return object == null ? "" : toStringFunc.apply(object);
            }
            
            @Override
            public T fromString(String string) {
                return null;
            }
        });
    }
    
    /**
     * Gets trimmed text from a TextField or empty string if null
     */
    public static String getTexto(TextField campo) {
        if (campo == null || campo.getText() == null) {
            return "";
        }
        return campo.getText().trim();
    }
    
    /**
     * Gets selected value from a ComboBox or null if not selected
     */
    public static <T> T getValor(ComboBox<T> combo) {
        return combo == null ? null : combo.getValue();
    }
    
    /**
     * Sets text to a TextField handling null values
     */
    public static void setTexto(TextField campo, String valor) {
        if (campo != null) {
            campo.setText(valor == null ? "" : valor);
        }
    }
    
    /**
     * Sets value to a ComboBox handling null values
     */
    public static <T> void setValor(ComboBox<T> combo, T valor) {
        if (combo != null) {
            combo.setValue(valor);
        }
    }
}