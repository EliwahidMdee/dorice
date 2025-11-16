-- ================================================================
-- Database Schema for Bank Data Analysis System
-- Database: bank_data_analysis
-- Compatible with: MySQL/MariaDB (XAMPP)
-- ================================================================

-- Drop database if exists (use with caution!)
-- DROP DATABASE IF EXISTS bank_data_analysis;

-- Create database
CREATE DATABASE IF NOT EXISTS bank_data_analysis
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE bank_data_analysis;

-- ================================================================
-- Table: accounts
-- Description: Stores bank account information
-- ================================================================
CREATE TABLE IF NOT EXISTS accounts (
    account_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    account_type VARCHAR(50) NOT NULL CHECK (account_type IN ('Savings', 'Checking', 'Business', 'Fixed Deposit')),
    balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    date_opened DATE NOT NULL,
    branch VARCHAR(100) NOT NULL,
    status VARCHAR(20) DEFAULT 'Active' CHECK (status IN ('Active', 'Closed', 'Frozen', 'Dormant')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_account_type (account_type),
    INDEX idx_branch (branch),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================================
-- Table: transactions
-- Description: Stores transaction records
-- ================================================================
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    account_id INT NOT NULL,
    transaction_type VARCHAR(50) NOT NULL CHECK (transaction_type IN ('Deposit', 'Withdrawal', 'Transfer', 'Payment', 'Interest')),
    amount DECIMAL(15, 2) NOT NULL,
    transaction_date DATE NOT NULL,
    description VARCHAR(255),
    status VARCHAR(20) DEFAULT 'Pending' CHECK (status IN ('Pending', 'Completed', 'Failed', 'Reversed')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_account_id (account_id),
    INDEX idx_transaction_type (transaction_type),
    INDEX idx_transaction_date (transaction_date),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================================
-- Table: loans
-- Description: Stores loan information
-- ================================================================
CREATE TABLE IF NOT EXISTS loans (
    loan_id INT PRIMARY KEY AUTO_INCREMENT,
    account_id INT NOT NULL,
    loan_type VARCHAR(50) NOT NULL CHECK (loan_type IN ('Personal', 'Home', 'Auto', 'Business', 'Education')),
    amount DECIMAL(15, 2) NOT NULL,
    interest_rate DECIMAL(5, 2) NOT NULL CHECK (interest_rate >= 0),
    duration_months INT NOT NULL CHECK (duration_months > 0),
    start_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'Active' CHECK (status IN ('Active', 'Paid', 'Defaulted', 'Closed')),
    monthly_payment DECIMAL(15, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_account_id (account_id),
    INDEX idx_loan_type (loan_type),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================================
-- Table: cards
-- Description: Stores debit and credit card information
-- ================================================================
CREATE TABLE IF NOT EXISTS cards (
    card_id INT PRIMARY KEY AUTO_INCREMENT,
    account_id INT NOT NULL,
    card_type VARCHAR(20) NOT NULL CHECK (card_type IN ('Debit', 'Credit')),
    card_number VARCHAR(20) NOT NULL UNIQUE,
    expiry_date DATE NOT NULL,
    credit_limit DECIMAL(15, 2) DEFAULT 0.00,
    status VARCHAR(20) DEFAULT 'Active' CHECK (status IN ('Active', 'Blocked', 'Expired', 'Cancelled')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_account_id (account_id),
    INDEX idx_card_type (card_type),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================================
-- Create views for common queries
-- ================================================================

-- View: account_transaction_summary
CREATE OR REPLACE VIEW account_transaction_summary AS
SELECT 
    a.account_id,
    a.customer_name,
    a.email,
    a.account_type,
    a.balance,
    a.branch,
    COUNT(t.transaction_id) AS total_transactions,
    SUM(CASE WHEN t.transaction_type = 'Deposit' THEN t.amount ELSE 0 END) AS total_deposits,
    SUM(CASE WHEN t.transaction_type = 'Withdrawal' THEN t.amount ELSE 0 END) AS total_withdrawals,
    a.status
FROM accounts a
LEFT JOIN transactions t ON a.account_id = t.account_id
GROUP BY a.account_id, a.customer_name, a.email, a.account_type, a.balance, a.branch, a.status;

-- View: account_statistics
CREATE OR REPLACE VIEW account_statistics AS
SELECT 
    account_type,
    COUNT(account_id) AS total_accounts,
    SUM(balance) AS total_balance,
    AVG(balance) AS avg_balance,
    COUNT(CASE WHEN status = 'Active' THEN 1 END) AS active_accounts,
    COUNT(CASE WHEN status = 'Closed' THEN 1 END) AS closed_accounts
FROM accounts
GROUP BY account_type;

-- ================================================================
-- Insert sample verification data
-- ================================================================
-- This ensures the schema is working correctly
-- DELETE FROM cards;
-- DELETE FROM loans;
-- DELETE FROM transactions;
-- DELETE FROM accounts;
