package com.grafica.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Window;

/**
 * Base controller class providing common functionality for all controllers
 */
public abstract class BaseController {
    
    @FXML
    public void initialize() {
        onInit();
    }
    
    /**
     * Called when the controller is initialized
     * Override this method instead of initialize()
     */
    protected abstract void onInit();
    
    /**
     * Shows an error dialog
     */
    protected void mostrarErro(String mensagem) {
        UiUtils.mostrarErro(getWindow(), mensagem);
    }
    
    /**
     * Shows an information dialog
     */
    protected void mostrarInfo(String mensagem) {
        UiUtils.mostrarInfo(getWindow(), mensagem);
    }
    
    /**
     * Shows a confirmation dialog
     * @return true if user confirmed
     */
    protected boolean mostrarConfirmacao(String titulo, String mensagem) {
        return UiUtils.mostrarConfirmacao(getWindow(), titulo, mensagem);
    }
    
    /**
     * Gets the current window from any @FXML annotated node
     */
    protected Window getWindow() {
        return getScene() != null ? getScene().getWindow() : null;
    }
    
    /**
     * Gets the current scene - must be implemented by subclass
     */
    protected abstract javafx.scene.Scene getScene();
    
    /**
     * Closes the current window
     */
    protected void fecharJanela() {
        NavigationUtils.fecharJanela(getScene() != null ? getScene().getRoot() : null);
    }
    
    /**
     * Formats a value as Brazilian currency (R$)
     */
    protected String formatarMoeda(java.math.BigDecimal valor) {
        return UiUtils.formatarMoeda(valor);
    }
    
    /**
     * Formats a date as dd/MM/yyyy
     */
    protected String formatarData(java.time.LocalDate data) {
        return UiUtils.formatarData(data);
    }
    
    /**
     * Parses a string to BigDecimal
     */
    protected java.math.BigDecimal parsearDecimal(String valor, java.math.BigDecimal padrao) {
        return UiUtils.parsearDecimal(valor, padrao);
    }
    
    /**
     * Parses a string to int
     */
    protected int parsearInteiro(String valor, int padrao) {
        return UiUtils.parsearInteiro(valor, padrao);
    }
    
    /**
     * Checks if a button is active (has the active style class)
     */
    protected boolean isBotaoAtivo(Button botao) {
        return botao != null && botao.getStyleClass().contains("active");
    }
    
    /**
     * Sets a button as active and removes active from others
     */
    protected void marcarBotaoAtivo(Button botaoAtivo, Button... outrosBotoes) {
        if (botaoAtivo != null && !isBotaoAtivo(botaoAtivo)) {
            botaoAtivo.getStyleClass().add("active");
        }
        
        for (Button botao : outrosBotoes) {
            if (botao != null && isBotaoAtivo(botao)) {
                botao.getStyleClass().remove("active");
            }
        }
    }
    
    /**
     * Clears all text fields in a list
     */
    protected void limparCampos(javafx.scene.control.TextField... campos) {
        if (campos == null) {
            return;
        }
        for (javafx.scene.control.TextField campo : campos) {
            if (campo != null) {
                campo.clear();
            }
        }
    }
    
    /**
     * Resets combo boxes to null/first position
     */
    protected void limparCombos(javafx.scene.control.ComboBox<?>... combos) {
        if (combos == null) {
            return;
        }
        for (javafx.scene.control.ComboBox<?> combo : combos) {
            if (combo != null) {
                combo.setValue(null);
            }
        }
    }
}