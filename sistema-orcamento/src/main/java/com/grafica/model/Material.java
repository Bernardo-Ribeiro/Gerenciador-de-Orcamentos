package com.grafica.model;

import java.math.BigDecimal;

public class Material {
    private Integer id;
    private String nome;
    private String categoria;
    private String tipoCobranca;
    private BigDecimal custoBase;
    private BigDecimal custoProducao;
    private String status;

    public Material() {
    }

    public Material(Integer id, String nome, String categoria, String tipoCobranca, BigDecimal custoBase, BigDecimal custoProducao, String status) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.tipoCobranca = tipoCobranca;
        this.custoBase = custoBase;
        this.custoProducao = custoProducao;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTipoCobranca() {
        return tipoCobranca;
    }

    public void setTipoCobranca(String tipoCobranca) {
        this.tipoCobranca = tipoCobranca;
    }

    public BigDecimal getCustoBase() {
        return custoBase;
    }

    public void setCustoBase(BigDecimal custoBase) {
        this.custoBase = custoBase;
    }

    public BigDecimal getCustoProducao() {
        return custoProducao;
    }

    public void setCustoProducao(BigDecimal custoProducao) {
        this.custoProducao = custoProducao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}