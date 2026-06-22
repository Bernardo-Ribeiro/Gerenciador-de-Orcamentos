package com.grafica.controller;

import com.grafica.dao.ClienteDAO;
import com.grafica.dao.LayoutProdutoDAO;
import com.grafica.dao.MaterialDAO;
import com.grafica.dao.OrcamentoDAO;
import com.grafica.dao.CategoriaLucroDAO;
import com.grafica.dao.ProdutoDAO;
import com.grafica.dao.ProdutoMaterialDAO;
import com.grafica.dao.ProdutoAcabamentoDAO;
import com.grafica.model.Cliente;
import com.grafica.model.LayoutProduto;
import com.grafica.model.Material;
import com.grafica.model.Orcamento;
import com.grafica.model.Produto;
import com.grafica.model.ProdutoMaterial;
import com.grafica.model.ProdutoAcabamento;
import com.grafica.model.CategoriaLucro;
import com.grafica.service.CalculoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

public class OrcamentoController {
    @FXML private SearchableComboBox<Cliente> clienteCombo;
    @FXML private SearchableComboBox<Produto> produtoCombo;
    @FXML private SearchableComboBox<LayoutProduto> layoutCombo;
    @FXML private SearchableComboBox<Material> materialCombo;
    @FXML private VBox acabamentosContainer;
    @FXML private TextField larguraField;
    @FXML private TextField alturaField;
    @FXML private ToggleGroup tamanhoGroup;
    @FXML private RadioButton usarLayoutRadio;
    @FXML private RadioButton personalizadoRadio;
    @FXML private TextField quantidadeField;
    @FXML private TextField margemField;
    @FXML private TextField descuentoField;
    @FXML private Label areaCalculadaLabel;
    @FXML private Label itensCountLabel;
    @FXML private Label orcamentoBadgeLabel;
    @FXML private Label valorBrutoLabel;
    @FXML private Label valorFinalLabel;
    @FXML private TableView<ItemOrcamentoView> itensTable;
    @FXML private TableColumn<ItemOrcamentoView, String> produtoColumn;
    @FXML private TableColumn<ItemOrcamentoView, String> materialColumn;
    @FXML private TableColumn<ItemOrcamentoView, String> dimensoesColumn;
    @FXML private TableColumn<ItemOrcamentoView, Integer> quantidadeItemColumn;
    @FXML private TableColumn<ItemOrcamentoView, BigDecimal> valorBrutoColumn;
    @FXML private TableColumn<ItemOrcamentoView, BigDecimal> valorFinalColumn;
    @FXML private TableColumn<ItemOrcamentoView, Void> acaoColumn;

    private final ObservableList<ItemOrcamentoView> itens = FXCollections.observableArrayList();
    private OrcamentoDAO orcamentoDAO;
    private ClienteDAO clienteDAO;
    private ProdutoDAO produtoDAO;
    private ProdutoMaterialDAO produtoMaterialDAO;
    private ProdutoAcabamentoDAO produtoAcabamentoDAO;
    private LayoutProdutoDAO layoutDAO;
    private MaterialDAO materialDAO;
    private CategoriaLucroDAO categoriaLucroDAO;
    private MainController mainController;
    private Orcamento orcamentoAtual;

    private Map<Integer, CheckBox> acabamentoCheckboxes = new HashMap<>();
    private List<ProdutoAcabamento> acabamentosDisponiveis = new ArrayList<>();

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        orcamentoDAO = new OrcamentoDAO();
        clienteDAO = new ClienteDAO();
        produtoDAO = new ProdutoDAO();
        produtoMaterialDAO = new ProdutoMaterialDAO();
        produtoAcabamentoDAO = new ProdutoAcabamentoDAO();
        layoutDAO = new LayoutProdutoDAO();
        materialDAO = new MaterialDAO();
        categoriaLucroDAO = new CategoriaLucroDAO();

        configurarTabelaItens();
        configurarComboClientes();
        configurarComboProdutos();
        configurarComboLayouts();
        configurarComboMateriais();
        configurarTamanhoRadios();
        configurarListenersCalculo();

