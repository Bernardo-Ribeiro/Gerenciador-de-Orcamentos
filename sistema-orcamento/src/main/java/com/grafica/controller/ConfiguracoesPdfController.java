package com.grafica.controller;

import com.grafica.dao.ConfiguracaoPdfDAO;
import com.grafica.model.ConfiguracaoPdf;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

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
    
    private ConfiguracaoPdfDAO configDAO;
    private String caminhoLogoAtual;

    @FXML
    private void initialize() {
        configDAO = new ConfiguracaoPdfDAO();
        carregarConfiguracoes();
    }

    @FXML
    private void carregarLogo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Logomarca");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        
        File arquivoSelecionado = fileChooser.showOpenDialog(txtNomeComercial.getScene().getWindow());
        
        if (arquivoSelecionado != null) {
            try {
                String diretorioDestino = System.getProperty("user.home") + "/Orcamentos/logos/";
                File dir = new File(diretorioDestino);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                
                String nomeArquivo = "logo_" + System.currentTimeMillis() + "_" + arquivoSelecionado.getName();
                Path destino = Path.of(diretorioDestino, nomeArquivo);
                Files.copy(arquivoSelecionado.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
                
                caminhoLogoAtual = destino.toString();
                mostrarInfo("Logo carregada com sucesso!\nArquivo: " + nomeArquivo);
            } catch (Exception e) {
                mostrarErro("Erro ao carregar logo: " + e.getMessage());
            }
        }
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
        caminhoLogoAtual = null;
        mostrarInfo("Padrões restaurados.");
    }

    @FXML
    private void salvarConfiguracoes() {
        if (!camposValidos()) {
            mostrarErro("Preencha os campos obrigatórios (Nome, CNPJ, Telefone, Email) antes de salvar.");
            return;
        }
        
        try {
            ConfiguracaoPdf config = new ConfiguracaoPdf();
            config.setNomeEmpresa(txtNomeComercial.getText().trim());
            config.setCnpj(txtCnpj.getText().trim());
            config.setTelefone(txtTelefone.getText().trim());
            config.setEmail(txtEmail.getText().trim());
            config.setEndereco(txtEndereco.getText().trim());
            config.setLogoPath(caminhoLogoAtual != null ? caminhoLogoAtual : "");
            config.setRodape(txtObservacoes.getText().trim());
            config.setCores("#333333");
            config.setFonte("Helvetica");
            
            configDAO.salvar(config);
            
            mostrarInfo("Configurações de PDF salvas com sucesso!\n\nDados da empresa: " + config.getNomeEmpresa() + 
                "\nLogo: " + (config.getLogoPath() != null && !config.getLogoPath().isEmpty() ? "Configurada" : "Não configurada"));
        } catch (Exception e) {
            mostrarErro("Erro ao salvar configurações: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void carregarConfiguracoes() {
        try {
            ConfiguracaoPdf config = configDAO.obter();
            if (config != null) {
                txtNomeComercial.setText(config.getNomeEmpresa() != null ? config.getNomeEmpresa() : "");
                txtCnpj.setText(config.getCnpj() != null ? config.getCnpj() : "");
                txtTelefone.setText(config.getTelefone() != null ? config.getTelefone() : "");
                txtEmail.setText(config.getEmail() != null ? config.getEmail() : "");
                txtEndereco.setText(config.getEndereco() != null ? config.getEndereco() : "");
                txtObservacoes.setText(config.getRodape() != null ? config.getRodape() : "");
                caminhoLogoAtual = config.getLogoPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
