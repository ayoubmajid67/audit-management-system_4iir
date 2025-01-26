package controller.uiControllers.adminDashboard.Tabs;

import model.Organization.Site;
import utils.ControllersGetter;
import utils.SaveUtil;
import utils.interfaces.IButtonEditorEventsHandler;
import utils.interfaces.IFormDialogEventHandler;
import utils.interfaces.objectConverter.SiteConverter;
import view.pages.AdminDashboard.ButtonEditor;
import view.pages.AdminDashboard.FormDialog;
import view.pages.AdminDashboard.SiteTab;

import javax.swing.*;

public class SiteTabController {
    private SiteTab view;
    private FormDialog createSiteForm;
    private FormDialog editSiteForm;
    private String[] columnNames = SiteTab.getColumnNamesCreateEdit();
    private SaveUtil<Site> saveUtil = new SaveUtil(new SiteConverter());

    public SiteTabController(SiteTab view) {
        this.view = view;
        initController();
    }

    private void initController() {
        addCreateSiteButtonEvent();
    }

    private void addCreateSiteButtonEvent() {
        view.getCreateButton().addActionListener(ActionEvent -> {
            createSiteForm = new FormDialog("Create Site", columnNames, saveCreateSiteIFormEventHandler);
        });
    }

    private IFormDialogEventHandler saveEditSiteIFormEventHandler = (formDialog) -> {
        try {
            if (formDialog.validateForm()) {
                Site site = saveUtil.saveFormData(formDialog.getFormData());
                String idOrg = site.getIdOrganization(); // Get the organization ID
                ControllersGetter.organizationsController.editSiteInOrganization(idOrg, formDialog.getId(), site);
                view.refreshTable();
                JOptionPane.showMessageDialog(
                        editSiteForm,
                        "Site updated successfully!",
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
                    editSiteForm,
                    "An error occurred: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    };

    private IFormDialogEventHandler saveCreateSiteIFormEventHandler = (formDialog) -> {
        try {
            if (formDialog.validateForm()) {
                Site site = saveUtil.saveFormData(formDialog.getFormData());
                String idOrg = site.getIdOrganization(); // Get the organization ID
                ControllersGetter.organizationsController.addSiteToOrganization(idOrg, site);
                JOptionPane.showMessageDialog(
                        formDialog,
                        "New Site added successfully!",
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
            String[] columnNames = SiteTab.getColumnNamesCreateEdit();
            editSiteForm = new FormDialog("Edit", columnNames, view.getRowData(), saveEditSiteIFormEventHandler, view.getId());
        }

        @Override
        public void deleteObjectEventHandler(ButtonEditor buttonEditorView) {
            try {
                int response = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to delete this Site?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

                if (response == JOptionPane.YES_OPTION) {
                    String idOrg = buttonEditorView.getRowData()[0].toString(); // Get the organization ID
                    ControllersGetter.organizationsController.deleteSiteFromOrganization(idOrg, buttonEditorView.getId());
                    view.refreshTable();
                    System.out.println("Deleting Site");
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