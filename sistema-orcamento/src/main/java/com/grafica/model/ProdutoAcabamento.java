package com.grafica.model;

public class ProdutoAcabamento {
    private Integer idProduto;
    private Integer idMaterial;
    private Boolean obrigatorio;

    public ProdutoAcabamento() {}

    public ProdutoAcabamento(Integer idProduto, Integer idMaterial) {
        this(idProduto, idMaterial, null);
    }

    public ProdutoAcabamento(Integer idProduto, Integer idMaterial, Boolean obrigatorio) {
        this.idProduto = idProduto;
        this.idMaterial = idMaterial;
        this.obrigatorio = obrigatorio;
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

    public Boolean getObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(Boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    @Override
    public String toString() {
        return "ProdutoAcabamento{" +
                "idProduto=" + idProduto +
                ", idMaterial=" + idMaterial +
                ", obrigatorio=" + obrigatorio +
                '}';
    }
}