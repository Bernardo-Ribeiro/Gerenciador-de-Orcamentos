package com.grafica.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestor de conexiones con la base de datos MySQL
 * Usa variables de entorno o valores por defecto
 */
public class DbConnection {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/grafica_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Error cargando driver MySQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obtiene una conexión a la base de datos
     * @return Connection activa o null si falla
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error al conectar con la BD: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Cierra una conexión de forma segura
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error cerrando conexión: " + e.getMessage());
            }
        }
    }
}
