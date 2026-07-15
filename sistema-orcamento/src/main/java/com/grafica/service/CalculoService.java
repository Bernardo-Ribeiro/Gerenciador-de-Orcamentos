package com.grafica.service;

import com.grafica.model.EscalaProdutiva;
import com.grafica.dao.EscalaProdutivaDAO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

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

        BigDecimal areaMm2 = larguraMm.multiply(alturaMm);
        BigDecimal areaM2 = areaMm2.divide(
                BigDecimal.valueOf(1_000_000),
                10,
                RoundingMode.HALF_UP
        );

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
     * Resuelve el costo unitario efectivo para un material/quantidade
     * considerando escala productiva con custo_unitario o desconto_percentual.
     *
     * Regra:
     * - Se a escala encontrada tiver custo_unitario != null, usa esse valor.
     * - Senão, usa: custoBase * (1 - descontoPercentual / 100)
     * - Se não houver escala, retorna custoBase.
     *
     * @param idMaterial  ID do material
     * @param quantidade  Quantidade do item
     * @param custoBase   Custo base do material
     * @return custo unitário efetivo
     */
    public static BigDecimal resolverCustoUnitarioEfetivo(Integer idMaterial, int quantidade, BigDecimal custoBase) {
        if (idMaterial == null || custoBase == null) {
            return custoBase;
        }

        EscalaProdutivaDAO escalaDAO = new EscalaProdutivaDAO();
        List<EscalaProdutiva> escalas = escalaDAO.listarPorMaterial(idMaterial);

        for (EscalaProdutiva escala : escalas) {
            if ( quantidade >= escala.getQtdMinima() && quantidade <= escala.getQtdMaxima()) {
                if (escala.getCustoUnitario() != null) {
                    return BigDecimal.valueOf(escala.getCustoUnitario());
                }
                if (escala.getDescontoPercentual() != null) {
                    BigDecimal desc = escala.getDescontoPercentual().setScale(2, RoundingMode.HALF_UP);
                    return custoBase.multiply(BigDecimal.ONE.subtract(desc.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)))
                            .setScale(2, RoundingMode.HALF_UP);
                }
            }
        }

        return custoBase;
    }

    /**
     * Valida que el descuento progresivo no resulte en margen negativo
     *
     * RN005: Validación de límite de descuento
     *
     * @param valorBruto            Valor original
     * @param descuentoProgresivo   Descuento progresivo en %
     * @param margenMinimo          Margen mínimo permitida en %
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
     * - Custo/descuento por escala productiva (modo custo_unitario ou desconto_percentual)
     * - Margen de ganancia
     *
     * @param larguraMm              Ancho en mm
     * @param alturaMm               Alto en mm
     * @param quantidade             Cantidad
     * @param custoBaso              Precio base
     * @param idMaterial             Material para buscar escala produtiva
     * @param margenGananciaPercent  Margen de ganancia (0-100)
     * @return valor final del item
     */
    public static BigDecimal calcularValorFinalItem(
            BigDecimal larguraMm,
            BigDecimal alturaMm,
            BigDecimal quantidade,
            BigDecimal custoBaso,
            Integer idMaterial,
            BigDecimal margenGananciaPercent) {

        // Paso 1: obtener costo unitario efectivo considerando escala
        BigDecimal custoEfetivo = resolverCustoUnitarioEfetivo(idMaterial, quantidade.intValue(), custoBaso);

        // Paso 2: Calcular valor bruto com custo efetivo
        BigDecimal valorBruto = calcularValorBrutoItem(
                larguraMm, alturaMm, quantidade, custoEfetivo
        );

        // Paso 3: Aplicar margen de ganancia
        BigDecimal valorFinal = aplicarMargenGanancia(
                valorBruto, margenGananciaPercent
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
     * - Escala: custo_unitario = R$ 0.30 ou desconto 10%
     * - Margen ganancia: 35%
     *
     * BigDecimal valor = CalculoService.calcularValorFinalItem(
     *     new BigDecimal("210"),      // largura mm
     *     new BigDecimal("297"),      // altura mm
     *     new BigDecimal("5000"),     // cantidad
     *     new BigDecimal("0.50"),     // custo base m²
     *     idMaterial,                 // material id para escala
     *     new BigDecimal("35")        // margen ganancia %
     * );
     */
}