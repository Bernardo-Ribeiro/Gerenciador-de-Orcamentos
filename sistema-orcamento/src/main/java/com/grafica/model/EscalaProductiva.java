package com.grafica.model;

import java.math.BigDecimal;

/**
 * Model class para la tabla escalas_produtivas
 * Define descuentos por volumen de producción
 */
public class EscalaProductiva {
    private Integer id;
    private Integer idMaterial;
    private BigDecimal qtdMinima;
    private BigDecimal qtdMaxima;
    private BigDecimal descontoPercentual;

    public EscalaProductiva() {}

    public EscalaProductiva(Integer idMaterial, BigDecimal qtdMinima, BigDecimal qtdMaxima, BigDecimal descontoPercentual) {
        this.idMaterial = idMaterial;
        this.qtdMinima = qtdMinima;
        this.qtdMaxima = qtdMaxima;
        this.descontoPercentual = descontoPercentual;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(Integer idMaterial) {
        this.idMaterial = idMaterial;
    }

    public BigDecimal getQtdMinima() {
        return qtdMinima;
    }

    public void setQtdMinima(BigDecimal qtdMinima) {
        this.qtdMinima = qtdMinima;
    }

    public BigDecimal getQtdMaxima() {
        return qtdMaxima;
    }

    public void setQtdMaxima(BigDecimal qtdMaxima) {
        this.qtdMaxima = qtdMaxima;
    }

    public BigDecimal getDescontoPercentual() {
        return descontoPercentual;
    }

    public void setDescontoPercentual(BigDecimal descontoPercentual) {
        this.descontoPercentual = descontoPercentual;
    }

    @Override
    public String toString() {
        return "EscalaProductiva{" +
                "id=" + id +
                ", idMaterial=" + idMaterial +
                ", qtdMinima=" + qtdMinima +
                ", qtdMaxima=" + qtdMaxima +
                ", descontoPercentual=" + descontoPercentual +
                '%' +
                '}';
    }
}
