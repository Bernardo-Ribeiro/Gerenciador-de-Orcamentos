package com.grafica.controller;

import com.grafica.dao.OrcamentoDAO;
import com.grafica.model.Orcamento;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import java.math.BigDecimal;

public class RelatoriosController {
    @FXML
    private Label totalOrcamentosLabel;

    @FXML
    private Label valorTotalLabel;

    @FXML
    private TableView<Orcamento> relatorioTable;

    @FXML
    private TableColumn<Orcamento, Integer> idColumn;

    @FXML
    private TableColumn<Orcamento, BigDecimal> valorColumn;

    private OrcamentoDAO orcamentoDAO;

    @FXML
    private void initialize() {
        orcamentoDAO = new OrcamentoDAO();
        configurarTabela();
        atualizarRelatorios();
    }

    private void configurarTabela() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        valorColumn.setCellValueFactory(new PropertyValueFactory<>("valorFinal"));
    }

    @FXML
    private void atualizarRelatorios() {
        try {
            var orcamentos = orcamentoDAO.listarTodos();
            relatorioTable.setItems(FXCollections.observableArrayList(orcamentos));

            int total = orcamentos.size();
            totalOrcamentosLabel.setText("Total: " + total);

            BigDecimal valorTotal = orcamentos.stream()
                .map(Orcamento::getValorFinal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            valorTotalLabel.setText("Valor Total: R$ " + String.format("%.2f", valorTotal));
        } catch (Exception e) {
            System.err.println("Erro ao carregar relatórios: " + e.getMessage());
        }
    }
}
