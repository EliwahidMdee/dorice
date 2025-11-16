package ui;

import analysis.StatService;
import db.DBConnection;
import util.CSVImporter;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Map;

/**
 * Main Application Frame for Student Data Analysis System.
 * Provides the primary GUI window with tabbed interface.
 * 
 * This is the main GUI container that holds all panels including
 * dashboard, data tables, and charts.
 * 
 * @author Student Data Analysis Team
 * @version 1.0
 */
public class MainFrame extends JFrame {
    
    private JTabbedPane tabbedPane;
    private DashboardPanel dashboardPanel;
    private DataTablePanel dataTablePanel;
    private ChartPanel chartPanel;
    private StatService statService;
    private CSVImporter csvImporter;
    
    /**
     * Constructor - creates and initializes the main frame.
     */
    public MainFrame() {
        this.statService = new StatService();
        this.csvImporter = new CSVImporter();
        
        initializeFrame();
        createMenuBar();
        createComponents();
        
        // Test database connection on startup
        testDatabaseConnection();
    }
    
    /**
     * Initializes the main frame properties.
     */
    private void initializeFrame() {
        setTitle("Student Data Analysis System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null); // Center on screen
        
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Creates the menu bar with File and Tools menus.
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File Menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        
        JMenuItem importMenuItem = new JMenuItem("Import CSV Files");
        importMenuItem.addActionListener(e -> importCSVFiles());
        
        JMenuItem refreshMenuItem = new JMenuItem("Refresh Data");
        refreshMenuItem.addActionListener(e -> refreshAllData());
        
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> exitApplication());
        
        fileMenu.add(importMenuItem);
        fileMenu.add(refreshMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        
        // Tools Menu
        JMenu toolsMenu = new JMenu("Tools");
        toolsMenu.setMnemonic('T');
        
        JMenuItem dbTestMenuItem = new JMenuItem("Test Database Connection");
        dbTestMenuItem.addActionListener(e -> testDatabaseConnection());
        
        JMenuItem summaryMenuItem = new JMenuItem("Show Summary");
        summaryMenuItem.addActionListener(e -> showSummary());
        
        toolsMenu.add(dbTestMenuItem);
        toolsMenu.add(summaryMenuItem);
        
        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(e -> showAbout());
        
        helpMenu.add(aboutMenuItem);
        
        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    /**
     * Creates and adds all panel components to the frame.
     */
    private void createComponents() {
        tabbedPane = new JTabbedPane();
        
        // Create panels
        dashboardPanel = new DashboardPanel(statService);
        dataTablePanel = new DataTablePanel(statService);
        chartPanel = new ChartPanel(statService);
        
        // Add panels to tabbed pane
        tabbedPane.addTab("Dashboard", new ImageIcon(), dashboardPanel, "View summary statistics");
        tabbedPane.addTab("Data Tables", new ImageIcon(), dataTablePanel, "View detailed data tables");
        tabbedPane.addTab("Charts & Graphs", new ImageIcon(), chartPanel, "View visualizations");
        
        add(tabbedPane);
    }
    
    /**
     * Tests database connection and shows result to user.
     */
    private void testDatabaseConnection() {
        DBConnection dbConnection = DBConnection.getInstance();
        boolean connected = dbConnection.testConnection();
        
        if (connected) {
            JOptionPane.showMessageDialog(this,
                    "Database connection successful!\n\n" +
                    "URL: " + dbConnection.getUrl() + "\n" +
                    "User: " + dbConnection.getUser(),
                    "Connection Test",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Database connection failed!\n\n" +
                    "Please check:\n" +
                    "1. XAMPP MySQL/MariaDB is running\n" +
                    "2. Database 'student_data_analysis' exists\n" +
                    "3. Connection settings in config.properties",
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Imports CSV files from the data directory.
     */
    private void importCSVFiles() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Select CSV Directory");
        
        // Set default directory
        fileChooser.setCurrentDirectory(new java.io.File("src/main/resources/data"));
        
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            String directoryPath = fileChooser.getSelectedFile().getAbsolutePath();
            
            // Show progress dialog
            JDialog progressDialog = new JDialog(this, "Importing CSV Files", true);
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            progressDialog.add(new JLabel("Importing data, please wait..."), BorderLayout.NORTH);
            progressDialog.add(progressBar, BorderLayout.CENTER);
            progressDialog.setSize(300, 100);
            progressDialog.setLocationRelativeTo(this);
            
            // Run import in background thread
            SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    return csvImporter.importAllFromDirectory(directoryPath);
                }
                
                @Override
                protected void done() {
                    progressDialog.dispose();
                    try {
                        String importResult = get();
                        JOptionPane.showMessageDialog(MainFrame.this,
                                importResult,
                                "Import Complete",
                                JOptionPane.INFORMATION_MESSAGE);
                        refreshAllData();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(MainFrame.this,
                                "Import failed: " + e.getMessage(),
                                "Import Error",
                                JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }
            };
            
            worker.execute();
            progressDialog.setVisible(true);
        }
    }
    
    /**
     * Refreshes all data in panels.
     */
    private void refreshAllData() {
        dashboardPanel.refreshData();
        dataTablePanel.refreshData();
        chartPanel.refreshCharts();
        JOptionPane.showMessageDialog(this,
                "All data refreshed successfully!",
                "Refresh Complete",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Shows summary statistics dialog.
     */
    private void showSummary() {
        try {
            Map<String, Object> stats = statService.getSummaryStatistics();
            StringBuilder message = new StringBuilder("=== SUMMARY STATISTICS ===\n\n");
            
            for (Map.Entry<String, Object> entry : stats.entrySet()) {
                message.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            
            JOptionPane.showMessageDialog(this,
                    message.toString(),
                    "Summary Statistics",
                    JOptionPane.INFORMATION_MESSAGE);
                    
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading summary: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Shows about dialog.
     */
    private void showAbout() {
        String message = "Student Data Analysis System\n" +
                "Version 1.0.0\n\n" +
                "A Java Swing + JDBC application for analyzing\n" +
                "student admission and performance data.\n\n" +
                "Technologies Used:\n" +
                "- Java 11+\n" +
                "- Swing GUI\n" +
                "- JDBC (MySQL/MariaDB)\n" +
                "- XChart for visualizations\n" +
                "- Apache Commons CSV\n\n" +
                "© 2024 School of Computing & Engineering Sciences";
        
        JOptionPane.showMessageDialog(this,
                message,
                "About",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Exits the application gracefully.
     */
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Close database connection
            DBConnection.getInstance().closeConnection();
            System.exit(0);
        }
    }
}
