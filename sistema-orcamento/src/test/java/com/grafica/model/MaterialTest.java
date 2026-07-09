package com.grafica.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

class MaterialTest {

    @Test
    void testCriarMaterial() {
        Material material = new Material();
        assertThat(material).isNotNull();
    }

    @Test
    void testSetters() {
        Material material = new Material();
        
        material.setId(1);
        material.setNome("Papel Couché");
        material.setCategoria("Impressos");
        material.setCustoBase(new BigDecimal("25.50"));
        material.setTipoCobranca("AREA");
        material.setStatus("ATIVO");
        
        assertThat(material.getId()).isEqualTo(1);
        assertThat(material.getNome()).isEqualTo("Papel Couché");
        assertThat(material.getCategoria()).isEqualTo("Impressos");
        assertThat(material.getCustoBase()).isEqualByComparingTo(new BigDecimal("25.50"));
        assertThat(material.getTipoCobranca()).isEqualTo("AREA");
        assertThat(material.getStatus()).isEqualTo("ATIVO");
    }

    @Test
    void testCriarMaterialComTodosCampos() {
        Material material = new Material(
            1,
            "Adesivo Vinil",
            "Comunicação Visual",
            1,
            "BASE",
            "AREA",
            new BigDecimal("45.00"),
            new BigDecimal("5.00"),
            "ATIVO"
        );
        
        assertThat(material.getId()).isEqualTo(1);
        assertThat(material.getNome()).isEqualTo("Adesivo Vinil");
        assertThat(material.getCategoria()).isEqualTo("Comunicação Visual");
        assertThat(material.getCustoBase()).isEqualByComparingTo(new BigDecimal("45.00"));
    }

    @Test
    void testCriarMaterialComConstrutorSimples() {
        Material material = new Material(
            "Lona",
            "Comunicação Visual",
            "BASE",
            "AREA",
            new BigDecimal("30.00")
        );
        
        assertThat(material.getNome()).isEqualTo("Lona");
        assertThat(material.getCategoria()).isEqualTo("Comunicação Visual");
        assertThat(material.getCustoBase()).isEqualByComparingTo(new BigDecimal("30.00"));
        assertThat(material.getStatus()).isEqualTo("ATIVO");
    }
}