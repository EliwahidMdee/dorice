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
 * CSV Importer Utility for Student Data Analysis System.
 * Reads CSV files and imports data into the database.
 * 
 * This class handles CSV parsing, data validation, and batch insertion
 * into database tables using JDBC prepared statements.
 * 
 * @author Student Data Analysis Team
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
     * Imports student data from CSV file to database.
     * 
     * @param csvFilePath Path to the CSV file
     * @return Number of records imported
     * @throws IOException if file reading fails
     * @throws SQLException if database insertion fails
     */
    public int importStudents(String csvFilePath) throws IOException, SQLException {
        int count = 0;
        
        try (Reader reader = new FileReader(csvFilePath, StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, 
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            
            for (CSVRecord record : csvParser) {
                String query = "INSERT INTO students (student_id, first_name, last_name, email, " +
                        "phone, gender, date_of_birth, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE first_name=VALUES(first_name), " +
                        "last_name=VALUES(last_name), email=VALUES(email)";
                
                queryExecutor.executeParameterizedUpdate(query,
                        Integer.parseInt(record.get("student_id")),
                        record.get("first_name"),
                        record.get("last_name"),
                        record.get("email"),
                        record.get("phone"),
                        record.get("gender"),
                        record.get("date_of_birth"),
                        record.get("address"));
                
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Imports program data from CSV file to database.
     * 
     * @param csvFilePath Path to the CSV file
     * @return Number of records imported
     * @throws IOException if file reading fails
     * @throws SQLException if database insertion fails
     */
    public int importPrograms(String csvFilePath) throws IOException, SQLException {
        int count = 0;
        
        try (Reader reader = new FileReader(csvFilePath, StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, 
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            
            for (CSVRecord record : csvParser) {
                String query = "INSERT INTO programs (program_id, program_name, department, " +
                        "duration_years, tuition_fee) VALUES (?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE program_name=VALUES(program_name), " +
                        "department=VALUES(department)";
                
                queryExecutor.executeParameterizedUpdate(query,
                        Integer.parseInt(record.get("program_id")),
                        record.get("program_name"),
                        record.get("department"),
                        Integer.parseInt(record.get("duration_years")),
                        Double.parseDouble(record.get("tuition_fee")));
                
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Imports admission data from CSV file to database.
     * 
     * @param csvFilePath Path to the CSV file
     * @return Number of records imported
     * @throws IOException if file reading fails
     * @throws SQLException if database insertion fails
     */
    public int importAdmissions(String csvFilePath) throws IOException, SQLException {
        int count = 0;
        
        try (Reader reader = new FileReader(csvFilePath, StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, 
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            
            for (CSVRecord record : csvParser) {
                String query = "INSERT INTO admissions (admission_id, student_id, program_id, " +
                        "admission_date, admission_year, entrance_score, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE status=VALUES(status)";
                
                queryExecutor.executeParameterizedUpdate(query,
                        Integer.parseInt(record.get("admission_id")),
                        Integer.parseInt(record.get("student_id")),
                        Integer.parseInt(record.get("program_id")),
                        record.get("admission_date"),
                        Integer.parseInt(record.get("admission_year")),
                        Double.parseDouble(record.get("entrance_score")),
                        record.get("status"));
                
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Imports grade data from CSV file to database.
     * 
     * @param csvFilePath Path to the CSV file
     * @return Number of records imported
     * @throws IOException if file reading fails
     * @throws SQLException if database insertion fails
     */
    public int importGrades(String csvFilePath) throws IOException, SQLException {
        int count = 0;
        
        try (Reader reader = new FileReader(csvFilePath, StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, 
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            
            for (CSVRecord record : csvParser) {
                String query = "INSERT INTO grades (grade_id, student_id, course_name, " +
                        "semester, academic_year, grade, credits) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE grade=VALUES(grade)";
                
                queryExecutor.executeParameterizedUpdate(query,
                        Integer.parseInt(record.get("grade_id")),
                        Integer.parseInt(record.get("student_id")),
                        record.get("course_name"),
                        Integer.parseInt(record.get("semester")),
                        Integer.parseInt(record.get("academic_year")),
                        record.get("grade"),
                        Integer.parseInt(record.get("credits")));
                
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
                if (fileName.contains("student") && !fileName.contains("admission")) {
                    count = importStudents(csvFile.getAbsolutePath());
                    result.append(String.format("Students: %d records imported\n", count));
                } 
                else if (fileName.contains("program")) {
                    count = importPrograms(csvFile.getAbsolutePath());
                    result.append(String.format("Programs: %d records imported\n", count));
                } 
                else if (fileName.contains("admission")) {
                    count = importAdmissions(csvFile.getAbsolutePath());
                    result.append(String.format("Admissions: %d records imported\n", count));
                } 
                else if (fileName.contains("grade")) {
                    count = importGrades(csvFile.getAbsolutePath());
                    result.append(String.format("Grades: %d records imported\n", count));
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
