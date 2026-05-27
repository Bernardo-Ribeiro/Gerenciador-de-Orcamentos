package com.grafica.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ConfiguracoesPdfController {

    @FXML private TextField txtNomeComercial;
    @FXML private TextField txtCnpj;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtEndereco;
    @FXML private TextField txtTamanhoPapel;
    @FXML private TextField txtMargemSuperior;
    @FXML private TextField txtMargemLateral;
    @FXML private TextArea txtObservacoes;

    @FXML
    private void carregarLogo() {
        mostrarInfo("Carga de logo será integrada na próxima etapa.");
    }

    @FXML
    private void restaurarPadroes() {
        txtNomeComercial.setText("Serralheria Visual Pro");
        txtCnpj.setText("12.345.678/0001-90");
        txtTelefone.setText("(11) 98765-4321");
        txtEmail.setText("contato@visualpro.com.br");
        txtEndereco.setText("Av. Industrial, 1500 - Bloco B, Sao Paulo - SP");
        txtTamanhoPapel.setText("A4 (210 x 297mm)");
        txtMargemSuperior.setText("15");
        txtMargemLateral.setText("10");
        txtObservacoes.setText("Este orcamento tem validade de 10 dias uteis. Garantia de 12 meses contra defeitos de fabricacao.");
        mostrarInfo("Padrões restaurados.");
    }

    @FXML
    private void salvarConfiguracoes() {
        if (camposValidos()) {
            mostrarInfo("Configurações de PDF salvas com sucesso.");
        } else {
            mostrarErro("Preencha os campos obrigatórios antes de salvar.");
        }
    }

    private boolean camposValidos() {
        return naoVazio(txtNomeComercial) && naoVazio(txtCnpj) && naoVazio(txtTelefone) && naoVazio(txtEmail);
    }

    private boolean naoVazio(TextField campo) {
        return campo.getText() != null && !campo.getText().trim().isEmpty();
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
        alert.setTitle("PDF");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
