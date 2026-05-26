package com.grafica.service;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class CalculoService {

    /**
     * ALGORITMO PRINCIPAL: Calcula el valor bruto de un item de orçamento
     * 
     * Fórmula:
     * 1. área (m²) = (largura_mm × altura_mm) / 1,000,000
     * 2. valor_bruto = área × quantidade × custo_base
     * 
     * @param larguraMm     Ancho en milímetros
     * @param alturaMm      Alto en milímetros
     * @param quantidade    Cantidad de unidades
     * @param custoBaso     Precio base del material por m²
     * @return valor bruto calculado
     */
    public static BigDecimal calcularValorBrutoItem(
            BigDecimal larguraMm,
            BigDecimal alturaMm,
            BigDecimal quantidade,
            BigDecimal custoBaso) {
        
        // Paso 1: Convertir mm² a m² (dividir por 1,000,000)
        BigDecimal areaMm2 = larguraMm.multiply(alturaMm);
        BigDecimal areaM2 = areaMm2.divide(
            BigDecimal.valueOf(1_000_000),
            10,
            RoundingMode.HALF_UP
        );
        
        // Paso 2: Multiplicar área × cantidad × precio base
        BigDecimal valorBruto = areaM2
            .multiply(quantidade)
            .multiply(custoBaso)
            .setScale(2, RoundingMode.HALF_UP);
        
        return valorBruto;
    }

    /**
     * Aplica descuento por escala productiva
     * 
     * @param valorBruto        Valor sin descuento
     * @param descuentoPercentual Porcentaje de descuento (0-100)
     * @return valor con descuento aplicado
     */
    public static BigDecimal aplicarDescuentoEscala(
            BigDecimal valorBruto,
            BigDecimal descuentoPercentual) {
        
        BigDecimal descuento = valorBruto.multiply(descuentoPercentual)
            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        
        return valorBruto.subtract(descuento)
            .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Aplica margen de ganancia
     * 
     * RN004: Margen de ganancia configurable por usuario
     * 
     * @param valorConDescuento Valor después de descuentos
     * @param margenPercentual   Porcentaje de margen (0-100)
     * @return valor final con margen
     */
    public static BigDecimal aplicarMargenGanancia(
            BigDecimal valorConDescuento,
            BigDecimal margenPercentual) {
        
        BigDecimal margen = valorConDescuento.multiply(margenPercentual)
            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        
        return valorConDescuento.add(margen)
            .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Valida que el descuento progresivo no resulte en margen negativo
     * 
     * RN005: Validación de límite de descuento
     * 
     * @param valorBruto            Valor original
     * @param descuentoProgresivo   Descuento progresivo en %
     * @param margenMinimo          Margen mínimo permitido en %
     * @return true si es válido, false si viola la regla
     */
    public static boolean validarDescuentoProgresivo(
            BigDecimal valorBruto,
            BigDecimal descuentoProgresivo,
            BigDecimal margenMinimo) {
        
        BigDecimal valorConDescuento = aplicarDescuentoEscala(valorBruto, descuentoProgresivo);
        BigDecimal margenFinal = margenMinimo;
        
        // Si el valor con descuento es menor al margen mínimo, es inválido
        return valorConDescuento.compareTo(
            valorBruto.multiply(margenFinal)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
        ) >= 0;
    }

    /**
     * Calcula el valor final completo del item considerando:
     * - Descuento por escala productiva
     * - Margen de ganancia
     * 
     * @param larguraMm              Ancho en mm
     * @param alturaMm               Alto en mm
     * @param quantidade             Cantidad
     * @param custoBaso              Precio base
     * @param descuentoEscalaPercent Descuento por escala (0-100)
     * @param margenGananciaPercent  Margen de ganancia (0-100)
     * @return valor final del item
     */
    public static BigDecimal calcularValorFinalItem(
            BigDecimal larguraMm,
            BigDecimal alturaMm,
            BigDecimal quantidade,
            BigDecimal custoBaso,
            BigDecimal descuentoEscalaPercent,
            BigDecimal margenGananciaPercent) {
        
        // Paso 1: Calcular valor bruto
        BigDecimal valorBruto = calcularValorBrutoItem(
            larguraMm, alturaMm, quantidade, custoBaso
        );
        
        // Paso 2: Aplicar descuento por escala
        BigDecimal valorConDescuento = aplicarDescuentoEscala(
            valorBruto, descuentoEscalaPercent
        );
        
        // Paso 3: Aplicar margen de ganancia
        BigDecimal valorFinal = aplicarMargenGanancia(
            valorConDescuento, margenGananciaPercent
        );
        
        return valorFinal;
    }

    /**
     * RESUMEN DE USO:
     * 
     * Ejemplo: Item de un orçamento
     * - Material: Papel A4 a R$ 0.50/m²
     * - Medidas: 210mm × 297mm (tamaño A4)
     * - Cantidad: 5000 unidades
     * - Descuento escala: 10%
     * - Margen ganancia: 35%
     * 
     * BigDecimal valor = CalculoService.calcularValorFinalItem(
     *     new BigDecimal("210"),      // largura mm
     *     new BigDecimal("297"),      // altura mm
     *     new BigDecimal("5000"),     // cantidad
     *     new BigDecimal("0.50"),     // custo base m²
     *     new BigDecimal("10"),       // descuento escala %
     *     new BigDecimal("35")        // margen ganancia %
     * );
     */
}
