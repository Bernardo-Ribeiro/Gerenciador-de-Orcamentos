package com.grafica;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/grafica/view/login.fxml"));
        Scene scene = new Scene(loader.load());

        // Define o ícone da aplicação
        try {
            stage.getIcons().add(new javafx.scene.image.Image(
                getClass().getResourceAsStream("/com/grafica/img/icon.png")
            ));
        } catch (Exception e) {
            System.err.println("Aviso: Nao foi possivel carregar o icone da aplicacao.");
        }

        stage.setScene(scene);
        stage.setTitle("Login");
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
