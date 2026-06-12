package com.grafica.model;

public class LayoutProduto {
    private Integer id;
    private Integer idMaterial;
    private String nomeLayout;
    private Integer larguraMm;
    private Integer alturaMm;

    public LayoutProduto() {}

    public LayoutProduto(Integer id, Integer idMaterial, String nomeLayout, Integer larguraMm, Integer alturaMm) {
        this.id = id;
        this.idMaterial = idMaterial;
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

    public Integer getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(Integer idMaterial) {
        this.idMaterial = idMaterial;
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
