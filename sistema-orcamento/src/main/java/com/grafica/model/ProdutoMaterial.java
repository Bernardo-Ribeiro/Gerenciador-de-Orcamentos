package com.grafica.model;

public class ProdutoMaterial {
    private Integer idProduto;
    private Integer idMaterial;
    private Boolean materialPadrao;

    public ProdutoMaterial() {}

    public ProdutoMaterial(Integer idProduto, Integer idMaterial) {
        this(idProduto, idMaterial, null);
    }

    public ProdutoMaterial(Integer idProduto, Integer idMaterial, Boolean materialPadrao) {
        this.idProduto = idProduto;
        this.idMaterial = idMaterial;
        this.materialPadrao = materialPadrao;
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

    public Boolean getMaterialPadrao() {
        return materialPadrao;
    }

    public void setMaterialPadrao(Boolean materialPadrao) {
        this.materialPadrao = materialPadrao;
    }

    @Override
    public String toString() {
        return "ProdutoMaterial{" +
                "idProduto=" + idProduto +
                ", idMaterial=" + idMaterial +
                ", materialPadrao=" + materialPadrao +
                '}';
    }
}