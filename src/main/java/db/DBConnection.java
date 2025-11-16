package db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database Connection Manager for Bank Data Analysis System.
 * Handles database connectivity using JDBC with MySQL/MariaDB.
 * 
 * This class implements the Singleton pattern to ensure only one
 * connection manager instance exists throughout the application.
 * 
 * @author Bank Data Analysis Team
 * @version 1.0
 */
public class DBConnection {
    
    private static DBConnection instance;
    private String url;
    private String user;
    private String password;
    private Connection connection;
    
    /**
     * Private constructor to prevent direct instantiation.
     * Loads database configuration from config.properties file.
     */
    private DBConnection() {
        loadConfiguration();
    }
    
    /**
     * Gets the singleton instance of DBConnection.
     * Creates a new instance if one doesn't exist.
     * 
     * @return The singleton DBConnection instance
     */
    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }
    
    /**
     * Loads database configuration from the properties file.
     * Configuration includes URL, username, and password.
     */
    private void loadConfiguration() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            
            if (input == null) {
                System.err.println("Unable to find config.properties");
                // Set default values
                this.url = "jdbc:mysql://localhost:3306/student_data_analysis?useSSL=false&serverTimezone=UTC";
                this.user = "root";
                this.password = "";
                return;
            }
            
            props.load(input);
            this.url = props.getProperty("db.url");
            this.user = props.getProperty("db.user");
            this.password = props.getProperty("db.password");
            
            System.out.println("Database configuration loaded successfully");
            
        } catch (IOException ex) {
            System.err.println("Error loading database configuration: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    /**
     * Establishes and returns a database connection.
     * Reuses existing connection if available and valid.
     * 
     * @return Active database Connection object
     * @throws SQLException if connection cannot be established
     */
    public Connection getConnection() throws SQLException {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Check if connection exists and is valid
            if (connection == null || connection.isClosed() || !connection.isValid(5)) {
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Database connection established successfully");
            }
            
            return connection;
            
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found");
            e.printStackTrace();
            throw new SQLException("Driver not found", e);
        }
    }
    
    /**
     * Closes the database connection if it's open.
     * Should be called when the application is shutting down.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed successfully");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Tests the database connection.
     * 
     * @return true if connection is successful, false otherwise
     */
    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed() && conn.isValid(5);
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Gets the database URL being used.
     * 
     * @return Database connection URL
     */
    public String getUrl() {
        return url;
    }
    
    /**
     * Gets the database username being used.
     * 
     * @return Database username
     */
    public String getUser() {
        return user;
    }
}
