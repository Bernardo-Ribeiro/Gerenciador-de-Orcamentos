package com.grafica.controller;

import com.grafica.dao.UsuarioDAO;
import com.grafica.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;

public class GestaoUsuariosController {

    @FXML
    private TextField txtBusca;

    @FXML
    private TableView<Usuario> tabelaUsuarios;

    @FXML
    private TableColumn<Usuario, String> colNome;

    @FXML
    private TableColumn<Usuario, String> colEmail;

    @FXML
    private TableColumn<Usuario, String> colCargo;

    @FXML
    private TableColumn<Usuario, String> colStatus;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtSenha;

    @FXML
    private ComboBox<String> cmbCargo;

    @FXML
    private ComboBox<String> cmbStatus;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
    private Usuario selecionado;

    @FXML
    private void initialize() {
        cmbCargo.setItems(FXCollections.observableArrayList(
            "Administrador",
            "Vendedor Senior",
            "Vendedor Junior",
            "Financeiro",
            "Producao"
        ));
        cmbStatus.setItems(FXCollections.observableArrayList("ATIVO", "INATIVO"));

        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCargo.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(obterCargo(cell.getValue())));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tabelaUsuarios.setColumnResizePolicy(
            TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );
        tabelaUsuarios.setItems(usuarios);
        tabelaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> preencherFormulario(newValue));
        recarregar();
    }

    @FXML
    private void recarregar() {
        usuarios.setAll(usuarioDAO.listarTodos());
        filtrarUsuarios();
    }

    @FXML
    private void novoUsuario() {
        selecionado = null;
        tabelaUsuarios.getSelectionModel().clearSelection();
        limparFormulario();
    }

    @FXML
    private void filtrarUsuarios() {
        String busca = txtBusca.getText() == null ? "" : txtBusca.getText().trim().toLowerCase();
        if (busca.isEmpty()) {
            tabelaUsuarios.setItems(usuarios);
            return;
        }

        ObservableList<Usuario> filtrados = FXCollections.observableArrayList();
        for (Usuario usuario : usuarios) {
            String nome = usuario.getNome() != null ? usuario.getNome().toLowerCase() : "";
            String email = usuario.getEmail() != null ? usuario.getEmail().toLowerCase() : "";
            if (nome.contains(busca) || email.contains(busca)) {
                filtrados.add(usuario);
            }
        }
        tabelaUsuarios.setItems(filtrados);
    }

    @FXML
    private void salvarUsuario() {
        try {
            if (txtNome.getText() == null || txtNome.getText().trim().isEmpty() ||
                txtEmail.getText() == null || txtEmail.getText().trim().isEmpty()) {
                mostrarErro("Nome e e-mail são obrigatórios.");
                return;
            }

            if (cmbCargo.getValue() == null || cmbStatus.getValue() == null) {
                mostrarErro("Selecione cargo e status.");
                return;
            }

            if (selecionado == null) {
                if (txtSenha.getText() == null || txtSenha.getText().trim().isEmpty()) {
                    mostrarErro("Informe uma senha inicial.");
                    return;
                }

                Usuario usuario = new Usuario(
                    txtNome.getText().trim(),
                    txtEmail.getText().trim().toLowerCase(),
                    txtSenha.getText().trim(),
                    cmbStatus.getValue()
                );
                usuario.setDataCadastro(LocalDateTime.now());
                usuarioDAO.criar(usuario);
            } else {
                selecionado.setNome(txtNome.getText().trim());
                selecionado.setEmail(txtEmail.getText().trim().toLowerCase());
                selecionado.setStatus(cmbStatus.getValue());
                usuarioDAO.atualizar(selecionado);
            }

            recarregar();
            novoUsuario();
            mostrarInfo("Usuário salvo com sucesso.");
        } catch (Exception e) {
            mostrarErro("Erro ao salvar usuário: " + e.getMessage());
        }
    }

    @FXML
    private void excluirSelecionado() {
        Usuario usuario = tabelaUsuarios.getSelectionModel().getSelectedItem();
        if (usuario == null) {
            mostrarErro("Selecione um usuário para excluir.");
            return;
        }

        usuarioDAO.deletar(usuario.getId());
        recarregar();
        novoUsuario();
    }

    private void preencherFormulario(Usuario usuario) {
        selecionado = usuario;
        if (usuario == null) {
            return;
        }
        txtNome.setText(usuario.getNome());
        txtEmail.setText(usuario.getEmail());
        txtSenha.clear();
        cmbStatus.setValue(usuario.getStatus());
        cmbCargo.setValue(obterCargo(usuario));
    }

    private void limparFormulario() {
        txtNome.clear();
        txtEmail.clear();
        txtSenha.clear();
        cmbCargo.setValue(null);
        cmbStatus.setValue(null);
    }

    private String obterCargo(Usuario usuario) {
        if (usuario == null || usuario.getEmail() == null) {
            return "Administrador";
        }
        if (usuario.getEmail().equalsIgnoreCase("admin@grafica.com")) {
            return "Administrador";
        }
        return "Colaborador";
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Usuários");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
