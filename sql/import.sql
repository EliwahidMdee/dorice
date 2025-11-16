-- ================================================================
-- Data Import Scripts for Student Data Analysis System
-- Note: This script assumes CSV files are loaded via Java application
-- For manual import, use LOAD DATA INFILE or the commands below
-- ================================================================

USE student_data_analysis;

-- ================================================================
-- Clear existing data (use with caution!)
-- ================================================================
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE grades;
TRUNCATE TABLE admissions;
TRUNCATE TABLE students;
TRUNCATE TABLE programs;
SET FOREIGN_KEY_CHECKS = 1;

-- ================================================================
-- Import Programs Data
-- ================================================================
-- Method 1: Using LOAD DATA INFILE (requires FILE privilege)
-- LOAD DATA INFILE '/path/to/programs.csv'
-- INTO TABLE programs
-- FIELDS TERMINATED BY ','
-- ENCLOSED BY '"'
-- LINES TERMINATED BY '\n'
-- IGNORE 1 ROWS
-- (program_id, program_name, department, duration_years, tuition_fee);

-- Method 2: Manual INSERT statements (for reference)
INSERT INTO programs (program_id, program_name, department, duration_years, tuition_fee) VALUES
(1, 'Computer Networks & Security', 'Computing & Engineering Sciences', 4, 1500000.00),
(2, 'Software Engineering', 'Computing & Engineering Sciences', 4, 1500000.00),
(3, 'Information Technology', 'Computing & Engineering Sciences', 4, 1400000.00),
(4, 'Data Science', 'Computing & Engineering Sciences', 4, 1600000.00),
(5, 'Computer Science', 'Computing & Engineering Sciences', 4, 1500000.00),
(6, 'Electrical Engineering', 'Engineering', 4, 1550000.00),
(7, 'Civil Engineering', 'Engineering', 4, 1450000.00),
(8, 'Mechanical Engineering', 'Engineering', 4, 1500000.00),
(9, 'Business Administration', 'Business School', 3, 1200000.00),
(10, 'Accounting & Finance', 'Business School', 4, 1300000.00);

-- ================================================================
-- Import Students Data
-- ================================================================
INSERT INTO students (student_id, first_name, last_name, email, phone, gender, date_of_birth, address) VALUES
(1, 'John', 'Doe', 'john.doe@email.com', '+255712345001', 'M', '2000-03-15', 'Dar es Salaam'),
(2, 'Jane', 'Smith', 'jane.smith@email.com', '+255712345002', 'F', '1999-07-22', 'Arusha'),
(3, 'Ahmed', 'Hassan', 'ahmed.hassan@email.com', '+255712345003', 'M', '2001-01-10', 'Dodoma'),
(4, 'Fatuma', 'Ali', 'fatuma.ali@email.com', '+255712345004', 'F', '2000-11-30', 'Mwanza'),
(5, 'David', 'Johnson', 'david.j@email.com', '+255712345005', 'M', '1999-05-18', 'Mbeya'),
(6, 'Sarah', 'Williams', 'sarah.w@email.com', '+255712345006', 'F', '2001-09-25', 'Tanga'),
(7, 'Mohamed', 'Ibrahim', 'mohamed.i@email.com', '+255712345007', 'M', '2000-02-14', 'Zanzibar'),
(8, 'Grace', 'Mwangi', 'grace.m@email.com', '+255712345008', 'F', '1999-12-08', 'Morogoro'),
(9, 'Peter', 'Kamau', 'peter.k@email.com', '+255712345009', 'M', '2001-04-20', 'Kilimanjaro'),
(10, 'Amina', 'Said', 'amina.s@email.com', '+255712345010', 'F', '2000-08-17', 'Pwani'),
(11, 'James', 'Brown', 'james.b@email.com', '+255712345011', 'M', '1999-06-12', 'Singida'),
(12, 'Mary', 'Kimani', 'mary.k@email.com', '+255712345012', 'F', '2001-10-05', 'Iringa'),
(13, 'Hassan', 'Omar', 'hassan.o@email.com', '+255712345013', 'M', '2000-01-28', 'Kigoma'),
(14, 'Elizabeth', 'Njeri', 'elizabeth.n@email.com', '+255712345014', 'F', '1999-11-19', 'Rukwa'),
(15, 'Ibrahim', 'Juma', 'ibrahim.j@email.com', '+255712345015', 'M', '2001-03-22', 'Mara'),
(16, 'Lucy', 'Wambui', 'lucy.w@email.com', '+255712345016', 'F', '2000-07-09', 'Kagera'),
(17, 'Rashid', 'Salim', 'rashid.s@email.com', '+255712345017', 'M', '1999-09-14', 'Lindi'),
(18, 'Catherine', 'Nyambura', 'catherine.n@email.com', '+255712345018', 'F', '2001-05-30', 'Mtwara'),
(19, 'Ali', 'Bakari', 'ali.b@email.com', '+255712345019', 'M', '2000-12-03', 'Ruvuma'),
(20, 'Rose', 'Muthoni', 'rose.m@email.com', '+255712345020', 'F', '1999-08-26', 'Shinyanga');

