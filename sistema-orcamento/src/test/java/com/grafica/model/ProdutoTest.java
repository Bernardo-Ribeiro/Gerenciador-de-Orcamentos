package com.grafica.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ProdutoTest {

    @Test
    void testCriarProduto() {
        Produto produto = new Produto();
        assertThat(produto).isNotNull();
    }

    @Test
    void testSetters() {
        Produto produto = new Produto();
        
        produto.setId(1);
        produto.setNome("Cartão de Visita");
        produto.setCategoria("Impressos");
        
        assertThat(produto.getId()).isEqualTo(1);
        assertThat(produto.getNome()).isEqualTo("Cartão de Visita");
        assertThat(produto.getCategoria()).isEqualTo("Impressos");
    }

    @Test
    void testCriarProdutoComTodosCampos() {
        Produto produto = new Produto(
            1,
            "Banner",
            "Comunicação Visual"
        );
        
        assertThat(produto.getId()).isEqualTo(1);
        assertThat(produto.getNome()).isEqualTo("Banner");
        assertThat(produto.getCategoria()).isEqualTo("Comunicação Visual");
    }

    @Test
    void testToString() {
        Produto produto = new Produto(1, "Teste", "Categoria");
        String toString = produto.toString();
        
        assertThat(toString).contains("id=1");
        assertThat(toString).contains("nome='Teste'");
        assertThat(toString).contains("categoria='Categoria'");
    }
}