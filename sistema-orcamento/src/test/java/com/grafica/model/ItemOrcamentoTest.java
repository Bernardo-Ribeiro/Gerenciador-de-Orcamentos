package com.grafica.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

class ItemOrcamentoTest {

    @Test
    void testCriarItemOrcamento() {
        ItemOrcamento item = new ItemOrcamento();
        assertThat(item).isNotNull();
    }

    @Test
    void testSetters() {
        ItemOrcamento item = new ItemOrcamento();
        
        item.setId(1);
        item.setIdOrcamento(100);
        item.setIdProduto(10);
        item.setIdMaterial(5);
        item.setLarguraMm(210);
        item.setAlturaMm(297);
        item.setQuantidade(1000);
        item.setAreaCalculada(new BigDecimal("0.062370"));
        item.setValorBrutoItem(new BigDecimal("500.00"));
        item.setValorFinalItem(new BigDecimal("700.00"));
        item.setCustoUnitario(new BigDecimal("0.50"));
        item.setTipoCobrancaAplicado("AREA");
        
        assertThat(item.getId()).isEqualTo(1);
        assertThat(item.getIdOrcamento()).isEqualTo(100);
        assertThat(item.getLarguraMm()).isEqualTo(210);
        assertThat(item.getAlturaMm()).isEqualTo(297);
        assertThat(item.getQuantidade()).isEqualTo(1000);
        assertThat(item.getValorFinalItem()).isEqualByComparingTo(new BigDecimal("700.00"));
    }

    @Test
    void testCriarItemOrcamentoCompleto() {
        Produto produto = new Produto(1, "Cartão", "Impressos");
        Material material = new Material(1, "Papel Couché", "Impressos", 1, "BASE", "AREA", new BigDecimal("25.00"), new BigDecimal("5.00"), "ATIVO");
        
        ItemOrcamento item = new ItemOrcamento(
            1, 100, 10, 5, 2,
            210, 297, 1000,
            new BigDecimal("0.062370"),
            new BigDecimal("500.00"),
            new BigDecimal("700.00"),
            new BigDecimal("0.50"),
            "AREA"
        );
        
        item.setProduto(produto);
        item.setMaterial(material);
        
        assertThat(item.getProduto()).isNotNull();
        assertThat(item.getMaterial()).isNotNull();
        assertThat(item.getProduto().getNome()).isEqualTo("Cartão");
        assertThat(item.getMaterial().getNome()).isEqualTo("Papel Couché");
    }

    @Test
    void testToString() {
        ItemOrcamento item = new ItemOrcamento();
        item.setId(1);
        item.setQuantidade(100);
        item.setValorBrutoItem(new BigDecimal("250.00"));
        
        String toString = item.toString();
        
        assertThat(toString).contains("id=1");
        assertThat(toString).contains("quantidade=100");
        assertThat(toString).contains("250.00");
    }

    @Test
    void testItemOrcamentoComProdutoEMaterial() {
        ItemOrcamento item = new ItemOrcamento();
        
        Produto produto = new Produto();
        produto.setNome("Banner");
        
        Material material = new Material();
        material.setNome("Lona");
        
        item.setProduto(produto);
        item.setMaterial(material);
        
        assertThat(item.getProduto().getNome()).isEqualTo("Banner");
        assertThat(item.getMaterial().getNome()).isEqualTo("Lona");
    }

    @Test
    void testCalculoAreaItem() {
        ItemOrcamento item = new ItemOrcamento();
        item.setLarguraMm(1000);
        item.setAlturaMm(1000);
        
        BigDecimal areaM2 = new BigDecimal("1000")
            .multiply(new BigDecimal("1000"))
            .divide(new BigDecimal("1000000"), 6, java.math.RoundingMode.HALF_UP);
        
        item.setAreaCalculada(areaM2);
        
        assertThat(item.getAreaCalculada()).isEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void testTiposCobranca() {
        ItemOrcamento item = new ItemOrcamento();
        
        item.setTipoCobrancaAplicado("AREA");
        assertThat(item.getTipoCobrancaAplicado()).isEqualTo("AREA");
        
        item.setTipoCobrancaAplicado("UNIDADE");
        assertThat(item.getTipoCobrancaAplicado()).isEqualTo("UNIDADE");
        
        item.setTipoCobrancaAplicado("TIRAGEM");
        assertThat(item.getTipoCobrancaAplicado()).isEqualTo("TIRAGEM");
    }
}