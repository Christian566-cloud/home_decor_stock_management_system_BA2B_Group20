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
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WarehouseManagerDashboard extends JFrame {
    private int userId; // Warehouse Manager's ID
    private JTable productsTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JTextField nameField;
    private JTextField descriptionField;
    private JTextField categoryField;
    private JTextField quantityField;
    private JTextField priceField;
    private JComboBox<String> supplierComboBox; // For supplier selection
    private JTextArea orderArea; // For order requests

    public WarehouseManagerDashboard(int userId) {
        this.userId = userId;
        setTitle("Warehouse Manager Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Table Model
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Description", "Category", "Quantity", "Price", "Supplier ID", "Date Added"}, 0);
        productsTable = new JTable(tableModel);

        // Buttons
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        // Input Fields
        nameField = new JTextField(20);
        descriptionField = new JTextField(20);
        categoryField = new JTextField(20);
        quantityField = new JTextField(20);
        priceField = new JTextField(20);

        // Combo Box for Suppliers
        supplierComboBox = new JComboBox<>();
        loadSuppliers(); // Load suppliers from the database

        // Order Area
        orderArea = new JTextArea(5, 20);
        orderArea.setLineWrap(true);

        // Layout (BorderLayout for main frame, GridBagLayout for input panel)
        setLayout(new BorderLayout());

        add(new JScrollPane(productsTable), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add input fields and labels to inputPanel using GridBagConstraints
        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; inputPanel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; inputPanel.add(descriptionField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; inputPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; inputPanel.add(categoryField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; inputPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; inputPanel.add(quantityField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; inputPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; inputPanel.add(priceField, gbc);
        gbc.gridx = 0; gbc.gridy = 5; inputPanel.add(new JLabel("Supplier:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; inputPanel.add(supplierComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 6; inputPanel.add(new JLabel("Order Request:"), gbc);
        gbc.gridx = 1; gbc.gridy = 6; inputPanel.add(new JScrollPane(orderArea), gbc);

        add(inputPanel, BorderLayout.NORTH); // Input panel at the top

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH); // Buttons at the bottom

        // Load products from database
        loadProducts();

        // Button Actions
        addButton.addActionListener(e -> addProduct());
        updateButton.addActionListener(e -> updateProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        
        // Add Order Request Functionality
        JButton orderRequestButton = new JButton("Send Order Request"); // New button
        buttonPanel.add(orderRequestButton); // Add to button panel
        
        JButton viewApprovedOrdersButton = new JButton("View Approved Orders"); // New button
        buttonPanel.add(viewApprovedOrdersButton); // Add to button panel
        viewApprovedOrdersButton.addActionListener(e -> viewApprovedOrders());

        orderRequestButton.addActionListener(e -> {
            try {
                sendOrderRequest();
            } catch (SQLException ex) {
                Logger.getLogger(WarehouseManagerDashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
        });


        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void viewApprovedOrders() {
        // Create a new JFrame for displaying approved orders
        JFrame approvedOrdersFrame = new JFrame("Approved Orders");
        approvedOrdersFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only the new frame

        // Table Model for approved orders
        DefaultTableModel approvedOrdersTableModel = new DefaultTableModel(
                new Object[]{"Order ID", "Supplier ID", "Product ID", "Quantity", "Order Date", "Status"}, 0);
        JTable approvedOrdersTable = new JTable(approvedOrdersTableModel);

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM orders WHERE warehouse_manager_id = ? AND order_status = 'approved'"; // Select approved orders
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    approvedOrdersTableModel.setRowCount(0); // Clear existing data
                    while (rs.next()) {
                        approvedOrdersTableModel.addRow(new Object[]{
                                rs.getInt("order_id"),
                                rs.getInt("supplier_id"),
                                rs.getInt("product_id"),
                                rs.getInt("quantity_requested"),
                                rs.getString("order_date"),
                                rs.getString("order_status")
                        });
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }

        // Add the table to a scroll pane and the frame
        approvedOrdersFrame.add(new JScrollPane(approvedOrdersTable));
        approvedOrdersFrame.pack();
        approvedOrdersFrame.setLocationRelativeTo(null);
        approvedOrdersFrame.setVisible(true);
    }

    
    
    private void sendOrderRequest() throws SQLException {
    int selectedRow = productsTable.getSelectedRow();
    if (selectedRow != -1) {
        try {
            int productId = (int) tableModel.getValueAt(selectedRow, 0);
            int quantityRequested = Integer.parseInt(orderArea.getText());

            // Get selected supplier NAME from the combo box
            String selectedSupplierName = (String) supplierComboBox.getSelectedItem();

            // Handle the case where no supplier is selected:
            if (selectedSupplierName == null || selectedSupplierName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a supplier.");
                return; // Stop the order request process
            }

            int supplierId = getSupplierId(selectedSupplierName); // Pass name to get ID

            if (supplierId != -1) { // Check if supplier exists
                try (Connection conn = DBConnection.getConnection()) {
                    String sql = "INSERT INTO orders (warehouse_manager_id, supplier_id, product_id, quantity_requested, order_status, order_date) VALUES (?, ?, ?, ?, ?, NOW())";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setInt(1, userId);
                        stmt.setInt(2, supplierId);
                        stmt.setInt(3, productId);
                        stmt.setInt(4, quantityRequested);
                        stmt.setString(5, "pending");
                        stmt.setString(6, LocalDate.now().toString());

                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Order request sent.");
                        orderArea.setText("");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Supplier not found in the database.  This should not happen. Check database integrity.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity requested.");
        }
    } else {
        JOptionPane.showMessageDialog(this, "No product selected.");
    }
}
// Corrected getSupplierId function (now takes supplier name)


    private void loadProducts() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT p.id, p.name, p.description, p.category, p.quantity_in_stock, p.price, p.supplier_id, p.date_added FROM products p"; // Join with suppliers table

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    tableModel.setRowCount(0); // Clear existing data
                    while (rs.next()) {
                        tableModel.addRow(new Object[]{
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getString("description"),
                                rs.getString("category"),
                                rs.getInt("quantity_in_stock"),
                                rs.getDouble("price"),
                                rs.getInt("supplier_id"), // Display supplier ID
                                rs.getString("date_added")
                        });
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }


    private void loadSuppliers() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT uid, name FROM users WHERE role = 'supplier'"; // Get supplier names and IDs
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        supplierComboBox.addItem(rs.getString("name")); // Add supplier name to combo box
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }


    private void addProduct() {
        try {
            String name = nameField.getText();
            String description = descriptionField.getText();
            String category = categoryField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());
            String currentDate = new Date().toLocaleString();
            String supplierName = (String) supplierComboBox.getSelectedItem(); // Get selected supplier name

            // Get supplier ID based on selected name
            int supplierId = getSupplierId(supplierName);

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO products (name, description, category, quantity_in_stock, price, supplier_id, date_added) VALUES (?, ?, ?, ?, ?, ?, NOW())";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, name);
                    stmt.setString(2, description);
                    stmt.setString(3, category);
                    stmt.setInt(4, quantity);
                    stmt.setDouble(5, price);
                    stmt.setInt(6, supplierId);
//                    stmt.setString(7, "");
                    stmt.executeUpdate();
                    loadProducts(); // Refresh table
                    clearInputFields();
                    JOptionPane.showMessageDialog(this, "Product added.");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input for quantity or price.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }


    // Helper function to get supplier ID from name
    private int getSupplierId(String supplierName) { // No throws declaration here
    try (Connection conn = DBConnection.getConnection()) {
        String sql = "SELECT uid FROM users WHERE name = ?"; // Corrected query
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, supplierName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("uid");
                } else {
                    return -1; // Return -1 if not found
                }
            }
        }
    } catch (SQLException ex) {  // Catch the SQLException
        ex.printStackTrace(); // Print the error for debugging (REMOVE IN PRODUCTION)
        JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage()); // User-friendly error message
        return -1; // Or throw a custom exception if needed
    }
}


    private void updateProduct() {
        int selectedRow = productsTable.getSelectedRow();
        if (selectedRow != -1) {
            try {
                int productId = (int) tableModel.getValueAt(selectedRow, 0);
                String name = nameField.getText();
                String description = descriptionField.getText();
                String category = categoryField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                double price = Double.parseDouble(priceField.getText());
                String supplierName = (String) supplierComboBox.getSelectedItem(); // Get selected supplier name

                // Get supplier ID based on selected name
                int supplierId = getSupplierId(supplierName);

                try (Connection conn = DBConnection.getConnection()) {
                    String sql = "UPDATE products SET name = ?, description = ?, category = ?, quantity_in_stock = ?, price = ?, supplier_id = ? WHERE id = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, name);
                        stmt.setString(2, description);
                        stmt.setString(3, category);
                        stmt.setInt(4, quantity);
                        stmt.setDouble(5, price);
                        stmt.setInt(6, supplierId);
                        stmt.setInt(7, productId);
                        stmt.executeUpdate();
                        loadProducts(); // Refresh table
                        clearInputFields();
                        JOptionPane.showMessageDialog(this, "Product updated.");
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input for quantity or price.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "No product selected.");
        }
    }

    private void deleteProduct() {
        int selectedRow = productsTable.getSelectedRow();
        if (selectedRow != -1) {
            int productId = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = DBConnection.getConnection()) {
                    String sql = "DELETE FROM products WHERE id = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setInt(1, productId);
                        stmt.executeUpdate();
                        loadProducts(); // Refresh table
                        JOptionPane.showMessageDialog(this, "Product deleted.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No product selected.");
        }
    }

    private void clearInputFields() {
        nameField.setText("");
        descriptionField.setText("");
        categoryField.setText("");
        quantityField.setText("");
        priceField.setText("");
        supplierComboBox.setSelectedIndex(0); // Reset to the first item
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WarehouseManagerDashboard(1)); // Replace 1 with actual user ID
    }
}