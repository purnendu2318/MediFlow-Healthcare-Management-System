package com.mediflow.ui;

import com.mediflow.model.User;
import com.mediflow.service.AuthService;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final JTextField email = new JTextField("admin@mediflow.local", 24);
    private final JPasswordField password = new JPasswordField("Admin@123", 24);

    public LoginFrame() {
        setTitle("MediFlow | Sign in");
        setSize(460, 390);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel card = new JPanel(new GridLayout(0, 1, 8, 8));
        card.setBorder(BorderFactory.createEmptyBorder(32, 38, 32, 38));
        JLabel brand = new JLabel("MediFlow");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 30));
        brand.setForeground(new Color(23, 48, 75));
        card.add(brand);
        card.add(new JLabel("Healthcare Management System"));
        card.add(new JLabel("Email address"));
        card.add(email);
        card.add(new JLabel("Password"));
        card.add(password);
        JButton signIn = new JButton("Sign in");
        signIn.addActionListener(event -> signIn());
        card.add(signIn);
        JLabel hint = new JLabel("Local demo: admin@mediflow.local / Admin@123");
        hint.setForeground(Color.GRAY);
        card.add(hint);
        add(card);
    }

    private void signIn() {
        try {
            User user = new AuthService().login(email.getText(), new String(password.getPassword()));
            if (user == null) {
                JOptionPane.showMessageDialog(this, "Invalid email or password, or account disabled.");
                return;
            }
            dispose();
            new DashboardFrame(user).setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Sign-in error", JOptionPane.ERROR_MESSAGE);
        }
    }
}