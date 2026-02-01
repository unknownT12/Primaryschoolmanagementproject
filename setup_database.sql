-- Primary School Admin Database Setup Script
-- This script creates the database and all required tables

-- Create database
CREATE DATABASE IF NOT EXISTS schooldb;
USE schooldb;

-- Table: users
-- Stores user accounts for login (admin, teacher, parent)
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL, -- 'admin', 'teacher', 'parent'
    full_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: students
-- Stores student information
CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    birth_certificate_no VARCHAR(50) UNIQUE,
    gender VARCHAR(10),
    dob DATE,
    address TEXT,
    guardian_name VARCHAR(100),
    guardian_contact VARCHAR(20),
    guardian_email VARCHAR(100),
    registration_date DATE,
    status VARCHAR(20) DEFAULT 'Active', -- 'Active', 'Inactive', 'Transferred', 'Graduated'
    class VARCHAR(50), -- e.g., 'Standard 3'
    remarks TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: teachers
-- Stores teacher information
CREATE TABLE IF NOT EXISTS teachers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    omang_or_passport VARCHAR(50) UNIQUE,
    gender VARCHAR(10),
    dob DATE,
    address TEXT,
    contact_no VARCHAR(20),
    email VARCHAR(100),
    qualifications TEXT,
    subjects_qualified TEXT, -- comma-separated list
    date_joined DATE,
    class_to_subject TEXT, -- JSON-like string mapping classes to subjects
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: subjects
-- Stores subject information
CREATE TABLE IF NOT EXISTS subjects (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: grades
-- Stores student grades/marks
CREATE TABLE IF NOT EXISTS grades (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    subject_id INT NOT NULL,
    teacher_id INT,
    mark DECIMAL(5,2) NOT NULL,
    term VARCHAR(20), -- e.g., 'Term 1', 'Term 2', 'Term 3'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE SET NULL,
    INDEX idx_student (student_id),
    INDEX idx_subject (subject_id),
    INDEX idx_teacher (teacher_id)
);

-- Table: classes
-- Stores class information (created automatically by ClassDAO if needed)
CREATE TABLE IF NOT EXISTS classes (
    name VARCHAR(100) PRIMARY KEY
);

-- Insert default admin user (password: admin123 - you should change this!)
-- Password is hashed using BCrypt (you'll need to hash it properly in your app)
-- For now, this is a placeholder - you should create the admin user through the registration page
-- or use PasswordUtil to hash the password properly

-- Note: The password hash below is a placeholder. 
-- You should use the PasswordUtil class to generate a proper hash
-- Example: INSERT INTO users (username, password, role, full_name) 
--          VALUES ('admin', '$2a$10$...', 'admin', 'System Administrator');

-- Create indexes for better performance
CREATE INDEX idx_student_class ON students(class);
CREATE INDEX idx_student_status ON students(status);
CREATE INDEX idx_student_name ON students(name, surname);
CREATE INDEX idx_teacher_name ON teachers(name, surname);
CREATE INDEX idx_subject_name ON subjects(name);

-- Display success message
SELECT 'Database setup completed successfully!' AS message;

