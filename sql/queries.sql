-- ================================================================
-- Statistical Analysis Queries for Bank Data Analysis System
-- These queries support the analysis requirements from the project PDF
-- ================================================================

USE bank_data_analysis;

-- ================================================================
-- Query 1: Account Balances by Type
-- Purpose: Analyze total and average balances per account type
-- ================================================================
SELECT 
    account_type,
    COUNT(account_id) AS total_accounts,
    SUM(balance) AS total_balance,
    AVG(balance) AS avg_balance,
    MIN(balance) AS min_balance,
    MAX(balance) AS max_balance
FROM accounts
GROUP BY account_type
ORDER BY total_balance DESC;

-- ================================================================
-- Query 2: Transaction Analysis by Type
-- Purpose: Track transaction volumes and amounts by type
-- ================================================================
SELECT 
    transaction_type,
    COUNT(transaction_id) AS total_transactions,
    SUM(amount) AS total_amount,
    AVG(amount) AS avg_amount,
    MIN(amount) AS min_amount,
    MAX(amount) AS max_amount
FROM transactions
WHERE status = 'Completed'
GROUP BY transaction_type
ORDER BY total_amount DESC;

-- ================================================================
-- Query 3: Branch-wise Account Distribution
-- Purpose: Analyze account distribution across branches
-- ================================================================
SELECT 
    branch,
    COUNT(account_id) AS total_accounts,
    SUM(balance) AS total_balance,
    AVG(balance) AS avg_balance,
    COUNT(CASE WHEN status = 'Active' THEN 1 END) AS active_accounts,
    COUNT(CASE WHEN status = 'Closed' THEN 1 END) AS closed_accounts
FROM accounts
GROUP BY branch
ORDER BY total_balance DESC;

-- ================================================================
-- Query 4: Loan Portfolio Analysis
-- Purpose: Analyze loans by type and status
-- ================================================================
SELECT 
    loan_type,
    COUNT(loan_id) AS total_loans,
    SUM(amount) AS total_loan_amount,
    AVG(amount) AS avg_loan_amount,
    AVG(interest_rate) AS avg_interest_rate,
    SUM(monthly_payment) AS total_monthly_payments
FROM loans
WHERE status = 'Active'
GROUP BY loan_type
ORDER BY total_loan_amount DESC;

-- ================================================================
-- Query 5: Account Status Distribution
-- Purpose: Distribution of accounts by status
-- ================================================================
SELECT 
    status,
    COUNT(*) AS count,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM accounts), 2) AS percentage,
    SUM(balance) AS total_balance
FROM accounts
GROUP BY status
ORDER BY count DESC;

-- ================================================================
-- Query 6: Top Accounts by Balance
-- Purpose: Identify high-value accounts
-- ================================================================
SELECT 
    account_id,
    customer_name,
    email,
    account_type,
    balance,
    branch,
    status
FROM accounts
ORDER BY balance DESC
LIMIT 10;

-- ================================================================
-- Query 7: Transaction Volume Trends
-- Purpose: Track transaction patterns over time
-- ================================================================
SELECT 
    DATE_FORMAT(transaction_date, '%Y-%m') AS month,
    COUNT(transaction_id) AS total_transactions,
    SUM(CASE WHEN amount > 0 THEN amount ELSE 0 END) AS total_inflow,
    SUM(CASE WHEN amount < 0 THEN ABS(amount) ELSE 0 END) AS total_outflow,
    SUM(amount) AS net_amount
FROM transactions
WHERE status = 'Completed'
GROUP BY month
ORDER BY month DESC;

-- ================================================================
-- Query 8: Card Distribution Analysis
-- Purpose: Analyze debit and credit card distribution
-- ================================================================
SELECT 
    card_type,
    COUNT(card_id) AS total_cards,
    COUNT(CASE WHEN status = 'Active' THEN 1 END) AS active_cards,
    SUM(CASE WHEN card_type = 'Credit' THEN credit_limit ELSE 0 END) AS total_credit_limit,
    AVG(CASE WHEN card_type = 'Credit' THEN credit_limit ELSE NULL END) AS avg_credit_limit
FROM cards
GROUP BY card_type;

-- ================================================================
-- Query 9: Account Activity Analysis
-- Purpose: Identify most active accounts
-- ================================================================
SELECT 
    a.account_id,
    a.customer_name,
    a.account_type,
    a.balance,
    COUNT(t.transaction_id) AS transaction_count,
    SUM(CASE WHEN t.amount > 0 THEN t.amount ELSE 0 END) AS total_deposits,
    SUM(CASE WHEN t.amount < 0 THEN ABS(t.amount) ELSE 0 END) AS total_withdrawals
FROM accounts a
LEFT JOIN transactions t ON a.account_id = t.account_id AND t.status = 'Completed'
GROUP BY a.account_id, a.customer_name, a.account_type, a.balance
HAVING transaction_count > 0
ORDER BY transaction_count DESC
LIMIT 10;

-- ================================================================
-- Query 10: Loan Repayment Analysis
-- Purpose: Calculate total loan obligations
-- ================================================================
SELECT 
    a.account_id,
    a.customer_name,
    COUNT(l.loan_id) AS total_loans,
    SUM(l.amount) AS total_borrowed,
    SUM(l.monthly_payment) AS monthly_obligation,
    AVG(l.interest_rate) AS avg_interest_rate,
    a.balance
FROM accounts a
INNER JOIN loans l ON a.account_id = l.account_id
WHERE l.status = 'Active'
GROUP BY a.account_id, a.customer_name, a.balance
ORDER BY total_borrowed DESC;

-- ================================================================
-- Query 11: Balance Range Analysis
-- Purpose: Categorize accounts by balance ranges
-- ================================================================
SELECT 
    CASE 
        WHEN balance >= 10000000 THEN '10M+ (High Value)'
        WHEN balance >= 5000000 THEN '5M-10M (Medium-High)'
        WHEN balance >= 1000000 THEN '1M-5M (Medium)'
        WHEN balance >= 500000 THEN '500K-1M (Low-Medium)'
        ELSE 'Below 500K'
    END AS balance_range,
    COUNT(*) AS account_count,
    SUM(balance) AS total_balance,
    AVG(balance) AS avg_balance
FROM accounts
WHERE status = 'Active'
GROUP BY balance_range
ORDER BY avg_balance DESC;

-- ================================================================
-- Query 12: Monthly Revenue from Interest
-- Purpose: Calculate potential revenue from loans
-- ================================================================
SELECT 
    loan_type,
    COUNT(loan_id) AS active_loans,
    SUM(amount) AS total_principal,
    AVG(interest_rate) AS avg_rate,
    SUM(monthly_payment) AS monthly_revenue,
    SUM(monthly_payment * 12) AS annual_revenue
FROM loans
WHERE status = 'Active'
GROUP BY loan_type
ORDER BY annual_revenue DESC;
