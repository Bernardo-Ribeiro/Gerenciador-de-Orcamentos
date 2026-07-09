package com.grafica.service;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

@DisplayName("Regras de Negócio - RN003 e RN004")
class RegrasNegocioTest {

    @DisplayName("Deve aplicar desconto progressivo conforme quantidade")
    @Test
    void testRN003_DescontoPorQuantidade() {
        BigDecimal largura = new BigDecimal("1000");
        BigDecimal altura = new BigDecimal("1000");
        BigDecimal custoBase = new BigDecimal("100.00");
        
        BigDecimal quantidadePequena = new BigDecimal("10");
        BigDecimal quantidadeMedia = new BigDecimal("100");
        BigDecimal quantidadeGrande = new BigDecimal("1000");
        
        BigDecimal valorPequeno = CalculoService.calcularValorBrutoItem(
            largura, altura, quantidadePequena, custoBase
        );
        
        BigDecimal valorMedio = CalculoService.calcularValorBrutoItem(
            largura, altura, quantidadeMedia, custoBase
        );
        
        BigDecimal valorGrande = CalculoService.calcularValorBrutoItem(
            largura, altura, quantidadeGrande, custoBase
        );
        
        assertThat(valorMedio).isGreaterThan(valorPequeno);
        assertThat(valorGrande).isGreaterThan(valorMedio);
    }

    @DisplayName("Deve calcular valor final com desconto e margem")
    @Test
    void testRN004_CalculoComMargem() {
        BigDecimal largura = new BigDecimal("1000");
        BigDecimal altura = new BigDecimal("1000");
        BigDecimal quantidade = new BigDecimal("10");
        BigDecimal custoBase = new BigDecimal("100.00");
        BigDecimal descuentoEscala = new BigDecimal("10.00");
        BigDecimal margenLucro = new BigDecimal("40.00");
        
        BigDecimal valorFinal = CalculoService.calcularValorFinalItem(
            largura, altura, quantidade, custoBase,
            descuentoEscala, margenLucro
        );
        
        assertThat(valorFinal).isGreaterThan(BigDecimal.ZERO);
    }

    @DisplayName("Não deve permitir desconto que resulte em margem negativa")
    @Test
    void testRN005_ValidacaoLimiteDesconto() {
        BigDecimal valorBruto = new BigDecimal("100.00");
        BigDecimal descontoExcessivo = new BigDecimal("80.00");
        BigDecimal margemMinima = new BigDecimal("30.00");
        
        boolean valido = CalculoService.validarDescuentoProgresivo(
            valorBruto, descontoExcessivo, margemMinima
        );
        
        assertThat(valido).isFalse();
    }

    @DisplayName("Deve permitir desconto dentro da margem")
    @Test
    void testRN005_DescontoDentroMargem() {
        BigDecimal valorBruto = new BigDecimal("100.00");
        BigDecimal descontoNormal = new BigDecimal("10.00");
        BigDecimal margemMinima = new BigDecimal("30.00");
        
        boolean valido = CalculoService.validarDescuentoProgresivo(
            valorBruto, descontoNormal, margemMinima
        );
        
        assertThat(valido).isTrue();
    }

    @DisplayName("Deve calcular preço final completo")
    @Test
    void testCalculoPrecoFinalCompleto() {
        BigDecimal largura = new BigDecimal("1000");
        BigDecimal altura = new BigDecimal("1000");
        BigDecimal quantidade = new BigDecimal("100");
        BigDecimal custoBase = new BigDecimal("50.00");
        BigDecimal descontoEscala = new BigDecimal("15.00");
        BigDecimal margemLucro = new BigDecimal("40.00");
        
        BigDecimal valorFinal = CalculoService.calcularValorFinalItem(
            largura, altura, quantidade, custoBase,
            descontoEscala, margemLucro
        );
        
        assertThat(valorFinal).isGreaterThan(BigDecimal.ZERO);
        
        BigDecimal valorBruto = CalculoService.calcularValorBrutoItem(
            largura, altura, quantidade, custoBase
        );
        
        assertThat(valorFinal).isGreaterThanOrEqualTo(valorBruto);
    }

    @DisplayName("Deve manter valor com zero desconto e zero margem")
    @Test
    void testSemDescontoSemMargem() {
        BigDecimal largura = new BigDecimal("1000");
        BigDecimal altura = new BigDecimal("1000");
        BigDecimal quantidade = new BigDecimal("1");
        BigDecimal custoBase = new BigDecimal("100.00");
        
        BigDecimal valorFinal = CalculoService.calcularValorFinalItem(
            largura, altura, quantidade, custoBase,
            BigDecimal.ZERO, BigDecimal.ZERO
        );
        
        assertThat(valorFinal).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @DisplayName("Deve aplicar apenas desconto sem margem")
    @Test
    void testApenasDesconto() {
        BigDecimal valorBruto = new BigDecimal("1000.00");
        BigDecimal desconto = new BigDecimal("20.00");
        
        BigDecimal valorComDesconto = CalculoService.aplicarDescuentoEscala(valorBruto, desconto);
        
        assertThat(valorComDesconto).isEqualByComparingTo(new BigDecimal("800.00"));
    }

    @DisplayName("Deve aplicar margem de lucro configurada")
    @Test
    void testAplicarMargemLucro() {
        BigDecimal largura = new BigDecimal("1000");
        BigDecimal altura = new BigDecimal("1000");
        BigDecimal quantidade = new BigDecimal("1");
        BigDecimal custoBase = new BigDecimal("100.00");
        BigDecimal margem20 = new BigDecimal("20.00");
        BigDecimal margem50 = new BigDecimal("50.00");
        
        BigDecimal valorComMargem20 = CalculoService.calcularValorFinalItem(
            largura, altura, quantidade, custoBase,
            BigDecimal.ZERO, margem20
        );
        
        BigDecimal valorComMargem50 = CalculoService.calcularValorFinalItem(
            largura, altura, quantidade, custoBase,
            BigDecimal.ZERO, margem50
        );
        
        assertThat(valorComMargem20).isEqualByComparingTo(new BigDecimal("120.00"));
        assertThat(valorComMargem50).isEqualByComparingTo(new BigDecimal("150.00"));
        assertThat(valorComMargem50).isGreaterThan(valorComMargem20);
    }
}