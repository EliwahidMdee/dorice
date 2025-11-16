-- ================================================================
-- Database Schema for Student Data Analysis System
-- Database: student_data_analysis
-- Compatible with: MySQL/MariaDB (XAMPP)
-- ================================================================

-- Drop database if exists (use with caution!)
-- DROP DATABASE IF EXISTS student_data_analysis;

-- Create database
CREATE DATABASE IF NOT EXISTS student_data_analysis
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE student_data_analysis;

-- ================================================================
-- Table: students
-- Description: Stores student personal information
-- ================================================================
CREATE TABLE IF NOT EXISTS students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    gender CHAR(1) CHECK (gender IN ('M', 'F')),
    date_of_birth DATE,
    address VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_last_name (last_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================================
-- Table: programs
-- Description: Stores academic program information
-- ================================================================
CREATE TABLE IF NOT EXISTS programs (
    program_id INT PRIMARY KEY AUTO_INCREMENT,
    program_name VARCHAR(100) NOT NULL,
    department VARCHAR(100) NOT NULL,
    duration_years INT NOT NULL CHECK (duration_years > 0),
    tuition_fee DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_department (department),
    INDEX idx_program_name (program_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================================
-- Table: admissions
-- Description: Stores student admission records
-- ================================================================
CREATE TABLE IF NOT EXISTS admissions (
    admission_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    program_id INT NOT NULL,
    admission_date DATE NOT NULL,
    admission_year INT NOT NULL,
    entrance_score DECIMAL(5, 2) CHECK (entrance_score >= 0 AND entrance_score <= 100),
    status VARCHAR(20) DEFAULT 'Active' CHECK (status IN ('Active', 'Graduated', 'Deferred', 'Withdrawn')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (program_id) REFERENCES programs(program_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_admission_year (admission_year),
    INDEX idx_status (status),
    INDEX idx_student_id (student_id),
    INDEX idx_program_id (program_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================================
-- Table: grades
-- Description: Stores student course grades
-- ================================================================
CREATE TABLE IF NOT EXISTS grades (
    grade_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    course_name VARCHAR(100) NOT NULL,
    semester INT NOT NULL CHECK (semester > 0),
    academic_year INT NOT NULL,
    grade VARCHAR(5) NOT NULL,
    credits INT NOT NULL CHECK (credits > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_student_id (student_id),
    INDEX idx_academic_year (academic_year),
    INDEX idx_grade (grade)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================================
-- Create views for common queries
-- ================================================================

-- View: student_admission_details
CREATE OR REPLACE VIEW student_admission_details AS
SELECT 
    s.student_id,
    CONCAT(s.first_name, ' ', s.last_name) AS full_name,
    s.email,
    s.gender,
    p.program_name,
    p.department,
    a.admission_year,
    a.entrance_score,
    a.status
FROM students s
INNER JOIN admissions a ON s.student_id = a.student_id
INNER JOIN programs p ON a.program_id = p.program_id;

-- View: program_statistics
CREATE OR REPLACE VIEW program_statistics AS
SELECT 
    p.program_id,
    p.program_name,
    p.department,
    COUNT(a.admission_id) AS total_admissions,
    AVG(a.entrance_score) AS avg_entrance_score,
    COUNT(CASE WHEN a.status = 'Active' THEN 1 END) AS active_students,
    COUNT(CASE WHEN a.status = 'Graduated' THEN 1 END) AS graduated_students
FROM programs p
LEFT JOIN admissions a ON p.program_id = a.program_id
GROUP BY p.program_id, p.program_name, p.department;

-- ================================================================
-- Insert sample verification data
-- ================================================================
-- This ensures the schema is working correctly
-- DELETE FROM grades;
-- DELETE FROM admissions;
-- DELETE FROM students;
-- DELETE FROM programs;
