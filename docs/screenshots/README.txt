APPLICATION SCREENSHOTS

This directory should contain screenshots of the Bank Data Analysis System application
showing the key features and functionality:

Recommended Screenshots:
1. dashboard.png - Main dashboard with summary cards showing:
   - Total Accounts
   - Total Balance
   - Total Transactions
   - Active Accounts
   - Active Loans
   - Active Cards
   - Bank data insights panel

2. data-tables.png - Data Tables tab showing:
   - Dropdown menu with analysis options
   - Tabular data display (e.g., Account Balances by Type)
   - Export to CSV button

3. charts-bar.png - Bar chart visualization showing:
   - Account Balances by Type
   - Or Branch Distribution
   - With proper axis labels and legends

4. charts-line.png - Line chart showing:
   - Transaction Trends Over Time
   - Multiple data series if applicable

5. charts-pie.png - Pie chart showing:
   - Account Status Distribution
   - Or Transaction Types Distribution

6. database-connection.png - Database connection test dialog

7. menu-system.png - Main menu showing File, Tools, and Help menus

To generate these screenshots:
1. Build the application: mvn clean package
2. Ensure MySQL/MariaDB is running with bank_data_analysis database
3. Run: java -jar target/data-analysis-system-1.0.0-with-dependencies.jar
4. Navigate to each tab and feature
5. Take screenshots using system screenshot tool
6. Save them in this directory with descriptive names
