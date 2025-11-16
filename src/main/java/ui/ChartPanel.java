package ui;

import analysis.StatService;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

/**
 * Chart Panel for Bank Data Analysis System.
 * Displays various charts and graphs using XChart library.
 * 
 * This panel provides visual representations of banking data including
 * bar charts, pie charts, and line charts.
 * 
 * @author Bank Data Analysis Team
 * @version 1.0
 */
public class ChartPanel extends JPanel {
    
    private StatService statService;
    private JPanel chartDisplayPanel;
    private JComboBox<String> chartTypeComboBox;
    private XChartPanel<? extends org.knowm.xchart.internal.chartpart.Chart> currentChartPanel;
    
    /**
     * Constructor - creates the chart panel.
     * 
     * @param statService Service for retrieving statistics
     */
    public ChartPanel(StatService statService) {
        this.statService = statService;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        createControlPanel();
        createChartDisplayPanel();
        
        // Load default chart
        loadDefaultChart();
    }
    
    /**
     * Creates the control panel with chart selector and buttons.
     */
    private void createControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel label = new JLabel("Select Chart:");
        label.setFont(new Font("Arial", Font.BOLD, 12));
        
        String[] chartTypes = {
            "Account Balances by Type (Bar)",
            "Transaction Trends (Line)",
            "Account Status Distribution (Pie)",
            "Branch Distribution (Bar)",
            "Transaction Types (Pie)",
            "Balance Range Analysis (Bar)",
            "Loan Portfolio (Bar)"
        };
        
