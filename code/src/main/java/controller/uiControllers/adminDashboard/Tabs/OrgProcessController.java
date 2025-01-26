package controller.uiControllers.adminDashboard.Tabs;

import model.Organization.OrgProcess;
import utils.ControllersGetter;
import utils.SaveUtil;
import utils.interfaces.IButtonEditorEventsHandler;
import utils.interfaces.IFormDialogEventHandler;
import utils.interfaces.objectConverter.OrgProcessConverter;
import view.pages.AdminDashboard.ButtonEditor;
import view.pages.AdminDashboard.FormDialog;
import view.pages.AdminDashboard.OrgProcessTab;

import javax.swing.*;

public class OrgProcessController {
    private OrgProcessTab view;
    private FormDialog createOrgProcessForm;
    private FormDialog editOrgProcessForm;
    private String[] columnNames = OrgProcessTab.getColumnNamesCreateEdit();
    private SaveUtil<OrgProcess> saveUtil = new SaveUtil(new OrgProcessConverter());

    public OrgProcessController(OrgProcessTab view) {
        this.view = view;
        initController();
    }

    private void initController() {
        addCreateOrgProcessButtonEvent();
    }

    private void addCreateOrgProcessButtonEvent() {
        view.getCreateButton().addActionListener(ActionEvent -> {
            createOrgProcessForm = new FormDialog("Create Process", columnNames, saveCreateOrgProcessIFormEventHandler);
        });
    }

    private IFormDialogEventHandler saveEditOrgProcessIFormEventHandler = (formDialog) -> {
        try {
            if (formDialog.validateForm()) {
                OrgProcess process = saveUtil.saveFormData(formDialog.getFormData());
                String idOrg = process.getIdOrganization(); // Get the organization ID
                ControllersGetter.organizationsController.editOrgProcess(idOrg, formDialog.getId(), process);
                view.refreshTable();
                JOptionPane.showMessageDialog(
                        editOrgProcessForm,
                        "Process updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                formDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(
                        formDialog,
                        "Please fill in all fields.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    editOrgProcessForm,
                    "An error occurred: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    };

    private IFormDialogEventHandler saveCreateOrgProcessIFormEventHandler = (formDialog) -> {
        try {
            if (formDialog.validateForm()) {
                OrgProcess process = saveUtil.saveFormData(formDialog.getFormData());
                String idOrg = process.getIdOrganization(); // Get the organization ID
                ControllersGetter.organizationsController.createProcess(idOrg, process);
                JOptionPane.showMessageDialog(
                        formDialog,
                        "New Process added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                view.refreshTable();
                formDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(
                        formDialog,
                        "Please fill in all fields.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    formDialog,
                    "An error occurred: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    };

    private IButtonEditorEventsHandler iButtonEditorEventsHandler = new IButtonEditorEventsHandler() {
        @Override
        public void editObjectEventHandler(ButtonEditor view) {
            String[] columnNames = OrgProcessTab.getColumnNamesCreateEdit();
            editOrgProcessForm = new FormDialog("Edit", columnNames, view.getRowData(), saveEditOrgProcessIFormEventHandler, view.getId());
        }

        @Override
        public void deleteObjectEventHandler(ButtonEditor buttonEditorView) {
            try {
                int response = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to delete this Process?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

                if (response == JOptionPane.YES_OPTION) {
                    String idOrg = buttonEditorView.getRowData()[0].toString(); // Get the organization ID
                    ControllersGetter.organizationsController.deleteProcess(idOrg, buttonEditorView.getId());
                    view.refreshTable();
                    System.out.println("Deleting Process");
                } else {
                    System.out.println("Deleting operation canceled.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,
                        "An error occurred: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    };

    public IButtonEditorEventsHandler getIButtonEditorEventsHandler() {
        return iButtonEditorEventsHandler;
    }
}