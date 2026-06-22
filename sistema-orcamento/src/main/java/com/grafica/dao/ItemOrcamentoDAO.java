package com.grafica.dao;

import com.grafica.model.ItemOrcamento;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemOrcamentoDAO {

    public void criar(ItemOrcamento item) {
        String sql = "INSERT INTO itens_orcamento (id_orcamento, id_produto, id_material, id_layout, largura_mm, altura_mm, " +
                    "quantidade, area_calculada, valor_bruto_item, valor_final_item, custo_unitario, tipo_cobranca_aplicado) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, item.getIdOrcamento());
            stmt.setInt(2, item.getIdProduto());
            stmt.setInt(3, item.getIdMaterial());
            if (item.getIdLayout() != null) {
                stmt.setInt(4, item.getIdLayout());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setInt(5, item.getLarguraMm() != null ? item.getLarguraMm() : 0);
            stmt.setInt(6, item.getAlturaMm() != null ? item.getAlturaMm() : 0);
            stmt.setInt(7, item.getQuantidade() != null ? item.getQuantidade() : 1);
            if (item.getAreaCalculada() != null) {
                stmt.setBigDecimal(8, item.getAreaCalculada());
            } else {
                stmt.setNull(8, Types.DECIMAL);
            }
            stmt.setBigDecimal(9, item.getValorBrutoItem());
            stmt.setBigDecimal(10, item.getValorFinalItem());
            stmt.setBigDecimal(11, item.getCustoUnitario());
            stmt.setString(12, item.getTipoCobrancaAplicado());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ItemOrcamento> listarPorOrcamento(Integer idOrcamento) {
        List<ItemOrcamento> itens = new ArrayList<>();
        String sql = "SELECT id, id_orcamento, id_produto, id_material, id_layout, largura_mm, altura_mm, " +
                    "quantidade, area_calculada, valor_bruto_item, valor_final_item, custo_unitario, tipo_cobranca_aplicado " +
                    "FROM itens_orcamento WHERE id_orcamento = ? ORDER BY id";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idOrcamento);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    itens.add(mapearItem(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itens;
    }

    public void deletar(Integer id) {
        String sql = "DELETE FROM itens_orcamento WHERE id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletarPorOrcamento(Integer idOrcamento) {
        String sql = "DELETE FROM itens_orcamento WHERE id_orcamento = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idOrcamento);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void inserirEmLote(List<ItemOrcamento> itens) {
        String sql = "INSERT INTO itens_orcamento (id_orcamento, id_produto, id_material, id_layout, largura_mm, altura_mm, " +
                    "quantidade, area_calculada, valor_bruto_item, valor_final_item, custo_unitario, tipo_cobranca_aplicado) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (ItemOrcamento item : itens) {
                stmt.setInt(1, item.getIdOrcamento());
                stmt.setInt(2, item.getIdProduto());
                stmt.setInt(3, item.getIdMaterial());
                if (item.getIdLayout() != null) {
                    stmt.setInt(4, item.getIdLayout());
                } else {
                    stmt.setNull(4, Types.INTEGER);
                }
                stmt.setInt(5, item.getLarguraMm() != null ? item.getLarguraMm() : 0);
                stmt.setInt(6, item.getAlturaMm() != null ? item.getAlturaMm() : 0);
                stmt.setInt(7, item.getQuantidade() != null ? item.getQuantidade() : 1);
                if (item.getAreaCalculada() != null) {
                    stmt.setBigDecimal(8, item.getAreaCalculada());
                } else {
                    stmt.setNull(8, Types.DECIMAL);
                }
                stmt.setBigDecimal(9, item.getValorBrutoItem());
                stmt.setBigDecimal(10, item.getValorFinalItem());
                stmt.setBigDecimal(11, item.getCustoUnitario());
                stmt.setString(12, item.getTipoCobrancaAplicado());
                stmt.addBatch();
            }

            stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ItemOrcamento mapearItem(ResultSet rs) throws SQLException {
        ItemOrcamento item = new ItemOrcamento();
        item.setId(rs.getInt("id"));
        item.setIdOrcamento(rs.getInt("id_orcamento"));
        item.setIdProduto(rs.getInt("id_produto"));
        item.setIdMaterial(rs.getInt("id_material"));

        int idLayout = rs.getInt("id_layout");
        if (!rs.wasNull()) {
            item.setIdLayout(idLayout);
        }

        item.setLarguraMm(rs.getInt("largura_mm"));
        item.setAlturaMm(rs.getInt("altura_mm"));
        item.setQuantidade(rs.getInt("quantidade"));

        BigDecimal area = rs.getBigDecimal("area_calculada");
        if (area != null) {
            item.setAreaCalculada(area);
        }

        item.setValorBrutoItem(rs.getBigDecimal("valor_bruto_item"));
        item.setValorFinalItem(rs.getBigDecimal("valor_final_item"));
        item.setCustoUnitario(rs.getBigDecimal("custo_unitario"));
        item.setTipoCobrancaAplicado(rs.getString("tipo_cobranca_aplicado"));

        return item;
    }
}