package com.grafica.controller;

import com.grafica.dao.OrcamentoDAO;
import com.grafica.dao.ClienteDAO;
import com.grafica.model.Orcamento;
import com.grafica.model.Cliente;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OrcamentoController {
    @FXML
    private ComboBox<Cliente> clienteCombo;

    @FXML
    private TextField margemField;

    @FXML
    private TextField descuentoField;

    @FXML
    private TableView<Orcamento> orcamentosTable;

    @FXML
    private TableColumn<Orcamento, Integer> idColumn;

    @FXML
    private TableColumn<Orcamento, String> clienteColumn;

    @FXML
    private TableColumn<Orcamento, BigDecimal> valorColumn;

    private OrcamentoDAO orcamentoDAO;
    private ClienteDAO clienteDAO;

    @FXML
    private void initialize() {
        orcamentoDAO = new OrcamentoDAO();
        clienteDAO = new ClienteDAO();
        configurarTabela();
        carregarClientes();
        carregarOrcamentos();
    }

    private void configurarTabela() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        valorColumn.setCellValueFactory(new PropertyValueFactory<>("valorFinal"));
    }

    private void carregarClientes() {
        try {
            var clientes = clienteDAO.listarTodos();
            clienteCombo.setItems(FXCollections.observableArrayList(clientes));
        } catch (Exception e) {
            System.err.println("Erro ao carregar clientes: " + e.getMessage());
        }
    }

    private void carregarOrcamentos() {
        try {
            var orcamentos = orcamentoDAO.listarTodos();
            orcamentosTable.setItems(FXCollections.observableArrayList(orcamentos));
        } catch (Exception e) {
            System.err.println("Erro ao carregar orçamentos: " + e.getMessage());
        }
    }

    @FXML
    private void criarOrcamento() {
        Cliente cliente = clienteCombo.getValue();
        if (cliente != null) {
            try {
                Orcamento orcamento = new Orcamento();
                orcamento.setIdCliente(cliente.getId());
                orcamento.setMargemLucroPercentual(new BigDecimal(margemField.getText()));
                orcamento.setDescontoProgressivo(new BigDecimal(descuentoField.getText()));

                orcamentoDAO.criar(orcamento);
                carregarOrcamentos();
                limparCampos();
            } catch (Exception e) {
                System.err.println("Erro ao criar orçamento: " + e.getMessage());
            }
        }
    }

    private void limparCampos() {
        margemField.clear();
        descuentoField.clear();
        clienteCombo.setValue(null);
    }
}
