package com.grafica.model;

public class ConfiguracaoPdf {
    private Integer id;
    private String nomeEmpresa;
    private String cnpj;
    private String endereco;
    private String telefone;
    private String email;
    private String logoPath;
    private String rodape;
    private String cores;
    private String fonte;

    public ConfiguracaoPdf() {
    }

    public ConfiguracaoPdf(Integer id, String nomeEmpresa, String cnpj, String endereco, String telefone, String email, String logoPath, String rodape, String cores, String fonte) {
        this.id = id;
        this.nomeEmpresa = nomeEmpresa;
        this.cnpj = cnpj;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
        this.logoPath = logoPath;
        this.rodape = rodape;
        this.cores = cores;
        this.fonte = fonte;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getRodape() {
        return rodape;
    }

    public void setRodape(String rodape) {
        this.rodape = rodape;
    }

    public String getCores() {
        return cores;
    }

    public void setCores(String cores) {
        this.cores = cores;
    }

    public String getFonte() {
        return fonte;
    }

    public void setFonte(String fonte) {
        this.fonte = fonte;
    }

    @Override
    public String toString() {
        return "ConfiguracaoPdf{" +
                "id=" + id +
                ", nomeEmpresa='" + nomeEmpresa + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", endereco='" + endereco + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                ", logoPath='" + logoPath + '\'' +
                ", rodape='" + rodape + '\'' +
                ", cores='" + cores + '\'' +
                ", fonte='" + fonte + '\'' +
                '}';
    }
}