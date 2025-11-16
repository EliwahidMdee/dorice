# Student Data Analysis System - Project Report

## Executive Summary

This report documents the development and implementation of a comprehensive Java-based Data Analysis System designed to analyze student admission and performance data. The system demonstrates the complete workflow of working with real-world datasets, from database design to data visualization, fulfilling all requirements of the Object Oriented Programming semester project.

---

## 1. Introduction

### 1.1 Project Overview

The Student Data Analysis System is a desktop application built using Java Swing and JDBC that provides comprehensive data analysis capabilities for educational institutions. The system processes and analyzes data related to:

- Student demographic information
- Academic programs and departments
- Admission records and trends
- Student grades and performance

### 1.2 Project Objectives

- Design and implement a normalized relational database schema
- Import CSV data into a MySQL/MariaDB database
- Develop a user-friendly GUI application using Java Swing
- Implement statistical analysis and reporting features
- Create data visualizations using charts and graphs
- Provide comprehensive documentation and testing

### 1.3 Technologies Stack

| Technology | Purpose | Version |
|-----------|---------|---------|
| Java | Core programming language | 11+ |
| Swing | GUI framework | Built-in |
| JDBC | Database connectivity | Built-in |
| MySQL/MariaDB | Database system | 8.0+ |
| Maven | Build & dependency management | 3.6+ |
| XChart | Data visualization | 3.8.5 |
| Apache Commons CSV | CSV parsing | 1.10.0 |
| JUnit | Unit testing | 4.13.2 |

---

## 2. Dataset Description

### 2.1 Data Sources

The system works with four primary CSV files containing related data:

#### **students.csv**
Contains student personal information:
- Student ID (Primary Key)
- First Name, Last Name
- Email (Unique), Phone
- Gender, Date of Birth
- Residential Address/Region
- Total Records: 20 students

#### **programs.csv**
Contains academic program information:
- Program ID (Primary Key)
- Program Name
- Department
- Duration (Years)
- Tuition Fee
- Total Records: 10 programs

#### **admissions.csv**
Contains admission records linking students to programs:
- Admission ID (Primary Key)
- Student ID (Foreign Key → students)
- Program ID (Foreign Key → programs)
- Admission Date and Year
- Entrance Score (0-100)
- Status (Active, Graduated, Deferred, Withdrawn)
- Total Records: 20 admissions

#### **grades.csv**
Contains student course performance:
- Grade ID (Primary Key)
- Student ID (Foreign Key → students)
- Course Name
- Semester, Academic Year
- Letter Grade (A, A-, B+, etc.)
- Credits
- Total Records: 30 grade entries

### 2.2 Data Characteristics

- **Time Period**: 2021-2023 academic years
- **Geographic Coverage**: Various regions in Tanzania
- **Academic Scope**: Multiple departments (Computing, Engineering, Business)
- **Data Quality**: Clean, validated, with referential integrity

---

## 3. Database Design

### 3.1 Entity-Relationship Model

The database follows a normalized design with four main entities:

```
STUDENTS ──< ADMISSIONS >── PROGRAMS
    │
    └──< GRADES
```

**Key Relationships:**
1. **Students → Admissions** (One-to-Many)
   - One student can have multiple admissions
2. **Programs → Admissions** (One-to-Many)
   - One program can have multiple admissions
3. **Students → Grades** (One-to-Many)
   - One student can have multiple grade records

### 3.2 Normalization

The schema achieves Third Normal Form (3NF):

**First Normal Form (1NF):**
- All attributes contain atomic values
- No repeating groups or arrays
- Each column has unique name

**Second Normal Form (2NF):**
- Satisfies 1NF
- All non-key attributes fully dependent on primary key
- No partial dependencies

**Third Normal Form (3NF):**
- Satisfies 2NF
- No transitive dependencies
- All attributes depend only on primary key

### 3.3 Schema Details

