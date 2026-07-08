package com.grafica.dao;

import java.io.*;
import java.nio.file.*;
import java.sql.*;

/**
 * Gerenciador de conexões SQLite
 * Cria e inicializa o banco de dados automaticamente
 */
public class DbConnection {

    private static final String DB_DIR = "database";
    private static final String DB_PATH = DB_DIR + File.separator + "app.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;
    private static final String DB_DRIVER = "org.sqlite.JDBC";
    
    private static boolean initialized = false;

    static {
        try {
            Class.forName(DB_DRIVER);
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            System.err.println("Erro carregando driver SQLite: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Inicializa o banco de dados automaticamente
     * Cria diretório, arquivo do banco, schema e dados iniciais
     */
    private static synchronized void initializeDatabase() {
        if (initialized) {
            return;
        }
        
        try {
            // Criar diretório do banco de dados se não existir
            Path dbDirPath = Paths.get(DB_DIR);
            if (!Files.exists(dbDirPath)) {
                Files.createDirectories(dbDirPath);
            }
            
            // Conectar ao banco (cria o arquivo se não existir)
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                // Habilitar foreign keys
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON");
                }
                
                // Verificar se o schema já existe
                if (!tableExists(conn, "categorias_lucro")) {
                    System.out.println("Criando schema do banco de dados...");
                    executeSqlScript(conn, "/schema.sql");
                    System.out.println("Schema criado com sucesso!");
                    
                    System.out.println("Carregando dados iniciais...");
                    executeSqlScript(conn, "/seed.sql");
                    System.out.println("Dados iniciais carregados com sucesso!");
                }
                
                initialized = true;
                System.out.println("Banco de dados SQLite inicializado: " + DB_PATH);
            }
            
        } catch (IOException | SQLException e) {
            System.err.println("Erro ao inicializar banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Verifica se uma tabela existe no banco de dados
     */
    private static boolean tableExists(Connection conn, String tableName) throws SQLException {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tableName);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Executa um script SQL a partir do classpath
     */
    private static void executeSqlScript(Connection conn, String resourcePath) throws IOException, SQLException {
        try (InputStream is = DbConnection.class.getResourceAsStream(resourcePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            
            StringBuilder sqlBuilder = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                // Ignorar comentários
                if (line.trim().startsWith("--")) {
                    continue;
                }
                sqlBuilder.append(line).append("\n");
            }
            
            String sql = sqlBuilder.toString();
            String[] statements = sql.split(";");
            
            for (String stmt : statements) {
                String trimmed = stmt.trim();
                if (!trimmed.isEmpty()) {
                    try (Statement s = conn.createStatement()) {
                        s.execute(trimmed);
                    }
                }
            }
        }
    }

    /**
     * Obtém uma conexão com o banco de dados
     * @return Connection ativa ou null se falhar
     */
    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            // Habilitar foreign keys para cada conexão
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
            return conn;
        } catch (SQLException e) {
            System.err.println("Erro ao conectar com banco de dados: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Fecha uma conexão de forma segura
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro fechando conexão: " + e.getMessage());
            }
        }
    }
    
    /**
     * Obtém o caminho do banco de dados
     */
    public static String getDatabasePath() {
        return DB_PATH;
    }
}