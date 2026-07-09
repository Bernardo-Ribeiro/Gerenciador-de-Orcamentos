package com.grafica.service;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

class CalculoServiceTest {

    @Test
    void testCalcularValorBrutoItem() {
        BigDecimal largura = new BigDecimal("1000");
        BigDecimal altura = new BigDecimal("1000");
        BigDecimal quantidade = new BigDecimal("10");
        BigDecimal custoBase = new BigDecimal("50.00");
        
        BigDecimal resultado = CalculoService.calcularValorBrutoItem(
            largura, altura, quantidade, custoBase
        );
        
        assertThat(resultado).isEqualByComparingTo(new BigDecimal("500.00"));
    }

    @Test
    void testCalcularValorBrutoItemComValoresDecimais() {
        BigDecimal largura = new BigDecimal("210");
        BigDecimal altura = new BigDecimal("297");
        BigDecimal quantidade = new BigDecimal("5000");
        BigDecimal custoBase = new BigDecimal("0.50");
        
        BigDecimal resultado = CalculoService.calcularValorBrutoItem(
            largura, altura, quantidade, custoBase
        );
        
        assertThat(resultado).isGreaterThan(new BigDecimal("0"));
    }

    @Test
    void testCalcularValorBrutoItemQuantidadeUnitaria() {
        BigDecimal largura = new BigDecimal("1000");
        BigDecimal altura = new BigDecimal("1000");
        BigDecimal quantidade = BigDecimal.ONE;
        BigDecimal custoBase = new BigDecimal("100.00");
        
        BigDecimal resultado = CalculoService.calcularValorBrutoItem(
            largura, altura, quantidade, custoBase
        );
        
        assertThat(resultado).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    void testAplicarDescuentoEscala() {
        BigDecimal valorBruto = new BigDecimal("1000.00");
        BigDecimal descuentoPercentual = new BigDecimal("10.00");
        
        BigDecimal resultado = CalculoService.aplicarDescuentoEscala(
            valorBruto, descuentoPercentual
        );
        
        assertThat(resultado).isEqualByComparingTo(new BigDecimal("900.00"));
    }

    @Test
    void testAplicarDescuentoEscalaSemDescuento() {
        BigDecimal valorBruto = new BigDecimal("1000.00");
        BigDecimal descuentoPercentual = BigDecimal.ZERO;
        
        BigDecimal resultado = CalculoService.aplicarDescuentoEscala(
            valorBruto, descuentoPercentual
        );
        
        assertThat(resultado).isEqualByComparingTo(new BigDecimal("1000.00"));
    }

    @Test
    void testAplicarDescuentoEscalaCemPorCento() {
        BigDecimal valorBruto = new BigDecimal("500.00");
        BigDecimal descuentoPercentual = new BigDecimal("100.00");
        
        BigDecimal resultado = CalculoService.aplicarDescuentoEscala(
            valorBruto, descuentoPercentual
        );
        
        assertThat(resultado).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void testCalcularValorFinalItem() {
        BigDecimal largura = new BigDecimal("1000");
        BigDecimal altura = new BigDecimal("1000");
        BigDecimal quantidade = new BigDecimal("10");
        BigDecimal custoBase = new BigDecimal("50.00");
        BigDecimal descuentoEscala = new BigDecimal("10.00");
        BigDecimal margenGanancia = new BigDecimal("40.00");
        
        BigDecimal resultado = CalculoService.calcularValorFinalItem(
            largura, altura, quantidade, custoBase,
            descuentoEscala, margenGanancia
        );
        
        assertThat(resultado).isGreaterThan(new BigDecimal("0"));
    }

    @Test
    void testCalcularValorFinalItemSemDescontoEMargem() {
        BigDecimal largura = new BigDecimal("1000");
        BigDecimal altura = new BigDecimal("1000");
        BigDecimal quantidade = new BigDecimal("1");
        BigDecimal custoBase = new BigDecimal("100.00");
        BigDecimal descuentoEscala = BigDecimal.ZERO;
        BigDecimal margenGanancia = BigDecimal.ZERO;
        
        BigDecimal resultado = CalculoService.calcularValorFinalItem(
            largura, altura, quantidade, custoBase,
            descuentoEscala, margenGanancia
        );
        
        assertThat(resultado).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    void testValidarDescuentoProgresivo() {
        BigDecimal valorBruto = new BigDecimal("1000.00");
        BigDecimal descuentoProgresivo = new BigDecimal("10.00");
        BigDecimal margenMinimo = new BigDecimal("20.00");
        
        boolean resultado = CalculoService.validarDescuentoProgresivo(
            valorBruto, descuentoProgresivo, margenMinimo
        );
        
        assertThat(resultado).isTrue();
    }

    @Test
    void testCalcularAreaEmMetrosQuadrados() {
        BigDecimal larguraMm = new BigDecimal("1000");
        BigDecimal alturaMm = new BigDecimal("1000");
        
        BigDecimal areaMm2 = larguraMm.multiply(alturaMm);
        BigDecimal areaM2 = areaMm2.divide(new BigDecimal("1000000"), 10, java.math.RoundingMode.HALF_UP);
        
        assertThat(areaM2).isEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void testCalcularValorBrutoComValoresGrandes() {
        BigDecimal largura = new BigDecimal("5000");
        BigDecimal altura = new BigDecimal("3000");
        BigDecimal quantidade = new BigDecimal("1000");
        BigDecimal custoBase = new BigDecimal("25.00");
        
        BigDecimal resultado = CalculoService.calcularValorBrutoItem(
            largura, altura, quantidade, custoBase
        );
        
        assertThat(resultado).isGreaterThan(new BigDecimal("0"));
        assertThat(resultado).isNotNull();
    }

    @Test
    void testPrecisionoDeCalculos() {
        BigDecimal largura = new BigDecimal("210.5");
        BigDecimal altura = new BigDecimal("297.3");
        BigDecimal quantidade = new BigDecimal("100");
        BigDecimal custoBase = new BigDecimal("12.50");
        
        BigDecimal resultado = CalculoService.calcularValorBrutoItem(
            largura, altura, quantidade, custoBase
        );
        
        assertThat(resultado).isNotNull();
        assertThat(resultado.scale()).isLessThanOrEqualTo(2);
    }
}