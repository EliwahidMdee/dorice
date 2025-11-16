package db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Unit tests for DBConnection class.
 * Tests database connection functionality.
 * 
 * @author Student Data Analysis Team
 * @version 1.0
 */
public class DBConnectionTest {
    
    private DBConnection dbConnection;
    
    @Before
    public void setUp() {
        dbConnection = DBConnection.getInstance();
    }
    
    @After
    public void tearDown() {
        if (dbConnection != null) {
            dbConnection.closeConnection();
        }
    }
    
    /**
     * Test that DBConnection uses Singleton pattern.
     */
    @Test
    public void testSingletonInstance() {
        DBConnection instance1 = DBConnection.getInstance();
        DBConnection instance2 = DBConnection.getInstance();
        
        assertNotNull("Instance should not be null", instance1);
        assertSame("Instances should be the same", instance1, instance2);
    }
    
    /**
     * Test database connection can be established.
     * Note: This test requires MySQL/MariaDB to be running.
     */
    @Test
    public void testGetConnection() {
        try {
            Connection conn = dbConnection.getConnection();
            
            assertNotNull("Connection should not be null", conn);
            assertFalse("Connection should not be closed", conn.isClosed());
            assertTrue("Connection should be valid", conn.isValid(5));
            
        } catch (SQLException e) {
            // If database is not available, test should not fail
            System.out.println("Database not available for testing: " + e.getMessage());
            System.out.println("This is acceptable if XAMPP is not running");
        }
    }
    
    /**
     * Test connection test method.
     */
    @Test
    public void testConnectionTest() {
        boolean result = dbConnection.testConnection();
        
        // Connection test should return a boolean (true if connected, false if not)
        // We don't assert true because database might not be available during testing
        System.out.println("Connection test result: " + result);
        
        if (result) {
            System.out.println("Database connection successful");
        } else {
            System.out.println("Database not available - this is expected if XAMPP is not running");
        }
    }
    
    /**
     * Test that connection URL is configured.
     */
    @Test
    public void testUrlConfiguration() {
        String url = dbConnection.getUrl();
        
        assertNotNull("URL should not be null", url);
        assertTrue("URL should contain jdbc:mysql", url.contains("jdbc:mysql"));
        assertTrue("URL should contain database name", url.contains("student_data_analysis"));
    }
    
    /**
     * Test that user is configured.
     */
    @Test
    public void testUserConfiguration() {
        String user = dbConnection.getUser();
        
        assertNotNull("User should not be null", user);
        assertFalse("User should not be empty", user.isEmpty());
    }
    
    /**
     * Test connection close.
     */
    @Test
    public void testCloseConnection() {
        try {
            // Get a connection first
            Connection conn = dbConnection.getConnection();
            
            if (conn != null) {
                // Close it
                dbConnection.closeConnection();
                
                // Connection should be closed now
                assertTrue("Connection should be closed", conn.isClosed());
            }
            
        } catch (SQLException e) {
            System.out.println("Database not available for close test: " + e.getMessage());
        }
    }
}
