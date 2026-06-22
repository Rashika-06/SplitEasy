package com.spliteasy.db;

import java.sql.*;
import com.spliteasy.model.User;

public class DatabaseHelper {
    
    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/spliteasy";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";  // Empty if no password set
    
    // Load MySQL Driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    // Get database connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    // Register new user
    public static String registerUser(User user) {
        try (Connection conn = getConnection()) {
            // Check if username already exists
            String checkQuery = "SELECT * FROM users WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, user.getUsername());
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                return "Username already exists!";
            }
            
            // Insert new user
            String insertQuery = "INSERT INTO users (fullname, username, password) VALUES (?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, user.getFullname());
            insertStmt.setString(2, user.getUsername());
            insertStmt.setString(3, user.getPassword());
            
            int rowsInserted = insertStmt.executeUpdate();
            
            if (rowsInserted > 0) {
                return "Registration successful! Please login.";
            } else {
                return "Registration failed! Please try again.";
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error! " + e.getMessage();
        }
    }
    
    // Login user
    public static User loginUser(String username, String password) {
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("fullname"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Initialize database tables (run once)
    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "fullname VARCHAR(100) NOT NULL," +
                "username VARCHAR(50) NOT NULL UNIQUE," +
                "password VARCHAR(100) NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            
            Statement stmt = conn.createStatement();
            stmt.execute(createTableQuery);
            System.out.println("Database initialized successfully!");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
