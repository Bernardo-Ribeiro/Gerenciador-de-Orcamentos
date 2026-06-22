package com.grafica.dao;

import com.grafica.model.ItemOrcamentoAcabamento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemOrcamentoAcabamentoDAO {

    public void criar(ItemOrcamentoAcabamento acabamento) {
        String sql = "INSERT INTO item_orcamento_acabamentos (id_item_orcamento, id_material_acabamento, quantidade, valor_unitario, valor_total) " +
                    "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, acabamento.getIdItemOrcamento());
            stmt.setInt(2, acabamento.getIdMaterialAcabamento());
            stmt.setInt(3, acabamento.getQuantidade() != null ? acabamento.getQuantidade() : 1);
            stmt.setBigDecimal(4, acabamento.getValorUnitario());
            stmt.setBigDecimal(5, acabamento.getValorTotal());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    acabamento.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ItemOrcamentoAcabamento> listarPorItemOrcamento(Integer idItemOrcamento) {
        List<ItemOrcamentoAcabamento> acabamentos = new ArrayList<>();
        String sql = "SELECT id, id_item_orcamento, id_material_acabamento, quantidade, valor_unitario, valor_total " +
                    "FROM item_orcamento_acabamentos WHERE id_item_orcamento = ? ORDER BY id";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idItemOrcamento);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    acabamentos.add(mapearAcabamento(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return acabamentos;
    }

    public void deletar(Integer id) {
        String sql = "DELETE FROM item_orcamento_acabamentos WHERE id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletarPorItemOrcamento(Integer idItemOrcamento) {
        String sql = "DELETE FROM item_orcamento_acabamentos WHERE id_item_orcamento = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idItemOrcamento);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ItemOrcamentoAcabamento mapearAcabamento(ResultSet rs) throws SQLException {
        return new ItemOrcamentoAcabamento(
            rs.getInt("id"),
            rs.getInt("id_item_orcamento"),
            rs.getInt("id_material_acabamento"),
            rs.getInt("quantidade"),
            rs.getBigDecimal("valor_unitario"),
            rs.getBigDecimal("valor_total")
        );
    }
}