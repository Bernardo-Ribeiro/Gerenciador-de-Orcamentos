package com.grafica.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;

import java.io.IOException;

public class AjustesController {

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
	private Label breadcrumbLabel;

	@FXML
	private StackPane contentArea;

	private Button botaoAtual;

	@FXML
	private void initialize() {
		marcarBotaoAtivo(btnGestaoUsuarios);
		carregarConteudo("/com/grafica/view/ajustes/gestao-usuarios.fxml");
	}

	@FXML
	private void selecionarGestaoUsuarios() {
		marcarBotaoAtivo(btnGestaoUsuarios);
		carregarConteudo("/com/grafica/view/ajustes/gestao-usuarios.fxml");
	}

	@FXML
	private void selecionarCustosInsumos() {
		marcarBotaoAtivo(btnCustosInsumos);
		carregarConteudo("/com/grafica/view/ajustes/custos-insumos.fxml");
	}

	@FXML
	private void selecionarEscalaProdutiva() {
		marcarBotaoAtivo(btnEscalaProdutiva);
		carregarConteudo("/com/grafica/view/ajustes/escala-produtiva.fxml");
	}

	@FXML
	private void selecionarMargensLucro() {
		marcarBotaoAtivo(btnMargensLucro);
		carregarConteudo("/com/grafica/view/ajustes/margens-lucro.fxml");
	}

	@FXML
	private void selecionarConfiguracoesPdf() {
		marcarBotaoAtivo(btnConfiguracoesPdf);
		carregarConteudo("/com/grafica/view/ajustes/configuracoes-pdf.fxml");
	}

	private void marcarBotaoAtivo(Button botao) {
		if (botaoAtual != null) {
			botaoAtual.getStyleClass().remove("active");
		}

		if (!botao.getStyleClass().contains("active")) {
			botao.getStyleClass().add("active");
		}

		botaoAtual = botao;
	}

	private void carregarConteudo(String fxmlPath) {
		if (contentArea == null) {
			return;
		}

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
			Parent content = loader.load();
			contentArea.getChildren().setAll(content);
		} catch (IOException exception) {
			contentArea.getChildren().setAll(new Label("Não foi possível carregar a seção de ajustes."));
		}
	}
}