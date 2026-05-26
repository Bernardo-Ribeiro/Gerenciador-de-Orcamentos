package com.grafica.controller;

import com.grafica.dao.ClienteDAO;
import com.grafica.model.Cliente;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class ClienteController {
    @FXML
    private TextField nomeField;

    @FXML
    private TextField cpfCnpjField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField telefonejField;

    @FXML
    private TableView<Cliente> clientesTable;

    @FXML
    private TableColumn<Cliente, Integer> idColumn;

    @FXML
    private TableColumn<Cliente, String> nomeColumn;

    @FXML
    private TableColumn<Cliente, String> cpfCnpjColumn;

    @FXML
    private TableColumn<Cliente, String> emailColumn;

    private ClienteDAO clienteDAO;
    private ObservableList<Cliente> clientes;

    @FXML
    private void initialize() {
        clienteDAO = new ClienteDAO();
        configurarTabela();
        carregarClientes();
    }

    private void configurarTabela() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cpfCnpjColumn.setCellValueFactory(new PropertyValueFactory<>("cpfCnpj"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("emailContato"));
    }

    @FXML
    private void novoCliente() {
        limparCampos();
    }

    @FXML
    private void salvarCliente() {
        try {
            Cliente cliente = new Cliente(
                nomeField.getText(),
                cpfCnpjField.getText(),
                emailField.getText(),
                telefonejField.getText()
            );

            clienteDAO.criar(cliente);
            carregarClientes();
            limparCampos();
        } catch (Exception e) {
            System.err.println("Erro ao salvar cliente: " + e.getMessage());
        }
    }

    @FXML
    private void deletarCliente() {
        Cliente cliente = clientesTable.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            try {
                clienteDAO.deletar(cliente.getId());
                carregarClientes();
            } catch (Exception e) {
                System.err.println("Erro ao deletar cliente: " + e.getMessage());
            }
        }
    }

    private void carregarClientes() {
        try {
            clientes = FXCollections.observableArrayList(clienteDAO.listarTodos());
            clientesTable.setItems(clientes);
        } catch (Exception e) {
            System.err.println("Erro ao carregar clientes: " + e.getMessage());
        }
    }

    private void limparCampos() {
        nomeField.clear();
        cpfCnpjField.clear();
        emailField.clear();
        telefonejField.clear();
    }
}
