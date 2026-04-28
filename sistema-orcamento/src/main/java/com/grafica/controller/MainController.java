package com.grafica.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {
	@FXML
	private BorderPane rootPane;

	@FXML
	private Label userLabel;

	@FXML
	private StackPane contentArea;

	@FXML
	private Button btnDashboard;

	@FXML
	private Button btnClientes;

	@FXML
	private Button btnOrcamentos;

	@FXML
	private Button btnRelatorios;

	@FXML
	private Button btnAjustes;

	private Button botaoAtual;

	@FXML
	private void initialize() {
		marcarBotaoAtivo(btnDashboard);
		abrirDashboard();
	}

	public void setUsuarioLogado(String usuario) {
		if (userLabel != null) {
			userLabel.setText("Bem-vindo, " + usuario);
		}
	}

	@FXML
	private void abrirDashboard() {
		marcarBotaoAtivo(btnDashboard);
		carregarConteudo("/com/grafica/view/dashboard.fxml");
	}

	@FXML
	private void abrirClientes() {
		marcarBotaoAtivo(btnClientes);
		carregarConteudo("/com/grafica/view/clientes.fxml");
	}

	@FXML
	private void abrirOrcamentos() {
		marcarBotaoAtivo(btnOrcamentos);
		carregarConteudo("/com/grafica/view/orcamentos.fxml");
	}

	@FXML
	private void abrirRelatorios() {
		marcarBotaoAtivo(btnRelatorios);
		carregarConteudo("/com/grafica/view/relatorios.fxml");
	}

	@FXML
	private void abrirAjustes() {
		marcarBotaoAtivo(btnAjustes);
		carregarConteudo("/com/grafica/view/ajustes.fxml");
	}

	@FXML
	private void sair() {
		if (rootPane != null && rootPane.getScene() != null) {
			rootPane.getScene().getWindow().hide();
		}
	}

	private void marcarBotaoAtivo(Button botao) {
		if (botaoAtual != null) {
			botaoAtual.getStyleClass().remove("active");
		}
		botao.getStyleClass().add("active");
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
		} catch (Exception exception) {
			contentArea.getChildren().setAll(new Label("Não foi possível carregar a tela."));
		}
	}
}
