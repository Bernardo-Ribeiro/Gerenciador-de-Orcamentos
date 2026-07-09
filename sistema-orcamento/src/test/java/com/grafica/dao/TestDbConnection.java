package com.grafica.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utilitário de conexão com banco de dados para testes
 * Usa banco de dados H2 em memória
 */
public class TestDbConnection {

    private static final String DB_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Erro carregando driver H2: " + e.getMessage(), e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    public static void initializeSchema(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS clientes (
                    id INTEGER AUTO_INCREMENT PRIMARY KEY,
                    nome_razao_social VARCHAR(255),
                    cpf_cnpj VARCHAR(20),
                    email_contato VARCHAR(100),
                    telefone_whatsapp VARCHAR(20),
                    status VARCHAR(20),
                    data_cadastro TIMESTAMP
                )
                """);
            
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS orcamentos (
                    id INTEGER AUTO_INCREMENT PRIMARY KEY,
                    id_cliente INTEGER,
                    id_usuario INTEGER,
                    data_emissao DATE,
                    data_validade DATE,
                    status VARCHAR(50),
                    valor_bruto DECIMAL(10,2),
                    margem_lucro_percentual DECIMAL(5,2),
                    desconto_progressivo DECIMAL(5,2),
                    valor_final DECIMAL(10,2)
                )
                """);
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}