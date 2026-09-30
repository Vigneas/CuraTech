package com.medikiosk.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/medikiosk";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // To be updated by user/env
    
    private static Connection connection = null;

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Ensure driver is loaded
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found.", e);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Setup basic schema for development if it doesn't exist
    public static void initializeDatabase() {
        // Use a connection without database selected to create the DB first
        String baseUrl = "jdbc:mysql://localhost:3306/";
        try (Connection conn = DriverManager.getConnection(baseUrl, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS medikiosk");
            stmt.execute("USE medikiosk");
            
            // For development: drop tables to apply new schema
            stmt.execute("DROP TABLE IF EXISTS assessments");
            stmt.execute("DROP TABLE IF EXISTS patients");

            String createPatientsTable = "CREATE TABLE IF NOT EXISTS patients (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "age INT NOT NULL, " +
                    "contact_info VARCHAR(100), " +
                    "gender VARCHAR(20), " +
                    "blood_type VARCHAR(10), " +
                    "allergies TEXT, " +
                    "preferred_language VARCHAR(50), " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
            stmt.execute(createPatientsTable);

            String createAssessmentsTable = "CREATE TABLE IF NOT EXISTS assessments (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "patient_id INT, " +
                    "symptoms TEXT, " +
                    "severity_score INT, " +
                    "priority_tier VARCHAR(50), " +
                    "status VARCHAR(50), " +
                    "arrival_time TIMESTAMP, " +
                    "diagnosis TEXT, " +
                    "prescription_notes TEXT, " +
                    "FOREIGN KEY (patient_id) REFERENCES patients(id)" +
                    ")";
            stmt.execute(createAssessmentsTable);
            
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }
}
