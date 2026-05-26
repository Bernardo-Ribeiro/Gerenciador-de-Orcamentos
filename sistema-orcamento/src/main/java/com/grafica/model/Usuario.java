package com.grafica.model;

import java.time.LocalDateTime;

public class Usuario {
    private Integer id;
    private String nome;
    private String email;
    private String senhaHash;
    private String status;
    private LocalDateTime dataCadastro;

    public Usuario() {
    }

    public Usuario(String nome, String email, String senhaHash, String status) {
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.status = status;
        this.dataCadastro = LocalDateTime.now();
    }

    public Usuario(Integer id, String nome, String email, String senhaHash, String status, LocalDateTime dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.status = status;
        this.dataCadastro = dataCadastro;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
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

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                ", dataCadastro=" + dataCadastro +
                '}';
    }
}