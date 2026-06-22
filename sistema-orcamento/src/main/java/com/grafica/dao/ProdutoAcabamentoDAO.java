package com.grafica.dao;

import com.grafica.model.ProdutoAcabamento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoAcabamentoDAO {

    public void criar(ProdutoAcabamento pa) {
        String sql = "INSERT INTO produto_acabamentos (id_produto, id_material, obrigatorio) VALUES (?, ?, ?)";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pa.getIdProduto());
            stmt.setInt(2, pa.getIdMaterial());
            stmt.setBoolean(3, pa.getObrigatorio() != null && pa.getObrigatorio());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ProdutoAcabamento> listarPorProduto(Integer idProduto) {
        List<ProdutoAcabamento> lista = new ArrayList<>();
        String sql = "SELECT pa.id_produto, pa.id_material, pa.obrigatorio " +
                    "FROM produto_acabamentos pa " +
                    "WHERE pa.id_produto = ? ORDER BY pa.obrigatorio DESC, pa.id_material";

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

    public List<ProdutoAcabamento> listarTodos() {
        List<ProdutoAcabamento> lista = new ArrayList<>();
        String sql = "SELECT id_produto, id_material, obrigatorio FROM produto_acabamentos ORDER BY id_produto, id_material";

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

    public void deletar(Integer idProduto, Integer idMaterial) {
        String sql = "DELETE FROM produto_acabamentos WHERE id_produto = ? AND id_material = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProduto);
            stmt.setInt(2, idMaterial);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletarPorProduto(Integer idProduto) {
        String sql = "DELETE FROM produto_acabamentos WHERE id_produto = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProduto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ProdutoAcabamento mapear(ResultSet rs) throws SQLException {
        return new ProdutoAcabamento(
            rs.getInt("id_produto"),
            rs.getInt("id_material"),
            rs.getBoolean("obrigatorio")
        );
    }
}