package com.grafica.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class AjustesController {

    @FXML
    private StackPane contentArea;

    @FXML
    private Button btnGestaoUsuarios;

    @FXML
    private Button btnCustosInsumos;

    @FXML
    private Button btnEscalaProdutiva;

    @FXML
    private Button btnMargensLucro;

    @FXML
    private Button btnConfiguracoesPdf;

    @FXML
    private void initialize() {
        carregarSubpantalla("ajustes/gestao-usuarios.fxml");
        marcarBotaoAtivo(btnGestaoUsuarios);
    }

    @FXML
    private void selecionarGestaoUsuarios(ActionEvent event) {
        carregarSubpantalla("ajustes/gestao-usuarios.fxml");
        marcarBotaoAtivo(btnGestaoUsuarios);
    }

    @FXML
    private void selecionarCustosInsumos(ActionEvent event) {
        carregarSubpantalla("ajustes/custos-insumos.fxml");
        marcarBotaoAtivo(btnCustosInsumos);
    }

    @FXML
    private void selecionarEscalaProdutiva(ActionEvent event) {
        carregarSubpantalla("ajustes/escala-produtiva.fxml");
        marcarBotaoAtivo(btnEscalaProdutiva);
    }

    @FXML
    private void selecionarMargensLucro(ActionEvent event) {
        carregarSubpantalla("ajustes/margens-lucro.fxml");
        marcarBotaoAtivo(btnMargensLucro);
    }

    @FXML
    private void selecionarConfiguracoesPdf(ActionEvent event) {
        carregarSubpantalla("ajustes/configuracoes-pdf.fxml");
        marcarBotaoAtivo(btnConfiguracoesPdf);
    }

    private void marcarBotaoAtivo(Button botaoAtivo) {
        Button[] botoes = {
            btnGestaoUsuarios,
            btnCustosInsumos,
            btnEscalaProdutiva,
            btnMargensLucro,
            btnConfiguracoesPdf
        };

        for (Button botao : botoes) {
            if (botao == null) {
                continue;
            }
            botao.getStyleClass().remove("active");
        }

        if (botaoAtivo != null && !botaoAtivo.getStyleClass().contains("active")) {
            botaoAtivo.getStyleClass().add("active");
        }
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