        carregarClientes();
        carregarProdutos();

        itensTable.setItems(itens);
        atualizarResumo();
    }

    private void configurarTabelaItens() {
        produtoColumn.setCellValueFactory(new PropertyValueFactory<>("produtoDescricao"));
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
                    itens.remove(getTableView().getItems().get(getIndex()));
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

        itensTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
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
            @Override public String toString(Cliente cliente) { return cliente == null ? "" : cliente.getNome(); }
            @Override public Cliente fromString(String string) { return null; }
        });
    }

    private void configurarComboProdutos() {
        produtoCombo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Produto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome() + " (" + item.getCategoria() + ")");
            }
        });
        produtoCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Produto p) { return p == null ? "" : p.getNome(); }
            @Override public Produto fromString(String string) { return null; }
        });

        produtoCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                carregarLayouts(newVal.getId());
                carregarMateriais(newVal.getId());
                carregarAcabamentos(newVal.getId());
                limparCamposItem();
            } else {
                layoutCombo.setItems(FXCollections.emptyObservableList());
                materialCombo.setItems(FXCollections.emptyObservableList());
                acabamentosContainer.getChildren().clear();
                acabamentosDisponiveis.clear();
                acabamentoCheckboxes.clear();
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
            @Override public String toString(LayoutProduto l) { return l == null ? "" : l.getNomeLayout(); }
            @Override public LayoutProduto fromString(String string) { return null; }
        });

        layoutCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && usarLayoutRadio.isSelected()) {
                larguraField.setText(newVal.getLarguraMm().toString());
                alturaField.setText(newVal.getAlturaMm().toString());
            }
        });
    }

    private void configurarComboMateriais() {
        materialCombo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Material item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome());
            }
        });
        materialCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Material m) { return m == null ? "" : m.getNome(); }
            @Override public Material fromString(String string) { return null; }
        });

        materialCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.getIdCategoriaLucro() != null) {
                // Busca a categoria de lucro associada ao material selecionado
                CategoriaLucro cat = categoriaLucroDAO.obterPorId(newVal.getIdCategoriaLucro());
                if (cat != null) {
                    margemField.setText(cat.getMargemPadrao().toString()); // Preenche o campo de margem automaticamente
                }
            }
        });
    }

    private void configurarTamanhoRadios() {
        usarLayoutRadio.setUserData("layout");
        personalizadoRadio.setUserData("personalizado");

        usarLayoutRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal && layoutCombo.getValue() != null) {
                larguraField.setText(layoutCombo.getValue().getLarguraMm().toString());
                alturaField.setText(layoutCombo.getValue().getAlturaMm().toString());
            }
            larguraField.setDisable(!newVal);
            alturaField.setDisable(!newVal);
        });

        personalizadoRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            larguraField.setDisable(!newVal);
            alturaField.setDisable(!newVal);
            if (newVal) {
                layoutCombo.setValue(null);
                larguraField.clear();
                alturaField.clear();
            }
        });

        usarLayoutRadio.setSelected(true);
    }

    private void configurarListenersCalculo() {
        larguraField.textProperty().addListener((obs, old, nou) -> atualizarAreaPreview());
        alturaField.textProperty().addListener((obs, old, nou) -> atualizarAreaPreview());
        quantidadeField.textProperty().addListener((obs, old, nou) -> atualizarAreaPreview());
    }

    private void carregarClientes() {
        try { clienteCombo.setItems(FXCollections.observableArrayList(clienteDAO.listarTodos())); }
        catch (Exception e) { mostrarErro("Erro ao carregar clientes: " + e.getMessage()); }
    }

    private void carregarProdutos() {
        try { produtoCombo.setItems(FXCollections.observableArrayList(produtoDAO.listarTodos())); }
        catch (Exception e) { mostrarErro("Erro ao carregar produtos: " + e.getMessage()); }
    }

    private void carregarLayouts(int idProduto) {
        try { layoutCombo.setItems(FXCollections.observableArrayList(layoutDAO.listarPorProduto(idProduto))); }
        catch (Exception e) { mostrarErro("Erro ao carregar layouts: " + e.getMessage()); }
    }

    private void carregarMateriais(int idProduto) {
        try {
            List<ProdutoMaterial> pms = produtoMaterialDAO.listarPorProduto(idProduto);
            List<Material> materiais = new ArrayList<>();
            for (ProdutoMaterial pm : pms) {
                Material m = materialDAO.obterPorId(pm.getIdMaterial());
                if (m != null) materiais.add(m);
            }
            materialCombo.setItems(FXCollections.observableArrayList(materiais));
            if (!materiais.isEmpty()) {
                // Selecionar material padrão
                for (int i = 0; i < pms.size(); i++) {
                    if (pms.get(i).getMaterialPadrao()) {
                        if (i < materiais.size()) {
                            materialCombo.setValue(materiais.get(i));
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) { mostrarErro("Erro ao carregar materiais: " + e.getMessage()); }
    }

    private void carregarAcabamentos(int idProduto) {
        acabamentosContainer.getChildren().clear();
        acabamentoCheckboxes.clear();
        acabamentosDisponiveis = produtoAcabamentoDAO.listarPorProduto(idProduto);

        if (acabamentosDisponiveis.isEmpty()) {
            acabamentosContainer.getChildren().add(new Label("Nenhum acabamento disponível"));
            return;
        }

        for (ProdutoAcabamento pa : acabamentosDisponiveis) {
            Material mat = materialDAO.obterPorId(pa.getIdMaterial());
            String nome = mat != null ? mat.getNome() : "Acabamento #" + pa.getIdMaterial();
            CheckBox cb = new CheckBox(nome + " (R$ " + (mat != null ? mat.getCustoBase() : "0") + ")");
            cb.setSelected(pa.getObrigatorio() != null && pa.getObrigatorio());
            if (pa.getObrigatorio() != null && pa.getObrigatorio()) {
                cb.setDisable(true);
            }
            acabamentoCheckboxes.put(pa.getIdMaterial(), cb);
            acabamentosContainer.getChildren().add(cb);
        }
    }

    @FXML
    private void adicionarItem() {
        Produto produto = produtoCombo.getValue();
        if (produto == null) { mostrarErro("Selecione um produto."); return; }

        LayoutProduto layout = layoutCombo.getValue();
        Material material = materialCombo.getValue();
        if (material == null) { mostrarErro("Selecione um material."); return; }

        // Validação: O layout pertence ao produto?
        if (layout != null && !layout.getIdProduto().equals(produto.getId())) {
            mostrarErro("Inconsistência de dados: O layout '" + layout.getNomeLayout() + "' não pertence ao produto '" + produto.getNome() + "'.");
            return;
        }

        // Validação: A categoria do material é compatível com a do produto?
        if (!produto.getCategoria().equals(material.getCategoria())) {
            mostrarErro("Inconsistência de dados: O material '" + material.getNome() + "' (categoria " + material.getCategoria() + ") não é compatível com o produto '" + produto.getNome() + "' (categoria " + produto.getCategoria() + ").");
            return;
        }

        BigDecimal largura = lerDecimal(larguraField, "0");
        BigDecimal altura = lerDecimal(alturaField, "0");
        if (largura.compareTo(BigDecimal.ZERO) <= 0 || altura.compareTo(BigDecimal.ZERO) <= 0) {
            mostrarErro("Informe a largura e altura em mm.");
            return;
        }

        BigDecimal quantidade = lerDecimal(quantidadeField, "1");
        BigDecimal margem = lerDecimal(margemField, "0");
        BigDecimal desconto = lerDecimal(descuentoField, "0");

        // Obter custo base do material
        BigDecimal custoBase = material.getCustoBase();
        if (custoBase == null) {
            mostrarErro("O material selecionado não possui um custo base definido.");
            return;
        }

        // Calcular área
        BigDecimal area = largura.multiply(altura).divide(BigDecimal.valueOf(1_000_000), 4, RoundingMode.HALF_UP);

        BigDecimal valorBruto;
        BigDecimal valorFinal;

        if ("AREA".equals(material.getTipoCobranca())) {
            valorBruto = CalculoService.calcularValorBrutoItem(largura, altura, quantidade, custoBase);
            valorFinal = CalculoService.calcularValorFinalItem(largura, altura, quantidade, custoBase, desconto, margem);
        } else { // Assumindo "UNIDADE" ou outro tipo
            valorBruto = custoBase.multiply(quantidade);
            BigDecimal valorComDesconto = CalculoService.aplicarDescuentoEscala(valorBruto, desconto);
            valorFinal = CalculoService.aplicarMargenGanancia(valorComDesconto, margem);
        }

        // Calcular custo dos acabamentos selecionados
        BigDecimal custoAcabamentos = BigDecimal.ZERO;
        StringBuilder descAcabamentos = new StringBuilder();
        for (Map.Entry<Integer, CheckBox> entry : acabamentoCheckboxes.entrySet()) {
            if (entry.getValue().isSelected()) {
                Material ac = materialDAO.obterPorId(entry.getKey());
                if (ac != null) {
                    custoAcabamentos = custoAcabamentos.add(ac.getCustoBase().multiply(quantidade));
                    if (descAcabamentos.length() > 0) descAcabamentos.append(", ");
                    descAcabamentos.append(ac.getNome());
                }
            }
        }

        valorBruto = valorBruto.add(custoAcabamentos);
        valorFinal = valorFinal.add(custoAcabamentos);

        String descMaterial = material.getNome();
        String descAcab = descAcabamentos.toString();

        itens.add(new ItemOrcamentoView(
            produto.getNome(),
            descMaterial,
            descAcab,
            largura.intValue(),
            altura.intValue(),
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
        if (cliente == null) { mostrarErro("Selecione um cliente."); return; }
        if (itens.isEmpty()) { mostrarErro("Adicione pelo menos um item ao orçamento."); return; }

        try {
            if (orcamentoAtual == null) orcamentoAtual = new Orcamento();
            orcamentoAtual.setIdCliente(cliente.getId());
            orcamentoAtual.setIdUsuario(1);
            orcamentoAtual.setDataValidade(LocalDate.now().plusDays(15));
            orcamentoAtual.setStatus("PENDENTE");
            orcamentoAtual.setMargemLucroPercentual(lerDecimal(margemField, "0"));
            orcamentoAtual.setDescontoProgressivo(lerDecimal(descuentoField, "0"));
            orcamentoAtual.setValorBruto(calcularTotalBruto());
            orcamentoAtual.setValorFinal(calcularTotalFinal());

            if (orcamentoAtual.getId() == null) {
                orcamentoDAO.criar(orcamentoAtual);
                mostrarInfo("Orçamento salvo com sucesso. ID: " + orcamentoAtual.getId());
            } else {
                orcamentoDAO.atualizar(orcamentoAtual);
                mostrarInfo("Orçamento atualizado com sucesso. ID: " + orcamentoAtual.getId());
            }
            orcamentoBadgeLabel.setText("ID: #" + orcamentoAtual.getId());
        } catch (Exception e) {
            mostrarErro("Erro ao salvar orçamento: " + e.getMessage());
        }
    }

    @FXML
    private void excluirOrcamentoAtual() {
        if (orcamentoAtual != null && orcamentoAtual.getId() != null) {
            try {
                orcamentoDAO.deletar(orcamentoAtual.getId());
                mostrarInfo("Orçamento excluído.");
                limparCampos();
            } catch (Exception e) { mostrarErro("Erro ao excluir: " + e.getMessage()); }
        } else { mostrarErro("Nenhum orçamento salvo para excluir."); }
    }

    @FXML
    private void limparCampos() {
        clienteCombo.setValue(null);
        produtoCombo.setValue(null);
        itens.clear();
        limparCamposItem();
        if (margemField != null) margemField.clear();
        if (descuentoField != null) descuentoField.clear();
        orcamentoAtual = null;
        atualizarResumo();
    }

    private void limparCamposItem() {
        layoutCombo.setValue(null);
        materialCombo.setValue(null);
        larguraField.clear();
        alturaField.clear();
        quantidadeField.clear();
        areaCalculadaLabel.setText("* Área calculada: 0,00 m²");
        usarLayoutRadio.setSelected(true);
        // Desmarcar checkboxes não obrigatórios
        for (Map.Entry<Integer, CheckBox> entry : acabamentoCheckboxes.entrySet()) {
            if (!entry.getValue().isDisable()) entry.getValue().setSelected(false);
        }
    }

    private void atualizarAreaPreview() {
        BigDecimal largura = lerDecimal(larguraField, "0");
        BigDecimal altura = lerDecimal(alturaField, "0");
        BigDecimal area = largura.multiply(altura).divide(BigDecimal.valueOf(1_000_000), 4, RoundingMode.HALF_UP);
        areaCalculadaLabel.setText(String.format(Locale.ROOT, "* Área calculada: %.2f m²", area));
    }

    private void atualizarResumo() {
        itensCountLabel.setText("Itens do Orçamento (" + itens.size() + ")");
        orcamentoBadgeLabel.setText(orcamentoAtual != null && orcamentoAtual.getId() != null
            ? "ID: #" + orcamentoAtual.getId()
            : itens.isEmpty() ? "ID: Novo" : "ID: Rascunho");
        valorBrutoLabel.setText(formatarMoeda(calcularTotalBruto()));
        valorFinalLabel.setText(formatarMoeda(calcularTotalFinal()));
        itensTable.refresh();
    }

    private BigDecimal calcularTotalBruto() {
        return itens.stream().map(ItemOrcamentoView::getValorBruto).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularTotalFinal() {
        return itens.stream().map(ItemOrcamentoView::getValorFinal).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
    }

    @FXML
    private void gerarPropostaPdf() {
        mostrarInfo("Geração de PDF será adicionada na próxima etapa.");
    }

    @FXML
    private void abrirNovoCliente() {
        if (mainController != null) mainController.abrirClientesTela();
        else mostrarInfo("Abra a tela de clientes pelo menu lateral.");
    }

    private BigDecimal lerDecimal(TextField campo, String valorPadrao) {
        String texto = campo.getText() == null ? "" : campo.getText().trim();
        if (texto.isEmpty()) texto = valorPadrao;
        texto = texto.replace(',', '.');
        try { return new BigDecimal(texto); }
        catch (NumberFormatException e) { return new BigDecimal(valorPadrao); }
    }

    private String formatarMoeda(BigDecimal valor) {
        return "R$ " + valor.setScale(2, RoundingMode.HALF_UP).toString().replace('.', ',');
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro"); alert.setHeaderText(null); alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Orçamento"); alert.setHeaderText(null); alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public static class ItemOrcamentoView {
        private final String produtoDescricao;
        private final String materialDescricao;
        private final String dimensoes;
        private final Integer quantidade;
        private final BigDecimal valorBruto;
        private final BigDecimal valorFinal;

        public ItemOrcamentoView(String produto, String material, String acabamentos, int largura, int altura, Integer quantidade, BigDecimal valorBruto, BigDecimal valorFinal) {
            this.produtoDescricao = produto;
            this.materialDescricao = acabamentos.isEmpty() ? material : material + " + " + acabamentos;
            this.dimensoes = largura + " x " + altura + " mm";
            this.quantidade = quantidade;
            this.valorBruto = valorBruto;
            this.valorFinal = valorFinal;
        }

        public String getProdutoDescricao() { return produtoDescricao; }
        public String getMaterialDescricao() { return materialDescricao; }
        public String getDimensoes() { return dimensoes; }
        public Integer getQuantidade() { return quantidade; }
        public BigDecimal getValorBruto() { return valorBruto; }
        public BigDecimal getValorFinal() { return valorFinal; }
    }
}