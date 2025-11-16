-- ================================================================
-- Data Import Scripts for Bank Data Analysis System
-- Note: This script assumes CSV files are loaded via Java application
-- For manual import, use LOAD DATA INFILE or the commands below
-- ================================================================

USE bank_data_analysis;

-- ================================================================
-- Clear existing data (use with caution!)
-- ================================================================
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE cards;
TRUNCATE TABLE loans;
TRUNCATE TABLE transactions;
TRUNCATE TABLE accounts;
SET FOREIGN_KEY_CHECKS = 1;

-- ================================================================
-- Import Accounts Data
-- ================================================================
INSERT INTO accounts (account_id, customer_name, email, phone, account_type, balance, date_opened, branch, status) VALUES
(1, 'John Doe', 'john.doe@email.com', '+255712345001', 'Savings', 5500000.00, '2021-03-15', 'Dar es Salaam', 'Active'),
(2, 'Jane Smith', 'jane.smith@email.com', '+255712345002', 'Checking', 3200000.00, '2021-07-22', 'Arusha', 'Active'),
(3, 'Ahmed Hassan', 'ahmed.hassan@email.com', '+255712345003', 'Savings', 8900000.00, '2021-01-10', 'Dodoma', 'Active'),
(4, 'Fatuma Ali', 'fatuma.ali@email.com', '+255712345004', 'Business', 15000000.00, '2021-11-30', 'Mwanza', 'Active'),
(5, 'David Johnson', 'david.j@email.com', '+255712345005', 'Checking', 2100000.00, '2022-05-18', 'Mbeya', 'Active'),
(6, 'Sarah Williams', 'sarah.w@email.com', '+255712345006', 'Savings', 6700000.00, '2022-09-25', 'Tanga', 'Active'),
(7, 'Mohamed Ibrahim', 'mohamed.i@email.com', '+255712345007', 'Business', 22000000.00, '2022-02-14', 'Zanzibar', 'Active'),
(8, 'Grace Mwangi', 'grace.m@email.com', '+255712345008', 'Checking', 1800000.00, '2022-12-08', 'Morogoro', 'Active'),
(9, 'Peter Kamau', 'peter.k@email.com', '+255712345009', 'Savings', 9500000.00, '2023-04-20', 'Kilimanjaro', 'Active'),
(10, 'Amina Said', 'amina.s@email.com', '+255712345010', 'Fixed Deposit', 45000000.00, '2023-08-17', 'Pwani', 'Active'),
(11, 'James Brown', 'james.b@email.com', '+255712345011', 'Checking', 2800000.00, '2021-06-12', 'Singida', 'Active'),
(12, 'Mary Kimani', 'mary.k@email.com', '+255712345012', 'Savings', 7200000.00, '2022-10-05', 'Iringa', 'Active'),
(13, 'Hassan Omar', 'hassan.o@email.com', '+255712345013', 'Business', 18500000.00, '2021-01-28', 'Kigoma', 'Active'),
(14, 'Elizabeth Njeri', 'elizabeth.n@email.com', '+255712345014', 'Checking', 1500000.00, '2021-11-19', 'Rukwa', 'Closed'),
(15, 'Ibrahim Juma', 'ibrahim.j@email.com', '+255712345015', 'Savings', 5800000.00, '2022-03-22', 'Mara', 'Active'),
(16, 'Lucy Wambui', 'lucy.w@email.com', '+255712345016', 'Fixed Deposit', 35000000.00, '2023-07-09', 'Kagera', 'Active'),
(17, 'Rashid Salim', 'rashid.s@email.com', '+255712345017', 'Business', 12000000.00, '2021-09-14', 'Lindi', 'Active'),
(18, 'Catherine Nyambura', 'catherine.n@email.com', '+255712345018', 'Checking', 3500000.00, '2023-05-30', 'Mtwara', 'Active'),
(19, 'Ali Bakari', 'ali.b@email.com', '+255712345019', 'Savings', 8200000.00, '2022-12-03', 'Ruvuma', 'Active'),
(20, 'Rose Muthoni', 'rose.m@email.com', '+255712345020', 'Checking', 2400000.00, '2021-08-26', 'Shinyanga', 'Closed');

