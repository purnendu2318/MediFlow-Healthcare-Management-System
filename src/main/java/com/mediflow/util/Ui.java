package com.mediflow.util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public final class Ui {
    public static final Color NAVY = new Color(23, 48, 75);
    public static final Color BLUE = new Color(35, 111, 171);
    public static final Color BG = new Color(245, 248, 252);
    public static final Color TEAL = new Color(31, 145, 139);
    private Ui() {}
    public static void styleButton(JButton button) {
        button.setBackground(BLUE);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 16, 10, 16));
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }
    public static JPanel page() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        return panel;
    }
    public static JLabel heading(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        label.setForeground(NAVY);
        return label;
    }
}