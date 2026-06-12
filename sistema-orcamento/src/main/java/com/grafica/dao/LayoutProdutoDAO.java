package com.grafica.dao;

import com.grafica.model.LayoutProduto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LayoutProdutoDAO {
    
    public List<LayoutProduto> listarPorMaterial(int idMaterial) throws SQLException {
        List<LayoutProduto> layouts = new ArrayList<>();
        String sql = "SELECT * FROM layouts_produto WHERE id_material = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMaterial);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    layouts.add(new LayoutProduto(
                        rs.getInt("id"),
                        rs.getInt("id_material"),
                        rs.getString("nome_layout"),
                        rs.getInt("largura_mm"),
                        rs.getInt("altura_mm")
                    ));
                }
            }
        }
        return layouts;
    }

    public void criar(LayoutProduto layout) throws SQLException {
        String sql = "INSERT INTO layouts_produto (id_material, nome_layout, largura_mm, altura_mm) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, layout.getIdMaterial());
            stmt.setString(2, layout.getNomeLayout());
            stmt.setInt(3, layout.getLarguraMm());
            stmt.setInt(4, layout.getAlturaMm());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    layout.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<LayoutProduto> listarTodos() throws SQLException {
        List<LayoutProduto> layouts = new ArrayList<>();
        String sql = "SELECT * FROM layouts_produto";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                layouts.add(new LayoutProduto(
                    rs.getInt("id"),
                    rs.getInt("id_material"),
                    rs.getString("nome_layout"),
                    rs.getInt("largura_mm"),
                    rs.getInt("altura_mm")
                ));
            }
        }
        return layouts;
    }

    public void atualizar(LayoutProduto layout) throws SQLException {
        String sql = "UPDATE layouts_produto SET id_material = ?, nome_layout = ?, largura_mm = ?, altura_mm = ? WHERE id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, layout.getIdMaterial());
            stmt.setString(2, layout.getNomeLayout());
            stmt.setInt(3, layout.getLarguraMm());
            stmt.setInt(4, layout.getAlturaMm());
            stmt.setInt(5, layout.getId());
            
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM layouts_produto WHERE id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
