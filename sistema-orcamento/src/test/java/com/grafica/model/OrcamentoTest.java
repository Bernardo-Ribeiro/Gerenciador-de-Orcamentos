package com.grafica.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

class OrcamentoTest {

    @Test
    void testCriarOrcamentoVazio() {
        Orcamento orcamento = new Orcamento();
        
        assertThat(orcamento).isNotNull();
        assertThat(orcamento.getId()).isNull();
        assertThat(orcamento.getValorBruto()).isNull();
    }

    @Test
    void testCriarOrcamentoCompleto() {
        LocalDate emissao = LocalDate.of(2024, 6, 15);
        LocalDate validade = LocalDate.of(2024, 6, 30);
        
        Orcamento orcamento = new Orcamento(
            1,
            100,
            5,
            emissao,
            validade,
            "Pendente",
            new BigDecimal("1500.00"),
            new BigDecimal("40.00"),
            new BigDecimal("10.00"),
            new BigDecimal("1890.00")
        );
        
        assertThat(orcamento.getId()).isEqualTo(1);
        assertThat(orcamento.getIdCliente()).isEqualTo(100);
        assertThat(orcamento.getIdUsuario()).isEqualTo(5);
        assertThat(orcamento.getDataEmissao()).isEqualTo(emissao);
        assertThat(orcamento.getDataValidade()).isEqualTo(validade);
        assertThat(orcamento.getStatus()).isEqualTo("Pendente");
        assertThat(orcamento.getValorBruto()).isEqualByComparingTo(new BigDecimal("1500.00"));
        assertThat(orcamento.getMargemLucroPercentual()).isEqualByComparingTo(new BigDecimal("40.00"));
        assertThat(orcamento.getDescontoProgressivo()).isEqualByComparingTo(new BigDecimal("10.00"));
        assertThat(orcamento.getValorFinal()).isEqualByComparingTo(new BigDecimal("1890.00"));
    }

    @Test
    void testSetters() {
        Orcamento orcamento = new Orcamento();
        
        orcamento.setId(2);
        orcamento.setIdCliente(200);
        orcamento.setIdUsuario(10);
        orcamento.setDataEmissao(LocalDate.now());
        orcamento.setDataValidade(LocalDate.now().plusDays(15));
        orcamento.setStatus("Aprovado");
        orcamento.setValorBruto(new BigDecimal("2500.00"));
        orcamento.setMargemLucroPercentual(new BigDecimal("50.00"));
        orcamento.setDescontoProgressivo(new BigDecimal("5.00"));
        orcamento.setValorFinal(new BigDecimal("3000.00"));
        
        assertThat(orcamento.getId()).isEqualTo(2);
        assertThat(orcamento.getIdCliente()).isEqualTo(200);
        assertThat(orcamento.getStatus()).isEqualTo("Aprovado");
        assertThat(orcamento.getValorFinal()).isEqualByComparingTo(new BigDecimal("3000.00"));
    }

    @Test
    void testStatusValidos() {
        Orcamento orcamento = new Orcamento();
        
        orcamento.setStatus("Pendente");
        assertThat(orcamento.getStatus()).isEqualTo("Pendente");
        
        orcamento.setStatus("Aprovado (Venda)");
        assertThat(orcamento.getStatus()).isEqualTo("Aprovado (Venda)");
        
        orcamento.setStatus("Cancelado/Reprovado");
        assertThat(orcamento.getStatus()).isEqualTo("Cancelado/Reprovado");
    }

    @Test
    void testToString() {
        Orcamento orcamento = new Orcamento(
            1, 100, 5,
            LocalDate.of(2024, 6, 15),
            LocalDate.of(2024, 6, 30),
            "Pendente",
            new BigDecimal("1500.00"),
            new BigDecimal("40.00"),
            new BigDecimal("10.00"),
            new BigDecimal("1890.00")
        );
        
        String toString = orcamento.toString();
        
        assertThat(toString).contains("id=1");
        assertThat(toString).contains("idCliente=100");
        assertThat(toString).contains("Pendente");
    }
}