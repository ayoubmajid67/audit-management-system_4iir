package utils.interfaces;


import view.pages.AdminDashboard.FormDialog;

import java.awt.*;

@FunctionalInterface
public interface IFormDialogEventHandler {
    void save(FormDialog formDialog);


    default void cancel(Dialog dialog){
        System.out.println("The Form dialog closed with success");
         dialog.dispose();
    }

}

