package com.grafica.model;

import java.math.BigDecimal;

public class ItemOrcamento {
    private Integer id;
    private Integer idOrcamento;
    private Integer idProduto;
    private Integer idMaterial;
    private Integer idLayout;
    private Integer larguraMm;
    private Integer alturaMm;
    private Integer quantidade;
    private BigDecimal areaCalculada;
    private BigDecimal valorBrutoItem;
    private BigDecimal valorFinalItem;
    private BigDecimal custoUnitario;
    private String tipoCobrancaAplicado;
    
    private Produto produto;
    private Material material; // AREA, UNIDADE, TIRAGEM, FOLHA, PACOTE, SERVICO, CHAPA

    public ItemOrcamento() {}

    public ItemOrcamento(Integer id, Integer idOrcamento, Integer idProduto, Integer idMaterial, Integer idLayout,
                         Integer larguraMm, Integer alturaMm, Integer quantidade, BigDecimal areaCalculada,
                         BigDecimal valorBrutoItem, BigDecimal valorFinalItem, BigDecimal custoUnitario,
                         String tipoCobrancaAplicado) {
        this.id = id;
        this.idOrcamento = idOrcamento;
        this.idProduto = idProduto;
        this.idMaterial = idMaterial;
        this.idLayout = idLayout;
        this.larguraMm = larguraMm;
        this.alturaMm = alturaMm;
        this.quantidade = quantidade;
        this.areaCalculada = areaCalculada;
        this.valorBrutoItem = valorBrutoItem;
        this.valorFinalItem = valorFinalItem;
        this.custoUnitario = custoUnitario;
        this.tipoCobrancaAplicado = tipoCobrancaAplicado;
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

    public Integer getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Integer idProduto) {
        this.idProduto = idProduto;
    }

    public Integer getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(Integer idMaterial) {
        this.idMaterial = idMaterial;
    }

    public Integer getIdLayout() {
        return idLayout;
    }

    public void setIdLayout(Integer idLayout) {
        this.idLayout = idLayout;
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

    public BigDecimal getAreaCalculada() {
        return areaCalculada;
    }

    public void setAreaCalculada(BigDecimal areaCalculada) {
        this.areaCalculada = areaCalculada;
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

    public BigDecimal getCustoUnitario() {
        return custoUnitario;
    }

    public void setCustoUnitario(BigDecimal custoUnitario) {
        this.custoUnitario = custoUnitario;
    }

    public String getTipoCobrancaAplicado() {
        return tipoCobrancaAplicado;
    }

    public void setTipoCobrancaAplicado(String tipoCobrancaAplicado) {
        this.tipoCobrancaAplicado = tipoCobrancaAplicado;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public String toString() {
        return "ItemOrcamento{" +
                "id=" + id +
                ", idOrcamento=" + idOrcamento +
                ", idProduto=" + idProduto +
                ", idMaterial=" + idMaterial +
                ", idLayout=" + idLayout +
                ", larguraMm=" + larguraMm +
                ", alturaMm=" + alturaMm +
                ", quantidade=" + quantidade +
                ", areaCalculada=" + areaCalculada +
                ", valorBrutoItem=" + valorBrutoItem +
                ", valorFinalItem=" + valorFinalItem +
                ", custoUnitario=" + custoUnitario +
                ", tipoCobrancaAplicado='" + tipoCobrancaAplicado + '\'' +
                ", produto=" + (produto != null ? produto.getNome() : "null") +
                ", material=" + (material != null ? material.getNome() : "null") +
                '}';
    }
}