-- ================================================================
-- Import Admissions Data
-- ================================================================
INSERT INTO admissions (admission_id, student_id, program_id, admission_date, admission_year, entrance_score, status) VALUES
(1, 1, 1, '2021-09-01', 2021, 85.5, 'Active'),
(2, 2, 2, '2021-09-01', 2021, 88.0, 'Active'),
(3, 3, 1, '2021-09-01', 2021, 82.3, 'Active'),
(4, 4, 3, '2021-09-01', 2021, 90.5, 'Active'),
(5, 5, 4, '2022-09-01', 2022, 92.0, 'Active'),
(6, 6, 2, '2022-09-01', 2022, 86.7, 'Active'),
(7, 7, 1, '2022-09-01', 2022, 84.2, 'Active'),
(8, 8, 5, '2022-09-01', 2022, 89.5, 'Active'),
(9, 9, 6, '2023-09-01', 2023, 87.8, 'Active'),
(10, 10, 3, '2023-09-01', 2023, 91.2, 'Active'),
(11, 11, 1, '2023-09-01', 2023, 83.9, 'Active'),
(12, 12, 7, '2023-09-01', 2023, 88.4, 'Active'),
(13, 13, 8, '2023-09-01', 2023, 85.1, 'Active'),
(14, 14, 9, '2021-09-01', 2021, 78.5, 'Graduated'),
(15, 15, 10, '2021-09-01', 2021, 80.2, 'Graduated'),
(16, 16, 2, '2022-09-01', 2022, 87.3, 'Active'),
(17, 17, 4, '2022-09-01', 2022, 93.5, 'Active'),
(18, 18, 5, '2023-09-01', 2023, 89.0, 'Active'),
(19, 19, 1, '2023-09-01', 2023, 86.5, 'Active'),
(20, 20, 3, '2023-09-01', 2023, 88.9, 'Active');

-- ================================================================
-- Import Grades Data
-- ================================================================
INSERT INTO grades (grade_id, student_id, course_name, semester, academic_year, grade, credits) VALUES
(1, 1, 'Object Oriented Programming', 1, 2021, 'A', 4),
(2, 1, 'Database Systems', 1, 2021, 'B+', 4),
(3, 1, 'Data Structures', 2, 2022, 'A', 4),
(4, 2, 'Software Engineering', 1, 2021, 'A', 4),
(5, 2, 'Web Development', 1, 2021, 'A-', 3),
(6, 2, 'Mobile Computing', 2, 2022, 'B+', 4),
(7, 3, 'Network Security', 1, 2021, 'B+', 4),
(8, 3, 'Operating Systems', 1, 2021, 'A-', 4),
(9, 3, 'Computer Networks', 2, 2022, 'A', 4),
(10, 4, 'Information Systems', 1, 2021, 'A', 4),
(11, 4, 'Systems Analysis', 1, 2021, 'A', 3),
(12, 4, 'Project Management', 2, 2022, 'A-', 3),
(13, 5, 'Machine Learning', 1, 2022, 'A', 4),
(14, 5, 'Data Mining', 1, 2022, 'A', 4),
(15, 5, 'Big Data Analytics', 2, 2023, 'A', 4),
(16, 6, 'Software Testing', 1, 2022, 'B+', 3),
(17, 6, 'Agile Methods', 1, 2022, 'A-', 3),
(18, 6, 'DevOps', 2, 2023, 'A', 3),
(19, 7, 'Cryptography', 1, 2022, 'A-', 4),
(20, 7, 'Ethical Hacking', 1, 2022, 'B+', 4),
(21, 7, 'Firewall Systems', 2, 2023, 'A', 3),
(22, 8, 'Algorithms', 1, 2022, 'A', 4),
(23, 8, 'Compiler Design', 1, 2022, 'A-', 4),
(24, 8, 'Artificial Intelligence', 2, 2023, 'A', 4),
(25, 9, 'Circuit Analysis', 1, 2023, 'B+', 4),
(26, 9, 'Digital Electronics', 1, 2023, 'A-', 4),
(27, 10, 'Database Management', 1, 2023, 'A', 4),
(28, 10, 'Cloud Computing', 1, 2023, 'A', 3),
(29, 11, 'Cybersecurity', 1, 2023, 'B+', 4),
(30, 11, 'Penetration Testing', 1, 2023, 'A-', 3);

-- ================================================================
-- Verification Queries
-- ================================================================
-- Verify data import
SELECT 'Students' AS TableName, COUNT(*) AS RecordCount FROM students
UNION ALL
SELECT 'Programs', COUNT(*) FROM programs
UNION ALL
SELECT 'Admissions', COUNT(*) FROM admissions
UNION ALL
SELECT 'Grades', COUNT(*) FROM grades;
