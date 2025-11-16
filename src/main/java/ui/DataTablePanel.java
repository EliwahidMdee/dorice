package ui;

import analysis.StatService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

/**
 * Data Table Panel for Student Data Analysis System.
 * Displays query results in tabular format with various analysis options.
 * 
 * This panel provides interactive data tables showing different
 * statistical analyses and allows users to switch between views.
 * 
 * @author Student Data Analysis Team
 * @version 1.0
 */
public class DataTablePanel extends JPanel {
    
    private StatService statService;
    private JTable dataTable;
    private JLabel statusLabel;
    private JComboBox<String> analysisComboBox;
    
    /**
     * Constructor - creates the data table panel.
     * 
     * @param statService Service for retrieving statistics
     */
    public DataTablePanel(StatService statService) {
        this.statService = statService;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        createControlPanel();
        createTablePanel();
        createStatusBar();
        
        // Load initial data
        loadDefaultData();
    }
    
    /**
     * Creates the control panel with analysis selector and buttons.
     */
    private void createControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel label = new JLabel("Select Analysis:");
        label.setFont(new Font("Arial", Font.BOLD, 12));
        
        String[] analyses = {
            "Account Balances by Type",
            "Transactions by Type",
            "Branch Distribution",
            "Loan Portfolio Analysis",
            "Account Status Distribution",
            "Top Accounts (10)",
            "Transaction Trends",
            "Card Distribution",
            "Account Activity",
            "Loan Repayment Analysis",
            "Balance Range Analysis",
            "Revenue Analysis"
        };
        
        analysisComboBox = new JComboBox<>(analyses);
        analysisComboBox.addActionListener(e -> loadSelectedAnalysis());
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadSelectedAnalysis());
        
        JButton exportButton = new JButton("Export to CSV");
        exportButton.addActionListener(e -> exportTableToCSV());
        
        controlPanel.add(label);
        controlPanel.add(analysisComboBox);
        controlPanel.add(refreshButton);
        controlPanel.add(exportButton);
        
        add(controlPanel, BorderLayout.NORTH);
    }
    
    /**
     * Creates the table panel with scrollable table.
     */
    private void createTablePanel() {
        dataTable = new JTable();
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        dataTable.setFont(new Font("Arial", Font.PLAIN, 12));
        dataTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        dataTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Data Results"));
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Creates the status bar at the bottom.
     */
    private void createStatusBar() {
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        
        statusLabel = new JLabel("Ready");
        statusPanel.add(statusLabel);
        
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Loads the default analysis on panel initialization.
     */
    private void loadDefaultData() {
        loadSelectedAnalysis();
    }
    
    /**
     * Loads data based on selected analysis type.
     */
    private void loadSelectedAnalysis() {
        String selected = (String) analysisComboBox.getSelectedItem();
        statusLabel.setText("Loading: " + selected + "...");
        
        SwingWorker<DefaultTableModel, Void> worker = new SwingWorker<DefaultTableModel, Void>() {
            @Override
            protected DefaultTableModel doInBackground() throws Exception {
                return getDataForAnalysis(selected);
            }
            
            @Override
            protected void done() {
                try {
                    DefaultTableModel model = get();
                    dataTable.setModel(model);
                    
                    // Auto-resize columns
                    for (int i = 0; i < dataTable.getColumnCount(); i++) {
                        dataTable.getColumnModel().getColumn(i).setPreferredWidth(150);
                    }
                    
                    statusLabel.setText("Loaded: " + selected + " (" + 
                            model.getRowCount() + " rows)");
                            
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(DataTablePanel.this,
                            "Error loading data: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    statusLabel.setText("Error loading data");
                    e.printStackTrace();
                }
            }
        };
        
        worker.execute();
    }
    
    /**
     * Gets table model for specified analysis type.
     * 
     * @param analysisType Type of analysis to perform
     * @return TableModel with query results
     * @throws SQLException if query fails
     */
    private DefaultTableModel getDataForAnalysis(String analysisType) throws SQLException {
        switch (analysisType) {
            case "Account Balances by Type":
                return statService.getAccountBalancesByType();
            case "Transactions by Type":
                return statService.getTransactionsByType();
            case "Branch Distribution":
                return statService.getBranchDistribution();
            case "Loan Portfolio Analysis":
                return statService.getLoanPortfolioAnalysis();
            case "Account Status Distribution":
                return statService.getStatusDistribution();
            case "Top Accounts (10)":
                return statService.getTopAccounts(10);
            case "Transaction Trends":
                return statService.getTransactionTrends();
            case "Card Distribution":
                return statService.getCardDistribution();
            case "Account Activity":
                return statService.getAccountActivity();
            case "Loan Repayment Analysis":
                return statService.getLoanRepaymentAnalysis();
            case "Balance Range Analysis":
                return statService.getBalanceRangeAnalysis();
            case "Revenue Analysis":
                return statService.getRevenueAnalysis();
            default:
                return new DefaultTableModel();
        }
    }
    
    /**
     * Exports current table data to CSV file.
     */
    private void exportTableToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export to CSV");
        fileChooser.setSelectedFile(new java.io.File("export.csv"));
        
        int result = fileChooser.showSaveDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                java.io.PrintWriter writer = new java.io.PrintWriter(file);
                
                DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
                
                // Write headers
                for (int i = 0; i < model.getColumnCount(); i++) {
                    writer.print(model.getColumnName(i));
                    if (i < model.getColumnCount() - 1) writer.print(",");
                }
                writer.println();
                
                // Write data
                for (int row = 0; row < model.getRowCount(); row++) {
                    for (int col = 0; col < model.getColumnCount(); col++) {
                        Object value = model.getValueAt(row, col);
                        writer.print(value != null ? value.toString() : "");
                        if (col < model.getColumnCount() - 1) writer.print(",");
                    }
                    writer.println();
                }
                
                writer.close();
                
                JOptionPane.showMessageDialog(this,
                        "Data exported successfully to:\n" + file.getAbsolutePath(),
                        "Export Success",
                        JOptionPane.INFORMATION_MESSAGE);
                        
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error exporting data: " + e.getMessage(),
                        "Export Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Refreshes the current data view.
     */
    public void refreshData() {
        loadSelectedAnalysis();
    }
}
