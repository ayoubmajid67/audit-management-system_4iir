package controller.uiControllers;

import controller.businessControllers.account.AccountSessionHandler;
import model.Accounts.Account;
import utils.PageSwitcher;
import view.pages.LoginPage;


import javax.swing.*;
import utils.ControllersGetter;
public class LoginPageController {
    private final LoginPage view;

    public LoginPageController(LoginPage loginPage) {
        this.view = loginPage;
        initLoginPageEvents();
    }

    private void initLoginPageEvents() {
        view.getLoginButton().addActionListener(e -> handleLoginEvent(
                view.getEmailField().getText(),
                new String(view.getPasswordField().getPassword())
        ));
    }

    public void handleLoginEvent(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Email and Password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        Account account = ControllersGetter.accountsController.getAccount(email,password);
        if (account != null) {
            AccountSessionHandler.UpdateCurrentAccountSession(account.getIdAccount(),account.getAccountType());
            if(account.isAdmin())
            PageSwitcher.switchPage("adminDashboard");
            if(account.isAuditor())
                PageSwitcher.switchPage("auditorDashboard");
        } else {
            JOptionPane.showMessageDialog(view, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

