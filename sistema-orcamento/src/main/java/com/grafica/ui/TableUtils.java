package com.grafica.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.util.Callback;

import java.util.List;
import java.util.function.Consumer;

/**
 * Utility class for TableView and TableColumn configuration
 */
public class TableUtils {
    
    private TableUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Configures a TableView with constrained column resize policy
     */
    public static <T> void configurarRedimensionamento(TableView<T> tabela) {
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }
    
    /**
     * Creates a button cell for table actions
     */
    public static <T> Callback<TableColumn<T, Void>, TableCell<T, Void>> criarCelulaBotao(
            String texto, String estilo, Consumer<T> acao) {
        return col -> new TableCell<>() {
            private final Button button = new Button(texto);
            {
                if (estilo != null && !estilo.isEmpty()) {
                    button.getStyleClass().add(estilo);
                }
                button.setOnAction(event -> {
                    T item = getTableView().getItems().get(getIndex());
                    if (item != null) {
                        acao.accept(item);
                    }
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : button);
                setText(null);
            }
        };
    }
    
    /**
     * Creates a cell factory for displaying currency values
     */
    public static <T> Callback<TableColumn<T, java.math.BigDecimal>, TableCell<T, java.math.BigDecimal>> criarCelulaMoeda() {
        return col -> new TableCell<>() {
            @Override
            protected void updateItem(java.math.BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(UiUtils.formatarMoeda(item));
                }
            }
        };
    }
    
    /**
     * Creates a cell factory for displaying dates
     */
    public static <T> Callback<TableColumn<T, java.time.LocalDate>, TableCell<T, java.time.LocalDate>> criarCelulaData() {
        return col -> new TableCell<>() {
            @Override
            protected void updateItem(java.time.LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : UiUtils.formatarData(item));
            }
        };
    }
    
    /**
     * Creates a cell factory for displaying status with style classes
     */
    public static <T> Callback<TableColumn<T, String>, TableCell<T, String>> criarCelulaStatus(
            String classeBase, String prefixoClasse) {
        return col -> new TableCell<>() {
            private final Label statusLabel = new Label();
            {
                if (classeBase != null) {
                    statusLabel.getStyleClass().add(classeBase);
                }
            }
            
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                    return;
                }
                
                statusLabel.setText(status);
                statusLabel.getStyleClass().removeIf(c -> c.startsWith(prefixoClasse));
                
                String normalized = status.toLowerCase();
                if (normalized.contains("ativo") || normalized.contains("aprovado") || 
                    normalized.contains("vendido") || normalized.contains("fechado")) {
                    statusLabel.getStyleClass().add(prefixoClasse + "-success");
                } else if (normalized.contains("inativo") || normalized.contains("reprovado") || 
                           normalized.contains("cancelado")) {
                    statusLabel.getStyleClass().add(prefixoClasse + "-danger");
                } else if (normalized.contains("pendente")) {
                    statusLabel.getStyleClass().add(prefixoClasse + "-warning");
                }
                
                setGraphic(statusLabel);
                setText(null);
            }
        };
    }
    
    /**
     * Updates table items with pagination support
     */
    public static <T> void atualizarTabelaPaginada(TableView<T> tabela, List<T> todosItens, 
                                                    int paginaAtual, int itensPorPagina, Label labelRegistros) {
        int totalRegistros = todosItens.size();
        int totalPaginas = Math.max(1, (int) Math.ceil(totalRegistros / (double) itensPorPagina));
        paginaAtual = Math.min(Math.max(paginaAtual, 1), totalPaginas);
        
        int inicio = (paginaAtual - 1) * itensPorPagina;
        int fim = Math.min(inicio + itensPorPagina, totalRegistros);
        
        List<T> pagina = totalRegistros == 0 
            ? java.util.Collections.emptyList() 
            : todosItens.subList(inicio, fim);
        
        tabela.setItems(FXCollections.observableArrayList(pagina));
        
        if (labelRegistros != null) {
            int exibindo = totalRegistros == 0 ? 0 : (fim - inicio);
            labelRegistros.setText("Mostrando " + exibindo + " de " + totalRegistros + " registros");
        }
    }
    
    /**
     * Configures pagination buttons state
     */
    public static void configurarPaginacao(Button btnAnterior, Button btnProximo, 
                                           int paginaAtual, int totalPaginas) {
        if (btnAnterior != null) {
            btnAnterior.setDisable(paginaAtual <= 1);
        }
        if (btnProximo != null) {
            btnProximo.setDisable(paginaAtual >= totalPaginas);
        }
    }
    
    /**
     * Configures a page button with active state
     */
    public static void configurarBotaoPagina(Button botao, int pagina, int paginaAtual, int totalPaginas) {
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
        
        String classeAtiva = "clients-pagination-active";
        botao.getStyleClass().remove(classeAtiva);
        if (pagina == paginaAtual) {
            botao.getStyleClass().add(classeAtiva);
        }
    }
    
    /**
     * Creates a placeholder label for empty tables
     */
    public static Region criarPlaceholder(String mensagem, String classeEstilo) {
        Label placeholder = new Label(mensagem);
        if (classeEstilo != null) {
            placeholder.getStyleClass().add(classeEstilo);
        }
        return placeholder;
    }
}