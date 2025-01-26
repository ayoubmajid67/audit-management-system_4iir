package view.pages.AdminDashboard;

import controller.uiControllers.adminDashboard.Tabs.OrgProcessController;
import model.Organization.OrgProcess;
import utils.TableConverterUtility;
import utils.ControllersGetter;
import view.components.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

public class OrgProcessTab extends JPanel {

    private JButton createButton = new JButton("Create New Process");
    private ButtonRenderer buttonRenderer = new ButtonRenderer();
    private List<OrgProcess> data;
    private OrgProcessController orgProcessController;
    private static String[] columnNamesCreateEdit = {"IdOrganization", "Name", "Description"};
    DefaultTableModel model;
    JTable orgProcessTable;

    public OrgProcessTab() {
        this.data = ControllersGetter.organizationsController.getAllProcesses(); // Get all processes

        orgProcessController = new OrgProcessController(this);
        setUpUi();
    }

    public static String[] getColumnNamesCreateEdit() {
        return columnNamesCreateEdit;
    }

    public JButton getCreateButton() {
        return createButton;
    }

    public JButton getEditButton() {
        return buttonRenderer.getEditButton();
    }

    public JButton getDeleteButton() {
        return buttonRenderer.getDeleteButton();
    }

    private void setUpUi() {
        // Set the layout manager for the panel
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(236, 240, 241)); // Light gray background
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create a button panel at the top
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(236, 240, 241)); // Light gray background
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Add "Create" button
        createButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        createButton.setBackground(new Color(52, 152, 219)); // Blue color
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        createButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        createButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonPanel.add(createButton);

        // Add the button panel to the top of the tab
        this.add(buttonPanel, BorderLayout.NORTH);

        // Define column names
        String[] columnNames = {"IdOrgProcess", "IdOrganization", "Name", "Description", "Actions"};

        Object[][] tableData = TableConverterUtility.convertToTableData(data, columnNames);

        // Create and return the table model
        model = new DefaultTableModel(tableData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only the "Actions" column (column index 4) is editable {accept event}
                return column == 4;
            }
        };

        orgProcessTable = new JTable(model);
        orgProcessTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        orgProcessTable.setRowHeight(30);
        orgProcessTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        orgProcessTable.getTableHeader().setBackground(new Color(98, 78, 136)); // Dark blue header
        orgProcessTable.getTableHeader().setForeground(Color.WHITE);
        orgProcessTable.setFillsViewportHeight(true);

        // Add action buttons (Edit and Delete) to each row
        TableColumn actionsColumn = orgProcessTable.getColumnModel().getColumn(4);
        actionsColumn.setCellRenderer(buttonRenderer);
        actionsColumn.setCellEditor(new ButtonEditor(new JCheckBox(), orgProcessTable, orgProcessController.getIButtonEditorEventsHandler()));

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(orgProcessTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshTable() {
        // Fetch the latest data
        data = ControllersGetter.organizationsController.getAllProcesses();
        System.out.println(data);
        // Clear the existing table data
        model.setRowCount(0);

        // Add the new data to the table
        for (OrgProcess process : data) {
            Object[] rowData = {
                    process.getIdOrgProcess(),
                    process.getIdOrganization(), // Include the organization ID
                    process.getName(),
                    process.getDescription(),
                    "Actions" // Placeholder for the action buttons
            };
            model.addRow(rowData);
        }

        TableColumn actionsColumn = orgProcessTable.getColumnModel().getColumn(4);
        orgProcessTable.removeColumn(actionsColumn);

        // Recreate the "Actions" column with a new ButtonRenderer and ButtonEditor
        actionsColumn = new TableColumn(4);
        actionsColumn.setHeaderValue("Actions");
        actionsColumn.setCellRenderer(new ButtonRenderer());
        actionsColumn.setCellEditor(new ButtonEditor(new JCheckBox(), orgProcessTable, orgProcessController.getIButtonEditorEventsHandler()));

        // Re-add the "Actions" column to the table
        orgProcessTable.addColumn(actionsColumn);

        // Repaint the table to reflect the changes
        orgProcessTable.repaint();
    }

    public static void main(String[] args) {
        // Create a JFrame to display the OrgProcessTab
        JFrame frame = new JFrame("Process Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 768);
        frame.setLocationRelativeTo(null); // Center the frame

        // Add the OrgProcessTab panel to the frame
        OrgProcessTab orgProcessTab = new OrgProcessTab();
        frame.add(orgProcessTab);

        // Display the frame
        frame.setVisible(true);
    }
}