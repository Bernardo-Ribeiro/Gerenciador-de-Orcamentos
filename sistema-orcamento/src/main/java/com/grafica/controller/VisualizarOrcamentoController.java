package com.grafica.controller;

import com.grafica.dao.ClienteDAO;
import com.grafica.dao.ItemOrcamentoDAO;
import com.grafica.dao.OrcamentoDAO;
import com.grafica.model.ItemOrcamento;
import com.grafica.model.Orcamento;
import com.grafica.service.PdfService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class VisualizarOrcamentoController {
    
    @FXML
    private Label lblNumeroOrcamento;
    
    @FXML
    private Label lblCliente;
    
    @FXML
    private Label lblDataEmissao;
    
    @FXML
    private Label lblDataValidade;
    
    @FXML
    private Label lblStatus;
    
    @FXML
    private TableView<ItemOrcamento> tabelaItens;
    
    @FXML
    private TableColumn<ItemOrcamento, String> colProduto;
    
    @FXML
    private TableColumn<ItemOrcamento, String> colMaterial;
    
    @FXML
    private TableColumn<ItemOrcamento, String> colDimensoes;
    
    @FXML
    private TableColumn<ItemOrcamento, Integer> colQuantidade;
    
    @FXML
    private TableColumn<ItemOrcamento, String> colValorBruto;
    
    @FXML
    private TableColumn<ItemOrcamento, String> colValorFinal;
    
    @FXML
    private Label lblTotalBruto;
    
    @FXML
    private Label lblDesconto;
    
    @FXML
    private Label lblMargem;
    
    @FXML
    private Label lblTotalFinal;
    
    @FXML
    private Button btnGerarPdf;
    
    @FXML
    private Button btnDuplicar;
    
    private Orcamento orcamento;
    private List<ItemOrcamento> itens;
    
    private OrcamentoDAO orcamentoDAO;
    private ClienteDAO clienteDAO;
    private ItemOrcamentoDAO itemOrcamentoDAO;
    
    private static final DateTimeFormatter DATA_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final NumberFormat MOEDA_FORMAT = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    
    @FXML
    private void initialize() {
        orcamentoDAO = new OrcamentoDAO();
        clienteDAO = new ClienteDAO();
        itemOrcamentoDAO = new ItemOrcamentoDAO();
        
        configurarTabela();
    }
    
    public void carregarOrcamento(Integer idOrcamento) {
        orcamento = orcamentoDAO.buscarPorId(idOrcamento);
        
        if (orcamento == null) {
            mostrarErro("Orçamento não encontrado.");
            return;
        }
        
        itens = itemOrcamentoDAO.listarPorOrcamento(idOrcamento);
        
        preencherDados();
    }
    
    private void configurarTabela() {
        colProduto.setCellValueFactory(cellData -> {
            String nomeProduto = cellData.getValue().getProduto() != null 
                ? cellData.getValue().getProduto().getNome() 
                : "N/A";
            return new javafx.beans.property.SimpleStringProperty(nomeProduto);
        });
        
        colMaterial.setCellValueFactory(cellData -> {
            String nomeMaterial = cellData.getValue().getMaterial() != null 
                ? cellData.getValue().getMaterial().getNome() 
                : "N/A";
            return new javafx.beans.property.SimpleStringProperty(nomeMaterial);
        });
        
        colDimensoes.setCellValueFactory(cellData -> {
            ItemOrcamento item = cellData.getValue();
            String dimensoes = item.getLarguraMm() + " x " + item.getAlturaMm() + " mm";
            return new javafx.beans.property.SimpleStringProperty(dimensoes);
        });
        
        colQuantidade.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getQuantidade()).asObject()
        );
        
        colValorBruto.setCellValueFactory(cellData -> {
            BigDecimal valor = cellData.getValue().getValorBrutoItem();
            return new javafx.beans.property.SimpleStringProperty(formatarMoeda(valor));
        });
        
        colValorFinal.setCellValueFactory(cellData -> {
            BigDecimal valor = cellData.getValue().getValorFinalItem();
            return new javafx.beans.property.SimpleStringProperty(formatarMoeda(valor));
        });
    }
    
    private void preencherDados() {
        lblNumeroOrcamento.setText("Orçamento #" + String.format("%04d", orcamento.getId()));
        
        String nomeCliente = clienteDAO.obterPorId(orcamento.getIdCliente()).getNome();
        lblCliente.setText(nomeCliente);
        
        lblDataEmissao.setText(orcamento.getDataEmissao() != null 
            ? orcamento.getDataEmissao().format(DATA_FORMAT) 
            : "-");
        
        lblDataValidade.setText(orcamento.getDataValidade() != null 
            ? orcamento.getDataValidade().format(DATA_FORMAT) 
            : "-");
        
        lblStatus.setText(orcamento.getStatus() != null ? orcamento.getStatus() : "PENDENTE");
        lblStatus.getStyleClass().add(getStatusStyleClass(orcamento.getStatus()));
        
        ObservableList<ItemOrcamento> itensObservable = FXCollections.observableArrayList(itens);
        tabelaItens.setItems(itensObservable);
        
        lblTotalBruto.setText(formatarMoeda(orcamento.getValorBruto()));
        lblDesconto.setText(orcamento.getDescontoProgressivo() != null 
            ? orcamento.getDescontoProgressivo() + "%" 
            : "0%");
        lblMargem.setText(orcamento.getMargemLucroPercentual() + "%");
        lblTotalFinal.setText(formatarMoeda(orcamento.getValorFinal()));
    }
    
    private String getStatusStyleClass(String status) {
        if (status == null) return "reports-pill";
        return switch (status.toUpperCase()) {
            case "APROVADO" -> "reports-pill-success";
            case "REPROVADO" -> "reports-pill-danger";
            case "PENDENTE" -> "reports-pill-warning";
            default -> "reports-pill";
        };
    }
    
    private String formatarMoeda(BigDecimal valor) {
        if (valor == null) return "R$ 0,00";
        return MOEDA_FORMAT.format(valor);
    }
    
    @FXML
    private void gerarPdf() {
        if (orcamento == null) return;
        
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Selecionar local para salvar o PDF");
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            
            File diretorioSelecionado = directoryChooser.showDialog(tabelaItens.getScene().getWindow());
            
            if (diretorioSelecionado == null) {
                return;
            }
            
            String caminhoPdf = PdfService.gerarOrcamentoPdfPorId(orcamento.getId(), diretorioSelecionado.getAbsolutePath());
            
            File arquivoPdf = new File(caminhoPdf);
            if (arquivoPdf.exists()) {
                java.awt.Desktop.getDesktop().open(arquivoPdf);
                mostrarMensagem("PDF gerado com sucesso: " + caminhoPdf);
            }
            
        } catch (Exception e) {
            mostrarErro("Erro ao gerar PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void duplicarOrcamento() {
        if (orcamento == null) return;
        
        try {
            Orcamento novoOrcamento = new Orcamento();
            novoOrcamento.setIdCliente(orcamento.getIdCliente());
            novoOrcamento.setDataEmissao(LocalDate.now());
            novoOrcamento.setDataValidade(LocalDate.now().plusDays(15));
            novoOrcamento.setStatus("PENDENTE");
            novoOrcamento.setValorBruto(orcamento.getValorBruto());
            novoOrcamento.setMargemLucroPercentual(orcamento.getMargemLucroPercentual());
            novoOrcamento.setDescontoProgressivo(orcamento.getDescontoProgressivo());
            novoOrcamento.setValorFinal(orcamento.getValorFinal());
            
            orcamentoDAO.criar(novoOrcamento);
            
            for (ItemOrcamento item : itens) {
                ItemOrcamento novoItem = new ItemOrcamento();
                novoItem.setIdOrcamento(novoOrcamento.getId());
                novoItem.setIdProduto(item.getIdProduto());
                novoItem.setIdMaterial(item.getIdMaterial());
                novoItem.setIdLayout(item.getIdLayout());
                novoItem.setLarguraMm(item.getLarguraMm());
                novoItem.setAlturaMm(item.getAlturaMm());
                novoItem.setQuantidade(item.getQuantidade());
                novoItem.setAreaCalculada(item.getAreaCalculada());
                novoItem.setValorBrutoItem(item.getValorBrutoItem());
                novoItem.setValorFinalItem(item.getValorFinalItem());
                novoItem.setCustoUnitario(item.getCustoUnitario());
                novoItem.setTipoCobrancaAplicado(item.getTipoCobrancaAplicado());
                
                itemOrcamentoDAO.criar(novoItem);
            }
            
            mostrarMensagem("Orçamento duplicado com sucesso! Novo ID: " + novoOrcamento.getId() + "\n\nRedirecionando para edição...");
            
            fechar();
            abrirEdicaoOrcamento(novoOrcamento.getId());
            
        } catch (Exception e) {
            mostrarErro("Erro ao duplicar orçamento: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void abrirEdicaoOrcamento(Integer idOrcamento) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/grafica/view/orcamentos.fxml"));
            Parent root = loader.load();
            
            OrcamentoController controller = loader.getController();
            controller.carregarOrcamentoParaEdicao(idOrcamento);
            
            Stage stage = new Stage();
            stage.setTitle("Editar Orçamento #" + String.format("%04d", idOrcamento));
            stage.setScene(new Scene(root, 1200, 800));
            stage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void fechar() {
        Stage stage = (Stage) lblNumeroOrcamento.getScene().getWindow();
        stage.close();
    }
    
    private void mostrarMensagem(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}