-- ================================================================
-- Import Transactions Data
-- ================================================================
INSERT INTO transactions (transaction_id, account_id, transaction_type, amount, transaction_date, description, status) VALUES
(1, 1, 'Deposit', 500000.00, '2023-01-15', 'Salary Deposit', 'Completed'),
(2, 1, 'Withdrawal', 200000.00, '2023-01-20', 'ATM Withdrawal', 'Completed'),
(3, 2, 'Deposit', 800000.00, '2023-01-18', 'Business Revenue', 'Completed'),
(4, 2, 'Transfer', -300000.00, '2023-01-25', 'Transfer to Account 5', 'Completed'),
(5, 3, 'Deposit', 1000000.00, '2023-02-01', 'Investment Return', 'Completed'),
(6, 3, 'Withdrawal', 150000.00, '2023-02-10', 'Cash Withdrawal', 'Completed'),
(7, 4, 'Deposit', 2500000.00, '2023-02-05', 'Business Income', 'Completed'),
(8, 4, 'Payment', -450000.00, '2023-02-15', 'Supplier Payment', 'Completed'),
(9, 5, 'Deposit', 600000.00, '2023-03-01', 'Salary Deposit', 'Completed'),
(10, 5, 'Transfer', 300000.00, '2023-03-05', 'Transfer from Account 2', 'Completed'),
(11, 6, 'Deposit', 750000.00, '2023-03-12', 'Freelance Payment', 'Completed'),
(12, 6, 'Withdrawal', 100000.00, '2023-03-18', 'ATM Withdrawal', 'Completed'),
(13, 7, 'Deposit', 5000000.00, '2023-04-01', 'Contract Payment', 'Completed'),
(14, 7, 'Payment', -1200000.00, '2023-04-10', 'Tax Payment', 'Completed'),
(15, 8, 'Deposit', 400000.00, '2023-04-15', 'Salary Deposit', 'Completed'),
(16, 8, 'Withdrawal', 80000.00, '2023-04-22', 'Cash Withdrawal', 'Completed'),
(17, 9, 'Deposit', 1500000.00, '2023-05-01', 'Business Revenue', 'Completed'),
(18, 9, 'Transfer', -500000.00, '2023-05-08', 'Transfer to Account 12', 'Completed'),
(19, 10, 'Deposit', 45000000.00, '2023-05-15', 'Fixed Deposit', 'Completed'),
(20, 10, 'Interest', 2250000.00, '2023-08-15', 'Quarterly Interest', 'Completed'),
(21, 11, 'Deposit', 550000.00, '2023-06-01', 'Salary Deposit', 'Completed'),
(22, 11, 'Payment', -120000.00, '2023-06-10', 'Utility Bill', 'Completed'),
(23, 12, 'Deposit', 900000.00, '2023-06-15', 'Rental Income', 'Completed'),
(24, 12, 'Transfer', 500000.00, '2023-06-20', 'Transfer from Account 9', 'Completed'),
(25, 13, 'Deposit', 3500000.00, '2023-07-01', 'Contract Payment', 'Completed'),
(26, 13, 'Payment', -850000.00, '2023-07-12', 'Supplier Payment', 'Completed'),
(27, 14, 'Withdrawal', 500000.00, '2023-07-20', 'Account Closure', 'Completed'),
(28, 15, 'Deposit', 650000.00, '2023-08-01', 'Salary Deposit', 'Completed'),
(29, 16, 'Deposit', 35000000.00, '2023-08-10', 'Fixed Deposit', 'Completed'),
(30, 17, 'Deposit', 2200000.00, '2023-09-01', 'Business Income', 'Completed'),
(31, 18, 'Deposit', 700000.00, '2023-09-15', 'Salary Deposit', 'Completed'),
(32, 19, 'Deposit', 1100000.00, '2023-10-01', 'Investment Return', 'Completed'),
(33, 20, 'Withdrawal', 1000000.00, '2023-10-15', 'Account Closure', 'Completed'),
(34, 1, 'Payment', -80000.00, '2023-11-01', 'Insurance Premium', 'Completed'),
(35, 3, 'Deposit', 500000.00, '2023-11-10', 'Dividend Payment', 'Completed');

