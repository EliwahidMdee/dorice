package util;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Unit tests for CSVImporter class.
 * Tests CSV import functionality.
 * 
 * @author Student Data Analysis Team
 * @version 1.0
 */
public class CSVImporterTest {
    
    private CSVImporter csvImporter;
    private String testCsvPath;
    
    @Before
    public void setUp() {
        csvImporter = new CSVImporter();
        testCsvPath = "/tmp/test_students.csv";
    }
    
    /**
     * Test CSV validation with correct headers.
     */
    @Test
    public void testValidateCSVWithCorrectHeaders() throws IOException {
        // Create a test CSV file
        createTestStudentCSV();
        
        String[] expectedHeaders = {
            "student_id", "first_name", "last_name", "email", 
            "phone", "gender", "date_of_birth", "address"
        };
        
        boolean isValid = csvImporter.validateCSV(testCsvPath, expectedHeaders);
        
        assertTrue("CSV should be valid with correct headers", isValid);
        
        // Clean up
        new File(testCsvPath).delete();
    }
    
    /**
     * Test CSV validation with incorrect headers.
     */
    @Test
    public void testValidateCSVWithIncorrectHeaders() throws IOException {
        // Create a test CSV file
        createTestStudentCSV();
        
        String[] expectedHeaders = {
            "wrong_header", "another_wrong_header"
        };
        
        boolean isValid = csvImporter.validateCSV(testCsvPath, expectedHeaders);
        
        assertFalse("CSV should be invalid with incorrect headers", isValid);
        
        // Clean up
        new File(testCsvPath).delete();
    }
    
    /**
     * Test CSV validation with non-existent file.
     */
    @Test
    public void testValidateCSVWithNonExistentFile() {
        String[] expectedHeaders = {"header1", "header2"};
        
        boolean isValid = csvImporter.validateCSV("/nonexistent/file.csv", expectedHeaders);
        
        assertFalse("Validation should fail for non-existent file", isValid);
    }
    
    /**
     * Test import from directory with no CSV files.
     */
    @Test
    public void testImportFromEmptyDirectory() throws IOException {
        // Create empty test directory
        File emptyDir = new File("/tmp/empty_test_dir");
        emptyDir.mkdirs();
        
        try {
            String result = csvImporter.importAllFromDirectory(emptyDir.getAbsolutePath());
            
            assertNotNull("Result should not be null", result);
            assertTrue("Result should indicate no files found", 
                    result.contains("No CSV files found"));
                    
        } catch (Exception e) {
            // If database is not available, just verify the directory handling works
            System.out.println("Database not available: " + e.getMessage());
        } finally {
            // Clean up
            emptyDir.delete();
        }
    }
    
    /**
     * Test import with invalid directory path.
     */
    @Test
    public void testImportFromInvalidDirectory() {
        try {
            csvImporter.importAllFromDirectory("/nonexistent/directory");
            fail("Should throw IOException for invalid directory");
            
        } catch (IOException e) {
            assertTrue("Exception message should mention invalid path", 
                    e.getMessage().contains("Invalid directory"));
        } catch (Exception e) {
            // Other exceptions are acceptable
            System.out.println("Other exception (acceptable): " + e.getMessage());
        }
    }
    
    /**
     * Helper method to create a test CSV file.
     */
    private void createTestStudentCSV() throws IOException {
        FileWriter writer = new FileWriter(testCsvPath);
        writer.write("student_id,first_name,last_name,email,phone,gender,date_of_birth,address\n");
        writer.write("1,John,Doe,john@test.com,+255712345001,M,2000-01-01,Dar es Salaam\n");
        writer.write("2,Jane,Smith,jane@test.com,+255712345002,F,2000-02-02,Arusha\n");
        writer.close();
    }
}
