package com.grafica.dao;

import com.grafica.model.ProdutoMaterial;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoMaterialDAO {

    public void criar(ProdutoMaterial pm) {
        String sql = "INSERT INTO produto_materiais (id_produto, id_material, material_padrao) VALUES (?, ?, ?)";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pm.getIdProduto());
            stmt.setInt(2, pm.getIdMaterial());
            stmt.setBoolean(3, pm.getMaterialPadrao() != null && pm.getMaterialPadrao());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ProdutoMaterial> listarPorProduto(Integer idProduto) {
        List<ProdutoMaterial> lista = new ArrayList<>();
        String sql = "SELECT id_produto, id_material, material_padrao FROM produto_materiais WHERE id_produto = ? ORDER BY material_padrao DESC, id_material";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProduto);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<ProdutoMaterial> listarTodos() {
        List<ProdutoMaterial> lista = new ArrayList<>();
        String sql = "SELECT id_produto, id_material, material_padrao FROM produto_materiais ORDER BY id_produto, id_material";

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<ProdutoMaterial> listarPorMaterial(Integer idMaterial) {
        List<ProdutoMaterial> lista = new ArrayList<>();
        String sql = "SELECT id_produto, id_material, material_padrao FROM produto_materiais WHERE id_material = ? ORDER BY id_produto";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMaterial);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void deletar(Integer idProduto, Integer idMaterial) {
        String sql = "DELETE FROM produto_materiais WHERE id_produto = ? AND id_material = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProduto);
            stmt.setInt(2, idMaterial);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizar(ProdutoMaterial pm) {
        String sql = "UPDATE produto_materiais SET material_padrao = ? WHERE id_produto = ? AND id_material = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, pm.getMaterialPadrao() != null && pm.getMaterialPadrao());
            stmt.setInt(2, pm.getIdProduto());
            stmt.setInt(3, pm.getIdMaterial());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletarPorProduto(Integer idProduto) {
        String sql = "DELETE FROM produto_materiais WHERE id_produto = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProduto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ProdutoMaterial mapear(ResultSet rs) throws SQLException {
        return new ProdutoMaterial(
            rs.getInt("id_produto"),
            rs.getInt("id_material"),
            rs.getBoolean("material_padrao")
        );
    }
}