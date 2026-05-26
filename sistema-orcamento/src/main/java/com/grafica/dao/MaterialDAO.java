package com.grafica.dao;

import com.grafica.model.Material;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {
    
    public void criar(Material material) {
        String sql = "INSERT INTO materiais (nome, categoria, tipo_item, tipo_cobranca, custo_base, custo_producao, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, material.getNome());
            stmt.setString(2, material.getCategoria());
            stmt.setString(3, material.getTipoItem());
            stmt.setString(4, material.getTipoCobranca());
            stmt.setBigDecimal(5, material.getCustoBase());
            stmt.setBigDecimal(6, material.getCustoProducao());
            stmt.setString(7, material.getStatus());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    material.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Material obterPorId(Integer id) {
        String sql = "SELECT id, nome, categoria, tipo_item, tipo_cobranca, custo_base, custo_producao, status FROM materiais WHERE id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearMaterial(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Material> listarTodos() {
        List<Material> materiais = new ArrayList<>();
        String sql = "SELECT id, nome, categoria, tipo_item, tipo_cobranca, custo_base, custo_producao, status FROM materiais WHERE status = 'ATIVO' ORDER BY nome";
        
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                materiais.add(mapearMaterial(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materiais;
    }
    
    public List<Material> listarPorCategoria(String categoria) {
        List<Material> materiais = new ArrayList<>();
        String sql = "SELECT id, nome, categoria, tipo_item, tipo_cobranca, custo_base, custo_producao, status FROM materiais WHERE categoria = ? AND status = 'ATIVO' ORDER BY nome";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, categoria);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    materiais.add(mapearMaterial(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materiais;
    }
    
    public void atualizar(Material material) {
        String sql = "UPDATE materiais SET nome = ?, categoria = ?, tipo_item = ?, tipo_cobranca = ?, custo_base = ?, custo_producao = ?, status = ? WHERE id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, material.getNome());
            stmt.setString(2, material.getCategoria());
            stmt.setString(3, material.getTipoItem());
            stmt.setString(4, material.getTipoCobranca());
            stmt.setBigDecimal(5, material.getCustoBase());
            stmt.setBigDecimal(6, material.getCustoProducao());
            stmt.setString(7, material.getStatus());
            stmt.setInt(8, material.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void deletar(Integer id) {
        // Soft delete: cambiar status a INATIVO
        String sql = "UPDATE materiais SET status = 'INATIVO' WHERE id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private Material mapearMaterial(ResultSet rs) throws SQLException {
        return new Material(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getString("categoria"),
            rs.getString("tipo_item"),
            rs.getString("tipo_cobranca"),
            rs.getBigDecimal("custo_base"),
            rs.getBigDecimal("custo_producao"),
            rs.getString("status")
        );
    }
}
