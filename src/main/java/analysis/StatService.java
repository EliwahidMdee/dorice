package analysis;

import db.QueryExecutor;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.*;

/**
 * Statistical Analysis Service for Student Data Analysis System.
 * Provides methods for statistical queries and data analysis.
 * 
 * This class implements all the statistical analysis requirements
 * from the project specification, including aggregations, trends,
 * and distribution analysis.
 * 
 * @author Student Data Analysis Team
 * @version 1.0
 */
public class StatService {
    
    private QueryExecutor queryExecutor;
    
    /**
     * Constructor - initializes the service with query executor.
     */
    public StatService() {
        this.queryExecutor = new QueryExecutor();
    }
    
    /**
     * Gets admission statistics by program.
     * Returns program name, department, total admissions, and average scores.
     * 
     * @return TableModel with program admission statistics
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel getAdmissionsByProgram() throws SQLException {
        String query = "SELECT p.program_name, p.department, " +
                "COUNT(a.admission_id) AS total_admissions, " +
                "AVG(a.entrance_score) AS avg_entrance_score, " +
                "MIN(a.entrance_score) AS min_score, " +
                "MAX(a.entrance_score) AS max_score " +
                "FROM programs p " +
                "LEFT JOIN admissions a ON p.program_id = a.program_id " +
                "GROUP BY p.program_id, p.program_name, p.department " +
                "ORDER BY total_admissions DESC";
        
        return queryExecutor.executeQuery(query);
    }
    
    /**
     * Gets admission trends by year.
     * Shows total admissions, average scores, and status distribution per year.
     * 
     * @return TableModel with yearly admission trends
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel getAdmissionsByYear() throws SQLException {
        String query = "SELECT admission_year, " +
                "COUNT(admission_id) AS total_admissions, " +
                "AVG(entrance_score) AS avg_entrance_score, " +
                "COUNT(CASE WHEN status = 'Active' THEN 1 END) AS active_count, " +
                "COUNT(CASE WHEN status = 'Graduated' THEN 1 END) AS graduated_count " +
                "FROM admissions " +
                "GROUP BY admission_year " +
                "ORDER BY admission_year";
        
        return queryExecutor.executeQuery(query);
    }
    
    /**
     * Gets student status distribution.
     * Shows count and percentage for each status type.
     * 
     * @return TableModel with status distribution
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel getStatusDistribution() throws SQLException {
        String query = "SELECT status, COUNT(*) AS count, " +
                "ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM admissions), 2) AS percentage " +
                "FROM admissions " +
                "GROUP BY status " +
                "ORDER BY count DESC";
        
        return queryExecutor.executeQuery(query);
    }
    
    /**
     * Gets department-wise analysis.
     * Includes program count, admissions, scores, and tuition fees by department.
     * 
     * @return TableModel with department statistics
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel getDepartmentAnalysis() throws SQLException {
        String query = "SELECT p.department, " +
                "COUNT(DISTINCT p.program_id) AS total_programs, " +
                "COUNT(a.admission_id) AS total_admissions, " +
                "AVG(a.entrance_score) AS avg_entrance_score, " +
                "AVG(p.tuition_fee) AS avg_tuition_fee " +
                "FROM programs p " +
                "LEFT JOIN admissions a ON p.program_id = a.program_id " +
                "GROUP BY p.department " +
                "ORDER BY total_admissions DESC";
        
        return queryExecutor.executeQuery(query);
    }
    
    /**
     * Gets gender distribution in admissions.
     * Shows admissions, scores, and graduation rates by gender.
     * 
     * @return TableModel with gender analysis
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel getGenderDistribution() throws SQLException {
        String query = "SELECT s.gender, " +
                "COUNT(a.admission_id) AS total_admissions, " +
                "AVG(a.entrance_score) AS avg_entrance_score, " +
                "COUNT(CASE WHEN a.status = 'Graduated' THEN 1 END) AS graduated_count " +
                "FROM students s " +
                "INNER JOIN admissions a ON s.student_id = a.student_id " +
                "GROUP BY s.gender";
        
        return queryExecutor.executeQuery(query);
    }
    
    /**
     * Gets top performing students by entrance score.
     * 
     * @param limit Maximum number of students to return
     * @return TableModel with top students
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel getTopStudents(int limit) throws SQLException {
        String query = "SELECT CONCAT(s.first_name, ' ', s.last_name) AS student_name, " +
                "s.email, p.program_name, a.admission_year, " +
                "a.entrance_score, a.status " +
                "FROM students s " +
                "INNER JOIN admissions a ON s.student_id = a.student_id " +
                "INNER JOIN programs p ON a.program_id = p.program_id " +
                "ORDER BY a.entrance_score DESC " +
                "LIMIT " + limit;
        
        return queryExecutor.executeQuery(query);
    }
    
    /**
     * Gets program popularity trends over years.
     * Shows admission counts per program per year.
     * 
     * @return TableModel with program trends
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel getProgramTrends() throws SQLException {
        String query = "SELECT p.program_name, a.admission_year, " +
                "COUNT(a.admission_id) AS admissions_count " +
                "FROM programs p " +
                "INNER JOIN admissions a ON p.program_id = a.program_id " +
                "GROUP BY p.program_id, p.program_name, a.admission_year " +
                "ORDER BY p.program_name, a.admission_year";
        
        return queryExecutor.executeQuery(query);
    }
    
    /**
     * Gets student performance analysis with GPA calculation.
     * Calculates total courses, credits, and GPA for each student.
     * 
     * @return TableModel with student performance
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel getStudentPerformance() throws SQLException {
        String query = "SELECT s.student_id, " +
                "CONCAT(s.first_name, ' ', s.last_name) AS student_name, " +
                "COUNT(g.grade_id) AS total_courses, " +
                "SUM(g.credits) AS total_credits, " +
                "ROUND(AVG(CASE g.grade " +
                "WHEN 'A' THEN 4.0 WHEN 'A-' THEN 3.7 " +
                "WHEN 'B+' THEN 3.3 WHEN 'B' THEN 3.0 WHEN 'B-' THEN 2.7 " +
                "WHEN 'C+' THEN 2.3 WHEN 'C' THEN 2.0 ELSE 0 END), 2) AS gpa " +
                "FROM students s " +
                "LEFT JOIN grades g ON s.student_id = g.student_id " +
                "GROUP BY s.student_id, s.first_name, s.last_name " +
                "HAVING total_courses > 0 " +
                "ORDER BY gpa DESC";
        
        return queryExecutor.executeQuery(query);
    }
    
    /**
     * Gets course enrollment statistics.
     * Shows enrollment counts and grade distributions by course.
     * 
     * @return TableModel with course statistics
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel getCourseStatistics() throws SQLException {
        String query = "SELECT course_name, COUNT(*) AS enrollment_count, " +
                "AVG(credits) AS avg_credits, " +
                "COUNT(CASE WHEN grade IN ('A', 'A-') THEN 1 END) AS excellent_count, " +
                "COUNT(CASE WHEN grade LIKE 'B%' THEN 1 END) AS good_count, " +
                "COUNT(CASE WHEN grade LIKE 'C%' THEN 1 END) AS average_count " +
                "FROM grades " +
                "GROUP BY course_name " +
                "ORDER BY enrollment_count DESC";
        
        return queryExecutor.executeQuery(query);
    }
    
    /**
     * Gets regional distribution of students.
     * Shows student counts and statistics by geographic region.
     * 
     * @return TableModel with regional distribution
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel getRegionalDistribution() throws SQLException {
        String query = "SELECT s.address AS region, " +
                "COUNT(s.student_id) AS student_count, " +
                "COUNT(DISTINCT a.program_id) AS programs_chosen, " +
                "AVG(a.entrance_score) AS avg_entrance_score " +
                "FROM students s " +
                "INNER JOIN admissions a ON s.student_id = a.student_id " +
                "GROUP BY s.address " +
                "ORDER BY student_count DESC";
        
        return queryExecutor.executeQuery(query);
    }
    
    /**
     * Gets admission statistics by entrance score range.
     * Categorizes students into score bands and shows outcomes.
     * 
     * @return TableModel with score range analysis
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel getScoreRangeAnalysis() throws SQLException {
        String query = "SELECT " +
                "CASE WHEN entrance_score >= 90 THEN '90-100 (Excellent)' " +
                "WHEN entrance_score >= 80 THEN '80-89 (Very Good)' " +
                "WHEN entrance_score >= 70 THEN '70-79 (Good)' " +
                "ELSE 'Below 70' END AS score_range, " +
                "COUNT(*) AS student_count, " +
                "AVG(entrance_score) AS avg_score, " +
                "COUNT(CASE WHEN status = 'Active' THEN 1 END) AS active_students, " +
                "COUNT(CASE WHEN status = 'Graduated' THEN 1 END) AS graduated_students " +
                "FROM admissions " +
                "GROUP BY score_range " +
                "ORDER BY avg_score DESC";
        
        return queryExecutor.executeQuery(query);
    }
    
    /**
     * Gets revenue analysis by program.
     * Calculates potential revenue from tuition fees.
     * 
     * @return TableModel with revenue statistics
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel getRevenueAnalysis() throws SQLException {
        String query = "SELECT p.program_name, p.department, p.tuition_fee, " +
                "COUNT(a.admission_id) AS enrolled_students, " +
                "(p.tuition_fee * COUNT(a.admission_id)) AS total_revenue, " +
                "AVG(a.entrance_score) AS avg_student_score " +
                "FROM programs p " +
                "LEFT JOIN admissions a ON p.program_id = a.program_id " +
                "WHERE a.status = 'Active' OR a.status IS NULL " +
                "GROUP BY p.program_id, p.program_name, p.department, p.tuition_fee " +
                "ORDER BY total_revenue DESC";
        
        return queryExecutor.executeQuery(query);
    }
    
    /**
     * Gets summary statistics for dashboard.
     * Returns key metrics as a map.
     * 
     * @return Map containing summary statistics
     * @throws SQLException if query execution fails
     */
    public Map<String, Object> getSummaryStatistics() throws SQLException {
        Map<String, Object> stats = new LinkedHashMap<>();
        
        // Total students
        Object totalStudents = queryExecutor.executeScalar(
                "SELECT COUNT(*) FROM students");
        stats.put("Total Students", totalStudents);
        
        // Total programs
        Object totalPrograms = queryExecutor.executeScalar(
                "SELECT COUNT(*) FROM programs");
        stats.put("Total Programs", totalPrograms);
        
        // Total admissions
        Object totalAdmissions = queryExecutor.executeScalar(
                "SELECT COUNT(*) FROM admissions");
        stats.put("Total Admissions", totalAdmissions);
        
        // Active students
        Object activeStudents = queryExecutor.executeScalar(
                "SELECT COUNT(*) FROM admissions WHERE status = 'Active'");
        stats.put("Active Students", activeStudents);
        
        // Average entrance score
        Object avgScore = queryExecutor.executeScalar(
                "SELECT ROUND(AVG(entrance_score), 2) FROM admissions");
        stats.put("Avg Entrance Score", avgScore);
        
        // Total courses
        Object totalCourses = queryExecutor.executeScalar(
                "SELECT COUNT(DISTINCT course_name) FROM grades");
        stats.put("Total Courses", totalCourses);
        
        return stats;
    }
}
