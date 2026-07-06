package com.grafica.dao;

import com.grafica.model.ConfiguracaoPdf;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConfiguracaoPdfDAO {

    public void salvar(ConfiguracaoPdf config) {
        ConfiguracaoPdf existente = obter();
        
        if (existente != null && existente.getId() != null) {
            config.setId(existente.getId());
            atualizar(config);
        } else {
            inserir(config);
        }
    }

    private void inserir(ConfiguracaoPdf config) {
        String sql = "INSERT INTO configuracao_pdf (nome_empresa, cnpj, endereco, telefone, email, logo_path, rodape, cores, fonte) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, config.getNomeEmpresa());
            stmt.setString(2, config.getCnpj());
            stmt.setString(3, config.getEndereco());
            stmt.setString(4, config.getTelefone());
            stmt.setString(5, config.getEmail());
            stmt.setString(6, config.getLogoPath());
            stmt.setString(7, config.getRodape());
            stmt.setString(8, config.getCores());
            stmt.setString(9, config.getFonte());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    config.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir configuração de PDF: " + e.getMessage(), e);
        }
    }

    public ConfiguracaoPdf obter() {
        String sql = "SELECT * FROM configuracao_pdf LIMIT 1";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return mapearResultSet(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter configuração de PDF: " + e.getMessage(), e);
        }
    }

    public void atualizar(ConfiguracaoPdf config) {
        String sql = "UPDATE configuracao_pdf SET " +
                     "nome_empresa = ?, cnpj = ?, endereco = ?, telefone = ?, email = ?, " +
                     "logo_path = ?, rodape = ?, cores = ?, fonte = ? WHERE id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, config.getNomeEmpresa());
            stmt.setString(2, config.getCnpj());
            stmt.setString(3, config.getEndereco());
            stmt.setString(4, config.getTelefone());
            stmt.setString(5, config.getEmail());
            stmt.setString(6, config.getLogoPath());
            stmt.setString(7, config.getRodape());
            stmt.setString(8, config.getCores());
            stmt.setString(9, config.getFonte());
            stmt.setInt(10, config.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar configuração de PDF: " + e.getMessage(), e);
        }
    }

    private ConfiguracaoPdf mapearResultSet(ResultSet rs) throws SQLException {
        ConfiguracaoPdf config = new ConfiguracaoPdf();
        config.setId(rs.getInt("id"));
        config.setNomeEmpresa(rs.getString("nome_empresa"));
        config.setCnpj(rs.getString("cnpj"));
        config.setEndereco(rs.getString("endereco"));
        config.setTelefone(rs.getString("telefone"));
        config.setEmail(rs.getString("email"));
        config.setLogoPath(rs.getString("logo_path"));
        config.setRodape(rs.getString("rodape"));
        config.setCores(rs.getString("cores"));
        config.setFonte(rs.getString("fonte"));
        return config;
    }
}