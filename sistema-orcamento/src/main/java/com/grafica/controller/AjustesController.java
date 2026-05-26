package com.grafica.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class AjustesController {

    @FXML
    private StackPane contentArea;

    @FXML
    private void initialize() {
    }

    @FXML
    private void selecionarGestaoUsuarios(ActionEvent event) {
        carregarSubpantalla("ajustes/gestao-usuarios.fxml");
    }

    @FXML
    private void selecionarCustosInsumos(ActionEvent event) {
        carregarSubpantalla("ajustes/custos-insumos.fxml");
    }

    @FXML
    private void selecionarEscalaProdutiva(ActionEvent event) {
        carregarSubpantalla("ajustes/escala-produtiva.fxml");
    }

    @FXML
    private void selecionarMargensLucro(ActionEvent event) {
        carregarSubpantalla("ajustes/margens-lucro.fxml");
    }

    @FXML
    private void selecionarConfiguracoesPdf(ActionEvent event) {
        carregarSubpantalla("ajustes/configuracoes-pdf.fxml");
    }

    private void carregarSubpantalla(String resourcePath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/grafica/view/" + resourcePath));
            Node node = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(node);
        } catch (IOException e) {
            System.err.println("Error cargando subpantalla: " + resourcePath);
            e.printStackTrace();
        }
    }
}