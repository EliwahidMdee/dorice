package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 * Query Executor for Bank Data Analysis System.
 * Executes SQL queries and returns results in various formats.
 * 
 * This class provides methods to execute queries and format results
 * for display in GUI components like JTable, or for data processing.
 * 
 * @author Bank Data Analysis Team
 * @version 1.0
 */
public class QueryExecutor {
    
    private DBConnection dbConnection;
    
    /**
     * Constructor - initializes the query executor with database connection.
     */
    public QueryExecutor() {
        this.dbConnection = DBConnection.getInstance();
    }
    
    /**
     * Executes a SELECT query and returns results as a TableModel for JTable.
     * 
     * @param query SQL SELECT query to execute
     * @return DefaultTableModel containing query results
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel executeQuery(String query) throws SQLException {
        Connection conn = dbConnection.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            
            // Get column metadata
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            // Create column names array
            String[] columnNames = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = metaData.getColumnLabel(i + 1);
            }
            
            // Create data list
            List<Object[]> dataList = new ArrayList<>();
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                dataList.add(row);
            }
            
            // Convert list to array
            Object[][] data = dataList.toArray(new Object[0][]);
            
            // Create and return table model
            return new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table read-only
                }
            };
            
        } finally {
            // Close resources
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
    }
    
    /**
     * Executes a parameterized SELECT query and returns results as TableModel.
     * 
     * @param query SQL query with ? placeholders
     * @param params Parameters to replace placeholders
     * @return DefaultTableModel containing query results
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel executeParameterizedQuery(String query, Object... params) 
            throws SQLException {
        Connection conn = dbConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = conn.prepareStatement(query);
            
            // Set parameters
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            rs = pstmt.executeQuery();
            
            // Get column metadata
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            // Create column names array
            String[] columnNames = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = metaData.getColumnLabel(i + 1);
            }
            
            // Create data list
            List<Object[]> dataList = new ArrayList<>();
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                dataList.add(row);
            }
            
            // Convert list to array
            Object[][] data = dataList.toArray(new Object[0][]);
            
            // Create and return table model
            return new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
    }
    
    /**
     * Executes an UPDATE, INSERT, or DELETE query.
     * 
     * @param query SQL DML query to execute
     * @return Number of rows affected
     * @throws SQLException if query execution fails
     */
    public int executeUpdate(String query) throws SQLException {
        Connection conn = dbConnection.getConnection();
        Statement stmt = null;
        
        try {
            stmt = conn.createStatement();
            return stmt.executeUpdate(query);
        } finally {
            if (stmt != null) stmt.close();
        }
    }
    
    /**
     * Executes a parameterized UPDATE, INSERT, or DELETE query.
     * 
     * @param query SQL query with ? placeholders
     * @param params Parameters to replace placeholders
     * @return Number of rows affected
     * @throws SQLException if query execution fails
     */
    public int executeParameterizedUpdate(String query, Object... params) 
            throws SQLException {
        Connection conn = dbConnection.getConnection();
        PreparedStatement pstmt = null;
        
        try {
            pstmt = conn.prepareStatement(query);
            
            // Set parameters
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            return pstmt.executeUpdate();
            
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }
    
    /**
     * Executes a query and returns a single scalar value.
     * Useful for COUNT, SUM, AVG queries.
     * 
     * @param query SQL query returning single value
     * @return Result as Object (cast to appropriate type)
     * @throws SQLException if query execution fails
     */
    public Object executeScalar(String query) throws SQLException {
        Connection conn = dbConnection.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            
            if (rs.next()) {
                return rs.getObject(1);
            }
            return null;
            
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
    }
    
    /**
     * Executes multiple SQL statements in a batch.
     * Useful for importing large amounts of data.
     * 
     * @param queries Array of SQL statements to execute
     * @return Array of update counts for each statement
     * @throws SQLException if any query execution fails
     */
    public int[] executeBatch(String[] queries) throws SQLException {
        Connection conn = dbConnection.getConnection();
        Statement stmt = null;
        
        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            
            // Add all queries to batch
            for (String query : queries) {
                stmt.addBatch(query);
            }
            
            // Execute batch
            int[] results = stmt.executeBatch();
            conn.commit();
            
            return results;
            
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
            if (stmt != null) stmt.close();
        }
    }
}
