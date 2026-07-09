package com.grafica.dao;

import com.grafica.model.Orcamento;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class OrcamentoDAO {

    private Connection connection;

    public OrcamentoDAO() {
    }

    public OrcamentoDAO(Connection connection) {
        this.connection = connection;
    }

    private Connection getConnection() throws SQLException {
        if (connection != null) {
            return connection;
        }
        return DbConnection.getConnection();
    }

    public void criar(Orcamento orcamento) {
        String sql = "INSERT INTO orcamentos (id_cliente, id_usuario, data_emissao, data_validade, status, valor_bruto, margem_lucro_percentual, desconto_progressivo, valor_final) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, orcamento.getIdCliente());
            pstmt.setInt(2, orcamento.getIdUsuario() != null ? orcamento.getIdUsuario() : 1);
            pstmt.setDate(3, Date.valueOf(LocalDate.now()));
            pstmt.setDate(4, Date.valueOf(LocalDate.now().plusDays(15)));
            pstmt.setString(5, orcamento.getStatus() != null ? orcamento.getStatus().toUpperCase() : "PENDENTE");
            pstmt.setBigDecimal(6, orcamento.getValorBruto() != null ? orcamento.getValorBruto() : BigDecimal.ZERO);
            pstmt.setBigDecimal(7, orcamento.getMargemLucroPercentual());
            pstmt.setBigDecimal(8, orcamento.getDescontoProgressivo());
            pstmt.setBigDecimal(9, orcamento.getValorFinal() != null ? orcamento.getValorFinal() : BigDecimal.ZERO);
            
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                orcamento.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Orcamento> listarTodos() {
        List<Orcamento> orcamentos = new ArrayList<>();
        String sql = "SELECT * FROM orcamentos WHERE status != 'EXCLUIDO' ORDER BY data_emissao DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                orcamentos.add(mapearOrcamento(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orcamentos;
    }

    public Orcamento buscarPorId(Integer id) {
        String sql = "SELECT * FROM orcamentos WHERE id = ? AND status != 'EXCLUIDO'";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearOrcamento(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void atualizar(Orcamento orcamento) {
        String sql = "UPDATE orcamentos SET valor_final = ?, margem_lucro_percentual = ?, desconto_progressivo = ?, status = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setBigDecimal(1, orcamento.getValorFinal());
            pstmt.setBigDecimal(2, orcamento.getMargemLucroPercentual());
            pstmt.setBigDecimal(3, orcamento.getDescontoProgressivo());
            pstmt.setString(4, orcamento.getStatus() != null ? orcamento.getStatus().toUpperCase() : "PENDENTE");
            pstmt.setInt(5, orcamento.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletar(Integer id) {
        String sql = "UPDATE orcamentos SET status = 'EXCLUIDO' WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Orcamento mapearOrcamento(ResultSet rs) throws SQLException {
        Orcamento orcamento = new Orcamento();
        orcamento.setId(rs.getInt("id"));
        orcamento.setIdCliente(rs.getInt("id_cliente"));
        orcamento.setIdUsuario(rs.getInt("id_usuario"));
        
        String dataEmissao = rs.getString("data_emissao");
        if (dataEmissao != null && !dataEmissao.isBlank()) {
            try {
                orcamento.setDataEmissao(LocalDate.parse(dataEmissao));
            } catch (Exception e) {
                try {
                    long timestamp = Long.parseLong(dataEmissao);
                    orcamento.setDataEmissao(Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate());
                } catch (Exception e2) {}
            }
        }

        String dataValidade = rs.getString("data_validade");
        if (dataValidade != null && !dataValidade.isBlank()) {
            try {
                orcamento.setDataValidade(LocalDate.parse(dataValidade));
            } catch (Exception e) {
                try {
                    long timestamp = Long.parseLong(dataValidade);
                    orcamento.setDataValidade(Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate());
                } catch (Exception e2) {}
            }
        }
        
        orcamento.setStatus(rs.getString("status"));
        orcamento.setValorBruto(rs.getBigDecimal("valor_bruto"));
        orcamento.setMargemLucroPercentual(rs.getBigDecimal("margem_lucro_percentual"));
        orcamento.setDescontoProgressivo(rs.getBigDecimal("desconto_progressivo"));
        orcamento.setValorFinal(rs.getBigDecimal("valor_final"));
        
        return orcamento;
    }
}