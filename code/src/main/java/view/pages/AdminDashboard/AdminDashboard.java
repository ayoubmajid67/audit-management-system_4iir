package view.pages.AdminDashboard;

import controller.uiControllers.adminDashboard.AdminDashboardController;
import utils.PageSwitcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminDashboard extends JPanel {

    private AdminDashboardController adminDashboardController;
    private JButton logoutButton;
    private AuditorManagementTab auditorManagementTab = new AuditorManagementTab();
    private OrganizationManagementTab organizationManagementTab = new OrganizationManagementTab();
    private  ManagementSystemManagementTab managementSystemManagementTab = new ManagementSystemManagementTab();
    private  ResponsibleTab responsibleTab = new ResponsibleTab();
    private  SiteTab siteTab = new SiteTab();
    private  StandardTab StandardTab = new StandardTab();
    private RequirementManagementTab requirementManagementTab = new RequirementManagementTab();
    private  ClauseTab clauseTab = new ClauseTab();
    private  OrgProcessTab OrgProcessTab = new OrgProcessTab();
    private AuditsTab auditsTab = new AuditsTab();
    public AdminDashboard() {
        setUpUi();
        adminDashboardController = new AdminDashboardController(this);
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }

    void setUpUi() {
        // Set the layout manager for the panel
        this.setLayout(new BorderLayout());

        UIManager.put("TabbedPane.selected", new Color(128, 108, 166)); // Change selected tab background color

        // Create a top panel for the title and logout button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(98, 78, 136)); // Dark blue background
        topPanel.setPreferredSize(new Dimension(getWidth(), 80));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Add the title
        JLabel titleLabel = new JLabel("Admin Dashboard");
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
        tabbedPane.addTab("Auditor Management", auditorManagementTab);
        tabbedPane.addTab("Responsible Management", responsibleTab);
        tabbedPane.addTab("Organization Management", organizationManagementTab);
        tabbedPane.addTab("Site Management", siteTab);
        tabbedPane.addTab("OrgProcess Management", OrgProcessTab);
        tabbedPane.addTab("Requirements Management", requirementManagementTab);
        tabbedPane.addTab("Standards Management", StandardTab);
        tabbedPane.addTab("Clause Management", clauseTab);

        tabbedPane.addTab("System Management",managementSystemManagementTab);
        tabbedPane.addTab("Audits Management",auditsTab);

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

    /*public static void main(String[] args) {
        // Create a JFrame to display the AdminDashboard
        JFrame frame = new JFrame("Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 768);
        frame.setLocationRelativeTo(null); // Center the frame

        // Add the AdminDashboard panel to the frame
        AdminDashboard adminDashboard = new AdminDashboard();
        frame.add(adminDashboard);

        // Display the frame
        frame.setVisible(true);
    }*/
}