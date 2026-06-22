package com.grafica.dao;

import com.grafica.model.Material;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {
    
    public void criar(Material material) {
        String sql = "INSERT INTO materiais (nome, categoria, id_categoria_lucro, tipo_item, tipo_cobranca, custo_base, custo_producao, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, material.getNome());
            stmt.setString(2, material.getCategoria());
            if (material.getIdCategoriaLucro() != null) {
                stmt.setInt(3, material.getIdCategoriaLucro());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setString(4, material.getTipoItem());
            stmt.setString(5, material.getTipoCobranca());
            stmt.setBigDecimal(6, material.getCustoBase());
            stmt.setBigDecimal(7, material.getCustoProducao());
            stmt.setString(8, material.getStatus());
            
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
        String sql = "SELECT id, nome, categoria, id_categoria_lucro, tipo_item, tipo_cobranca, custo_base, custo_producao, status FROM materiais WHERE id = ?";
        
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
        return listarTodos(false);
    }

    public List<Material> listarTodos(boolean incluirInativos) {
        List<Material> materiais = new ArrayList<>();
        String sql = incluirInativos
            ? "SELECT id, nome, categoria, id_categoria_lucro, tipo_item, tipo_cobranca, custo_base, custo_producao, status FROM materiais ORDER BY nome"
            : "SELECT id, nome, categoria, id_categoria_lucro, tipo_item, tipo_cobranca, custo_base, custo_producao, status FROM materiais WHERE status = 'ATIVO' ORDER BY nome";
        
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
        String sql = "SELECT id, nome, categoria, id_categoria_lucro, tipo_item, tipo_cobranca, custo_base, custo_producao, status FROM materiais WHERE categoria = ? AND status = 'ATIVO' ORDER BY nome";
        
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
        String sql = "UPDATE materiais SET nome = ?, categoria = ?, id_categoria_lucro = ?, tipo_item = ?, tipo_cobranca = ?, custo_base = ?, custo_producao = ?, status = ? WHERE id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, material.getNome());
            stmt.setString(2, material.getCategoria());
            if (material.getIdCategoriaLucro() != null) {
                stmt.setInt(3, material.getIdCategoriaLucro());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setString(4, material.getTipoItem());
            stmt.setString(5, material.getTipoCobranca());
            stmt.setBigDecimal(6, material.getCustoBase());
            stmt.setBigDecimal(7, material.getCustoProducao());
            stmt.setString(8, material.getStatus());
            stmt.setInt(9, material.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void deletar(Integer id) {
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
        Integer idCategoriaLucro = rs.getInt("id_categoria_lucro");
        if (rs.wasNull()) {
            idCategoriaLucro = null;
        }
        
        return new Material(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getString("categoria"),
            idCategoriaLucro,
            rs.getString("tipo_item"),
            rs.getString("tipo_cobranca"),
            rs.getBigDecimal("custo_base"),
            rs.getBigDecimal("custo_producao"),
            rs.getString("status")
        );
    }
}