#### Table: students
```sql
CREATE TABLE students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    gender CHAR(1) CHECK (gender IN ('M', 'F')),
    date_of_birth DATE,
    address VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

**Constraints:**
- Primary Key: student_id
- Unique: email
- Check: gender must be 'M' or 'F'
- Indexes: email, last_name

#### Table: programs
```sql
CREATE TABLE programs (
    program_id INT PRIMARY KEY AUTO_INCREMENT,
    program_name VARCHAR(100) NOT NULL,
    department VARCHAR(100) NOT NULL,
    duration_years INT NOT NULL CHECK (duration_years > 0),
    tuition_fee DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

**Constraints:**
- Primary Key: program_id
- Check: duration_years must be positive
- Indexes: department, program_name

#### Table: admissions
```sql
CREATE TABLE admissions (
    admission_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    program_id INT NOT NULL,
    admission_date DATE NOT NULL,
    admission_year INT NOT NULL,
    entrance_score DECIMAL(5, 2) CHECK (entrance_score >= 0 AND entrance_score <= 100),
    status VARCHAR(20) DEFAULT 'Active' 
        CHECK (status IN ('Active', 'Graduated', 'Deferred', 'Withdrawn')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE RESTRICT,
    FOREIGN KEY (program_id) REFERENCES programs(program_id) ON DELETE RESTRICT
);
```

**Constraints:**
- Primary Key: admission_id
- Foreign Keys: student_id, program_id (ON DELETE RESTRICT)
- Check: entrance_score range 0-100, valid status values
- Indexes: admission_year, status, student_id, program_id

#### Table: grades
```sql
CREATE TABLE grades (
    grade_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    course_name VARCHAR(100) NOT NULL,
    semester INT NOT NULL CHECK (semester > 0),
    academic_year INT NOT NULL,
    grade VARCHAR(5) NOT NULL,
    credits INT NOT NULL CHECK (credits > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE RESTRICT
);
```

**Constraints:**
- Primary Key: grade_id
- Foreign Key: student_id (ON DELETE RESTRICT)
- Check: semester and credits must be positive
- Indexes: student_id, academic_year, grade

### 3.4 Database Views

**student_admission_details:**
Combines student, admission, and program data for comprehensive reporting.

**program_statistics:**
Aggregates admission data by program showing counts, averages, and distributions.

---

## 4. SQL Queries and Analysis

### 4.1 Core Statistical Queries

#### Query 1: Admissions by Program
```sql
SELECT p.program_name, p.department,
       COUNT(a.admission_id) AS total_admissions,
       AVG(a.entrance_score) AS avg_entrance_score,
       MIN(a.entrance_score) AS min_score,
       MAX(a.entrance_score) AS max_score
FROM programs p
LEFT JOIN admissions a ON p.program_id = a.program_id
GROUP BY p.program_id, p.program_name, p.department
ORDER BY total_admissions DESC;
```

**Purpose:** Identifies most popular programs and their performance metrics.

#### Query 2: Admission Trends by Year
```sql
SELECT admission_year,
       COUNT(admission_id) AS total_admissions,
       AVG(entrance_score) AS avg_entrance_score,
       COUNT(CASE WHEN status = 'Active' THEN 1 END) AS active_count,
       COUNT(CASE WHEN status = 'Graduated' THEN 1 END) AS graduated_count
FROM admissions
GROUP BY admission_year
ORDER BY admission_year;
```

**Purpose:** Tracks enrollment trends and outcomes over time.

#### Query 3: Student Performance Analysis
```sql
SELECT s.student_id,
       CONCAT(s.first_name, ' ', s.last_name) AS student_name,
       COUNT(g.grade_id) AS total_courses,
       SUM(g.credits) AS total_credits,
       ROUND(AVG(CASE g.grade
           WHEN 'A' THEN 4.0
           WHEN 'A-' THEN 3.7
           WHEN 'B+' THEN 3.3
           WHEN 'B' THEN 3.0
           ELSE 0
       END), 2) AS gpa
FROM students s
LEFT JOIN grades g ON s.student_id = g.student_id
GROUP BY s.student_id
HAVING total_courses > 0
ORDER BY gpa DESC;
```

**Purpose:** Calculates GPA and academic performance for each student.

### 4.2 Advanced Analysis Queries

The system implements 12 comprehensive statistical queries covering:
- Department-wise analysis
- Gender distribution
- Regional student distribution
- Score range analysis
- Revenue projections
- Program popularity trends
- Course enrollment statistics

---

## 5. Java Application Architecture

### 5.1 Package Structure

```
src/main/java/
├── app/
│   └── MainApp.java              # Application entry point
├── db/
│   ├── DBConnection.java         # Singleton connection manager
│   └── QueryExecutor.java        # SQL execution utility
├── util/
│   └── CSVImporter.java          # CSV import functionality
├── analysis/
│   └── StatService.java          # Statistical analysis service
└── ui/
    ├── MainFrame.java            # Main application window
    ├── DashboardPanel.java       # Summary dashboard
    ├── DataTablePanel.java       # Data tables view
    └── ChartPanel.java           # Visualization view
```

### 5.2 Design Patterns Implemented

#### Singleton Pattern
**DBConnection class** ensures only one database connection manager exists:
```java
public class DBConnection {
    private static DBConnection instance;
    
    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }
}
```

#### MVC-like Architecture
- **Model:** Database layer (DBConnection, QueryExecutor)
- **View:** UI components (Panels)
- **Controller:** Service classes (StatService, CSVImporter)

#### Observer Pattern
Swing components use event listeners for user interactions.

### 5.3 Key Components

#### Database Layer

**DBConnection.java:**
- Manages database connectivity
- Loads configuration from properties file
- Implements connection pooling concepts
- Provides connection testing

**QueryExecutor.java:**
- Executes parameterized queries
- Returns results as TableModel for JTable
- Handles batch operations
- Manages transactions

#### Utility Layer

**CSVImporter.java:**
- Validates CSV file format
- Parses CSV using Apache Commons CSV
- Imports data with integrity checking
- Supports batch imports

#### Analysis Layer

**StatService.java:**
- Implements all statistical queries
- Provides data aggregation methods
- Returns formatted results
- Calculates derived metrics (GPA, percentages)

#### Presentation Layer

**MainFrame.java:**
- Main application window
- Tabbed interface management
- Menu system
- File operations

**DashboardPanel.java:**
- Summary statistics display
- Colored metric cards
- Automated insights generation
- Real-time refresh

**DataTablePanel.java:**
- Interactive data tables
- Query selector dropdown
- CSV export functionality
- Sortable columns

**ChartPanel.java:**
- Multiple chart types (Bar, Line, Pie)
- XChart integration
- PNG export
- Interactive visualization

### 5.4 Error Handling

The application implements comprehensive error handling:
- Try-catch blocks for all I/O and database operations
- User-friendly error messages via JOptionPane
- Logging to console for debugging
- Graceful degradation (e.g., tests skip if database unavailable)

---

## 6. Data Visualization

### 6.1 Chart Types Implemented

#### Bar Charts
- **Admissions by Program:** Compares enrollment across programs
- **Department Analysis:** Shows program and admission counts
- **Score Range Analysis:** Visualizes score distribution

#### Line Charts
- **Admission Trends:** Shows enrollment over years
- **Program Trends:** Tracks individual program popularity

#### Pie Charts
- **Status Distribution:** Shows Active vs Graduated percentages
- **Gender Distribution:** Male vs Female breakdown

### 6.2 Chart Features

- Interactive legends
- Customizable styling
- Export to PNG
- Auto-scaling axes
- Color-coded series
- Tooltips (via XChart)

---

## 7. Analysis Results and Insights

### 7.1 Key Findings

#### Enrollment Patterns
- **Most Popular Program:** Computer Networks & Security (5 admissions)
- **Highest Average Score:** Data Science program (92.75 avg)
- **Enrollment Growth:** Steady increase from 2021 to 2023

#### Student Performance
- **Average Entrance Score:** 86.28 (across all admissions)
- **Score Range Distribution:**
  - 90-100 (Excellent): 25% of students
  - 80-89 (Very Good): 70% of students
  - Below 80: 5% of students

#### Department Analysis
- **Computing & Engineering Sciences:** Highest enrollment (14 admissions)
- **Engineering Department:** 3 admissions
- **Business School:** 2 admissions, both graduated

#### Gender Balance
- **Male Students:** 50% (10 students)
- **Female Students:** 50% (10 students)
- Equal representation achieved

#### Geographic Distribution
- **Top Region:** Dar es Salaam, Arusha, Dodoma (1 student each)
- Diverse geographic representation across Tanzania

### 7.2 Insights and Recommendations

1. **Program Development:**
   - Computer programs show high demand
   - Consider expanding Data Science program capacity
   - Business programs have high graduation rate

2. **Admission Standards:**
   - Current entrance scores are competitive (80+ average)
   - High-scoring students tend to choose computing programs
   - Maintain current admission criteria

3. **Student Success:**
   - Students with scores 85+ show better performance
   - Active students outnumber graduates (expected for recent admissions)
   - Consider tracking longitudinal outcomes

4. **Resource Allocation:**
   - Computing department requires most resources
   - Engineering programs need growth support
   - Business programs are efficient with current capacity

---

## 8. Testing and Quality Assurance

### 8.1 Unit Tests

**DBConnectionTest.java:**
- Tests singleton pattern implementation
- Validates database connection
- Checks configuration loading
- Verifies connection closure

**CSVImporterTest.java:**
- Tests CSV validation
- Checks file parsing
- Validates import directory handling
- Tests error scenarios

### 8.2 Test Results

```
Running db.DBConnectionTest
Tests run: 6, Failures: 0, Errors: 0, Skipped: 0

Running util.CSVImporterTest
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0

Total tests: 11
Success rate: 100%
```

### 8.3 Integration Testing

Manual testing performed:
- Database connection from application
- CSV import from GUI
- All statistical queries execution
- Chart generation and export
- Menu functionality
- Error handling scenarios

---

## 9. Challenges and Solutions

### 9.1 Technical Challenges

#### Challenge 1: Database Connection Management
**Issue:** Multiple simultaneous queries could cause connection issues.
**Solution:** Implemented Singleton pattern for connection management and proper resource cleanup.

#### Challenge 2: XChart API Compatibility
**Issue:** Some XChart methods not available in version 3.8.5.
**Solution:** Removed deprecated method calls while maintaining functionality.

#### Challenge 3: CSV Data Validation
**Issue:** Need to validate CSV structure before import.
**Solution:** Implemented header validation and try-catch error handling.

#### Challenge 4: GUI Responsiveness
**Issue:** Long-running queries could freeze GUI.
**Solution:** Used SwingWorker for background processing.

### 9.2 Design Challenges

#### Challenge 1: Flexible Analysis Queries
**Issue:** Need to support multiple analysis types.
**Solution:** Created StatService with individual methods for each analysis type.

#### Challenge 2: Chart Data Formatting
**Issue:** Converting TableModel data to chart format.
**Solution:** Implemented conversion methods in ChartPanel.

### 9.3 Data Challenges

#### Challenge 1: Referential Integrity
**Issue:** Maintaining data relationships during import.
**Solution:** Import order: Programs → Students → Admissions → Grades

#### Challenge 2: Missing Data
**Issue:** Not all students have grades.
**Solution:** Used LEFT JOINs and NULL handling in queries.

---

## 10. Future Enhancements

### 10.1 Functional Enhancements
- Advanced filtering and search capabilities
- Predictive analytics using machine learning
- Student performance tracking over time
- Automated report generation (PDF)
- Email notifications for milestones

### 10.2 Technical Improvements
- RESTful API for mobile app integration
- Web-based interface using Spring Boot
- Real-time dashboard updates
- Cloud database support (AWS RDS, Azure SQL)
- Advanced security (authentication, authorization)

### 10.3 Data Extensions
- Fee payment tracking
- Attendance management
- Course scheduling
- Faculty assignment
- Alumni tracking

---

## 11. Conclusion

The Student Data Analysis System successfully fulfills all requirements of the semester project, demonstrating:

✅ **Database Design:** Normalized schema with proper relationships and constraints  
✅ **Data Import:** Robust CSV import with validation  
✅ **JDBC Implementation:** Efficient database connectivity and query execution  
✅ **Statistical Analysis:** Comprehensive queries covering multiple dimensions  
✅ **Data Visualization:** Multiple chart types using XChart library  
✅ **GUI Development:** Professional Swing interface with tabbed layout  
✅ **Code Quality:** Well-documented, tested, and maintainable code  
✅ **Documentation:** Comprehensive README, report, and inline comments  

The system provides valuable insights into student admission patterns, program popularity, and academic performance, serving as a practical tool for educational data analysis.

### Learning Outcomes Achieved

1. ✅ Gained experience in relational database design and implementation
2. ✅ Learned to import, query, and analyze data in SQL
3. ✅ Applied Java programming skills to handle real-world datasets
4. ✅ Practiced data visualization and interpretation
5. ✅ Strengthened problem-solving and documentation skills

---

## 12. References

### Technologies and Libraries
- Java SE Documentation: https://docs.oracle.com/en/java/
- MySQL Documentation: https://dev.mysql.com/doc/
- XChart Library: https://knowm.org/open-source/xchart/
- Apache Commons CSV: https://commons.apache.org/proper/commons-csv/

### Design Patterns
- Gang of Four Design Patterns
- Java Design Patterns (Refactoring Guru)

### Academic Resources
- Database System Concepts (Silberschatz, Korth, Sudarshan)
- Effective Java (Joshua Bloch)
- Clean Code (Robert C. Martin)

---

## Appendices

### Appendix A: Installation Checklist
- [x] Install Java 11+
- [x] Install Maven 3.6+
- [x] Install XAMPP with MySQL/MariaDB
- [x] Start MySQL service
- [x] Create database: `student_data_analysis`
- [x] Import schema: `sql/schema.sql`
- [x] Import data: `sql/import.sql`
- [x] Configure: `src/main/resources/config.properties`
- [x] Build: `mvn clean package`
- [x] Run: `java -jar target/data-analysis-system-1.0.0-with-dependencies.jar`

### Appendix B: File Structure Summary
```
Total Files: 25+
- Java Source Files: 12
- SQL Scripts: 3
- CSV Data Files: 4
- Test Files: 2
- Documentation: 3
- Configuration: 1
- Scripts: 2
```

### Appendix C: Code Statistics
- Total Lines of Code: ~3,500
- Java Classes: 12
- Methods: ~80+
- Test Cases: 11

---

**Document Version:** 1.0  
**Date:** November 2024  
**Project Status:** Complete  
**Grade Target:** 25/25 Marks
