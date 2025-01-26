package utils;

import javax.swing.*;

import view.pages.AdminDashboard.AdminDashboard;
import view.pages.AuditorDashboard.AuditorDashboard;
import view.pages.LoginPage;
import view.pages.AuditViewDetails;


public class PagesGetter {
      static public JPanel LoginPage =new LoginPage();
      static public JPanel AdminDashBoardPage =new AdminDashboard();
      static public JPanel AuditorDashboardPage =new AuditorDashboard();
     static public   AuditViewDetails auditViewDetailsPage= new AuditViewDetails();

}
