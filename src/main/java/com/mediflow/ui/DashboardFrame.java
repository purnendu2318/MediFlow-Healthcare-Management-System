package com.mediflow.ui;

import com.mediflow.model.User;
import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    public DashboardFrame(User user) {
        setTitle("MediFlow | Healthcare Management");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1180, 760);
        setMinimumSize(new Dimension(950, 650));
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Overview", overview(user));
        tabs.addTab("Patients", new PatientPanel(user));
        for (String name : new String[]{"Doctors", "Departments", "Appointments", "Queue",
                "Medical Records", "Prescriptions", "Billing", "Reports", "Notifications", "Settings"}) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(new JLabel(name + " — planned module", SwingConstants.CENTER), BorderLayout.CENTER);
            tabs.addTab(name, panel);
        }

        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel title = new JLabel("MediFlow  |  Hospital Operations");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        JLabel signedIn = new JLabel(user.fullName() + "  •  " + user.role());
        header.add(title, BorderLayout.WEST);
        header.add(signedIn, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel overview(User user) {
        JPanel panel = new JPanel(new GridLayout(2, 2, 16, 16));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        panel.setBackground(new Color(245, 248, 252));
        String[] items = {"Patient Management", "Appointments", "Clinical Records", "Billing & Payments"};
        String[] details = {
                "Register, search and deactivate patient profiles.",
                "Scheduling is planned for a future release.",
                "Medical records and prescriptions are planned.",
                "Invoices and payment workflows are planned."
        };
        for (int i = 0; i < items.length; i++) {
            JPanel card = new JPanel(new BorderLayout(8, 8));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 228, 238)),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)));
            JLabel heading = new JLabel(items[i]);
            heading.setFont(new Font("Segoe UI", Font.BOLD, 18));
            card.add(heading, BorderLayout.NORTH);
            card.add(new JLabel("<html><body style='width:220px'>" + details[i] + "</body></html>"), BorderLayout.CENTER);
            panel.add(card);
        }
        return panel;
    }
}