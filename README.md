# Bank Data Analysis System

A comprehensive Java Swing + JDBC application for analyzing bank account and transaction data using MySQL/MariaDB database.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Database Setup](#database-setup)
- [Running the Application](#running-the-application)
- [Project Structure](#project-structure)
- [Usage Guide](#usage-guide)
- [Testing](#testing)
- [Troubleshooting](#troubleshooting)
- [Documentation](#documentation)
- [License](#license)

## 🎯 Overview

This project is a complete data analysis system developed as part of the Object Oriented Programming semester project. It demonstrates working with real-world datasets including:

- Bank account information
- Transaction records and trends
- Loan portfolio details
- Card distribution and usage

The system provides statistical analysis, data visualization, and comprehensive reporting capabilities through an intuitive graphical user interface.

## ✨ Features

### Data Management
- ✅ CSV file import with validation
- ✅ Normalized relational database schema
- ✅ Data integrity with foreign key constraints
- ✅ CRUD operations with prepared statements

### Statistical Analysis
- 📊 Account balances by type
- 📈 Transaction trends over time
- 🎓 Loan portfolio analysis
- 👥 Account status distribution
- 🌍 Branch-wise distribution
- 💰 Revenue analysis from loans
- 📉 Balance range analysis

### Data Visualization
- 📊 Bar charts for comparative analysis
- 📈 Line charts for trend analysis
- 🥧 Pie charts for distribution analysis
- 💾 Export charts as PNG images

### User Interface
- 🖥️ Modern Swing GUI with tabbed interface
- 📋 Interactive data tables with sorting
- 🔄 Real-time data refresh
- 💾 Export data to CSV
- 📊 Dashboard with summary cards

## 🛠️ Technologies Used

- **Java 11+** - Core programming language
- **Swing** - GUI framework
- **JDBC** - Database connectivity
- **MySQL/MariaDB** - Relational database (XAMPP)
- **Maven** - Build and dependency management
- **XChart** - Chart and graph visualization
- **Apache Commons CSV** - CSV file parsing
- **JUnit 4** - Unit testing

## 📦 Prerequisites

Before running the application, ensure you have:

1. **Java Development Kit (JDK) 11 or higher**
   ```bash
   java -version
   ```

2. **Maven 3.6 or higher**
   ```bash
   mvn -version
   ```

3. **XAMPP with MySQL/MariaDB**
   - Download from: https://www.apachefriends.org/
   - Ensure MySQL service is running
   - Default port: 3306

4. **Git** (optional, for cloning repository)

## 🚀 Installation & Setup

### Step 1: Clone or Download the Repository

```bash
git clone <repository-url>
cd dorice
```

Or download and extract the ZIP file.

### Step 2: Configure Database Connection

Edit `src/main/resources/config.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/bank_data_analysis?useSSL=false&serverTimezone=UTC
db.user=root
db.password=
```

**Note:** If your MySQL has a password, update the `db.password` field.

### Step 3: Build the Project

```bash
mvn clean install
```

This will:
- Download all dependencies
- Compile the source code
- Run unit tests
- Create executable JAR files in `target/` directory

## 🗄️ Database Setup

### Step 1: Start XAMPP MySQL

1. Open XAMPP Control Panel
2. Start **Apache** (for phpMyAdmin access)
3. Start **MySQL**

### Step 2: Create Database and Schema

**Option A: Using phpMyAdmin**

1. Open browser: http://localhost/phpmyadmin
2. Click "Import" tab
3. Choose file: `sql/schema.sql`
4. Click "Go"

**Option B: Using MySQL Command Line**

```bash
mysql -u root -p < sql/schema.sql
```

### Step 3: Import Sample Data

**Using phpMyAdmin:**
1. Select database: `bank_data_analysis`
2. Import file: `sql/import.sql`

**Using Command Line:**
```bash
mysql -u root -p bank_data_analysis < sql/import.sql
```

### Step 4: Verify Installation

```sql
USE bank_data_analysis;
SHOW TABLES;
SELECT COUNT(*) FROM accounts;
```

You should see 4 tables with sample data.

## ▶️ Running the Application

### Method 1: Using Maven

```bash
mvn exec:java -Dexec.mainClass="app.MainApp"
```

### Method 2: Using JAR File (Recommended)

```bash
# Build the JAR with dependencies
mvn clean package

# Run the application
java -jar target/data-analysis-system-1.0.0-with-dependencies.jar
```

### Method 3: Using Run Script

On Linux/Mac:
```bash
chmod +x scripts/run.sh
./scripts/run.sh
```

On Windows:
```bash
scripts\run.bat
```

## 📁 Project Structure

```
dorice/
├── pom.xml                          # Maven configuration
├── README.md                        # This file
├── sql/                            # SQL scripts
│   ├── schema.sql                  # Database schema
│   ├── import.sql                  # Data import script
│   └── queries.sql                 # Analysis queries
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── app/
│   │   │   │   └── MainApp.java           # Application entry point
│   │   │   ├── db/
│   │   │   │   ├── DBConnection.java      # Database connection manager
│   │   │   │   └── QueryExecutor.java     # Query execution utility
│   │   │   ├── util/
│   │   │   │   └── CSVImporter.java       # CSV import utility
│   │   │   ├── analysis/
│   │   │   │   └── StatService.java       # Statistical analysis service
│   │   │   └── ui/
│   │   │       ├── MainFrame.java         # Main application window
│   │   │       ├── DashboardPanel.java    # Dashboard view
│   │   │       ├── DataTablePanel.java    # Data tables view
│   │   │       └── ChartPanel.java        # Charts and graphs view
│   │   └── resources/
│   │       ├── config.properties          # Configuration file
│   │       └── data/                      # CSV data files
│   │           ├── accounts.csv
│   │           ├── transactions.csv
│   │           ├── loans.csv
│   │           └── cards.csv
│   └── test/
│       └── java/
│           ├── db/
│           │   └── DBConnectionTest.java  # Connection tests
│           └── util/
│               └── CSVImporterTest.java   # Import tests
├── docs/                           # Documentation
│   ├── report.md                   # Detailed project report
│   ├── erd.txt                     # ERD diagram
│   └── screenshots/                # Application screenshots
└── scripts/                        # Run scripts
    ├── run.sh                      # Linux/Mac run script
    └── run.bat                     # Windows run script
```

## 📖 Usage Guide

### 1. Starting the Application

When you launch the application:
- The main window opens with three tabs: Dashboard, Data Tables, and Charts & Graphs
- The system automatically tests the database connection
- If successful, data is loaded and displayed

### 2. Dashboard Tab

View summary statistics including:
- Total accounts, transactions, and balance
- Active accounts count
- Active loans and cards
- Generated insights and recommendations

Click **Refresh** to update the data.

### 3. Data Tables Tab

- Select an analysis type from the dropdown menu
- View results in tabular format
- Click **Export to CSV** to save data
- All tables support sorting by clicking column headers

Available analyses:
- Account Balances by Type
- Transactions by Type
- Branch Distribution
- Loan Portfolio Analysis
- Account Status Distribution
- Top Accounts (10)
- Transaction Trends
- Card Distribution
- Account Activity
- Loan Repayment Analysis
- Balance Range Analysis
- Revenue Analysis

### 4. Charts & Graphs Tab

- Select a chart type from the dropdown
- View interactive visualizations
- Click **Save as PNG** to export charts
- Switch between different chart types

Available charts:
- Bar charts for comparisons
- Line charts for trends
- Pie charts for distributions

### 5. Menu Options

**File Menu:**
- Import CSV Files - Import data from CSV files
- Refresh Data - Reload all data
- Exit - Close application

**Tools Menu:**
- Test Database Connection - Verify database connectivity
- Show Summary - Display quick statistics

**Help Menu:**
- About - Application information

## 🧪 Testing

### Run All Tests

```bash
mvn test
```

### Run Specific Test Class

```bash
mvn test -Dtest=DBConnectionTest
mvn test -Dtest=CSVImporterTest
```

### Test Coverage

The project includes tests for:
- Database connection functionality
- CSV file validation and import
- Singleton pattern implementation
- Configuration loading

**Note:** Some tests require MySQL to be running. Tests will skip gracefully if the database is unavailable.

## 🔧 Troubleshooting

### Issue: "Connection failed" error

**Solutions:**
1. Verify XAMPP MySQL is running
2. Check database exists: `SHOW DATABASES;`
3. Verify credentials in `config.properties`
4. Test connection: Tools → Test Database Connection

### Issue: "Table doesn't exist" error

**Solution:**
```bash
mysql -u root -p < sql/schema.sql
```

### Issue: No data displayed

**Solution:**
1. Import sample data: `mysql -u root -p bank_data_analysis < sql/import.sql`
2. Or use File → Import CSV Files

### Issue: Charts not displaying

**Solutions:**
1. Ensure XChart dependency is in pom.xml
2. Rebuild: `mvn clean install`
3. Check for Java version compatibility

### Issue: "Driver not found" error

**Solution:**
MySQL driver is included in pom.xml. Rebuild with:
```bash
mvn clean install
```

### Issue: Permission denied on CSV import

**Solution:**
- Use the application's CSV import feature (File → Import CSV Files)
- Ensure CSV files are in `src/main/resources/data/`

## 📚 Documentation

Additional documentation available in `docs/` directory:

- **report.md** - Comprehensive project report with:
  - Dataset description
  - ERD explanation
  - SQL query documentation
  - Architecture overview
  - Analysis insights
  - Challenges and solutions

- **erd.txt** - Entity-Relationship Diagram in ASCII format

- **screenshots/** - Application screenshots

## 🎓 Academic Project Information

**Course:** Object Oriented Programming  
**Program:** BSc. Computer Networks & Security  
**School:** School of Computing & Engineering Sciences  

**Project Requirements Met:**
✅ Database design and normalization  
✅ CSV data import  
✅ JDBC connectivity  
✅ Statistical analysis queries  
✅ Data visualization  
✅ Swing GUI implementation  
✅ Comprehensive documentation  
✅ Unit testing

## 🤝 Contributing

This is an academic project. For suggestions or improvements:

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

## 📝 License

This project is developed for academic purposes as part of the OOP semester project.

---

## 📞 Support

For issues or questions:
- Check the [Troubleshooting](#troubleshooting) section
- Review `docs/report.md` for detailed information
- Consult the inline code documentation (Javadoc)

---

**Version:** 1.0.0  
**Last Updated:** November 2024

---

## Quick Start Checklist

- [ ] Install Java 11+
- [ ] Install Maven
- [ ] Install and start XAMPP MySQL
- [ ] Clone/download project
- [ ] Configure `config.properties`
- [ ] Run `mvn clean install`
- [ ] Execute `sql/schema.sql`
- [ ] Execute `sql/import.sql`
- [ ] Run application: `java -jar target/data-analysis-system-1.0.0-with-dependencies.jar`
- [ ] Verify connection: Tools → Test Database Connection
- [ ] Explore Dashboard, Data Tables, and Charts tabs
