package com.grafica.controller;

import com.grafica.model.Usuario;
import com.grafica.controller.DashboardController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import javafx.stage.Stage;

/**
 * MainController - Controlador principal de la aplicación
 * Orquesta la navegación entre secciones principales
 * 
 * Estructura de navegación:
 * - Dashboard (inicio)
 * - Clientes (CRUD de clientes)
 * - Orçamentos (crear, editar, consultar presupuestos)
 * - Relatórios (análisis y reportes)
 * - Ajustes (configuración del usuario)
 */
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
	private Button btnMateriais;

	@FXML
	private Button btnOrcamentos;

	@FXML
	private Button btnRelatorios;

	@FXML
	private Button btnAjustes;

	@FXML
	private Button btnLogout;

	private Button botaoAtual;
	private Usuario usuarioLogado;

	@FXML
	private void initialize() {
		marcarBotaoAtivo(btnDashboard);
	}

	/**
	 * Establece el usuario logado y actualiza la etiqueta de bienvenida
	 */
	public void setUsuarioLogado(Usuario usuario) {
		this.usuarioLogado = usuario;
		if (userLabel != null && usuario != null) {
			userLabel.setText("Bem-vindo, " + usuario.getNome());
		}
	}

	/**
	 * Abre la vista Dashboard (inicio)
	 */
	public void iniciarTelaPrincipal() {
		abrirDashboard();
	}

	/**
	 * Abre la vista Dashboard (inicio)
	 */
	@FXML
	private void abrirDashboard() {
		marcarBotaoAtivo(btnDashboard);
		carregarPantalla("view/dashboard.fxml");
	}

	public void abrirDashboardTela() {
		abrirDashboard();
	}

	/**
	 * Abre la vista de Gestión de Clientes
	 */
	@FXML
	private void abrirClientes() {
		marcarBotaoAtivo(btnClientes);
		carregarPantalla("view/clientes.fxml");
	}

	public void abrirClientesTela() {
		abrirClientes();
	}

	/**
	 * Abre la vista de Gestión de Materiais (Catálogo)
	 */
	@FXML
	private void abrirMateriais() {
		marcarBotaoAtivo(btnMateriais);
		carregarPantalla("view/materiais.fxml");
	}

	public void abrirMateriaisTela() {
		abrirMateriais();
	}

	/**
	 * Abre la vista de Gestión de Orçamentos
	 */
	@FXML
	private void abrirOrcamentos() {
		marcarBotaoAtivo(btnOrcamentos);
		carregarPantalla("view/orcamentos.fxml");
	}

	public void abrirOrcamentosTela() {
		abrirOrcamentos();
	}

	/**
	 * Abre la vista de Relatórios
	 */
	@FXML
	private void abrirRelatorios() {
		marcarBotaoAtivo(btnRelatorios);
		carregarPantalla("view/relatorios.fxml");
	}

	public void abrirRelatoriosTela() {
		abrirRelatorios();
	}

	/**
	 * Abre la vista de Ajustes
	 */
	@FXML
	private void abrirAjustes() {
		marcarBotaoAtivo(btnAjustes);
		carregarPantalla("view/ajustes.fxml");
	}

	public void abrirAjustesTela() {
		abrirAjustes();
	}

	/**
	 * Carga una pantalla FXML en el área de contenido
	 */
	private void carregarPantalla(String fxmlPath) {
        try {
			java.net.URL location = getClass().getResource("/com/grafica/" + fxmlPath);
			if (location == null) {
				throw new IllegalStateException("FXML no encontrado: /com/grafica/" + fxmlPath);
			}
			FXMLLoader loader = new FXMLLoader(location);
            Parent pantalla = loader.load();
			Object controller = loader.getController();
				if (controller instanceof DashboardController dashboardController) {
					dashboardController.setMainController(this);
					dashboardController.setUsuarioLogado(usuarioLogado);
				}
            contentArea.getChildren().clear();
            contentArea.getChildren().add(pantalla);
        } catch (IOException e) {
            System.err.println("Error al cargar pantalla: " + fxmlPath);
            e.printStackTrace();
        }
    }

	/**
	 * Marca el botón actual como activo visualmente
	 */
	private void marcarBotaoAtivo(Button botao) {
		if (botaoAtual != null) {
			botaoAtual.setStyle("");
		}
		botaoAtual = botao;
		if (botao != null) {
			botao.setStyle("-fx-text-fill: #0078d4; -fx-font-weight: bold;");
		}
	}

	/**
	 * Obtiene el usuario logado
	 */
	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	private void carregarConteudo(String fxmlPath) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
			Parent content = loader.load();
			contentArea.getChildren().setAll(content);
		} catch (Exception e) {
			contentArea.getChildren().setAll(new Label("Erro ao carregar tela: " + fxmlPath));
		}
	}

	@FXML
	private void sair() {
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.close();
	}
}
