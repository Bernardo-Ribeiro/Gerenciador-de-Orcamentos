package com.grafica.dao;

import com.grafica.model.EscalaProdutiva;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EscalaProdutivaDAO {

    public void criar(EscalaProdutiva escala) {
        String sql = "INSERT INTO escalas_produtivas (id_material, qtd_minima, qtd_maxima, desconto_percentual) VALUES (?, ?, ?, ?)";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, escala.getIdMaterial());
            stmt.setInt(2, escala.getQtdMinima());
            stmt.setInt(3, escala.getQtdMaxima());
            stmt.setBigDecimal(4, escala.getDescontoPercentual());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    escala.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<EscalaProdutiva> listarPorMaterial(Integer idMaterial) {
        List<EscalaProdutiva> escalas = new ArrayList<>();
        String sql = "SELECT id, id_material, qtd_minima, qtd_maxima, desconto_percentual FROM escalas_produtivas WHERE id_material = ? ORDER BY qtd_minima";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMaterial);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    escalas.add(mapearEscala(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return escalas;
    }

    public List<EscalaProdutiva> listarTodos() {
        List<EscalaProdutiva> escalas = new ArrayList<>();
        String sql = "SELECT id, id_material, qtd_minima, qtd_maxima, desconto_percentual FROM escalas_produtivas ORDER BY id_material, qtd_minima";

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                escalas.add(mapearEscala(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return escalas;
    }

    public void atualizar(EscalaProdutiva escala) {
        String sql = "UPDATE escalas_produtivas SET id_material = ?, qtd_minima = ?, qtd_maxima = ?, desconto_percentual = ? WHERE id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, escala.getIdMaterial());
            stmt.setInt(2, escala.getQtdMinima());
            stmt.setInt(3, escala.getQtdMaxima());
            stmt.setBigDecimal(4, escala.getDescontoPercentual());
            stmt.setInt(5, escala.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletar(Integer id) {
        String sql = "DELETE FROM escalas_produtivas WHERE id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private EscalaProdutiva mapearEscala(ResultSet rs) throws SQLException {
        return new EscalaProdutiva(
            rs.getInt("id"),
            rs.getInt("id_material"),
            rs.getInt("qtd_minima"),
            rs.getInt("qtd_maxima"),
            rs.getBigDecimal("desconto_percentual")
        );
    }
}