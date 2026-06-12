package com.grafica.controller;

import com.grafica.dao.MaterialDAO;
import com.grafica.model.Material;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.math.BigDecimal;
import java.util.List;

public class MaterialController {
    @FXML
    private TextField nomeField;
    @FXML
    private ComboBox<String> categoriaCombo;
    @FXML
    private ComboBox<String> tipoItemCombo;
    @FXML
    private ComboBox<String> tipoCobrancaCombo;
    @FXML
    private TextField custoBaseField;
    @FXML
    private TextField custoProducaoField;
    @FXML
    private TableView<Material> materiaisTable;
    @FXML
    private TableColumn<Material, Integer> idColumn;
    @FXML
    private TableColumn<Material, String> nomeColumn;
    @FXML
    private TableColumn<Material, String> categoriaColumn;
    @FXML
    private TableColumn<Material, String> tipoItemColumn;
    @FXML
    private TableColumn<Material, String> tipoCobrancaColumn;
    @FXML
    private TableColumn<Material, BigDecimal> custoBaseColumn;
    @FXML
    private ComboBox<String> filtroCategoria;
    
    private MaterialDAO materialDAO;
    private ObservableList<Material> materiaisObservable;
    private Material materialSelecionado;
    
    @FXML
    public void initialize() {
        materialDAO = new MaterialDAO();

        
        // Configurar ComboBoxes de categorias
        categoriaCombo.setItems(FXCollections.observableArrayList(
            "COMUNICACAO_VISUAL", "IMPRESSOS"
        ));
        
        // Configurar ComboBoxes de tipo item
        tipoItemCombo.setItems(FXCollections.observableArrayList(
            "BASE", "ACABAMENTO"
        ));
        
        // Configurar ComboBoxes de tipo cobrança
        tipoCobrancaCombo.setItems(FXCollections.observableArrayList(
            "AREA", "UNIDADE", "TIRAGEM", "FOLHA", "PACOTE", "SERVICO", "CHAPA"
        ));
        
        // Configurar filtro
        filtroCategoria.setItems(FXCollections.observableArrayList(
            "TODOS", "COMUNICACAO_VISUAL", "IMPRESSOS"
        ));
        filtroCategoria.setValue("TODOS");
        
        configurarTabela();
        carregarMateriais();
    }
    
    private void configurarTabela() {

        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        nomeColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNome()));
        categoriaColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCategoria()));
        tipoItemColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTipoItem()));
        tipoCobrancaColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTipoCobranca()));
        custoBaseColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getCustoBase()));

        materiaisTable.setColumnResizePolicy(
            TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );

        materiaisTable.setOnMouseClicked(event -> {
            materialSelecionado = materiaisTable.getSelectionModel().getSelectedItem();
            if (materialSelecionado != null) {
                preencherFormulario(materialSelecionado);
            }
        });
    }
    
    private void carregarMateriais() {
        String filtro = filtroCategoria.getValue();
        List<Material> lista;
        
        if ("TODOS".equals(filtro)) {
            lista = materialDAO.listarTodos();
        } else {
            lista = materialDAO.listarPorCategoria(filtro);
        }
        
        materiaisObservable = FXCollections.observableArrayList(lista);
        materiaisTable.setItems(materiaisObservable);
    }
    
    private void preencherFormulario(Material material) {
        nomeField.setText(material.getNome());
        categoriaCombo.setValue(material.getCategoria());
        tipoItemCombo.setValue(material.getTipoItem());
        tipoCobrancaCombo.setValue(material.getTipoCobranca());
        custoBaseField.setText(material.getCustoBase().toString());
        custoProducaoField.setText(material.getCustoProducao().toString());
    }
    
    private void limparFormulario() {
        nomeField.clear();
        categoriaCombo.setValue(null);
        tipoItemCombo.setValue(null);
        tipoCobrancaCombo.setValue(null);
        custoBaseField.clear();
        custoProducaoField.clear();
        materialSelecionado = null;
    }
    
    private boolean validarDados() {
        if (nomeField.getText().isEmpty() || 
            categoriaCombo.getValue() == null || 
            tipoItemCombo.getValue() == null ||
            tipoCobrancaCombo.getValue() == null ||
            custoBaseField.getText().isEmpty()) {
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validação");
            alert.setHeaderText("Dados Incompletos");
            alert.setContentText("Todos os campos obrigatórios devem ser preenchidos.");
            alert.showAndWait();
            return false;
        }
        
        try {
            new BigDecimal(custoBaseField.getText());
            if (!custoProducaoField.getText().isEmpty()) {
                new BigDecimal(custoProducaoField.getText());
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validação");
            alert.setHeaderText("Formato Inválido");
            alert.setContentText("Os custos devem ser valores numéricos.");
            alert.showAndWait();
            return false;
        }
        
        return true;
    }
    
    @FXML
    private void salvarMaterial() {
        if (!validarDados()) {
            return;
        }
        
        if (materialSelecionado == null) {
            // Criar novo
            Material novoMaterial = new Material(
                nomeField.getText(),
                categoriaCombo.getValue(),
                tipoItemCombo.getValue(),
                tipoCobrancaCombo.getValue(),
                new BigDecimal(custoBaseField.getText())
            );
            
            if (!custoProducaoField.getText().isEmpty()) {
                novoMaterial.setCustoProducao(new BigDecimal(custoProducaoField.getText()));
            }
            
            materialDAO.criar(novoMaterial);
        } else {
            // Atualizar existente
            materialSelecionado.setNome(nomeField.getText());
            materialSelecionado.setCategoria(categoriaCombo.getValue());
            materialSelecionado.setTipoItem(tipoItemCombo.getValue());
            materialSelecionado.setTipoCobranca(tipoCobrancaCombo.getValue());
            materialSelecionado.setCustoBase(new BigDecimal(custoBaseField.getText()));
            
            if (!custoProducaoField.getText().isEmpty()) {
                materialSelecionado.setCustoProducao(new BigDecimal(custoProducaoField.getText()));
            }
            
            materialDAO.atualizar(materialSelecionado);
        }
        
        limparFormulario();
        carregarMateriais();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText("Material guardado com sucesso!");
        alert.showAndWait();
    }
    
    @FXML
    private void deletarMaterial() {
        if (materialSelecionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Seleção");
            alert.setHeaderText("Nenhum Material Selecionado");
            alert.setContentText("Selecione um material para deletar.");
            alert.showAndWait();
            return;
        }
        
        Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
        confirmar.setTitle("Confirmação");
        confirmar.setHeaderText("Deletar Material");
        confirmar.setContentText("Tem certeza que deseja deletar este material?");
        
        if (confirmar.showAndWait().get() == ButtonType.OK) {
            materialDAO.deletar(materialSelecionado.getId());
            limparFormulario();
            carregarMateriais();
        }
    }
    
    @FXML
    private void limpar() {
        limparFormulario();
    }
    
    @FXML
    private void filtrar() {
        carregarMateriais();
    }
}
