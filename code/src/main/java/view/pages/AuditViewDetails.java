package view.pages;

import controller.businessControllers.AuditsController;
import model.Accounts.Account;
import model.SystemManagement.Requirement;
import model.SystemManagement.Standard.Standard;
import model.audit.Audit;
import model.Organization.Organization;
import model.SystemManagement.ManagementSystem;
import model.audit.RequirementStat;
import model.audit.StandardStat;
import utils.ControllersGetter;
import utils.PageSwitcher; // Import the PageSwitcher utility

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AuditViewDetails extends JPanel {

    private Audit audit;
    private Organization organization;
    private Account auditor;
    private ManagementSystem managementSystem;
    private String getBackPage;
    private AuditsController auditsController= new AuditsController();
 private  String idAudit;
    public AuditViewDetails() {
        // Default constructor
    }

    public AuditViewDetails(String idAudit, String getBackPage) {
        loadAuditDetails(idAudit, getBackPage);
    }

    public void loadAuditDetails(String idAudit, String getBackPage) {
        try {
            this.getBackPage = getBackPage;
            this.idAudit = idAudit;
            refreshPage();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading audit details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fetchData() throws Exception {
        // Fetch audit details
        audit = ControllersGetter.auditsController.getAuditById(idAudit);
        if (audit == null) {
            throw new Exception("Audit not found with ID: " + idAudit);
        }

        // Fetch related entities
        organization = ControllersGetter.organizationsController.getOrganizationById(audit.getIdOrganization());
        if (organization == null) {
            throw new Exception("Organization not found with ID: " + audit.getIdOrganization());
        }

        // Fetch auditor details with try-catch
        try {
            auditor = ControllersGetter.accountsController.getAuditorById(audit.getIdAuditor());
        } catch (Exception e) {
            throw new Exception("Error fetching auditor: " + e.getMessage());
        }

        managementSystem = ControllersGetter.organizationsController.getSystemManagementById(
                audit.getIdOrganization(), audit.getIdSystemManagement());
        System.out.println(managementSystem);
        if (managementSystem == null) {
            throw new Exception("Management System not found with ID: " + audit.getIdSystemManagement());
        }

        // Populate StandardsStat if empty
        if (audit.getStandardsStat().isEmpty() && managementSystem.getStandards() != null) {
            List<StandardStat> standardsStat = new ArrayList<>();
            for (Standard standard : managementSystem.getStandards()) {
                StandardStat standardStat = new StandardStat();
                standardStat.setStatus("notYet"); // Default status
                standardsStat.add(standardStat);
            }
            audit.setStandardsStat(standardsStat);
        }

        // Populate RequirementsStat if empty
        if (audit.getRequirementsStat().isEmpty() && managementSystem.getRequirements() != null) {
            List<RequirementStat> requirementsStat = new ArrayList<>();
            for (Requirement requirement : managementSystem.getRequirements()) {
                RequirementStat requirementStat = new RequirementStat();
                requirementStat.setStatus("notYet"); // Default status
                requirementsStat.add(requirementStat);
            }
            audit.setRequirementsStat(requirementsStat);
        }
    }
    private void setUpUi() {
        // Set the layout manager for the panel
        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 241)); // Light gray background

        // Create a main panel to hold all components
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Apply border to mainPanel

        // Create a top panel for the dynamic button/label
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Align button/label to the right
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // Add spacing below the button/label

        // Add dynamic button/label based on audit status
        String auditStatus = audit.getStatus(); // Get the audit status

        if (getBackPage.equals("adminDashboard")) {
            addAuditStatLabel(topPanel, auditStatus);
        } else {
            switch (auditStatus.toLowerCase()) {
                case "pending":
                    addLaunchAuditButton(topPanel);
                    break;
                case "in progress":
                    addCompleteAuditButton(topPanel);
                    break;
                case "completed":
                    addCompletedLabel(topPanel);
                    break;
                default:
                    // Handle unknown status (optional)
                    JLabel unknownLabel = new JLabel("Unknown Status: " + auditStatus);
                    unknownLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    unknownLabel.setForeground(Color.RED);
                    topPanel.add(unknownLabel);
                    break;
            }
        }

        // Add the top panel to the main panel
        mainPanel.add(topPanel);

        // Add audit details
        addSection(mainPanel, "Audit Details", new String[][]{
                {"ID", audit.getIdAudit()},
                {"Start Date", audit.getDateDebut()},
                {"Expiration Date", audit.getExpDate()},
                {"Subject", audit.getSubject()},
                {"Status", audit.getStatus()},
                {"Take Certificate", String.valueOf(audit.isTakeCertificate())},
                {"Is Pass", audit.getIsPass()}
        });

        // Add final report details
        addSection(mainPanel, "Final Report", new String[][]{
                {"Report ID", audit.getFinalReport().getIdRapport()},
                {"Report URL", audit.getFinalReport().getUrl()}
        });

        // Add organization details
        addSection(mainPanel, "Organization", new String[][]{
                {"ID", organization.getIdOrganization()},
                {"Name", organization.getName()},
                {"Description", organization.getDescription()}
        });

        // Add auditor details
        addSection(mainPanel, "Auditor", new String[][]{
                {"ID", auditor.getIdAccount()},
                {"Name", auditor.getFirstName() + " " + auditor.getLastName()},
                {"Email", auditor.getEmail()}
        });

        // Add management system details
        addSection(mainPanel, "Management System", new String[][]{
                {"ID", managementSystem.getIdManagementSystem()},
                {"Description", managementSystem.getDescription()},
                {"Certificate", managementSystem.getCertificate()}
        });

        // Add requirements section
        if (managementSystem.getRequirements() != null && !managementSystem.getRequirements().isEmpty()) {
            addRequirementsSection(mainPanel, "Requirements", managementSystem.getRequirements());
        }

        // Add standards section
        if (managementSystem.getStandards() != null && !managementSystem.getStandards().isEmpty()) {
            addStandardsSection(mainPanel, "Standards", managementSystem.getStandards());
        }

        // Add the main panel to a scroll pane
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Add a "Back" button at the bottom
        addBackButton();
    }
    private void refreshPage(){
        try {
            auditsController.saveAudits();
            // Remove all components from the panel
            removeAll();
            // Revalidate and repaint the panel to reflect the changes
            revalidate();
            repaint();
            fetchData();
            setUpUi();
        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Error loading audit details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }
    }


    private void addLaunchAuditButton(JPanel topPanel) {
        // Create the "Launch Audit" button
        JButton launchAuditButton = new JButton("Launch Audit");
        launchAuditButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        launchAuditButton.setBackground(new Color(52, 152, 219)); // Blue color
        launchAuditButton.setForeground(Color.WHITE);
        launchAuditButton.setFocusPainted(false);
        launchAuditButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        launchAuditButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add action listener to the button

        launchAuditButton.addActionListener(e -> {
            // Define what happens when the button is clicked
            boolean auditLaunched = audit.setStatus("in progress");
            auditsController.saveAudits();
            if (auditLaunched) {
                JOptionPane.showMessageDialog(this, "Audit launched successfully!", "Audit Launched", JOptionPane.INFORMATION_MESSAGE);
                // Refresh the UI to reflect the new status
                refreshPage();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to launch audit.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add the button to the top panel
        topPanel.add(launchAuditButton);
    }



    private void addAuditStatLabel(JPanel topPanel, String status) {
        JLabel completedLabel = new JLabel(status);
        completedLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        completedLabel.setForeground(new Color(46, 204, 113)); // Green color
        completedLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));


        topPanel.add(completedLabel);
    }
    private boolean areAllRequirementsAndStandardsChecked() {
        // Check if all requirements are checked
        for (RequirementStat requirementStat : audit.getRequirementsStat()) {
            if ("not yet".equalsIgnoreCase(requirementStat.getStatus())) {
                return false; // Found an unchecked requirement
            }
        }

        // Check if all standards are checked
        for (StandardStat standardStat : audit.getStandardsStat()) {
            if ("not yet".equalsIgnoreCase(standardStat.getStatus())) {
                return false; // Found an unchecked standard
            }
        }

        return true; // All requirements and standards are checked
    }
    private void addCompleteAuditButton(JPanel topPanel) {
        // Create the "Complete Audit" button
        JButton completeAuditButton = new JButton("Complete Audit");
        completeAuditButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        completeAuditButton.setBackground(new Color(46, 204, 113)); // Green color
        completeAuditButton.setForeground(Color.WHITE);
        completeAuditButton.setFocusPainted(false);
        completeAuditButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        completeAuditButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add action listener to the button
        completeAuditButton.addActionListener(e -> {

            if (!areAllRequirementsAndStandardsChecked()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Cannot complete the audit. Some requirements or standards are not yet checked.",
                        "Incomplete Audit",
                        JOptionPane.WARNING_MESSAGE
                );
                return; // Exit the method if validation fails
            }
            // Define what happens when the button is clicked
            boolean auditCompleted = audit.setStatus("completed");
            auditsController.saveAudits();
            if (auditCompleted) {
                // Calculate the audit score
                double auditScore = calculateAuditScore();

                // Show the score in a message dialog
                JOptionPane.showMessageDialog(
                        this,
                        "Audit completed successfully!\nAudit Score: " + String.format("%.2f", auditScore) + "%",
                        "Audit Completed",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // Refresh the UI to reflect the new status and score
                refreshPage();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to complete audit.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add the button to the top panel
        topPanel.add(completeAuditButton);
    }

    private double calculateAuditScore() {
        List<StandardStat> standardsStat = audit.getStandardsStat();
        List<RequirementStat> requirementsStat = audit.getRequirementsStat();

        int totalItems = standardsStat.size() + requirementsStat.size();
        if (totalItems == 0) {
            return 0.0; // Avoid division by zero
        }

        int passedItems = 0;

        // Count passed standards
        for (StandardStat standardStat : standardsStat) {
            if ("checked pass".equalsIgnoreCase(standardStat.getStatus())) {
                passedItems++;
            }
        }

        // Count passed requirements
        for (RequirementStat requirementStat : requirementsStat) {
            if ("checked pass".equalsIgnoreCase(requirementStat.getStatus())) {
                passedItems++;
            }
        }

        // Calculate the percentage
        return (double) passedItems / totalItems * 100;
    }
    private void addCompletedLabel(JPanel topPanel) {
        // Create the "Completed" label
        JLabel completedLabel = new JLabel("Completed");
        completedLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        completedLabel.setForeground(new Color(46, 204, 113)); // Green color
        completedLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Add the label to the top panel
        topPanel.add(completedLabel);

        // Calculate and display the audit score
        double auditScore = calculateAuditScore();
        JLabel scoreLabel = new JLabel("Audit Score: " + String.format("%.2f", auditScore) + "%");
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        scoreLabel.setForeground(new Color(52, 73, 94)); // Dark blue text
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Add the score label to the top panel
        topPanel.add(scoreLabel);
    }
    private boolean launchAudit(String auditId) {
        // Add your logic here to launch the audit
        // Return true if successful, false otherwise
        return true; // Replace with actual logic
    }

    private boolean completeAudit(String auditId) {
        // Add your logic here to complete the audit
        // Return true if successful, false otherwise
        return true; // Replace with actual logic
    }

    private void addBackButton() {
        // Create a "Back" button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setBackground(new Color(98, 78, 136)); // Blue color
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add action listener to the button
        backButton.addActionListener(e -> {
            // Switch back to the main page using PageSwitcher
            PageSwitcher.switchPage(getBackPage);
        });

        // Create a panel to hold the button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(236, 240, 241)); // Light gray background
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add padding
        buttonPanel.add(backButton);

        // Add the button panel to the bottom of the main panel
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addSection(JPanel parent, String title, String[][] data) {
        // Create a section panel
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBackground(Color.WHITE);
        sectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add section title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(98, 78, 136)); // Dark blue text
        sectionPanel.add(titleLabel);

        // Add section data
        for (String[] row : data) {
            JLabel label = new JLabel(row[0] + ": " + row[1]);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            label.setForeground(new Color(98, 78, 136)); // Dark blue text
            sectionPanel.add(label);
        }

        // Add the section panel to the parent panel
        parent.add(sectionPanel);
        parent.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between sections
    }

    private void addRequirementsSection(JPanel parent, String title, List<Requirement> requirements) {
        // Create a section panel
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBackground(Color.WHITE);
        sectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add section title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(98, 78, 136)); // Dark blue text
        sectionPanel.add(titleLabel);

        // Add spacing after the title
        sectionPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Add a card for each requirement
        List<RequirementStat> requirementsStat = audit.getRequirementsStat();
        for (int i = 0; i < requirements.size(); i++) {
            Requirement requirement = requirements.get(i);
            RequirementStat requirementStat = requirementsStat.get(i); // Get corresponding RequirementStat
            JPanel cardPanel = createRequirementCard(requirement, requirementStat);
            sectionPanel.add(cardPanel);
            sectionPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between cards
        }

        // Add the section panel to the parent panel
        parent.add(sectionPanel);
        parent.add(Box.createRigidArea(new Dimension(0, 20))); // Add spacing after the section
    }
    private void handleRequirementStatusUpdate(Requirement requirement, RequirementStat requirementStat, String status) {
        // Define the checkout logic for the requirement
        String requirementId = requirement.getIdRequirement();
        String requirementName = requirement.getName();

        // Example: Show a confirmation dialog
        int option = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to mark the requirement '" + requirementName + "' as " + status + "?",
                "Confirm Status Change",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            // Perform the status change
            requirementStat.setStatus(status);
            boolean statusChangeSuccess = updateRequirementStatus(requirementId, status);

            if (statusChangeSuccess) {
                JOptionPane.showMessageDialog(
                        this,
                        "Requirement '" + requirementName + "' marked as " + status + " successfully!",
                        "Status Updated",
                        JOptionPane.INFORMATION_MESSAGE
                );

                refreshPage(); // Refresh the page to reflect the updated status
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to update status for requirement: " + requirementName,
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    private boolean updateRequirementStatus(String requirementId, String status) {
        // Add your logic here to update the requirement's status in the database
        // Return true if successful, false otherwise
        // Example:
        // return requirementController.updateStatus(requirementId, status);
        return true; // Replace with actual logic
    }
    private JPanel createRequirementCard(Requirement requirement, RequirementStat requirementStat) {
        // Create a card panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(new Color(249, 249, 249)); // Light gray background
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1), // Light border
                BorderFactory.createEmptyBorder(10, 10, 10, 10) // Padding
        ));

        // Add requirement ID
        JLabel idLabel = new JLabel("ID: " + requirement.getIdRequirement());
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        idLabel.setForeground(new Color(98, 78, 136)); // Dark blue text
        cardPanel.add(idLabel);

        // Add requirement name
        JLabel nameLabel = new JLabel("Name: " + requirement.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameLabel.setForeground(new Color(98, 78, 136)); // Dark blue text
        cardPanel.add(nameLabel);

        // Add requirement description
        JLabel descriptionLabel = new JLabel("Description: " + requirement.getDescription());
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descriptionLabel.setForeground(new Color(98, 78, 136)); // Dark blue text
        cardPanel.add(descriptionLabel);

        // Add requirement status from RequirementStat
        JLabel statusLabel = new JLabel("Status: " + requirementStat.getStatus());
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLabel.setForeground(getStatusColor(requirementStat.getStatus())); // Set color based on status
        cardPanel.add(statusLabel);

        // Add buttons only if the user is an auditor and the audit is not completed
        if (ControllersGetter.currentAccountSession.isAuditor() && !audit.getStatus().equals("completed") && !audit.getStatus().equals("pending")) {
            // Create a panel to hold the buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Align buttons to the right
            buttonPanel.setBackground(new Color(249, 249, 249)); // Match card background

            // Add "Pass" button
            JButton passButton = new JButton("Pass");
            passButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            passButton.setBackground(new Color(46, 204, 113)); // Green color
            passButton.setForeground(Color.WHITE);
            passButton.setFocusPainted(false);
            passButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            passButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Add action listener to the "Pass" button
            passButton.addActionListener(e -> {
                handleRequirementStatusUpdate(requirement, requirementStat, "checked pass");
            });

            // Add "Fail" button
            JButton failButton = new JButton("Fail");
            failButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            failButton.setBackground(new Color(231, 76, 60)); // Red color
            failButton.setForeground(Color.WHITE);
            failButton.setFocusPainted(false);
            failButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            failButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Add action listener to the "Fail" button
            failButton.addActionListener(e -> {
                handleRequirementStatusUpdate(requirement, requirementStat, "checked fail");
            });

            // Add buttons to the button panel
            buttonPanel.add(passButton);
            buttonPanel.add(failButton);

            // Add the button panel to the card
            cardPanel.add(buttonPanel);
        }

        return cardPanel;
    }
    private void addStandardsSection(JPanel parent, String title, List<Standard> standards) {
        // Create a section panel
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBackground(Color.WHITE);
        sectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add section title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(98, 78, 136)); // Dark blue text
        sectionPanel.add(titleLabel);

        // Add spacing after the title
        sectionPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Add a card for each standard
        List<StandardStat> standardsStat = audit.getStandardsStat();
        for (int i = 0; i < standards.size(); i++) {
            Standard standard = standards.get(i);
            StandardStat standardStat = standardsStat.get(i); // Get corresponding StandardStat
            JPanel cardPanel = createStandardCard(standard, standardStat);
            sectionPanel.add(cardPanel);
            sectionPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between cards
        }

        // Add the section panel to the parent panel
        parent.add(sectionPanel);
        parent.add(Box.createRigidArea(new Dimension(0, 20))); // Add spacing after the section
    }
    private JPanel createStandardCard(Standard standard, StandardStat standardStat) {
        // Create a card panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(new Color(249, 249, 249)); // Light gray background
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1), // Light border
                BorderFactory.createEmptyBorder(10, 10, 10, 10) // Padding
        ));

        // Add standard ID
        JLabel idLabel = new JLabel("ID: " + standard.getIdStandard());
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        idLabel.setForeground(new Color(98, 78, 136)); // Dark blue text
        cardPanel.add(idLabel);

        // Add standard name
        JLabel nameLabel = new JLabel("Name: " + standard.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameLabel.setForeground(new Color(98, 78, 136)); // Dark blue text
        cardPanel.add(nameLabel);

        // Add standard description
        JLabel descriptionLabel = new JLabel("Description: " + standard.getDescription());
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descriptionLabel.setForeground(new Color(98, 78, 136)); // Dark blue text
        cardPanel.add(descriptionLabel);

        // Add standard status from StandardStat
        JLabel statusLabel = new JLabel("Status: " + standardStat.getStatus());
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLabel.setForeground(getStatusColor(standardStat.getStatus())); // Set color based on status
        cardPanel.add(statusLabel);

        // Add buttons only if the user is an auditor and the audit is not completed
        if (ControllersGetter.currentAccountSession.isAuditor() && !audit.getStatus().equals("completed") && !audit.getStatus().equals("pending")) {
            // Create a panel to hold the buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Align buttons to the right
            buttonPanel.setBackground(new Color(249, 249, 249)); // Match card background

            // Add "Pass" button
            JButton passButton = new JButton("Pass");
            passButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            passButton.setBackground(new Color(46, 204, 113)); // Green color
            passButton.setForeground(Color.WHITE);
            passButton.setFocusPainted(false);
            passButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            passButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Add action listener to the "Pass" button
            passButton.addActionListener(e -> {
                handleCheckout(standard, standardStat, "checked pass");
            });

            // Add "Fail" button
            JButton failButton = new JButton("Fail");
            failButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            failButton.setBackground(new Color(231, 76, 60)); // Red color
            failButton.setForeground(Color.WHITE);
            failButton.setFocusPainted(false);
            failButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            failButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Add action listener to the "Fail" button
            failButton.addActionListener(e -> {
                handleCheckout(standard, standardStat, "checked fail");
            });

            // Add buttons to the button panel
            buttonPanel.add(passButton);
            buttonPanel.add(failButton);

            // Add the button panel to the card
            cardPanel.add(buttonPanel);
        }

        return cardPanel;
    }
    private Color getStatusColor(String status) {
        // Return a color based on the status
        switch (status.toLowerCase()) {
            case "checked pass":
                return new Color(46, 204, 113); // Green
            case "checked fail":
                return new Color(231, 76, 60); // Red
            case "not yet":
                return new Color(241, 196, 15); // Yellow
            default:
                return Color.BLACK; // Default color
        }
    }
    private void handleCheckout(Standard standard, StandardStat standardStat, String status) {
        // Define the checkout logic for the standard
        String standardId = standard.getIdStandard();
        String standardName = standard.getName();

        // Example: Show a confirmation dialog
        int option = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to mark the standard '" + standardName + "' as " + status + "?",
                "Confirm Status Change",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            // Perform the status change
            standardStat.setStatus(status);
            boolean statusChangeSuccess = updateStandardStatus(standardId, status);

            if (statusChangeSuccess) {
                JOptionPane.showMessageDialog(
                        this,
                        "Standard '" + standardName + "' marked as " + status + " successfully!",
                        "Status Updated",
                        JOptionPane.INFORMATION_MESSAGE
                );
                refreshPage(); // Refresh the page to reflect the updated status
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to update status for standard: " + standardName,
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    private boolean updateStandardStatus(String standardId, String status) {
        // Add your logic here to update the standard's status in the database
        // Return true if successful, false otherwise
        // Example:
        // return standardController.updateStatus(standardId, status);
        return true; // Replace with actual logic
    }

    public static void main(String[] args) {

        // Example usage
        JFrame frame = new JFrame("Audit Details");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800); // Set the size of the frame
        frame.setLocationRelativeTo(null); // Center the frame

        // Create and add the AuditViewDetails panel to the frame
        AuditViewDetails auditViewDetails = new AuditViewDetails("665e8952-9b4a-4b5f-84e5-2b083b97242f", "MainPage");
        frame.add(auditViewDetails);

        // Display the frame
        frame.setVisible(true);
    }
}