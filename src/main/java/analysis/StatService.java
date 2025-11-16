package analysis;

import db.QueryExecutor;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.*;

/**
 * Statistical Analysis Service for Bank Data Analysis System.
 * Provides methods for statistical queries and data analysis.
 * 
 * This class implements all the statistical analysis requirements
 * from the project specification, including aggregations, trends,
 * and distribution analysis for banking data.
 * 
 * @author Bank Data Analysis Team
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
     * Gets account balances by account type.
     * Returns account type, count, total and average balances.
     * 
     * @return TableModel with account balance statistics
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel getAccountBalancesByType() throws SQLException {
        String query = "SELECT account_type, " +
                "COUNT(account_id) AS total_accounts, " +
                "SUM(balance) AS total_balance, " +
                "AVG(balance) AS avg_balance, " +
                "MIN(balance) AS min_balance, " +
                "MAX(balance) AS max_balance " +
                "FROM accounts " +
                "GROUP BY account_type " +
                "ORDER BY total_balance DESC";
        
        return queryExecutor.executeQuery(query);
    }
    
    /**
     * Gets transaction analysis by type.
     * Shows volumes and amounts for each transaction type.
     * 
     * @return TableModel with transaction statistics
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel getTransactionsByType() throws SQLException {
        String query = "SELECT transaction_type, " +
                "COUNT(transaction_id) AS total_transactions, " +
                "SUM(amount) AS total_amount, " +
                "AVG(amount) AS avg_amount, " +
                "MIN(amount) AS min_amount, " +
                "MAX(amount) AS max_amount " +
                "FROM transactions " +
                "WHERE status = 'Completed' " +
                "GROUP BY transaction_type " +
                "ORDER BY total_amount DESC";
        
        return queryExecutor.executeQuery(query);
    }
    
    /**
     * Gets branch-wise account distribution.
     * Shows account counts and balances per branch.
     * 
     * @return TableModel with branch statistics
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel getBranchDistribution() throws SQLException {
        String query = "SELECT branch, " +
                "COUNT(account_id) AS total_accounts, " +
                "SUM(balance) AS total_balance, " +
                "AVG(balance) AS avg_balance, " +
                "COUNT(CASE WHEN status = 'Active' THEN 1 END) AS active_accounts, " +
                "COUNT(CASE WHEN status = 'Closed' THEN 1 END) AS closed_accounts " +
                "FROM accounts " +
                "GROUP BY branch " +
                "ORDER BY total_balance DESC";
        
        return queryExecutor.executeQuery(query);
    }
    
    /**
     * Gets loan portfolio analysis.
     * Analyzes loans by type with amounts and interest rates.
     * 
     * @return TableModel with loan statistics
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel getLoanPortfolioAnalysis() throws SQLException {
        String query = "SELECT loan_type, " +
                "COUNT(loan_id) AS total_loans, " +
                "SUM(amount) AS total_loan_amount, " +
                "AVG(amount) AS avg_loan_amount, " +
                "AVG(interest_rate) AS avg_interest_rate, " +
                "SUM(monthly_payment) AS total_monthly_payments " +
                "FROM loans " +
                "WHERE status = 'Active' " +
                "GROUP BY loan_type " +
                "ORDER BY total_loan_amount DESC";
        
        return queryExecutor.executeQuery(query);
    }
    
    /**
     * Gets account status distribution.
     * Shows count and percentage for each status type.
     * 
     * @return TableModel with status distribution
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel getStatusDistribution() throws SQLException {
        String query = "SELECT status, COUNT(*) AS count, " +
                "ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM accounts), 2) AS percentage, " +
                "SUM(balance) AS total_balance " +
                "FROM accounts " +
                "GROUP BY status " +
                "ORDER BY count DESC";
        
        return queryExecutor.executeQuery(query);
    }
    
    /**
     * Gets top accounts by balance.
     * 
     * @param limit Maximum number of accounts to return
     * @return TableModel with top accounts
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel getTopAccounts(int limit) throws SQLException {
        String query = "SELECT account_id, customer_name, email, " +
                "account_type, balance, branch, status " +
                "FROM accounts " +
                "ORDER BY balance DESC " +
                "LIMIT " + limit;
        
        return queryExecutor.executeQuery(query);
    }
    
    /**
     * Gets transaction volume trends over time.
     * Shows monthly transaction patterns.
     * 
     * @return TableModel with transaction trends
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel getTransactionTrends() throws SQLException {
        String query = "SELECT DATE_FORMAT(transaction_date, '%Y-%m') AS month, " +
                "COUNT(transaction_id) AS total_transactions, " +
                "SUM(CASE WHEN amount > 0 THEN amount ELSE 0 END) AS total_inflow, " +
                "SUM(CASE WHEN amount < 0 THEN ABS(amount) ELSE 0 END) AS total_outflow, " +
                "SUM(amount) AS net_amount " +
                "FROM transactions " +
                "WHERE status = 'Completed' " +
                "GROUP BY month " +
                "ORDER BY month DESC";
        
        return queryExecutor.executeQuery(query);
    }
    
    /**
     * Gets card distribution analysis.
     * Shows debit and credit card statistics.
     * 
     * @return TableModel with card statistics
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel getCardDistribution() throws SQLException {
        String query = "SELECT card_type, " +
                "COUNT(card_id) AS total_cards, " +
                "COUNT(CASE WHEN status = 'Active' THEN 1 END) AS active_cards, " +
                "SUM(CASE WHEN card_type = 'Credit' THEN credit_limit ELSE 0 END) AS total_credit_limit, " +
                "AVG(CASE WHEN card_type = 'Credit' THEN credit_limit ELSE NULL END) AS avg_credit_limit " +
                "FROM cards " +
                "GROUP BY card_type";
        
        return queryExecutor.executeQuery(query);
    }
    
    /**
     * Gets account activity analysis.
     * Identifies most active accounts based on transaction count.
     * 
     * @return TableModel with account activity
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel getAccountActivity() throws SQLException {
        String query = "SELECT a.account_id, a.customer_name, a.account_type, a.balance, " +
                "COUNT(t.transaction_id) AS transaction_count, " +
                "SUM(CASE WHEN t.amount > 0 THEN t.amount ELSE 0 END) AS total_deposits, " +
                "SUM(CASE WHEN t.amount < 0 THEN ABS(t.amount) ELSE 0 END) AS total_withdrawals " +
                "FROM accounts a " +
                "LEFT JOIN transactions t ON a.account_id = t.account_id AND t.status = 'Completed' " +
                "GROUP BY a.account_id, a.customer_name, a.account_type, a.balance " +
                "HAVING transaction_count > 0 " +
                "ORDER BY transaction_count DESC " +
                "LIMIT 10";
        
        return queryExecutor.executeQuery(query);
    }
    
    /**
     * Gets loan repayment analysis.
     * Calculates total loan obligations per account.
     * 
     * @return TableModel with loan obligations
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel getLoanRepaymentAnalysis() throws SQLException {
        String query = "SELECT a.account_id, a.customer_name, " +
                "COUNT(l.loan_id) AS total_loans, " +
                "SUM(l.amount) AS total_borrowed, " +
                "SUM(l.monthly_payment) AS monthly_obligation, " +
                "AVG(l.interest_rate) AS avg_interest_rate, " +
                "a.balance " +
                "FROM accounts a " +
                "INNER JOIN loans l ON a.account_id = l.account_id " +
                "WHERE l.status = 'Active' " +
                "GROUP BY a.account_id, a.customer_name, a.balance " +
                "ORDER BY total_borrowed DESC";
        
        return queryExecutor.executeQuery(query);
    }
    
    /**
     * Gets balance range analysis.
     * Categorizes accounts by balance ranges.
     * 
     * @return TableModel with balance range statistics
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel getBalanceRangeAnalysis() throws SQLException {
        String query = "SELECT " +
                "CASE WHEN balance >= 10000000 THEN '10M+ (High Value)' " +
                "WHEN balance >= 5000000 THEN '5M-10M (Medium-High)' " +
                "WHEN balance >= 1000000 THEN '1M-5M (Medium)' " +
                "WHEN balance >= 500000 THEN '500K-1M (Low-Medium)' " +
                "ELSE 'Below 500K' END AS balance_range, " +
                "COUNT(*) AS account_count, " +
                "SUM(balance) AS total_balance, " +
                "AVG(balance) AS avg_balance " +
                "FROM accounts " +
                "WHERE status = 'Active' " +
                "GROUP BY balance_range " +
                "ORDER BY avg_balance DESC";
        
        return queryExecutor.executeQuery(query);
    }
    
    /**
     * Gets revenue analysis from loan interest.
     * Calculates potential monthly and annual revenue.
     * 
     * @return TableModel with revenue statistics
     * @throws SQLException if query execution fails
     */
    public DefaultTableModel getRevenueAnalysis() throws SQLException {
        String query = "SELECT loan_type, " +
                "COUNT(loan_id) AS active_loans, " +
                "SUM(amount) AS total_principal, " +
                "AVG(interest_rate) AS avg_rate, " +
                "SUM(monthly_payment) AS monthly_revenue, " +
                "SUM(monthly_payment * 12) AS annual_revenue " +
                "FROM loans " +
                "WHERE status = 'Active' " +
                "GROUP BY loan_type " +
                "ORDER BY annual_revenue DESC";
        
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
        
        // Total accounts
        Object totalAccounts = queryExecutor.executeScalar(
                "SELECT COUNT(*) FROM accounts");
        stats.put("Total Accounts", totalAccounts);
        
        // Total balance
        Object totalBalance = queryExecutor.executeScalar(
                "SELECT ROUND(SUM(balance), 2) FROM accounts WHERE status = 'Active'");
        stats.put("Total Balance", totalBalance);
        
        // Total transactions
        Object totalTransactions = queryExecutor.executeScalar(
                "SELECT COUNT(*) FROM transactions WHERE status = 'Completed'");
        stats.put("Total Transactions", totalTransactions);
        
        // Active accounts
        Object activeAccounts = queryExecutor.executeScalar(
                "SELECT COUNT(*) FROM accounts WHERE status = 'Active'");
        stats.put("Active Accounts", activeAccounts);
        
        // Active loans
        Object activeLoans = queryExecutor.executeScalar(
                "SELECT COUNT(*) FROM loans WHERE status = 'Active'");
        stats.put("Active Loans", activeLoans);
        
        // Total cards
        Object totalCards = queryExecutor.executeScalar(
                "SELECT COUNT(*) FROM cards WHERE status = 'Active'");
        stats.put("Active Cards", totalCards);
        
        return stats;
    }
}
