package com.grafica.model;

public class LayoutProduto {
    private Integer id;
    private Integer idProduto;
    private String nomeLayout;
    private Integer larguraMm;
    private Integer alturaMm;

    public LayoutProduto() {}

    public LayoutProduto(Integer id, Integer idProduto, String nomeLayout, Integer larguraMm, Integer alturaMm) {
        this.id = id;
        this.idProduto = idProduto;
        this.nomeLayout = nomeLayout;
        this.larguraMm = larguraMm;
        this.alturaMm = alturaMm;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Integer idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeLayout() {
        return nomeLayout;
    }

    public void setNomeLayout(String nomeLayout) {
        this.nomeLayout = nomeLayout;
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

    @Override
    public String toString() {
        return nomeLayout + " (" + larguraMm + "x" + alturaMm + "mm)";
    }
}