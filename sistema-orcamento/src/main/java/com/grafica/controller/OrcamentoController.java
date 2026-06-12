package com.grafica.controller;

import com.grafica.dao.ClienteDAO;
import com.grafica.dao.LayoutProdutoDAO;
import com.grafica.dao.MaterialDAO;
import com.grafica.dao.OrcamentoDAO;
import com.grafica.dao.CategoriaLucroDAO;
import com.grafica.model.Cliente;
import com.grafica.model.LayoutProduto;
import com.grafica.model.Material;
import com.grafica.model.Orcamento;
import com.grafica.model.CategoriaLucro;
import com.grafica.service.CalculoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Locale;

public class OrcamentoController {
    @FXML
    private ComboBox<Cliente> clienteCombo;

    @FXML
    private ComboBox<Material> materialCombo;

    @FXML
    private ComboBox<LayoutProduto> layoutCombo;

    @FXML
    private TextField larguraField;

    @FXML
    private TextField alturaField;

    @FXML
    private TextField acabamentoField;

    @FXML
    private TextField quantidadeField;

    @FXML
    private TextField margemField;

    @FXML
    private TextField descuentoField;

    @FXML
    private Label areaCalculadaLabel;

    @FXML
    private Label itensCountLabel;

    @FXML
    private Label orcamentoBadgeLabel;

    @FXML
    private Label valorBrutoLabel;

    @FXML
    private Label valorFinalLabel;

    @FXML
    private TableView<ItemOrcamentoView> itensTable;

    @FXML
    private TableColumn<ItemOrcamentoView, String> materialColumn;

    @FXML
    private TableColumn<ItemOrcamentoView, String> dimensoesColumn;

    @FXML
    private TableColumn<ItemOrcamentoView, Integer> quantidadeItemColumn;

    @FXML
    private TableColumn<ItemOrcamentoView, BigDecimal> valorBrutoColumn;

    @FXML
    private TableColumn<ItemOrcamentoView, BigDecimal> valorFinalColumn;

    @FXML
    private TableColumn<ItemOrcamentoView, Void> acaoColumn;

    private final ObservableList<ItemOrcamentoView> itens = FXCollections.observableArrayList();
    private OrcamentoDAO orcamentoDAO;
    private ClienteDAO clienteDAO;
    private MaterialDAO materialDAO;
    private LayoutProdutoDAO layoutDAO;
    private CategoriaLucroDAO categoriaLucroDAO;
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        orcamentoDAO = new OrcamentoDAO();
        clienteDAO = new ClienteDAO();
        materialDAO = new MaterialDAO();
        layoutDAO = new LayoutProdutoDAO();
        categoriaLucroDAO = new CategoriaLucroDAO();

        configurarTabelaItens();
        configurarComboClientes();
        configurarComboMateriais();
        configurarComboLayouts();
        configurarListenersCalculo();

