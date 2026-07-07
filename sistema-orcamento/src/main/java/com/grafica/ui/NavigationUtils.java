package com.grafica.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Utility class for navigation and screen loading
 */
public class NavigationUtils {
    
    private NavigationUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Loads an FXML view and returns the loaded parent node
     */
    public static Parent carregarView(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(NavigationUtils.class.getResource(fxmlPath));
        return loader.load();
    }
    
    /**
     * Loads an FXML view and returns the controller
     */
    @SuppressWarnings("unchecked")
    public static <T> T carregarViewComController(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(NavigationUtils.class.getResource(fxmlPath));
        Parent root = loader.load();
        return loader.getController();
    }
    
    /**
     * Opens a modal dialog window
     */
    public static void abrirJanelaModal(Window owner, String titulo, String fxmlPath, double largura, double altura) {
        try {
            Parent root = carregarView(fxmlPath);
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(owner);
            stage.setScene(new Scene(root, largura, altura));
            stage.showAndWait();
        } catch (IOException e) {
            UiUtils.mostrarErro(owner, "Erro ao abrir janela: " + e.getMessage());
        }
    }
    
    /**
     * Opens a modeless dialog window
     */
    public static void abrirJanela(Window owner, String titulo, String fxmlPath, double largura, double altura) {
        try {
            Parent root = carregarView(fxmlPath);
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.initOwner(owner);
            stage.setScene(new Scene(root, largura, altura));
            stage.show();
        } catch (IOException e) {
            UiUtils.mostrarErro(owner, "Erro ao abrir janela: " + e.getMessage());
        }
    }
    
    /**
     * Opens a modal dialog and executes a callback with the controller after loading
     */
    @SuppressWarnings("unchecked")
    public static <T> void abrirJanelaModalComCallback(Window owner, String titulo, String fxmlPath, 
                                                        double largura, double altura, Consumer<T> callback) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtils.class.getResource(fxmlPath));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(owner);
            stage.setScene(new Scene(root, largura, altura));
            
            if (callback != null) {
                callback.accept((T) loader.getController());
            }
            
            stage.showAndWait();
        } catch (IOException e) {
            UiUtils.mostrarErro(owner, "Erro ao abrir janela: " + e.getMessage());
        }
    }
    
    /**
     * Closes the window containing the given node
     */
    public static void fecharJanela(javafx.scene.Node node) {
        if (node != null && node.getScene() != null && node.getScene().getWindow() != null) {
            ((Stage) node.getScene().getWindow()).close();
        }
    }
}