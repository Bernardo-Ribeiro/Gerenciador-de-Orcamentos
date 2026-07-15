package com.grafica.dao;

import com.grafica.model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private Connection connection;

    public ClienteDAO() {
    }

    public ClienteDAO(Connection connection) {
        this.connection = connection;
    }

    private Connection getConnection() throws SQLException {
        if (connection != null) {
            return connection;
        }
        return DbConnection.getConnection();
    }

    public void criar(Cliente cliente) {
        String sql = "INSERT INTO clientes (nome_razao_social, cpf_cnpj, email_contato, telefone_whatsapp, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, cliente.getNome());
            pstmt.setString(2, cliente.getCpfCnpj());
            pstmt.setString(3, cliente.getEmailContato());
            pstmt.setString(4, cliente.getTelefoneWhatsapp());
            String status = cliente.getStatus();
            if (status == null || !(status.equalsIgnoreCase("ATIVO") || status.equalsIgnoreCase("INATIVO"))) {
                status = "ATIVO";
            }
            pstmt.setString(5, status);
            
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
        try (Connection conn = getConnection();
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
        String sql = "SELECT * FROM clientes ORDER BY nome_razao_social ASC";
        
        try (Connection conn = getConnection();
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
        String sql = "UPDATE clientes SET nome_razao_social = ?, email_contato = ?, telefone_whatsapp = ?, status = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cliente.getNome());
            pstmt.setString(2, cliente.getEmailContato());
            pstmt.setString(3, cliente.getTelefoneWhatsapp());
            String status = cliente.getStatus();
            if (status == null || !(status.equalsIgnoreCase("ATIVO") || status.equalsIgnoreCase("INATIVO"))) {
                status = "ATIVO";
            }
            pstmt.setString(4, status);
            pstmt.setInt(5, cliente.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletar(Integer id) {
        String sql = "DELETE FROM clientes WHERE id = ?";
        try (Connection conn = getConnection();
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
