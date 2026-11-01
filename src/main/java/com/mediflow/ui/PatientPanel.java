package com.mediflow.ui;

import com.mediflow.model.Patient;
import com.mediflow.model.User;
import com.mediflow.service.PatientService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PatientPanel extends JPanel {
    private final PatientService service = new PatientService();
    private final JTextField search = new JTextField(22);
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Patient No.", "Full name", "Phone", "Email", "Status"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(model);

    public PatientPanel(User user) {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton register = new JButton("Register patient");
        JButton find = new JButton("Search");
        JButton refresh = new JButton("Refresh");
        JButton deactivate = new JButton("Deactivate selected");
        toolbar.add(new JLabel("Search"));
        toolbar.add(search);
        toolbar.add(find);
        toolbar.add(refresh);
        toolbar.add(register);
        toolbar.add(deactivate);
        add(toolbar, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        register.addActionListener(e -> registerPatient());
        find.addActionListener(e -> load());
        search.addActionListener(e -> load());
        refresh.addActionListener(e -> { search.setText(""); load(); });
        deactivate.addActionListener(e -> deactivateSelected());
        load();
    }

    private void load() {
        try {
            model.setRowCount(0);
            for (Patient p : service.search(search.getText())) {
                model.addRow(new Object[]{p.id(), p.patientNumber(), p.fullName(), p.phone(), p.email(), p.status()});
            }
        } catch (Exception ex) { showError(ex); }
    }

    private void registerPatient() {
        JTextField name = new JTextField(), phone = new JTextField(), email = new JTextField();
        JTextField dob = new JTextField(), address = new JTextField();
        JComboBox<String> gender = new JComboBox<>(new String[]{"Not specified", "Female", "Male", "Other"});
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Full name *")); form.add(name);
        form.add(new JLabel("Phone *")); form.add(phone);
        form.add(new JLabel("Email")); form.add(email);
        form.add(new JLabel("Date of birth (YYYY-MM-DD)")); form.add(dob);
        form.add(new JLabel("Gender")); form.add(gender);
        form.add(new JLabel("Address")); form.add(address);
        if (JOptionPane.showConfirmDialog(this, form, "Register patient",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;
        try {
            service.register(name.getText(), phone.getText(), email.getText(), dob.getText(),
                    (String) gender.getSelectedItem(), address.getText());
            load();
            JOptionPane.showMessageDialog(this, "Patient registered successfully.");
        } catch (Exception ex) { showError(ex); }
    }

    private void deactivateSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a patient first.");
            return;
        }
        long id = ((Number) model.getValueAt(table.convertRowIndexToModel(row), 0)).longValue();
        if (JOptionPane.showConfirmDialog(this, "Deactivate this patient record?",
                "Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try { service.deactivate(id); load(); }
        catch (Exception ex) { showError(ex); }
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Operation failed", JOptionPane.ERROR_MESSAGE);
    }
}