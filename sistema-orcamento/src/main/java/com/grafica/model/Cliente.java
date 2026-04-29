package com.grafica.model;

import java.time.LocalDateTime;

public class Cliente {
    private Integer id;
    private String nomeRazaoSocial;
    private String cpfCnpj;
    private String emailContato;
    private String telefoneWhatsapp;
    private String status;
    private LocalDateTime dataCadastro;

    public Cliente() {
    }

    public Cliente(Integer id, String nomeRazaoSocial, String cpfCnpj, String emailContato, String telefoneWhatsapp, String status, LocalDateTime dataCadastro) {
        this.id = id;
        this.nomeRazaoSocial = nomeRazaoSocial;
        this.cpfCnpj = cpfCnpj;
        this.emailContato = emailContato;
        this.telefoneWhatsapp = telefoneWhatsapp;
        this.status = status;
        this.dataCadastro = dataCadastro;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeRazaoSocial() {
        return nomeRazaoSocial;
    }

    public void setNomeRazaoSocial(String nomeRazaoSocial) {
        this.nomeRazaoSocial = nomeRazaoSocial;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getEmailContato() {
        return emailContato;
    }

    public void setEmailContato(String emailContato) {
        this.emailContato = emailContato;
    }

    public String getTelefoneWhatsapp() {
        return telefoneWhatsapp;
    }

    public void setTelefoneWhatsapp(String telefoneWhatsapp) {
        this.telefoneWhatsapp = telefoneWhatsapp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}