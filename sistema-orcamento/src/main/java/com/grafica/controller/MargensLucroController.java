package com.grafica.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class MargensLucroController {

    @FXML private TextField pctCategoria1;
    @FXML private TextField pctCategoria2;
    @FXML private TextField pctCategoria3;
    @FXML private TextField pctCategoria4;
    @FXML private TextField pctCategoria5;
    @FXML private TextField pctCategoria6;

    @FXML
    private void novaCategoria() {
        mostrarInfo("Nesta etapa as categorias já estão pré-cadastradas. O próximo passo será tornar esta lista dinâmica.");
    }

    @FXML
    private void salvarPadroes() {
        if (isValido(pctCategoria1) && isValido(pctCategoria2) && isValido(pctCategoria3) && isValido(pctCategoria4) && isValido(pctCategoria5) && isValido(pctCategoria6)) {
            mostrarInfo("Margens salvas com sucesso.");
        } else {
            mostrarErro("Preencha os percentuais com números válidos.");
        }
    }

    @FXML
    private void cancelarAlteracoes() {
        mostrarInfo("Alterações canceladas. Os valores da tela permanecem como referência visual.");
    }

    private boolean isValido(TextField campo) {
        try {
            String valor = campo.getText() == null ? "" : campo.getText().trim().replace(',', '.');
            Double.parseDouble(valor);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Margens de Lucro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