        chartTypeComboBox = new JComboBox<>(chartTypes);
        chartTypeComboBox.addActionListener(e -> loadSelectedChart());
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadSelectedChart());
        
        JButton exportButton = new JButton("Save as PNG");
        exportButton.addActionListener(e -> exportChart());
        
        controlPanel.add(label);
        controlPanel.add(chartTypeComboBox);
        controlPanel.add(refreshButton);
        controlPanel.add(exportButton);
        
        add(controlPanel, BorderLayout.NORTH);
    }
    
    /**
     * Creates the chart display panel.
     */
    private void createChartDisplayPanel() {
        chartDisplayPanel = new JPanel(new BorderLayout());
        chartDisplayPanel.setBorder(BorderFactory.createTitledBorder("Chart Visualization"));
        
        add(chartDisplayPanel, BorderLayout.CENTER);
    }
    
    /**
     * Loads the default chart on initialization.
     */
    private void loadDefaultChart() {
        loadSelectedChart();
    }
    
    /**
     * Loads the selected chart type.
     */
    private void loadSelectedChart() {
        String selected = (String) chartTypeComboBox.getSelectedItem();
        
        SwingWorker<XChartPanel<? extends org.knowm.xchart.internal.chartpart.Chart>, Void> worker = 
                new SwingWorker<XChartPanel<? extends org.knowm.xchart.internal.chartpart.Chart>, Void>() {
            
            @Override
            protected XChartPanel<? extends org.knowm.xchart.internal.chartpart.Chart> doInBackground() 
                    throws Exception {
                return createChartForType(selected);
            }
            
            @Override
            protected void done() {
                try {
                    if (currentChartPanel != null) {
                        chartDisplayPanel.remove(currentChartPanel);
                    }
                    
                    currentChartPanel = get();
                    chartDisplayPanel.add(currentChartPanel, BorderLayout.CENTER);
                    chartDisplayPanel.revalidate();
                    chartDisplayPanel.repaint();
                    
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(ChartPanel.this,
                            "Error creating chart: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        };
        
        worker.execute();
    }
    
    /**
     * Creates chart panel based on selected type.
     */
    private XChartPanel<? extends org.knowm.xchart.internal.chartpart.Chart> createChartForType(String chartType) 
            throws SQLException {
        
        switch (chartType) {
            case "Account Balances by Type (Bar)":
                return createAccountBalancesByTypeChart();
            case "Transaction Trends (Line)":
                return createTransactionTrendsChart();
            case "Account Status Distribution (Pie)":
                return createStatusDistributionChart();
            case "Branch Distribution (Bar)":
                return createBranchDistributionChart();
            case "Transaction Types (Pie)":
                return createTransactionTypesPieChart();
            case "Balance Range Analysis (Bar)":
                return createBalanceRangeChart();
            case "Loan Portfolio (Bar)":
                return createLoanPortfolioChart();
            default:
                return createAccountBalancesByTypeChart();
        }
    }
    
    /**
     * Creates bar chart for account balances by type.
     */
    private XChartPanel<CategoryChart> createAccountBalancesByTypeChart() throws SQLException {
        DefaultTableModel model = statService.getAccountBalancesByType();
        
        CategoryChart chart = new CategoryChartBuilder()
                .width(800).height(600)
                .title("Account Balances by Type")
                .xAxisTitle("Account Type")
                .yAxisTitle("Total Balance")
                .theme(Styler.ChartTheme.XChart)
                .build();
        
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setXAxisLabelRotation(45);
        
        List<String> accountTypes = new ArrayList<>();
        List<Number> balances = new ArrayList<>();
        
        for (int i = 0; i < model.getRowCount(); i++) {
            accountTypes.add((String) model.getValueAt(i, 0));
            balances.add((Number) model.getValueAt(i, 2));
        }
        
        chart.addSeries("Total Balance", accountTypes, balances);
        
        return new XChartPanel<>(chart);
    }
    
    /**
     * Creates line chart for transaction trends.
     */
    private XChartPanel<XYChart> createTransactionTrendsChart() throws SQLException {
        DefaultTableModel model = statService.getTransactionTrends();
        
        XYChart chart = new XYChartBuilder()
                .width(800).height(600)
                .title("Transaction Trends Over Time")
                .xAxisTitle("Month")
                .yAxisTitle("Amount")
                .theme(Styler.ChartTheme.XChart)
                .build();
        
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setMarkerSize(8);
        chart.getStyler().setXAxisLabelRotation(45);
        
        List<Double> months = new ArrayList<>();
        List<Double> totalTransactions = new ArrayList<>();
        
        for (int i = 0; i < model.getRowCount(); i++) {
            months.add((double) i);
            totalTransactions.add(((Number) model.getValueAt(i, 1)).doubleValue());
        }
        
        chart.addSeries("Total Transactions", months, totalTransactions);
        
        return new XChartPanel<>(chart);
    }
    
    /**
     * Creates pie chart for account status distribution.
     */
    private XChartPanel<PieChart> createStatusDistributionChart() throws SQLException {
        DefaultTableModel model = statService.getStatusDistribution();
        
        PieChart chart = new PieChartBuilder()
                .width(800).height(600)
                .title("Account Status Distribution")
                .theme(Styler.ChartTheme.XChart)
                .build();
        
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        
        for (int i = 0; i < model.getRowCount(); i++) {
            String status = (String) model.getValueAt(i, 0);
            Number count = (Number) model.getValueAt(i, 1);
            chart.addSeries(status, count);
        }
        
        return new XChartPanel<>(chart);
    }
    
    /**
     * Creates bar chart for branch distribution.
     */
    private XChartPanel<CategoryChart> createBranchDistributionChart() throws SQLException {
        DefaultTableModel model = statService.getBranchDistribution();
        
        CategoryChart chart = new CategoryChartBuilder()
                .width(800).height(600)
                .title("Branch-wise Account Distribution")
                .xAxisTitle("Branch")
                .yAxisTitle("Count")
                .theme(Styler.ChartTheme.XChart)
                .build();
        
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setXAxisLabelRotation(45);
        
        List<String> branches = new ArrayList<>();
        List<Number> accountCounts = new ArrayList<>();
        List<Number> activeAccounts = new ArrayList<>();
        
        for (int i = 0; i < model.getRowCount(); i++) {
            branches.add((String) model.getValueAt(i, 0));
            accountCounts.add((Number) model.getValueAt(i, 1));
            activeAccounts.add((Number) model.getValueAt(i, 4));
        }
        
        chart.addSeries("Total Accounts", branches, accountCounts);
        chart.addSeries("Active Accounts", branches, activeAccounts);
        
        return new XChartPanel<>(chart);
    }
    
    /**
     * Creates pie chart for transaction types distribution.
     */
    private XChartPanel<PieChart> createTransactionTypesPieChart() throws SQLException {
        DefaultTableModel model = statService.getTransactionsByType();
        
        PieChart chart = new PieChartBuilder()
                .width(800).height(600)
                .title("Transaction Types Distribution")
                .theme(Styler.ChartTheme.XChart)
                .build();
        
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        
        for (int i = 0; i < model.getRowCount(); i++) {
            String transType = (String) model.getValueAt(i, 0);
            Number count = (Number) model.getValueAt(i, 1);
            chart.addSeries(transType, count);
        }
        
        return new XChartPanel<>(chart);
    }
    
    /**
     * Creates bar chart for balance range analysis.
     */
    private XChartPanel<CategoryChart> createBalanceRangeChart() throws SQLException {
        DefaultTableModel model = statService.getBalanceRangeAnalysis();
        
        CategoryChart chart = new CategoryChartBuilder()
                .width(800).height(600)
                .title("Accounts by Balance Range")
                .xAxisTitle("Balance Range")
                .yAxisTitle("Account Count")
                .theme(Styler.ChartTheme.XChart)
                .build();
        
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setXAxisLabelRotation(45);
        
        List<String> ranges = new ArrayList<>();
        List<Number> counts = new ArrayList<>();
        
        for (int i = 0; i < model.getRowCount(); i++) {
            ranges.add((String) model.getValueAt(i, 0));
            counts.add((Number) model.getValueAt(i, 1));
        }
        
        chart.addSeries("Accounts", ranges, counts);
        
        return new XChartPanel<>(chart);
    }
    
    /**
     * Creates bar chart for loan portfolio analysis.
     */
    private XChartPanel<CategoryChart> createLoanPortfolioChart() throws SQLException {
        DefaultTableModel model = statService.getLoanPortfolioAnalysis();
        
        CategoryChart chart = new CategoryChartBuilder()
                .width(800).height(600)
                .title("Loan Portfolio by Type")
                .xAxisTitle("Loan Type")
                .yAxisTitle("Total Amount")
                .theme(Styler.ChartTheme.XChart)
                .build();
        
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setXAxisLabelRotation(45);
        
        List<String> loanTypes = new ArrayList<>();
        List<Number> amounts = new ArrayList<>();
        
        for (int i = 0; i < model.getRowCount(); i++) {
            loanTypes.add((String) model.getValueAt(i, 0));
            amounts.add((Number) model.getValueAt(i, 2));
        }
        
        chart.addSeries("Total Loan Amount", loanTypes, amounts);
        
        return new XChartPanel<>(chart);
    }
    
    /**
     * Exports the current chart to PNG file.
     */
    private void exportChart() {
        if (currentChartPanel == null) {
            JOptionPane.showMessageDialog(this,
                    "No chart to export!",
                    "Export Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Chart as PNG");
        fileChooser.setSelectedFile(new java.io.File("chart.png"));
        
        int result = fileChooser.showSaveDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                String path = file.getAbsolutePath();
                
                if (!path.toLowerCase().endsWith(".png")) {
                    path += ".png";
                    file = new java.io.File(path);
                }
                
                BitmapEncoder.saveBitmap(currentChartPanel.getChart(), path, BitmapEncoder.BitmapFormat.PNG);
                
                JOptionPane.showMessageDialog(this,
                        "Chart exported successfully to:\n" + path,
                        "Export Success",
                        JOptionPane.INFORMATION_MESSAGE);
                        
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Error exporting chart: " + e.getMessage(),
                        "Export Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Refreshes all charts.
     */
    public void refreshCharts() {
        loadSelectedChart();
    }
}
