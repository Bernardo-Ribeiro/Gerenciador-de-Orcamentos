package com.grafica.controller;

import com.grafica.model.Usuario;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

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
	private Button btnSair;

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
			configurarControllerFilho(loader.getController());
            contentArea.getChildren().setAll(pantalla);
        } catch (IOException e) {
            System.err.println("Error al cargar pantalla: " + fxmlPath);
            e.printStackTrace();
        }
    }

	/**
	 * Marca el botón actual como activo visualmente
	 */
	private void configurarControllerFilho(Object controller) {
		if (controller instanceof DashboardController dashboardController) {
			dashboardController.setMainController(this);
			dashboardController.setUsuarioLogado(usuarioLogado);
		} else if (controller instanceof OrcamentoController orcamentoController) {
			orcamentoController.setMainController(this);
		}
	}

	private void marcarBotaoAtivo(Button botao) {
		Button[] botoes = {
			btnDashboard, btnClientes, btnMateriais, btnOrcamentos, btnRelatorios, btnAjustes
		};
		for (Button item : botoes) {
			if (item != null) {
				item.getStyleClass().remove("active");
			}
		}
		botaoAtual = botao;
		if (botao != null && !botao.getStyleClass().contains("active")) {
			botao.getStyleClass().add("active");
		}
	}

	/**
	 * Obtiene el usuario logado
	 */
	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	@FXML
	private void sair() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/grafica/view/login.fxml"));
			Parent login = loader.load();
			Stage stage = (Stage) rootPane.getScene().getWindow();
			stage.setScene(new Scene(login));
			stage.setTitle("Login");
		} catch (IOException e) {
			Stage stage = (Stage) rootPane.getScene().getWindow();
			stage.close();
		}
	}
}
