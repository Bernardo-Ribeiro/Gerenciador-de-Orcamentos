package com.grafica.model;

import java.math.BigDecimal;

public class EscalaProdutiva {
    private Integer id;
    private Integer idMaterial;
    private Integer qtdMinima;
    private Integer qtdMaxima;
    private BigDecimal descontoPercentual;
    private Double custoUnitario;

    public EscalaProdutiva() {
    }

    public EscalaProdutiva(Integer id, Integer idMaterial, Integer qtdMinima, Integer qtdMaxima, BigDecimal descontoPercentual, Double custoUnitario) {
        this.id = id;
        this.idMaterial = idMaterial;
        this.qtdMinima = qtdMinima;
        this.qtdMaxima = qtdMaxima;
        this.descontoPercentual = descontoPercentual;
        this.custoUnitario = custoUnitario;
    }

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

    public Integer getQtdMinima() {
        return qtdMinima;
    }

    public void setQtdMinima(Integer qtdMinima) {
        this.qtdMinima = qtdMinima;
    }

    public Integer getQtdMaxima() {
        return qtdMaxima;
    }

    public void setQtdMaxima(Integer qtdMaxima) {
        this.qtdMaxima = qtdMaxima;
    }

    public BigDecimal getDescontoPercentual() {
        return descontoPercentual;
    }

    public void setDescontoPercentual(BigDecimal descontoPercentual) {
        this.descontoPercentual = descontoPercentual;
    }

    public Double getCustoUnitario() {
        return custoUnitario;
    }

    public void setCustoUnitario(Double custoUnitario) {
        this.custoUnitario = custoUnitario;
    }
}