package com.grafica.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Map;

public class AjustesController {

    private static final Map<String, String> BREADCRUMBS = Map.of(
        "ajustes/gestao-usuarios.fxml", "Início   >   Painel Administrador   >   Gestão de Usuários",
        "ajustes/gestao-layouts.fxml", "Início   >   Painel Administrador   >   Gestão de Layouts",
        "ajustes/gestao-produtos.fxml", "Início   >   Painel Administrador   >   Gestão de Produtos",
        "ajustes/custos-insumos.fxml", "Início   >   Painel Administrador   >   Custos de Insumos",
        "ajustes/escala-produtiva.fxml", "Início   >   Painel Administrador   >   Escala Produtiva",
        "ajustes/margens-lucro.fxml", "Início   >   Painel Administrador   >   Margens de Lucro",
        "ajustes/configuracoes-pdf.fxml", "Início   >   Painel Administrador   >   Configurações de PDF"
    );

    @FXML
    private StackPane contentArea;

    @FXML
    private Label breadcrumbLabel;

    @FXML
    private Button btnGestaoUsuarios;

    @FXML
    private Button btnGestaoLayouts;

    @FXML
    private Button btnGestaoProdutos;

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
    private void selecionarGestaoLayouts(ActionEvent event) {
        carregarSubpantalla("ajustes/gestao-layouts.fxml");
        marcarBotaoAtivo(btnGestaoLayouts);
    }

    @FXML
    private void selecionarGestaoProdutos(ActionEvent event) {
        carregarSubpantalla("ajustes/gestao-produtos.fxml");
        marcarBotaoAtivo(btnGestaoProdutos);
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
            btnGestaoLayouts,
            btnGestaoProdutos,
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
            contentArea.getChildren().setAll(node);
            atualizarBreadcrumb(resourcePath);
        } catch (IOException e) {
            System.err.println("Erro ao carregar sub-tela: " + resourcePath);
            e.printStackTrace();
        }
    }

    private void atualizarBreadcrumb(String resourcePath) {
        if (breadcrumbLabel == null) {
            return;
        }
        breadcrumbLabel.setText(BREADCRUMBS.getOrDefault(resourcePath, "Início   >   Painel Administrador"));
    }
}