package ui;

import analysis.StatService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.Map;

/**
 * Dashboard Panel for Bank Data Analysis System.
 * Displays summary statistics and key metrics in card format.
 * 
 * This panel provides an at-a-glance view of important statistics
 * using visual cards and summary information.
 * 
 * @author Bank Data Analysis Team
 * @version 1.0
 */
public class DashboardPanel extends JPanel {
    
    private StatService statService;
    private JPanel cardsPanel;
    private JTextArea insightsArea;
    
    /**
     * Constructor - creates the dashboard panel.
     * 
     * @param statService Service for retrieving statistics
     */
    public DashboardPanel(StatService statService) {
        this.statService = statService;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        createHeader();
        createCardsPanel();
        createInsightsPanel();
        
        // Load initial data
        refreshData();
    }
    
    /**
     * Creates the header section with title and refresh button.
     */
    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Dashboard - Summary Statistics");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshData());
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(refreshButton, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
    }
    
    /**
     * Creates the panel with summary statistic cards.
     */
    private void createCardsPanel() {
        cardsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Key Metrics",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)));
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Creates the insights panel for displaying analysis insights.
     */
    private void createInsightsPanel() {
        JPanel insightsPanel = new JPanel(new BorderLayout());
        insightsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Insights & Analysis",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)));
        
        insightsArea = new JTextArea(8, 40);
        insightsArea.setEditable(false);
        insightsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        insightsArea.setLineWrap(true);
        insightsArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(insightsArea);
        insightsPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(insightsPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Creates a statistic card component.
     * 
     * @param title Card title
     * @param value Card value
     * @param color Background color
     * @return JPanel representing the card
     */
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(5, 5));
        card.setBackground(color);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 2),
                new EmptyBorder(15, 15, 15, 15)));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 36));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    /**
     * Refreshes all dashboard data from the database.
     */
    public void refreshData() {
        try {
            // Get summary statistics
            Map<String, Object> stats = statService.getSummaryStatistics();
            
            // Clear existing cards
            cardsPanel.removeAll();
            
            // Define colors for cards
            Color[] colors = {
                new Color(52, 152, 219),  // Blue
                new Color(46, 204, 113),  // Green
                new Color(155, 89, 182),  // Purple
                new Color(52, 73, 94),    // Dark Blue
                new Color(230, 126, 34),  // Orange
                new Color(231, 76, 60)    // Red
            };
            
            // Create cards for each statistic
            int colorIndex = 0;
            for (Map.Entry<String, Object> entry : stats.entrySet()) {
                String value = entry.getValue() != null ? entry.getValue().toString() : "0";
                JPanel card = createStatCard(
                        entry.getKey(),
                        value,
                        colors[colorIndex % colors.length]);
                cardsPanel.add(card);
                colorIndex++;
            }
            
            // Generate insights
            generateInsights(stats);
            
            // Refresh UI
            cardsPanel.revalidate();
            cardsPanel.repaint();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading dashboard data: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Generates insights text based on statistics.
     * 
     * @param stats Map of statistics
     */
    private void generateInsights(Map<String, Object> stats) {
        StringBuilder insights = new StringBuilder();
        insights.append("=== BANK DATA ANALYSIS INSIGHTS ===\n\n");
        
        try {
            // Extract values
            int totalAccounts = getIntValue(stats.get("Total Accounts"));
            double totalBalance = getDoubleValue(stats.get("Total Balance"));
            int totalTransactions = getIntValue(stats.get("Total Transactions"));
            int activeAccounts = getIntValue(stats.get("Active Accounts"));
            int activeLoans = getIntValue(stats.get("Active Loans"));
            int activeCards = getIntValue(stats.get("Active Cards"));
            
            // Generate insights
            insights.append("📊 Banking System Overview:\n");
            insights.append(String.format("   • %d total accounts with total balance of %.2f\n",
                    totalAccounts, totalBalance));
            insights.append(String.format("   • %d completed transactions recorded\n", totalTransactions));
            insights.append(String.format("   • %d accounts currently active (%.1f%%)\n",
                    activeAccounts,
                    totalAccounts > 0 ? (activeAccounts * 100.0 / totalAccounts) : 0));
            
            insights.append("\n📈 Banking Activity Metrics:\n");
            insights.append(String.format("   • %d active loans currently being serviced\n", activeLoans));
            insights.append(String.format("   • %d active cards in circulation\n", activeCards));
            insights.append(String.format("   • Average balance per account: %.2f\n",
                    activeAccounts > 0 ? (totalBalance / activeAccounts) : 0));
            
            if (totalBalance > 100000000) {
                insights.append("   • Excellent overall deposit portfolio\n");
            } else if (totalBalance > 50000000) {
                insights.append("   • Good deposit growth with room for expansion\n");
            } else {
                insights.append("   • Consider deposit mobilization campaigns\n");
            }
            
            insights.append("\n💡 Recommendations:\n");
            if (activeLoans > 0 && totalAccounts > 0 && activeLoans * 100.0 / totalAccounts < 20) {
                insights.append("   • Low loan penetration - consider loan marketing campaigns\n");
            }
            if (activeCards > 0 && totalAccounts > 0 && activeCards * 100.0 / totalAccounts < 30) {
                insights.append("   • Consider promoting card products to customers\n");
            }
            insights.append("   • Regularly review and update account data for accurate analysis\n");
            insights.append("   • Use the Charts tab for detailed visual analysis\n");
            insights.append("   • Export data for external reporting if needed\n");
            
        } catch (Exception e) {
            insights.append("Unable to generate detailed insights.\n");
            insights.append("Please ensure data is loaded correctly.\n");
        }
        
        insightsArea.setText(insights.toString());
    }
    
    /**
     * Helper method to safely extract integer value from Object.
     */
    private int getIntValue(Object obj) {
        if (obj == null) return 0;
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        try {
            return Integer.parseInt(obj.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * Helper method to safely extract double value from Object.
     */
    private double getDoubleValue(Object obj) {
        if (obj == null) return 0.0;
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        try {
            return Double.parseDouble(obj.toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
