package com.mediflow;

import com.mediflow.config.Database;
import com.mediflow.repository.UserRepository;
import com.mediflow.security.PasswordUtil;
import com.mediflow.ui.LoginFrame;
import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                Database.testConnection();
                new UserRepository().createDefaultAdminIfEmpty(PasswordUtil.hash("Admin@123"));
                new LoginFrame().setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                    "MediFlow could not start. Check MySQL and db.properties.\n\n" + ex.getMessage(),
                    "Startup error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
