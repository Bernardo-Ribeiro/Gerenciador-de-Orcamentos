package com.grafica.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Orcamento {
    private Integer id;
    private Integer idCliente;
    private Integer idUsuario;
    private LocalDateTime dataEmissao;
    private LocalDate dataValidade;
    private String status;
    private BigDecimal valorBruto;
    private BigDecimal margemLucroPercentual;
    private BigDecimal descontoProgressivo;
    private BigDecimal valorFinal;

    public Orcamento() {
    }

    public Orcamento(Integer id, Integer idCliente, Integer idUsuario, LocalDateTime dataEmissao, LocalDate dataValidade, String status, BigDecimal valorBruto, BigDecimal margemLucroPercentual, BigDecimal descontoProgressivo, BigDecimal valorFinal) {
        this.id = id;
        this.idCliente = idCliente;
        this.idUsuario = idUsuario;
        this.dataEmissao = dataEmissao;
        this.dataValidade = dataValidade;
        this.status = status;
        this.valorBruto = valorBruto;
        this.margemLucroPercentual = margemLucroPercentual;
        this.descontoProgressivo = descontoProgressivo;
        this.valorFinal = valorFinal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDateTime getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDateTime dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getValorBruto() {
        return valorBruto;
    }

    public void setValorBruto(BigDecimal valorBruto) {
        this.valorBruto = valorBruto;
    }

    public BigDecimal getMargemLucroPercentual() {
        return margemLucroPercentual;
    }

    public void setMargemLucroPercentual(BigDecimal margemLucroPercentual) {
        this.margemLucroPercentual = margemLucroPercentual;
    }

    public BigDecimal getDescontoProgressivo() {
        return descontoProgressivo;
    }

    public void setDescontoProgressivo(BigDecimal descontoProgressivo) {
        this.descontoProgressivo = descontoProgressivo;
    }

    public BigDecimal getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(BigDecimal valorFinal) {
        this.valorFinal = valorFinal;
    }
}