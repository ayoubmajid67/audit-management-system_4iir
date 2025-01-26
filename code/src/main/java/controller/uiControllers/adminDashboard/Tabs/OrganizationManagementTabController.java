package controller.uiControllers.adminDashboard.Tabs;

import model.Organization.Organization;
import utils.ControllersGetter;
import utils.SaveUtil;
import utils.interfaces.IButtonEditorEventsHandler;
import utils.interfaces.IFormDialogEventHandler;
import utils.interfaces.objectConverter.OrganizationConverter;
import view.pages.AdminDashboard.ButtonEditor;
import view.pages.AdminDashboard.FormDialog;
import view.pages.AdminDashboard.OrganizationManagementTab;

import javax.swing.*;

import static utils.ControllersGetter.organizationsController;


public class OrganizationManagementTabController  {
    private OrganizationManagementTab view;
    private FormDialog createOrganizationForm ;
    private FormDialog editOrganizationForm ;
    private String[] columnNames = OrganizationManagementTab.getColumnNamesCreateEdit();
    private SaveUtil<Organization> saveUtil = new SaveUtil(new OrganizationConverter());




    public OrganizationManagementTabController(OrganizationManagementTab view) {
        this.view = view;
        initController();
    }



    private void initController() {

        addCreatAuditorButtonEvent();
    }
    private void addCreatAuditorButtonEvent() {
        view.getCreateButton().addActionListener(ActionEvent -> {
            createOrganizationForm= new FormDialog("Create Organization", columnNames, saveCreateOrganizationIFormEventHandler);


        });
    }


    private IFormDialogEventHandler saveEditOrganizationIFormEventHandler = (formDialog)->{



        try {
            if (formDialog.validateForm()) {

                Organization organization = saveUtil.saveFormData(formDialog.getFormData()); // Save form data
                organizationsController.editOrganization(formDialog.getId(),organization); // Create account using AccountsController
                view.refreshTable();
                // Show success message
                JOptionPane.showMessageDialog(
                        editOrganizationForm,
                        "New Auditor added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // Refresh the table to show the new data

                formDialog.dispose(); // Close the form dialog
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
                    editOrganizationForm,
                    "An error occurred: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    };

    private IFormDialogEventHandler saveCreateOrganizationIFormEventHandler = (formDialog)->{


        try {
            if (formDialog.validateForm()) {
                Organization organization = saveUtil.saveFormData(formDialog.getFormData());

                ControllersGetter.organizationsController.createOrganization(organization);
                // Show success message
                JOptionPane.showMessageDialog(
                        formDialog,
                        "New Organization added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // Refresh the table to show the new data
                view.refreshTable();

                formDialog.dispose(); // Close the form dialog
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
    private IFormDialogEventHandler saveDeleteOrganizationIFormEventHandler = (formDialog)->{


    };

    public IFormDialogEventHandler getSaveDeleteOrganizationIFormEventHandler() {
        return saveDeleteOrganizationIFormEventHandler;
    }

    public IFormDialogEventHandler getSaveEditOrganizationIFormEventHandler() {
        return saveEditOrganizationIFormEventHandler;
    }

    public IFormDialogEventHandler getSaveCreateOrganizationIFormEventHandler() {
        return saveCreateOrganizationIFormEventHandler;
    }

    private IButtonEditorEventsHandler iButtonEditorEventsHandler = new IButtonEditorEventsHandler() {


        @Override
        public void editObjectEventHandler(ButtonEditor view) {
            String[] columnNames = OrganizationManagementTab.getColumnNamesCreateEdit();
            editOrganizationForm=new FormDialog(" Edit",columnNames,view.getRowData(), saveEditOrganizationIFormEventHandler,view.getId());

        }

        @Override
        public void deleteObjectEventHandler(ButtonEditor ButtonEditorView) {
            // Show a confirmation dialog
            int response = JOptionPane.showConfirmDialog(
                    null, // No parent component
                    "Are you sure you want to delete this Organization?", // Message
                    "Confirm Delete", // Dialog title
                    JOptionPane.YES_NO_OPTION // Option type (Yes/No)
            );

            // Check the user's response
            if (response == JOptionPane.YES_OPTION) {
                organizationsController.deleteOrganization(ButtonEditorView.getId());
                view.refreshTable();
                System.out.println("Deleting Organization ");
            } else {
                // User clicked "No" or closed the dialog, do nothing
                System.out.println("Deleting operation canceled.");
            }
        }
    };
    public IButtonEditorEventsHandler getIButtonEditorEventsHandler() {
        return iButtonEditorEventsHandler;
    }






}


