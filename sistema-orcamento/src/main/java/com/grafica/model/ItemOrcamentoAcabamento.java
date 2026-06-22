package com.grafica.model;

import java.math.BigDecimal;

public class ItemOrcamentoAcabamento {
    private Integer id;
    private Integer idItemOrcamento;
    private Integer idMaterialAcabamento;
    private Integer quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;

    public ItemOrcamentoAcabamento() {}

    public ItemOrcamentoAcabamento(Integer id, Integer idItemOrcamento, Integer idMaterialAcabamento,
                                   Integer quantidade, BigDecimal valorUnitario, BigDecimal valorTotal) {
        this.id = id;
        this.idItemOrcamento = idItemOrcamento;
        this.idMaterialAcabamento = idMaterialAcabamento;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.valorTotal = valorTotal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdItemOrcamento() {
        return idItemOrcamento;
    }

    public void setIdItemOrcamento(Integer idItemOrcamento) {
        this.idItemOrcamento = idItemOrcamento;
    }

    public Integer getIdMaterialAcabamento() {
        return idMaterialAcabamento;
    }

    public void setIdMaterialAcabamento(Integer idMaterialAcabamento) {
        this.idMaterialAcabamento = idMaterialAcabamento;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    @Override
    public String toString() {
        return "ItemOrcamentoAcabamento{" +
                "id=" + id +
                ", idItemOrcamento=" + idItemOrcamento +
                ", idMaterialAcabamento=" + idMaterialAcabamento +
                ", quantidade=" + quantidade +
                ", valorUnitario=" + valorUnitario +
                ", valorTotal=" + valorTotal +
                '}';
    }
}