        carregarClientes();
        carregarMateriais();
        itensTable.setItems(itens);
        atualizarResumo();
    }

    private void configurarTabelaItens() {
        materialColumn.setCellValueFactory(new PropertyValueFactory<>("materialDescricao"));
        dimensoesColumn.setCellValueFactory(new PropertyValueFactory<>("dimensoes"));
        quantidadeItemColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        valorBrutoColumn.setCellValueFactory(new PropertyValueFactory<>("valorBruto"));
        valorFinalColumn.setCellValueFactory(new PropertyValueFactory<>("valorFinal"));

        valorBrutoColumn.setCellFactory(column -> moedaCell());
        valorFinalColumn.setCellFactory(column -> moedaCell());

        acaoColumn.setCellFactory(column -> new TableCell<>() {
            private final Button deleteButton = new Button("Excluir");

            {
                deleteButton.getStyleClass().add("budget-delete-button");
                deleteButton.setOnAction(event -> {
                    ItemOrcamentoView item = getTableView().getItems().get(getIndex());
                    itens.remove(item);
                    atualizarResumo();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
                setText(null);
            }
        });

        itensTable.setColumnResizePolicy(
            TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );
    }

    private TableCell<ItemOrcamentoView, BigDecimal> moedaCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : formatarMoeda(value));
            }
        };
    }

    private void configurarComboClientes() {
        clienteCombo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Cliente item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome());
            }
        });
        clienteCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Cliente cliente) {
                return cliente == null ? "" : cliente.getNome();
            }

            @Override
            public Cliente fromString(String string) {
                return null;
            }
        });
    }

    private void configurarComboMateriais() {
        materialCombo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Material item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome() + " - " + item.getCategoria());
            }
        });
        materialCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Material material) {
                if (material == null) {
                    return "";
                }
                return material.getNome() + " - " + material.getCategoria();
            }

            @Override
            public Material fromString(String string) {
                return null;
            }
        });

        materialCombo.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                carregarLayouts(newValue.getId());
                // Carregar margem de lucro automática
                if (newValue.getIdCategoriaLucro() != null) {
                    CategoriaLucro cat = categoriaLucroDAO.obterPorId(newValue.getIdCategoriaLucro());
                    if (cat != null) {
                        margemField.setText(cat.getMargemPadrao().toString());
                    }
                }
            } else {
                layoutCombo.setItems(FXCollections.emptyObservableList());
            }
        });
    }

    private void configurarComboLayouts() {
        layoutCombo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(LayoutProduto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNomeLayout() + " (" + item.getLarguraMm() + "x" + item.getAlturaMm() + "mm)");
            }
        });
        layoutCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(LayoutProduto layout) {
                return layout == null ? "" : layout.getNomeLayout();
            }

            @Override
            public LayoutProduto fromString(String string) {
                return null;
            }
        });

        layoutCombo.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                larguraField.setText(newValue.getLarguraMm().toString());
                alturaField.setText(newValue.getAlturaMm().toString());
            }
        });
    }

    private void carregarLayouts(int idMaterial) {
        try {
            layoutCombo.setItems(FXCollections.observableArrayList(layoutDAO.listarPorMaterial(idMaterial)));
        } catch (Exception e) {
            mostrarErro("Erro ao carregar layouts: " + e.getMessage());
        }
    }

    private void configurarListenersCalculo() {
        larguraField.textProperty().addListener((obs, oldValue, newValue) -> atualizarAreaPreview());
        alturaField.textProperty().addListener((obs, oldValue, newValue) -> atualizarAreaPreview());
        quantidadeField.textProperty().addListener((obs, oldValue, newValue) -> atualizarAreaPreview());
        materialCombo.valueProperty().addListener((obs, oldValue, newValue) -> atualizarAreaPreview());
    }

    private void carregarClientes() {
        try {
            clienteCombo.setItems(FXCollections.observableArrayList(clienteDAO.listarTodos()));
        } catch (Exception e) {
            mostrarErro("Erro ao carregar clientes: " + e.getMessage());
        }
    }

    private void carregarMateriais() {
        try {
            materialCombo.setItems(FXCollections.observableArrayList(materialDAO.listarTodos()));
        } catch (Exception e) {
            mostrarErro("Erro ao carregar materiais: " + e.getMessage());
        }
    }

    @FXML
    private void adicionarItem() {
        Material material = materialCombo.getValue();
        if (material == null) {
            mostrarErro("Selecione um material antes de adicionar o item.");
            return;
        }

        BigDecimal largura = lerDecimal(larguraField, "0");
        BigDecimal altura = lerDecimal(alturaField, "0");
        BigDecimal quantidade = lerDecimal(quantidadeField, "1");
        BigDecimal margem = lerDecimal(margemField, "0");
        BigDecimal desconto = lerDecimal(descuentoField, "0");
        String acabamento = acabamentoField.getText() != null ? acabamentoField.getText().trim() : "";

        BigDecimal valorBruto = CalculoService.calcularValorBrutoItem(largura, altura, quantidade, material.getCustoBase());
        BigDecimal valorFinal = CalculoService.calcularValorFinalItem(largura, altura, quantidade, material.getCustoBase(), desconto, margem);

        itens.add(new ItemOrcamentoView(
            material.getNome(),
            acabamento,
            largura,
            altura,
            quantidade.intValue(),
            valorBruto,
            valorFinal
        ));

        limparCamposItem();
        atualizarResumo();
    }

    @FXML
    private void criarOrcamento() {
        Cliente cliente = clienteCombo.getValue();
        if (cliente == null) {
            mostrarErro("Selecione um cliente.");
            return;
        }

        if (itens.isEmpty()) {
            mostrarErro("Adicione pelo menos um item ao orçamento.");
            return;
        }

        try {
            Orcamento orcamento = new Orcamento();
            orcamento.setIdCliente(cliente.getId());
            orcamento.setIdUsuario(1);
            orcamento.setDataValidade(LocalDate.now().plusDays(15));
            orcamento.setStatus("PENDENTE");
            orcamento.setMargemLucroPercentual(lerDecimal(margemField, "0"));
            orcamento.setDescontoProgressivo(lerDecimal(descuentoField, "0"));
            orcamento.setValorBruto(calcularTotalBruto());
            orcamento.setValorFinal(calcularTotalFinal());

            orcamentoDAO.criar(orcamento);
            mostrarInfo("Orçamento salvo com sucesso. ID: " + orcamento.getId());
            orcamentoBadgeLabel.setText("ID: #" + orcamento.getId());
        } catch (Exception e) {
            mostrarErro("Erro ao criar orçamento: " + e.getMessage());
        }
    }

    @FXML
    private void limparCampos() {
        clienteCombo.setValue(null);
        materialCombo.setValue(null);
        itens.clear();
        limparCamposItem();
        if (margemField != null) {
            margemField.clear();
        }
        if (descuentoField != null) {
            descuentoField.clear();
        }
        atualizarResumo();
    }

    @FXML
    private void gerarPropostaPdf() {
        mostrarInfo("Geração de PDF será adicionada na próxima etapa.");
    }

    @FXML
    private void abrirNovoCliente() {
        if (mainController != null) {
            mainController.abrirClientesTela();
        } else {
            mostrarInfo("Abra a tela de clientes pelo menu lateral para cadastrar um novo cliente.");
        }
    }

    private void limparCamposItem() {
        larguraField.clear();
        alturaField.clear();
        quantidadeField.clear();
        acabamentoField.clear();
        areaCalculadaLabel.setText("* Area calculada: 0.00 m2");
    }

    private void atualizarAreaPreview() {
        BigDecimal largura = lerDecimal(larguraField, "0");
        BigDecimal altura = lerDecimal(alturaField, "0");
        BigDecimal area = largura.multiply(altura)
            .divide(BigDecimal.valueOf(1_000_000), 4, RoundingMode.HALF_UP);
        areaCalculadaLabel.setText(String.format(Locale.ROOT, "* Area calculada: %.2f m2", area));
    }

    private void atualizarResumo() {
        itensCountLabel.setText("Itens do Orçamento (" + itens.size() + ")");
        orcamentoBadgeLabel.setText(itens.isEmpty() ? "ID: Novo" : "ID: Rascunho");
        valorBrutoLabel.setText(formatarMoeda(calcularTotalBruto()));
        valorFinalLabel.setText(formatarMoeda(calcularTotalFinal()));
        itensTable.refresh();
    }

    private BigDecimal calcularTotalBruto() {
        return itens.stream()
            .map(ItemOrcamentoView::getValorBruto)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularTotalFinal() {
        return itens.stream()
            .map(ItemOrcamentoView::getValorFinal)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal lerDecimal(TextField campo, String valorPadrao) {
        String texto = campo.getText() == null ? "" : campo.getText().trim();
        if (texto.isEmpty()) {
            texto = valorPadrao;
        }
        texto = texto.replace(',', '.');
        try {
            return new BigDecimal(texto);
        } catch (NumberFormatException e) {
            return new BigDecimal(valorPadrao);
        }
    }

    private String formatarMoeda(BigDecimal valor) {
        return "R$ " + valor.setScale(2, RoundingMode.HALF_UP).toString().replace('.', ',');
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
        alert.setTitle("Orçamento");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public static class ItemOrcamentoView {
        private final String materialDescricao;
        private final String dimensoes;
        private final Integer quantidade;
        private final BigDecimal valorBruto;
        private final BigDecimal valorFinal;

        public ItemOrcamentoView(String materialDescricao, String acabamento, BigDecimal largura, BigDecimal altura, Integer quantidade, BigDecimal valorBruto, BigDecimal valorFinal) {
            this.materialDescricao = (acabamento == null || acabamento.isBlank())
                ? materialDescricao
                : materialDescricao + " / " + acabamento;
            this.dimensoes = largura.setScale(0, RoundingMode.HALF_UP) + " x " + altura.setScale(0, RoundingMode.HALF_UP) + " mm";
            this.quantidade = quantidade;
            this.valorBruto = valorBruto;
            this.valorFinal = valorFinal;
        }

        public String getMaterialDescricao() {
            return materialDescricao;
        }

        public String getDimensoes() {
            return dimensoes;
        }

        public Integer getQuantidade() {
            return quantidade;
        }

        public BigDecimal getValorBruto() {
            return valorBruto;
        }

        public BigDecimal getValorFinal() {
            return valorFinal;
        }
    }
}
