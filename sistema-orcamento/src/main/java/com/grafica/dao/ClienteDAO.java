package com.grafica.dao;

import com.grafica.model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void criar(Cliente cliente) {
        // DB uses nome_razao_social as column name
        String sql = "INSERT INTO clientes (nome_razao_social, cpf_cnpj, email_contato, telefone_whatsapp, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, cliente.getNome());
            pstmt.setString(2, cliente.getCpfCnpj());
            pstmt.setString(3, cliente.getEmailContato());
            pstmt.setString(4, cliente.getTelefoneWhatsapp());
            pstmt.setString(5, cliente.getStatus());
            
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                cliente.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Cliente obterPorId(Integer id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapearCliente(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        // Order by the actual column name in schema
        String sql = "SELECT * FROM clientes ORDER BY nome_razao_social ASC";
        
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    public void atualizar(Cliente cliente) {
        // update uses nome_razao_social column
        String sql = "UPDATE clientes SET nome_razao_social = ?, email_contato = ?, telefone_whatsapp = ?, status = ? WHERE id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cliente.getNome());
            pstmt.setString(2, cliente.getEmailContato());
            pstmt.setString(3, cliente.getTelefoneWhatsapp());
            pstmt.setString(4, cliente.getStatus());
            pstmt.setInt(5, cliente.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletar(Integer id) {
        String sql = "DELETE FROM clientes WHERE id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("id"));
        // map DB columns to model
        cliente.setNome(rs.getString("nome_razao_social"));
        cliente.setCpfCnpj(rs.getString("cpf_cnpj"));
        cliente.setEmailContato(rs.getString("email_contato"));
        cliente.setTelefoneWhatsapp(rs.getString("telefone_whatsapp"));
        cliente.setStatus(rs.getString("status"));
        return cliente;
    }
}
