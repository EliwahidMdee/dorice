package app;

import ui.MainFrame;

import javax.swing.*;

/**
 * Main Application Entry Point for Bank Data Analysis System.
 * Launches the Swing GUI application.
 * 
 * This is the starting point of the application that initializes
 * and displays the main application window.
 * 
 * @author Bank Data Analysis Team
 * @version 1.0
 */
public class MainApp {
    
    /**
     * Main method - entry point of the application.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Print startup banner
        printBanner();
        
        // Run GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel for native appearance
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                // Create and display main frame
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
                
                System.out.println("Application started successfully!");
                System.out.println("GUI is now running...");
                
            } catch (Exception e) {
                System.err.println("Error starting application: " + e.getMessage());
                e.printStackTrace();
                
                JOptionPane.showMessageDialog(null,
                        "Failed to start application:\n" + e.getMessage(),
                        "Application Error",
                        JOptionPane.ERROR_MESSAGE);
                
                System.exit(1);
            }
        });
    }
    
    /**
     * Prints application startup banner to console.
     */
    private static void printBanner() {
        System.out.println("============================================================");
        System.out.println("  BANK DATA ANALYSIS SYSTEM");
        System.out.println("  Version 1.0.0");
        System.out.println("============================================================");
        System.out.println("  Banking Transaction & Account Management");
        System.out.println("  Object Oriented Programming - Semester Project");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("Starting application...");
        System.out.println();
    }
}
