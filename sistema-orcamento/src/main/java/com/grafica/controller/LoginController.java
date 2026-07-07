package com.grafica.controller;

import com.grafica.dao.UsuarioDAO;
import com.grafica.model.Usuario;
import com.grafica.ui.UiUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class LoginController {

    @FXML
    private StackPane rootPane;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private ImageView frostLayer;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField senhaField;

    private UsuarioDAO usuarioDAO;

    @FXML
    private void initialize() {
        usuarioDAO = new UsuarioDAO();
        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());
        frostLayer.fitWidthProperty().bind(rootPane.widthProperty());
        frostLayer.fitHeightProperty().bind(rootPane.heightProperty());
    }

    @FXML
    private void fazerLogin() {
        String email = emailField.getText() != null ? emailField.getText().trim().toLowerCase() : "";
        String senha = senhaField.getText() != null ? senhaField.getText().trim() : "";

        if (email.isEmpty() || senha.isEmpty()) {
            exibirErro("Email e senha são obrigatórios");
            return;
        }

        try {
            Usuario usuario = usuarioDAO.autenticar(email, senha);
            if (usuario != null) {
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
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Sistema de Orçamentos - Gráfica");
            forceMaximized(stage);
            mainController.iniciarTelaPrincipal();
        } catch (IOException e) {
            exibirErro("Erro ao carregar a tela principal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void forceMaximized(Stage stage) {
        Rectangle2D bounds = getCurrentScreenBounds(stage);
        stage.setMaximized(false);
        stage.show();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.setMaximized(true);
        Platform.runLater(() -> stage.setMaximized(true));
    }

    private Rectangle2D getCurrentScreenBounds(Stage stage) {
        List<Screen> screens = Screen.getScreensForRectangle(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());
        Screen screen = screens.isEmpty() ? Screen.getPrimary() : screens.get(0);
        return screen.getVisualBounds();
    }

    private void exibirErro(String mensagem) {
        UiUtils.mostrarErro(rootPane.getScene().getWindow(), mensagem);
    }
}