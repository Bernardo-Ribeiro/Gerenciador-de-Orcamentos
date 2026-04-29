package com.grafica.model;

import java.math.BigDecimal;

public class ItemOrcamento {
    private Integer id;
    private Integer idOrcamento;
    private Integer idMaterial;
    private Integer larguraMm;
    private Integer alturaMm;
    private Integer quantidade;
    private BigDecimal valorBrutoItem;
    private BigDecimal valorFinalItem;

    public ItemOrcamento() {
    }

    public ItemOrcamento(Integer id, Integer idOrcamento, Integer idMaterial, Integer larguraMm, Integer alturaMm, Integer quantidade, BigDecimal valorBrutoItem, BigDecimal valorFinalItem) {
        this.id = id;
        this.idOrcamento = idOrcamento;
        this.idMaterial = idMaterial;
        this.larguraMm = larguraMm;
        this.alturaMm = alturaMm;
        this.quantidade = quantidade;
        this.valorBrutoItem = valorBrutoItem;
        this.valorFinalItem = valorFinalItem;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdOrcamento() {
        return idOrcamento;
    }

    public void setIdOrcamento(Integer idOrcamento) {
        this.idOrcamento = idOrcamento;
    }

    public Integer getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(Integer idMaterial) {
        this.idMaterial = idMaterial;
    }

    public Integer getLarguraMm() {
        return larguraMm;
    }

    public void setLarguraMm(Integer larguraMm) {
        this.larguraMm = larguraMm;
    }

    public Integer getAlturaMm() {
        return alturaMm;
    }

    public void setAlturaMm(Integer alturaMm) {
        this.alturaMm = alturaMm;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorBrutoItem() {
        return valorBrutoItem;
    }

    public void setValorBrutoItem(BigDecimal valorBrutoItem) {
        this.valorBrutoItem = valorBrutoItem;
    }

    public BigDecimal getValorFinalItem() {
        return valorFinalItem;
    }

    public void setValorFinalItem(BigDecimal valorFinalItem) {
        this.valorFinalItem = valorFinalItem;
    }
}