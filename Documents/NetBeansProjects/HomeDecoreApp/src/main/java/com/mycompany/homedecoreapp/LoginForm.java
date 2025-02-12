/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.homedecoreapp;

/**
 *
 * @author Kapnang
 */
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class LoginForm extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginForm() {
        setTitle("Login");
        setBackground(Color.GREEN);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Important: Close on exit

        // GUI Components
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        // Labels
        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");

        // Layout (using GridBagLayout for more flexibility)
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add some padding

        // Add components with labels
        gbc.gridx = 0; gbc.gridy = 0; add(emailLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; add(passwordField, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 2; add(loginButton, gbc);
        gbc.gridx = 1; gbc.gridy = 2; add(registerButton, gbc);

        // Login Button Action
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            try (Connection conn = DBConnection.getConnection()) { // Use DBConnection class
                String sql = "SELECT uid, role FROM users WHERE email = ? AND password = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, email);
                    stmt.setString(2, password);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            int userId = rs.getInt("uid");
                            String role = rs.getString("role");

                            if (role.equals("supplier")) {
                                new SupplierDashboard(userId).setVisible(true);
                            } else if (role.equals("warehouse_manager")) {
                                new WarehouseManagerDashboard(userId).setVisible(true);
                            }
                            dispose(); // Close login form
                        } else {
                            JOptionPane.showMessageDialog(this, "Invalid credentials.");
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace(); // Handle errors appropriately
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage()); // Show error message
            }
        });

        // Register Button Action
        registerButton.addActionListener(e -> {
            new RegistrationForm().setVisible(true);
            dispose();
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true); // Make the form visible
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm());
    }
}