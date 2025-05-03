package com.hotel.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:hotel.db";
    private static Connection connection;
    
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Chargement du driver SQLite
                Class.forName("org.sqlite.JDBC");
                
                // Établissement de la connexion
                connection = DriverManager.getConnection(DB_URL);
                
                System.out.println("Connexion à la base de données SQLite établie.");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                System.err.println("Erreur lors de la connexion à la base de données: " + e.getMessage());
            }
        }
        return connection;
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Connexion à la base de données SQLite fermée.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
            }
        }
    }
}
