-- ================================================================
-- Statistical Analysis Queries for Student Data Analysis System
-- These queries support the analysis requirements from the project PDF
-- ================================================================

USE student_data_analysis;

-- ================================================================
-- Query 1: Admissions by Program
-- Purpose: Count total admissions per program
-- ================================================================
SELECT 
    p.program_name,
    p.department,
    COUNT(a.admission_id) AS total_admissions,
    AVG(a.entrance_score) AS avg_entrance_score,
    MIN(a.entrance_score) AS min_entrance_score,
    MAX(a.entrance_score) AS max_entrance_score
FROM programs p
LEFT JOIN admissions a ON p.program_id = a.program_id
GROUP BY p.program_id, p.program_name, p.department
ORDER BY total_admissions DESC;

-- ================================================================
-- Query 2: Admissions by Year
-- Purpose: Track admission trends over years
-- ================================================================
SELECT 
    admission_year,
    COUNT(admission_id) AS total_admissions,
    AVG(entrance_score) AS avg_entrance_score,
    COUNT(CASE WHEN status = 'Active' THEN 1 END) AS active_count,
    COUNT(CASE WHEN status = 'Graduated' THEN 1 END) AS graduated_count
FROM admissions
GROUP BY admission_year
ORDER BY admission_year;

-- ================================================================
-- Query 3: Student Status Distribution
-- Purpose: Distribution of students by current status
-- ================================================================
SELECT 
    status,
    COUNT(*) AS count,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM admissions), 2) AS percentage
FROM admissions
GROUP BY status
ORDER BY count DESC;

-- ================================================================
-- Query 4: Department-wise Analysis
-- Purpose: Analyze admissions by department
-- ================================================================
SELECT 
    p.department,
    COUNT(DISTINCT p.program_id) AS total_programs,
    COUNT(a.admission_id) AS total_admissions,
    AVG(a.entrance_score) AS avg_entrance_score,
    AVG(p.tuition_fee) AS avg_tuition_fee
FROM programs p
LEFT JOIN admissions a ON p.program_id = a.program_id
GROUP BY p.department
ORDER BY total_admissions DESC;

-- ================================================================
-- Query 5: Gender Distribution in Admissions
-- Purpose: Analyze gender balance in admissions
-- ================================================================
SELECT 
    s.gender,
    COUNT(a.admission_id) AS total_admissions,
    AVG(a.entrance_score) AS avg_entrance_score,
    COUNT(CASE WHEN a.status = 'Graduated' THEN 1 END) AS graduated_count
FROM students s
INNER JOIN admissions a ON s.student_id = a.student_id
GROUP BY s.gender;

-- ================================================================
-- Query 6: Top Performing Students
-- Purpose: Identify students with highest entrance scores
-- ================================================================
SELECT 
    CONCAT(s.first_name, ' ', s.last_name) AS student_name,
    s.email,
    p.program_name,
    a.admission_year,
    a.entrance_score,
    a.status
FROM students s
INNER JOIN admissions a ON s.student_id = a.student_id
INNER JOIN programs p ON a.program_id = p.program_id
ORDER BY a.entrance_score DESC
LIMIT 10;

-- ================================================================
-- Query 7: Program Popularity Trend
-- Purpose: Track which programs are gaining or losing popularity
-- ================================================================
SELECT 
    p.program_name,
    a.admission_year,
    COUNT(a.admission_id) AS admissions_count
FROM programs p
INNER JOIN admissions a ON p.program_id = a.program_id
GROUP BY p.program_id, p.program_name, a.admission_year
ORDER BY p.program_name, a.admission_year;

-- ================================================================
-- Query 8: Student Performance Analysis
-- Purpose: Calculate GPA and academic performance metrics
-- ================================================================
SELECT 
    s.student_id,
    CONCAT(s.first_name, ' ', s.last_name) AS student_name,
    COUNT(g.grade_id) AS total_courses,
    SUM(g.credits) AS total_credits,
    -- Grade point calculation (simplified)
    CASE 
        WHEN AVG(CASE g.grade
            WHEN 'A' THEN 4.0
            WHEN 'A-' THEN 3.7
            WHEN 'B+' THEN 3.3
            WHEN 'B' THEN 3.0
            WHEN 'B-' THEN 2.7
            WHEN 'C+' THEN 2.3
            WHEN 'C' THEN 2.0
            ELSE 0
        END) IS NOT NULL THEN ROUND(AVG(CASE g.grade
            WHEN 'A' THEN 4.0
            WHEN 'A-' THEN 3.7
            WHEN 'B+' THEN 3.3
            WHEN 'B' THEN 3.0
            WHEN 'B-' THEN 2.7
            WHEN 'C+' THEN 2.3
            WHEN 'C' THEN 2.0
            ELSE 0
        END), 2)
        ELSE 0
    END AS gpa
FROM students s
LEFT JOIN grades g ON s.student_id = g.student_id
GROUP BY s.student_id, s.first_name, s.last_name
HAVING total_courses > 0
ORDER BY gpa DESC;

-- ================================================================
-- Query 9: Course Enrollment Statistics
-- Purpose: Analyze popular courses and grade distribution
-- ================================================================
SELECT 
    course_name,
    COUNT(*) AS enrollment_count,
    AVG(credits) AS avg_credits,
    COUNT(CASE WHEN grade IN ('A', 'A-') THEN 1 END) AS excellent_count,
    COUNT(CASE WHEN grade LIKE 'B%' THEN 1 END) AS good_count,
    COUNT(CASE WHEN grade LIKE 'C%' THEN 1 END) AS average_count
FROM grades
GROUP BY course_name
ORDER BY enrollment_count DESC;

-- ================================================================
-- Query 10: Regional Distribution of Students
-- Purpose: Analyze student geographic distribution
-- ================================================================
SELECT 
    s.address AS region,
    COUNT(s.student_id) AS student_count,
    COUNT(DISTINCT a.program_id) AS programs_chosen,
    AVG(a.entrance_score) AS avg_entrance_score
FROM students s
INNER JOIN admissions a ON s.student_id = a.student_id
GROUP BY s.address
ORDER BY student_count DESC;

-- ================================================================
-- Query 11: Admission Success Rate by Score Range
-- Purpose: Analyze admission patterns based on entrance scores
-- ================================================================
SELECT 
    CASE 
        WHEN entrance_score >= 90 THEN '90-100 (Excellent)'
        WHEN entrance_score >= 80 THEN '80-89 (Very Good)'
        WHEN entrance_score >= 70 THEN '70-79 (Good)'
        ELSE 'Below 70'
    END AS score_range,
    COUNT(*) AS student_count,
    AVG(entrance_score) AS avg_score,
    COUNT(CASE WHEN status = 'Active' THEN 1 END) AS active_students,
    COUNT(CASE WHEN status = 'Graduated' THEN 1 END) AS graduated_students
FROM admissions
GROUP BY score_range
ORDER BY avg_score DESC;

-- ================================================================
-- Query 12: Revenue Analysis by Program
-- Purpose: Calculate potential revenue from tuition fees
-- ================================================================
SELECT 
    p.program_name,
    p.department,
    p.tuition_fee,
    COUNT(a.admission_id) AS enrolled_students,
    (p.tuition_fee * COUNT(a.admission_id)) AS total_revenue,
    AVG(a.entrance_score) AS avg_student_score
FROM programs p
LEFT JOIN admissions a ON p.program_id = a.program_id
WHERE a.status = 'Active'
GROUP BY p.program_id, p.program_name, p.department, p.tuition_fee
ORDER BY total_revenue DESC;
