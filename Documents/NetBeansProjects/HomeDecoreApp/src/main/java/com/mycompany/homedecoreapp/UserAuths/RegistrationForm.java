/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.homedecoreapp.UserAuths;

/**
 *
 * @author Kapnang
 */
import com.mycompany.homedecoreapp.DBConnection.DBConnection;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class RegistrationForm extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField phoneField;
    private JTextArea addressArea;
    private JComboBox<String> roleComboBox;
    private JButton registerButton;
    private JButton loginButton; // Button to go back to login

    public RegistrationForm() {
        setTitle("Registration");
        setBackground(Color.BLUE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Important: Close on exit

        // GUI Components
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        phoneField = new JTextField(20);
        addressArea = new JTextArea(5, 20); // 5 rows, 20 columns
        addressArea.setLineWrap(true); // Allow text to wrap within the area
        roleComboBox = new JComboBox<>(new String[]{"supplier", "warehouse_manager"});
        registerButton = new JButton("Register");
        loginButton = new JButton("Back to Login"); // Initialize login button

        // Labels
        JLabel nameLabel = new JLabel("Name:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel phoneLabel = new JLabel("Phone:");
        JLabel addressLabel = new JLabel("Address:");
        JLabel roleLabel = new JLabel("Role:");

        // Layout (using GridBagLayout for more flexibility)
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add some padding

        // Add components with labels
        gbc.gridx = 0; gbc.gridy = 0; add(nameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(emailLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; add(phoneLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; add(addressLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 4; add(new JScrollPane(addressArea), gbc); // Add scroll pane for address

        gbc.gridx = 0; gbc.gridy = 5; add(roleLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 5; add(roleComboBox, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 6; add(registerButton, gbc);
        gbc.gridx = 1; gbc.gridy = 6; add(loginButton, gbc); // Add login button

        // Register Button Action
        registerButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword()); // Get password as String
            String phone = phoneField.getText();
            String address = addressArea.getText();
            String role = (String) roleComboBox.getSelectedItem();

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO users (name, email, password, phone, address, role) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, name);
                    stmt.setString(2, email);
                    stmt.setString(3, password); // Store password (consider hashing!)
                    stmt.setString(4, phone);
                    stmt.setString(5, address);
                    stmt.setString(6, role);
                    stmt.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Registration successful.");
                    new LoginForm().setVisible(true); // Go back to login
                    dispose();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); // Show error message
            }
        });

        // Login Button Action (Go back to login)
        loginButton.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });


        pack();
        setLocationRelativeTo(null);
        setVisible(true); // Make the form visible
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistrationForm());
    }
}