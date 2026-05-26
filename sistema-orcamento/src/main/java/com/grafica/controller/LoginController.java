package com.grafica.controller;

import com.grafica.dao.UsuarioDAO;
import com.grafica.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField senhaField;

    private UsuarioDAO usuarioDAO;

    @FXML
    private void initialize() {
        usuarioDAO = new UsuarioDAO();
    }

    @FXML
    private void fazerLogin() {
        String email = emailField.getText();
        String senha = senhaField.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            exibirErro("Email e senha são obrigatórios");
            return;
        }

        try {
            Usuario usuario = usuarioDAO.autenticar(email, senha);
            if (usuario != null) {
                System.out.println("Usuário " + usuario.getNome() + " logado com sucesso");
                carregarPantallaPrincipal(usuario);
            } else {
                exibirErro("Email ou senha inválidos");
            }
        } catch (Exception e) {
            exibirErro("Erro ao fazer login: " + e.getMessage());
        }
    }

    private void carregarPantallaPrincipal(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/grafica/view/main.fxml"));
            Parent root = loader.load();
            
            MainController mainController = loader.getController();
            mainController.setUsuarioLogado(usuario);
            
            Stage stage = (Stage) emailField.getScene().getWindow();
            Scene scene = new Scene(root, 1024, 768);
            stage.setScene(scene);
            stage.setTitle("Sistema de Orçamentos - Gráfica");
            stage.show();
            mainController.iniciarTelaPrincipal();
        } catch (IOException e) {
            exibirErro("Erro ao carregar a tela principal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void exibirErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
