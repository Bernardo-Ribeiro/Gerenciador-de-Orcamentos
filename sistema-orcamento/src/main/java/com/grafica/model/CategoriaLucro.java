package com.grafica.model;

import java.math.BigDecimal;

public class CategoriaLucro {
    private int id;
    private String nome;
    private String descricao;
    private BigDecimal margemPadrao;

    public CategoriaLucro() {}

    public CategoriaLucro(int id, String nome, String descricao, BigDecimal margemPadrao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.margemPadrao = margemPadrao;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getMargemPadrao() { return margemPadrao; }
    public void setMargemPadrao(BigDecimal margemPadrao) { this.margemPadrao = margemPadrao; }
}
