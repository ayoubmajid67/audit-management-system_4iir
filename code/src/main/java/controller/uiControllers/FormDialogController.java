package controller.uiControllers;

import utils.interfaces.IFormDialogEventHandler;
import view.pages.AdminDashboard.FormDialog;

public class FormDialogController {
  FormDialog view;
  IFormDialogEventHandler formDialogEventHandler;

   public FormDialogController(FormDialog view, IFormDialogEventHandler formDialogEventHandler) {
       this.view = view;
       this.formDialogEventHandler = formDialogEventHandler;
       this.initController();
   }


    private void initController() {
        addSaveCreateAuditorButtonEvent();
        addCancelCreateAuditorButtonEvent();
    }
    private void addSaveCreateAuditorButtonEvent() {
        view.getSaveButton().addActionListener(ActionEvent -> {
             formDialogEventHandler.save(view);
        });
    }
    private void addCancelCreateAuditorButtonEvent() {
        view.getCancelButton().addActionListener(ActionEvent -> {
            formDialogEventHandler.cancel(view);
        });
    }

//     cancelButton.addActionListener(e -> {
//        System.out.println("Form canceled."); // Handle cancel action
//        dialog.dispose(); // Close the dialog
//    });
//
//   saveButton.addActionListener(e -> {
//        // Collect data from the form
//        Map<String, String> formData = new HashMap<>();
//        for (String fieldName : fieldNames) {
//            formData.put(fieldName, fields.get(fieldName).getText());
//        }
//        System.out.println("Form data: " + formData); // Print the data (or process it as needed)
//        dialog.dispose(); // Close the dialog
//    });



}
