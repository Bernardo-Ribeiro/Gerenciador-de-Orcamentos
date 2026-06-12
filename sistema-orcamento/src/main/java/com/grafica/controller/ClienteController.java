package com.grafica.controller;

import com.grafica.dao.ClienteDAO;
import com.grafica.model.Cliente;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import java.util.ArrayList;
import java.util.List;

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
    private TextField txtBusca;

    @FXML
    private ComboBox<String> cmbStatusFiltro;

    @FXML
    private HBox filtersRow;

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

    @FXML
    private TableColumn<Cliente, String> telefoneColumn;

    @FXML
    private TableColumn<Cliente, String> statusColumn;

    @FXML
    private Label lblRegistros;

    @FXML
    private Button btnAnterior;

    @FXML
    private Button btnPage1;

    @FXML
    private Button btnPage2;

    @FXML
    private Button btnProximo;

    private ClienteDAO clienteDAO;
    private ObservableList<Cliente> clientes;
    private List<Cliente> clientesFiltrados = new ArrayList<>();
    private int paginaAtual = 1;
    private int totalPaginas = 1;
    private final int itensPorPagina = 5;

    @FXML
    private void initialize() {
        clienteDAO = new ClienteDAO();
        if (cmbStatusFiltro != null) {
            cmbStatusFiltro.setItems(FXCollections.observableArrayList("Todos", "Ativo", "Inativo"));
            cmbStatusFiltro.setValue("Ativo");
        }
        configurarTabela();
        carregarClientes();
    }

    private void configurarTabela() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cpfCnpjColumn.setCellValueFactory(new PropertyValueFactory<>("cpfCnpj"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("emailContato"));
        telefoneColumn.setCellValueFactory(new PropertyValueFactory<>("telefoneWhatsapp"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setCellFactory(column -> new TableCell<Cliente, String>() {
            private final Label statusLabel = new Label();

            {
                statusLabel.getStyleClass().add("clients-status-pill");
            }

            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                String normalized = status.trim().toLowerCase();
                boolean ativo = normalized.equals("ativo") || normalized.equals("ativa");
                statusLabel.setText(ativo ? "Ativo" : "Inativo");
                statusLabel.getStyleClass().removeAll("clients-status-active", "clients-status-inactive");
                statusLabel.getStyleClass().add(ativo ? "clients-status-active" : "clients-status-inactive");
                setGraphic(statusLabel);
                setText(null);
            }
        });

        clientesTable.setColumnResizePolicy(
            TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );
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
            aplicarFiltro();
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

    @FXML
    private void filtrarClientes() {
        aplicarFiltro();
    }

    @FXML
    private void alternarFiltros() {
        if (filtersRow == null) {
            return;
        }
        boolean visivel = !filtersRow.isVisible();
        filtersRow.setVisible(visivel);
        filtersRow.setManaged(visivel);
    }

    @FXML
    private void limparFiltros() {
        if (txtBusca != null) {
            txtBusca.clear();
        }
        if (cmbStatusFiltro != null) {
            cmbStatusFiltro.setValue("Todos");
        }
        aplicarFiltro();
    }

    private void aplicarFiltro() {
        String busca = txtBusca != null && txtBusca.getText() != null
            ? txtBusca.getText().trim().toLowerCase()
            : "";
        String statusFiltro = cmbStatusFiltro != null ? cmbStatusFiltro.getValue() : null;

        clientesFiltrados = new ArrayList<>();
        for (Cliente cliente : clientes) {
            if (!passesFiltroStatus(cliente, statusFiltro)) {
                continue;
            }
            if (!busca.isEmpty() && !matchesBusca(cliente, busca)) {
                continue;
            }
            clientesFiltrados.add(cliente);
        }

        paginaAtual = 1;
        atualizarPaginacao();
    }

    private boolean passesFiltroStatus(Cliente cliente, String statusFiltro) {
        if (statusFiltro == null || statusFiltro.equalsIgnoreCase("Todos")) {
            return true;
        }
        String status = cliente.getStatus() != null ? cliente.getStatus() : "";
        return status.equalsIgnoreCase(statusFiltro);
    }

    private boolean matchesBusca(Cliente cliente, String busca) {
        String nome = cliente.getNome() != null ? cliente.getNome().toLowerCase() : "";
        String doc = cliente.getCpfCnpj() != null ? cliente.getCpfCnpj().toLowerCase() : "";
        String email = cliente.getEmailContato() != null ? cliente.getEmailContato().toLowerCase() : "";
        return nome.contains(busca) || doc.contains(busca) || email.contains(busca);
    }

    private void atualizarPaginacao() {
        int totalRegistros = clientesFiltrados.size();
        totalPaginas = Math.max(1, (int) Math.ceil(totalRegistros / (double) itensPorPagina));
        paginaAtual = Math.min(Math.max(paginaAtual, 1), totalPaginas);

        int inicio = (paginaAtual - 1) * itensPorPagina;
        int fim = Math.min(inicio + itensPorPagina, totalRegistros);
        List<Cliente> pagina = totalRegistros == 0
            ? new ArrayList<>()
            : clientesFiltrados.subList(inicio, fim);

        clientesTable.setItems(FXCollections.observableArrayList(pagina));
        atualizarFooter(totalRegistros, inicio, fim);
        atualizarBotoesPaginacao();
    }

    private void atualizarFooter(int totalRegistros, int inicio, int fim) {
        if (lblRegistros == null) {
            return;
        }
        int exibindo = totalRegistros == 0 ? 0 : (fim - inicio);
        lblRegistros.setText("Mostrando " + exibindo + " de " + totalRegistros + " registros");
    }

    private void atualizarBotoesPaginacao() {
        if (btnAnterior != null) {
            btnAnterior.setDisable(paginaAtual <= 1);
        }
        if (btnProximo != null) {
            btnProximo.setDisable(paginaAtual >= totalPaginas);
        }

        configurarBotaoPagina(btnPage1, paginaAtual);
        configurarBotaoPagina(btnPage2, paginaAtual + 1);
    }

    private void configurarBotaoPagina(Button botao, int pagina) {
        if (botao == null) {
            return;
        }
        if (pagina > totalPaginas) {
            botao.setVisible(false);
            botao.setManaged(false);
            return;
        }

        botao.setVisible(true);
        botao.setManaged(true);
        botao.setText(String.valueOf(pagina));
        botao.getStyleClass().remove("clients-pagination-active");
        if (pagina == paginaAtual) {
            botao.getStyleClass().add("clients-pagination-active");
        }
    }

    @FXML
    private void paginaAnterior() {
        if (paginaAtual > 1) {
            paginaAtual--;
            atualizarPaginacao();
        }
    }

    @FXML
    private void paginaProxima() {
        if (paginaAtual < totalPaginas) {
            paginaAtual++;
            atualizarPaginacao();
        }
    }

    @FXML
    private void irParaPagina(javafx.event.ActionEvent event) {
        if (!(event.getSource() instanceof Button)) {
            return;
        }
        Button botao = (Button) event.getSource();
        try {
            int pagina = Integer.parseInt(botao.getText());
            paginaAtual = pagina;
            atualizarPaginacao();
        } catch (NumberFormatException ignored) {
        }
    }
}
