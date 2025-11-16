package util;

import db.QueryExecutor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV Importer Utility for Bank Data Analysis System.
 * Reads CSV files and imports data into the database.
 * 
 * This class handles CSV parsing, data validation, and batch insertion
 * into database tables using JDBC prepared statements.
 * 
 * @author Bank Data Analysis Team
 * @version 1.0
 */
public class CSVImporter {
    
    private QueryExecutor queryExecutor;
    
    /**
     * Constructor - initializes the CSV importer with query executor.
     */
    public CSVImporter() {
        this.queryExecutor = new QueryExecutor();
    }
    
    /**
     * Imports account data from CSV file to database.
     * 
     * @param csvFilePath Path to the CSV file
     * @return Number of records imported
     * @throws IOException if file reading fails
     * @throws SQLException if database insertion fails
     */
    public int importAccounts(String csvFilePath) throws IOException, SQLException {
        int count = 0;
        
        try (Reader reader = new FileReader(csvFilePath, StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, 
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            
            for (CSVRecord record : csvParser) {
                String query = "INSERT INTO accounts (account_id, customer_name, email, " +
                        "phone, account_type, balance, date_opened, branch, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE customer_name=VALUES(customer_name), " +
                        "balance=VALUES(balance)";
                
                queryExecutor.executeParameterizedUpdate(query,
                        Integer.parseInt(record.get("account_id")),
                        record.get("customer_name"),
                        record.get("email"),
                        record.get("phone"),
                        record.get("account_type"),
                        Double.parseDouble(record.get("balance")),
                        record.get("date_opened"),
                        record.get("branch"),
                        record.get("status"));
                
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Imports transaction data from CSV file to database.
     * 
     * @param csvFilePath Path to the CSV file
     * @return Number of records imported
     * @throws IOException if file reading fails
     * @throws SQLException if database insertion fails
     */
    public int importTransactions(String csvFilePath) throws IOException, SQLException {
        int count = 0;
        
        try (Reader reader = new FileReader(csvFilePath, StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, 
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            
            for (CSVRecord record : csvParser) {
                String query = "INSERT INTO transactions (transaction_id, account_id, transaction_type, " +
                        "amount, transaction_date, description, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE status=VALUES(status)";
                
                queryExecutor.executeParameterizedUpdate(query,
                        Integer.parseInt(record.get("transaction_id")),
                        Integer.parseInt(record.get("account_id")),
                        record.get("transaction_type"),
                        Double.parseDouble(record.get("amount")),
                        record.get("transaction_date"),
                        record.get("description"),
                        record.get("status"));
                
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Imports loan data from CSV file to database.
     * 
     * @param csvFilePath Path to the CSV file
     * @return Number of records imported
     * @throws IOException if file reading fails
     * @throws SQLException if database insertion fails
     */
    public int importLoans(String csvFilePath) throws IOException, SQLException {
        int count = 0;
        
        try (Reader reader = new FileReader(csvFilePath, StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, 
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            
            for (CSVRecord record : csvParser) {
                String query = "INSERT INTO loans (loan_id, account_id, loan_type, amount, " +
                        "interest_rate, duration_months, start_date, status, monthly_payment) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE status=VALUES(status)";
                
                queryExecutor.executeParameterizedUpdate(query,
                        Integer.parseInt(record.get("loan_id")),
                        Integer.parseInt(record.get("account_id")),
                        record.get("loan_type"),
                        Double.parseDouble(record.get("amount")),
                        Double.parseDouble(record.get("interest_rate")),
                        Integer.parseInt(record.get("duration_months")),
                        record.get("start_date"),
                        record.get("status"),
                        Double.parseDouble(record.get("monthly_payment")));
                
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Imports card data from CSV file to database.
     * 
     * @param csvFilePath Path to the CSV file
     * @return Number of records imported
     * @throws IOException if file reading fails
     * @throws SQLException if database insertion fails
     */
    public int importCards(String csvFilePath) throws IOException, SQLException {
        int count = 0;
        
        try (Reader reader = new FileReader(csvFilePath, StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, 
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            
            for (CSVRecord record : csvParser) {
                String query = "INSERT INTO cards (card_id, account_id, card_type, card_number, " +
                        "expiry_date, credit_limit, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE status=VALUES(status)";
                
                queryExecutor.executeParameterizedUpdate(query,
                        Integer.parseInt(record.get("card_id")),
                        Integer.parseInt(record.get("account_id")),
                        record.get("card_type"),
                        record.get("card_number"),
                        record.get("expiry_date"),
                        Double.parseDouble(record.get("credit_limit")),
                        record.get("status"));
                
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Auto-detects and imports all CSV files from a directory.
     * 
     * @param directoryPath Path to directory containing CSV files
     * @return Summary of import results
     * @throws IOException if file operations fail
     * @throws SQLException if database operations fail
     */
    public String importAllFromDirectory(String directoryPath) throws IOException, SQLException {
        StringBuilder result = new StringBuilder();
        File directory = new File(directoryPath);
        
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IOException("Invalid directory path: " + directoryPath);
        }
        
        File[] csvFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
        
        if (csvFiles == null || csvFiles.length == 0) {
            return "No CSV files found in directory: " + directoryPath;
        }
        
        result.append("=== CSV Import Results ===\n\n");
        
        for (File csvFile : csvFiles) {
            String fileName = csvFile.getName().toLowerCase();
            int count = 0;
            
            try {
                if (fileName.contains("account") && !fileName.contains("transaction")) {
                    count = importAccounts(csvFile.getAbsolutePath());
                    result.append(String.format("Accounts: %d records imported\n", count));
                } 
                else if (fileName.contains("transaction")) {
                    count = importTransactions(csvFile.getAbsolutePath());
                    result.append(String.format("Transactions: %d records imported\n", count));
                } 
                else if (fileName.contains("loan")) {
                    count = importLoans(csvFile.getAbsolutePath());
                    result.append(String.format("Loans: %d records imported\n", count));
                } 
                else if (fileName.contains("card")) {
                    count = importCards(csvFile.getAbsolutePath());
                    result.append(String.format("Cards: %d records imported\n", count));
                } 
                else {
                    result.append(String.format("Skipped (unknown type): %s\n", fileName));
                }
            } catch (Exception e) {
                result.append(String.format("Error importing %s: %s\n", fileName, e.getMessage()));
            }
        }
        
        result.append("\n=== Import Complete ===");
        return result.toString();
    }
    
    /**
     * Validates CSV file format before import.
     * 
     * @param csvFilePath Path to CSV file
     * @param expectedHeaders Array of expected column headers
     * @return true if validation passes, false otherwise
     */
    public boolean validateCSV(String csvFilePath, String[] expectedHeaders) {
        try (Reader reader = new FileReader(csvFilePath, StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, 
                     CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            
            List<String> headers = new ArrayList<>(csvParser.getHeaderMap().keySet());
            
            if (headers.size() != expectedHeaders.length) {
                return false;
            }
            
            for (String expected : expectedHeaders) {
                if (!headers.contains(expected)) {
                    return false;
                }
            }
            
            return true;
            
        } catch (IOException e) {
            System.err.println("Error validating CSV: " + e.getMessage());
            return false;
        }
    }
}
