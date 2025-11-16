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
 * Chart Panel for Student Data Analysis System.
 * Displays various charts and graphs using XChart library.
 * 
 * This panel provides visual representations of data including
 * bar charts, pie charts, and line charts.
 * 
 * @author Student Data Analysis Team
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
            "Admissions by Program (Bar)",
            "Admissions by Year (Line)",
            "Status Distribution (Pie)",
            "Department Analysis (Bar)",
            "Gender Distribution (Pie)",
            "Score Range Analysis (Bar)",
            "Program Trends (Line)"
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
            case "Admissions by Program (Bar)":
                return createAdmissionsByProgramChart();
            case "Admissions by Year (Line)":
                return createAdmissionsByYearChart();
            case "Status Distribution (Pie)":
                return createStatusDistributionChart();
            case "Department Analysis (Bar)":
                return createDepartmentAnalysisChart();
            case "Gender Distribution (Pie)":
                return createGenderDistributionChart();
            case "Score Range Analysis (Bar)":
                return createScoreRangeChart();
            case "Program Trends (Line)":
                return createProgramTrendsChart();
            default:
                return createAdmissionsByProgramChart();
        }
    }
    
    /**
     * Creates bar chart for admissions by program.
     */
    private XChartPanel<CategoryChart> createAdmissionsByProgramChart() throws SQLException {
        DefaultTableModel model = statService.getAdmissionsByProgram();
        
        CategoryChart chart = new CategoryChartBuilder()
                .width(800).height(600)
                .title("Admissions by Program")
                .xAxisTitle("Program")
                .yAxisTitle("Total Admissions")
                .theme(Styler.ChartTheme.XChart)
                .build();
        
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setXAxisLabelRotation(45);
        
        List<String> programs = new ArrayList<>();
        List<Number> admissions = new ArrayList<>();
        
        for (int i = 0; i < model.getRowCount(); i++) {
            String programName = (String) model.getValueAt(i, 0);
            Number admissionCount = (Number) model.getValueAt(i, 2);
            
            // Truncate long program names
            if (programName.length() > 20) {
                programName = programName.substring(0, 17) + "...";
            }
            
            programs.add(programName);
            admissions.add(admissionCount);
        }
        
        chart.addSeries("Admissions", programs, admissions);
        
        return new XChartPanel<>(chart);
    }
    
    /**
     * Creates line chart for admissions by year.
     */
    private XChartPanel<XYChart> createAdmissionsByYearChart() throws SQLException {
        DefaultTableModel model = statService.getAdmissionsByYear();
        
        XYChart chart = new XYChartBuilder()
                .width(800).height(600)
                .title("Admission Trends by Year")
                .xAxisTitle("Year")
                .yAxisTitle("Count")
                .theme(Styler.ChartTheme.XChart)
                .build();
        
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setMarkerSize(8);
        
        List<Double> years = new ArrayList<>();
        List<Double> totalAdmissions = new ArrayList<>();
        List<Double> activeCount = new ArrayList<>();
        
        for (int i = 0; i < model.getRowCount(); i++) {
            years.add(((Number) model.getValueAt(i, 0)).doubleValue());
            totalAdmissions.add(((Number) model.getValueAt(i, 1)).doubleValue());
            activeCount.add(((Number) model.getValueAt(i, 3)).doubleValue());
        }
        
        chart.addSeries("Total Admissions", years, totalAdmissions);
        chart.addSeries("Active Students", years, activeCount);
        
        return new XChartPanel<>(chart);
    }
    
    /**
     * Creates pie chart for status distribution.
     */
    private XChartPanel<PieChart> createStatusDistributionChart() throws SQLException {
        DefaultTableModel model = statService.getStatusDistribution();
        
        PieChart chart = new PieChartBuilder()
                .width(800).height(600)
                .title("Student Status Distribution")
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
     * Creates bar chart for department analysis.
     */
    private XChartPanel<CategoryChart> createDepartmentAnalysisChart() throws SQLException {
        DefaultTableModel model = statService.getDepartmentAnalysis();
        
        CategoryChart chart = new CategoryChartBuilder()
                .width(800).height(600)
                .title("Department Analysis")
                .xAxisTitle("Department")
                .yAxisTitle("Count")
                .theme(Styler.ChartTheme.XChart)
                .build();
        
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setXAxisLabelRotation(45);
        
        List<String> departments = new ArrayList<>();
        List<Number> programs = new ArrayList<>();
        List<Number> admissions = new ArrayList<>();
        
        for (int i = 0; i < model.getRowCount(); i++) {
            String dept = (String) model.getValueAt(i, 0);
            if (dept.length() > 25) {
                dept = dept.substring(0, 22) + "...";
            }
            departments.add(dept);
            programs.add((Number) model.getValueAt(i, 1));
            admissions.add((Number) model.getValueAt(i, 2));
        }
        
        chart.addSeries("Programs", departments, programs);
        chart.addSeries("Admissions", departments, admissions);
        
        return new XChartPanel<>(chart);
    }
    
    /**
     * Creates pie chart for gender distribution.
     */
    private XChartPanel<PieChart> createGenderDistributionChart() throws SQLException {
        DefaultTableModel model = statService.getGenderDistribution();
        
        PieChart chart = new PieChartBuilder()
                .width(800).height(600)
                .title("Gender Distribution in Admissions")
                .theme(Styler.ChartTheme.XChart)
                .build();
        
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        
        for (int i = 0; i < model.getRowCount(); i++) {
            String gender = (String) model.getValueAt(i, 0);
            Number count = (Number) model.getValueAt(i, 1);
            String label = gender.equals("M") ? "Male" : "Female";
            chart.addSeries(label, count);
        }
        
        return new XChartPanel<>(chart);
    }
    
    /**
     * Creates bar chart for score range analysis.
     */
    private XChartPanel<CategoryChart> createScoreRangeChart() throws SQLException {
        DefaultTableModel model = statService.getScoreRangeAnalysis();
        
        CategoryChart chart = new CategoryChartBuilder()
                .width(800).height(600)
                .title("Admissions by Score Range")
                .xAxisTitle("Score Range")
                .yAxisTitle("Student Count")
                .theme(Styler.ChartTheme.XChart)
                .build();
        
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        
        List<String> ranges = new ArrayList<>();
        List<Number> counts = new ArrayList<>();
        
        for (int i = 0; i < model.getRowCount(); i++) {
            ranges.add((String) model.getValueAt(i, 0));
            counts.add((Number) model.getValueAt(i, 1));
        }
        
        chart.addSeries("Students", ranges, counts);
        
        return new XChartPanel<>(chart);
    }
    
    /**
     * Creates line chart for program trends.
     */
    private XChartPanel<XYChart> createProgramTrendsChart() throws SQLException {
        DefaultTableModel model = statService.getProgramTrends();
        
        XYChart chart = new XYChartBuilder()
                .width(800).height(600)
                .title("Program Enrollment Trends")
                .xAxisTitle("Year")
                .yAxisTitle("Admissions")
                .theme(Styler.ChartTheme.XChart)
                .build();
        
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setMarkerSize(6);
        
        // Group data by program
        Map<String, List<Double>> programYears = new LinkedHashMap<>();
        Map<String, List<Double>> programCounts = new LinkedHashMap<>();
        
        for (int i = 0; i < model.getRowCount(); i++) {
            String program = (String) model.getValueAt(i, 0);
            Double year = ((Number) model.getValueAt(i, 1)).doubleValue();
            Double count = ((Number) model.getValueAt(i, 2)).doubleValue();
            
            programYears.computeIfAbsent(program, k -> new ArrayList<>()).add(year);
            programCounts.computeIfAbsent(program, k -> new ArrayList<>()).add(count);
        }
        
        // Add series for each program (limit to top 5)
        int seriesCount = 0;
        for (Map.Entry<String, List<Double>> entry : programYears.entrySet()) {
            if (seriesCount++ >= 5) break;
            
            String programName = entry.getKey();
            if (programName.length() > 20) {
                programName = programName.substring(0, 17) + "...";
            }
            
            chart.addSeries(programName, entry.getValue(), programCounts.get(entry.getKey()));
        }
        
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
