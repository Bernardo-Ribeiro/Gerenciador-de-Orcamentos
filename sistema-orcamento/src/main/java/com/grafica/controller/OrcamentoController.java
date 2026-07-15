package com.grafica.controller;

import com.grafica.dao.ClienteDAO;
import com.grafica.dao.LayoutProdutoDAO;
import com.grafica.dao.MaterialDAO;
import com.grafica.dao.OrcamentoDAO;
import com.grafica.dao.CategoriaLucroDAO;
import com.grafica.dao.ProdutoDAO;
import com.grafica.dao.ProdutoMaterialDAO;
import com.grafica.dao.ProdutoAcabamentoDAO;
import com.grafica.dao.ConfiguracaoPdfDAO;
import com.grafica.dao.ItemOrcamentoDAO;
import com.grafica.model.Cliente;
import com.grafica.model.LayoutProduto;
import com.grafica.model.Material;
import com.grafica.model.Orcamento;
import com.grafica.model.Produto;
import com.grafica.model.ProdutoMaterial;
import com.grafica.model.ProdutoAcabamento;
import com.grafica.model.CategoriaLucro;
import com.grafica.model.ConfiguracaoPdf;
import com.grafica.model.ItemOrcamento;
import com.grafica.service.CalculoService;
import com.grafica.service.PdfService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
    private ItemOrcamentoDAO itemOrcamentoDAO;
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
        itemOrcamentoDAO = new ItemOrcamentoDAO();

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
                CategoriaLucro cat = categoriaLucroDAO.obterPorId(newVal.getIdCategoriaLucro());
                if (cat != null) {
                    margemField.setText(cat.getMargemPadrao().toString());
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

        if (layout != null && !layout.getIdProduto().equals(produto.getId())) {
            mostrarErro("Inconsistência de dados: O layout '" + layout.getNomeLayout() + "' não pertence ao produto '" + produto.getNome() + "'.");
            return;
        }

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

        BigDecimal custoBase = material.getCustoBase();
        if (custoBase == null) {
            mostrarErro("O material selecionado não possui um custo base definido.");
            return;
        }

        BigDecimal area = largura.multiply(altura).divide(BigDecimal.valueOf(1_000_000), 4, RoundingMode.HALF_UP);

        BigDecimal valorBruto;
        BigDecimal valorFinal;

        if ("AREA".equals(material.getTipoCobranca())) {
            valorFinal = CalculoService.calcularValorFinalItem(largura, altura, quantidade, custoBase, material.getId(), margem);
            valorBruto = CalculoService.calcularValorBrutoItem(largura, altura, quantidade, CalculoService.resolverCustoUnitarioEfetivo(material.getId(), quantidade.intValue(), custoBase));
        } else {
            BigDecimal custoEfetivo = CalculoService.resolverCustoUnitarioEfetivo(material.getId(), quantidade.intValue(), custoBase);
            valorBruto = custoEfetivo.multiply(quantidade);
            BigDecimal valorComDesconto = CalculoService.aplicarDescuentoEscala(valorBruto, desconto);
            valorFinal = CalculoService.aplicarMargenGanancia(valorComDesconto, margem);
        }

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
            valorFinal,
            produto.getId(),
            material.getId(),
            material.getTipoCobranca()
        ));

        limparCamposItem();
        atualizarResumo();
    }

    @FXML
    private void criarOrcamento() {
        System.out.println("=== SALVAR ORÇAMENTO ===");
        System.out.println("orcamentoAtual: " + (orcamentoAtual != null ? "ID " + orcamentoAtual.getId() : "null"));
        System.out.println("Quantidade de itens na lista: " + itens.size());
        
        Cliente cliente = clienteCombo.getValue();
        if (cliente == null) { mostrarErro("Selecione um cliente."); return; }
        if (itens.isEmpty()) { mostrarErro("Adicione pelo menos um item ao orçamento."); return; }

        try {
            if (orcamentoAtual == null) orcamentoAtual = new Orcamento();
            orcamentoAtual.setIdCliente(cliente.getId());
            orcamentoAtual.setIdUsuario(1);
            orcamentoAtual.setDataEmissao(LocalDate.now());
            orcamentoAtual.setDataValidade(LocalDate.now().plusDays(15));
            orcamentoAtual.setStatus("PENDENTE");
            orcamentoAtual.setMargemLucroPercentual(lerDecimal(margemField, "0"));
            orcamentoAtual.setDescontoProgressivo(lerDecimal(descuentoField, "0"));
            orcamentoAtual.setValorBruto(calcularTotalBruto());
            orcamentoAtual.setValorFinal(calcularTotalFinal());

            boolean novoOrcamento = orcamentoAtual.getId() == null;
            System.out.println("É novo orçamento? " + novoOrcamento);
            
            if (novoOrcamento) {
                orcamentoDAO.criar(orcamentoAtual);
                System.out.println("Orçamento criado com ID: " + orcamentoAtual.getId());
                mostrarInfo("Orçamento salvo com sucesso. ID: " + orcamentoAtual.getId());
            } else {
                orcamentoDAO.atualizar(orcamentoAtual);
                System.out.println("Orçamento atualizado ID: " + orcamentoAtual.getId());
                mostrarInfo("Orçamento atualizado com sucesso. ID: " + orcamentoAtual.getId());
            }
            orcamentoBadgeLabel.setText("ID: #" + orcamentoAtual.getId());
            
            salvarItensOrcamento();
            
        } catch (Exception e) {
            mostrarErro("Erro ao salvar orçamento: " + e.getMessage());
            e.printStackTrace();
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

    private void salvarItensOrcamento() {
        if (orcamentoAtual == null || orcamentoAtual.getId() == null) return;
        
        try {
            System.out.println("=== Salvando itens do orçamento " + orcamentoAtual.getId() + " ===");
            System.out.println("Quantidade de itens na lista: " + itens.size());
            
            new ItemOrcamentoDAO().deletarPorOrcamento(orcamentoAtual.getId());
            System.out.println("Itens antigos deletados.");
            
            for (ItemOrcamentoView itemView : itens) {
                ItemOrcamento item = new ItemOrcamento();
                item.setIdOrcamento(orcamentoAtual.getId());
                item.setIdProduto(itemView.getIdProduto());
                item.setIdMaterial(itemView.getIdMaterial());
                item.setLarguraMm(Integer.parseInt(itemView.getDimensoes().split(" x ")[0].replace(" mm", "")));
                item.setAlturaMm(Integer.parseInt(itemView.getDimensoes().split(" x ")[1].replace(" mm", "")));
                item.setQuantidade(itemView.getQuantidade());
                item.setValorBrutoItem(itemView.getValorBruto());
                item.setValorFinalItem(itemView.getValorFinal());
                item.setCustoUnitario(itemView.getValorBruto().divide(BigDecimal.valueOf(itemView.getQuantidade()), 2, java.math.RoundingMode.HALF_UP));
                item.setTipoCobrancaAplicado(itemView.getTipoCobranca());
                
                new ItemOrcamentoDAO().criar(item);
                System.out.println("Item salvo: " + itemView.getProdutoDescricao() + " | " + itemView.getMaterialDescricao());
            }
            System.out.println("=== Fim salvamento de itens ===");
            mostrarInfo("Itens salvos com sucesso!");
        } catch (Exception e) {
            mostrarErro("Erro ao salvar itens: " + e.getMessage());
            e.printStackTrace();
        }
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
        if (orcamentoAtual == null || orcamentoAtual.getId() == null) {
            mostrarErro("Salve o orçamento antes de gerar o PDF.");
            return;
        }

        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Selecionar local para salvar o PDF");
            directoryChooser.setInitialDirectory(new java.io.File(System.getProperty("user.home")));
            
            java.io.File diretorioSelecionado = directoryChooser.showDialog(
                orcamentoBadgeLabel.getScene().getWindow()
            );
            
            if (diretorioSelecionado == null) {
                return;
            }
            
            ConfiguracaoPdfDAO configDAO = new ConfiguracaoPdfDAO();
            ConfiguracaoPdf config = configDAO.obter();
            
            String caminhoLogo = (config != null && config.getLogoPath() != null && !config.getLogoPath().isEmpty()) 
                ? config.getLogoPath() 
                : null;

            String caminhoArquivo = PdfService.gerarOrcamentoPdf(
                orcamentoAtual, 
                caminhoLogo, 
                diretorioSelecionado.getAbsolutePath()
            );
            
            mostrarInfo("PDF gerado com sucesso!\nArquivo salvo em: " + caminhoArquivo);
            
            java.awt.Desktop.getDesktop().open(new java.io.File(caminhoArquivo));
            
        } catch (Exception e) {
            mostrarErro("Erro ao gerar PDF: " + e.getMessage());
            e.printStackTrace();
        }
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

    public void carregarOrcamentoParaEdicao(Integer idOrcamento) {
        Orcamento orcamento = orcamentoDAO.buscarPorId(idOrcamento);
        if (orcamento == null) {
            mostrarErro("Orçamento não encontrado: " + idOrcamento);
            return;
        }
        
        List<ItemOrcamento> itens = itemOrcamentoDAO.listarPorOrcamento(idOrcamento);
        
        clienteCombo.setValue(clienteDAO.obterPorId(orcamento.getIdCliente()));
        margemField.setText(orcamento.getMargemLucroPercentual().toString());
        descuentoField.setText(orcamento.getDescontoProgressivo() != null ? orcamento.getDescontoProgressivo().toString() : "0");
        
        this.orcamentoAtual = orcamento;
        
        for (ItemOrcamento item : itens) {
            ItemOrcamentoView view = new ItemOrcamentoView(
                item.getProduto() != null ? item.getProduto().getNome() : "N/A",
                item.getMaterial() != null ? item.getMaterial().getNome() : "N/A",
                "",
                item.getLarguraMm() != null ? item.getLarguraMm() : 0,
                item.getAlturaMm() != null ? item.getAlturaMm() : 0,
                item.getQuantidade() != null ? item.getQuantidade() : 1,
                item.getValorBrutoItem(),
                item.getValorFinalItem(),
                item.getIdProduto(),
                item.getIdMaterial(),
                item.getTipoCobrancaAplicado()
            );
            this.itens.add(view);
        }
        
        atualizarResumo();
        
        mostrarInfo("Orçamento #" + idOrcamento + " carregado para edição.\nFaça as alterações necessárias e clique em Salvar.");
    }

    public static class ItemOrcamentoView {
        private final String produtoDescricao;
        private final String materialDescricao;
        private final String dimensoes;
        private final Integer quantidade;
        private final BigDecimal valorBruto;
        private final BigDecimal valorFinal;
        private final Integer idProduto;
        private final Integer idMaterial;
        private final String tipoCobranca;

        public ItemOrcamentoView(String produto, String material, String acabamentos, int largura, int altura, Integer quantidade, BigDecimal valorBruto, BigDecimal valorFinal, Integer idProduto, Integer idMaterial, String tipoCobranca) {
            this.produtoDescricao = produto;
            this.materialDescricao = acabamentos.isEmpty() ? material : material + " + " + acabamentos;
            this.dimensoes = largura + " x " + altura + " mm";
            this.quantidade = quantidade;
            this.valorBruto = valorBruto;
            this.valorFinal = valorFinal;
            this.idProduto = idProduto;
            this.idMaterial = idMaterial;
            this.tipoCobranca = tipoCobranca;
        }

        public String getProdutoDescricao() { return produtoDescricao; }
        public String getMaterialDescricao() { return materialDescricao; }
        public String getDimensoes() { return dimensoes; }
        public Integer getQuantidade() { return quantidade; }
        public BigDecimal getValorBruto() { return valorBruto; }
        public BigDecimal getValorFinal() { return valorFinal; }
        public Integer getIdProduto() { return idProduto; }
        public Integer getIdMaterial() { return idMaterial; }
        public String getTipoCobranca() { return tipoCobranca; }
    }
}