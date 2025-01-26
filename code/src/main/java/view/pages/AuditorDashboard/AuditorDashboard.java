package view.pages.AuditorDashboard;

import controller.uiControllers.AuditorDashboard.AuditorDashboardController;
import utils.PageSwitcher;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AuditorDashboard extends JPanel {

    private AuditorDashboardController auditorDashboardController;
    private JButton logoutButton;
    private RequirementAuditorTab requirementAuditorTab = new RequirementAuditorTab();
    private ManagementSystemAuditorTab managementSystemAuditorTab = new ManagementSystemAuditorTab();
    private StandardAuditorTab standardAuditorTab = new StandardAuditorTab();
    private AuditsAuditorTab auditsAuditorTab = new AuditsAuditorTab();

    public AuditorDashboard() {
        setUpUi();
        auditorDashboardController = new AuditorDashboardController(this);
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }

    void setUpUi() {
        // Set the layout manager for the panel
        this.setLayout(new BorderLayout());

        // Set the selected tab color
        UIManager.put("TabbedPane.selected", new Color(128, 108, 166)); // Red color for selected tab

        // Create a top panel for the title and logout button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(98, 78, 136)); // Dark blue background
        topPanel.setPreferredSize(new Dimension(getWidth(), 80));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Add the title
        JLabel titleLabel = new JLabel("Auditor Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.WEST);

        // Add the logout button
        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoutButton.setBackground(new Color(128, 108, 166)); // Red color for logout
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle logout
                PageSwitcher.switchPage("login");
            }
        });
        topPanel.add(logoutButton, BorderLayout.EAST);

        // Create a tabbed pane for the dashboard
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabbedPane.setBackground(new Color(98, 78, 136)); // Dark blue background
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add tabs
        tabbedPane.addTab("Audits", auditsAuditorTab);
        tabbedPane.addTab("Requirements Management", requirementAuditorTab);
        tabbedPane.addTab("System Management", managementSystemAuditorTab);
        tabbedPane.addTab("Standards", standardAuditorTab);

        // Add panels to the main panel
        this.add(topPanel, BorderLayout.NORTH);
        this.add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createTabPanel(String tabName) {
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setBackground(new Color(236, 240, 241)); // Light gray background
        tabPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add content to the tab (customize as needed)
        JLabel label = new JLabel("Welcome to " + tabName);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        label.setForeground(new Color(98, 78, 136)); // Dark blue text
        label.setHorizontalAlignment(SwingConstants.CENTER);
        tabPanel.add(label, BorderLayout.CENTER);

        return tabPanel;
    }

    public static void main(String[] args) {
        // Create a JFrame to display the AuditorDashboard
        JFrame frame = new JFrame("Auditor Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 768);
        frame.setLocationRelativeTo(null); // Center the frame

        // Add the AuditorDashboard panel to the frame
        AuditorDashboard auditorDashboard = new AuditorDashboard();
        frame.add(auditorDashboard);

        // Display the frame
        frame.setVisible(true);
    }
}