-- ================================================================
-- Import Loans Data
-- ================================================================
INSERT INTO loans (loan_id, account_id, loan_type, amount, interest_rate, duration_months, start_date, status, monthly_payment) VALUES
(1, 1, 'Personal', 2000000.00, 12.5, 24, '2022-01-15', 'Active', 92708.33),
(2, 2, 'Auto', 8000000.00, 10.0, 60, '2022-03-01', 'Active', 169733.86),
(3, 3, 'Home', 25000000.00, 8.5, 240, '2021-06-15', 'Active', 196180.56),
(4, 4, 'Business', 15000000.00, 11.0, 36, '2022-09-01', 'Active', 492611.11),
(5, 5, 'Personal', 1500000.00, 13.0, 18, '2023-02-10', 'Active', 90416.67),
(6, 7, 'Business', 30000000.00, 9.5, 60, '2022-05-20', 'Active', 628166.67),
(7, 9, 'Home', 35000000.00, 8.0, 300, '2023-01-15', 'Active', 258487.50),
(8, 11, 'Personal', 1000000.00, 12.0, 12, '2023-06-01', 'Active', 88488.89),
(9, 13, 'Business', 20000000.00, 10.5, 48, '2022-11-10', 'Active', 509722.22),
(10, 15, 'Auto', 5000000.00, 11.5, 48, '2023-03-20', 'Active', 131388.89),
(11, 17, 'Business', 12000000.00, 9.0, 36, '2022-08-15', 'Active', 381666.67),
(12, 19, 'Personal', 3000000.00, 13.5, 24, '2023-07-01', 'Active', 141250.00);

-- ================================================================
-- Import Cards Data
-- ================================================================
INSERT INTO cards (card_id, account_id, card_type, card_number, expiry_date, credit_limit, status) VALUES
(1, 1, 'Debit', '4532********1234', '2026-03-31', 0.00, 'Active'),
(2, 2, 'Credit', '5412********5678', '2025-12-31', 5000000.00, 'Active'),
(3, 3, 'Debit', '4916********9012', '2027-06-30', 0.00, 'Active'),
(4, 4, 'Credit', '5485********3456', '2026-11-30', 10000000.00, 'Active'),
(5, 5, 'Debit', '4024********7890', '2025-08-31', 0.00, 'Active'),
(6, 6, 'Debit', '4532********2345', '2027-02-28', 0.00, 'Active'),
(7, 7, 'Credit', '5412********6789', '2026-09-30', 15000000.00, 'Active'),
(8, 8, 'Debit', '4916********0123', '2025-07-31', 0.00, 'Active'),
(9, 9, 'Credit', '5485********4567', '2027-05-31', 8000000.00, 'Active'),
(10, 10, 'Debit', '4024********8901', '2026-12-31', 0.00, 'Active'),
(11, 11, 'Debit', '4532********3456', '2025-10-31', 0.00, 'Active'),
(12, 12, 'Credit', '5412********7890', '2027-03-31', 6000000.00, 'Active'),
(13, 13, 'Credit', '5485********5678', '2026-08-31', 12000000.00, 'Active'),
(14, 15, 'Debit', '4024********9012', '2026-04-30', 0.00, 'Active'),
(15, 16, 'Debit', '4532********4567', '2027-09-30', 0.00, 'Active'),
(16, 17, 'Credit', '5412********8901', '2026-07-31', 9000000.00, 'Active'),
(17, 18, 'Debit', '4916********1234', '2025-11-30', 0.00, 'Active'),
(18, 19, 'Credit', '5485********6789', '2027-01-31', 7000000.00, 'Active');

-- ================================================================
-- Verification Queries
-- ================================================================
-- Verify data import
SELECT 'Accounts' AS TableName, COUNT(*) AS RecordCount FROM accounts
UNION ALL
SELECT 'Transactions', COUNT(*) FROM transactions
UNION ALL
SELECT 'Loans', COUNT(*) FROM loans
UNION ALL
SELECT 'Cards', COUNT(*) FROM cards;
