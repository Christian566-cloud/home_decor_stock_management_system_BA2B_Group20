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


public class SupplierDashboard extends JFrame {
    private int userId; // Supplier's ID
    private JTable ordersTable;
    private DefaultTableModel tableModel;
    private JButton approveButton;
    private JButton rejectButton;
    private JButton backButton;

     public SupplierDashboard(int userId) {
        this.userId = userId;
        setTitle("Supplier Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Table Model
        tableModel = new DefaultTableModel(new Object[]{"Order ID", "Warehouse Manager ID", "Product ID", "Quantity", "Order Date", "Status"}, 0);
        ordersTable = new JTable(tableModel);

        // Buttons
        approveButton = new JButton("Approve");
        rejectButton = new JButton("Reject");
        backButton = new JButton("Back"); // Initialize back button

        // Layout
        setLayout(new BorderLayout());
        add(new JScrollPane(ordersTable), BorderLayout.CENTER); // Table in center

        JPanel buttonPanel = new JPanel(new FlowLayout()); // Buttons in panel
        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(backButton); // Add back button to panel
        add(buttonPanel, BorderLayout.SOUTH); // Buttons at bottom

        // Load pending orders from the database
        loadPendingOrders();

        // Button Actions
        approveButton.addActionListener(e -> updateOrderStatus("approved"));
        rejectButton.addActionListener(e -> updateOrderStatus("rejected"));

        // Back Button Action
        backButton.addActionListener(e -> {
            new LoginForm().setVisible(true); // Go back to login form
            dispose(); // Close supplier dashboard
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void loadPendingOrders() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT order_id, warehouse_manager_id, product_id, quantity_requested, order_date, order_status FROM orders WHERE supplier_id = ? AND order_status = 'pending'";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    tableModel.setRowCount(0); // Clear existing data
                    while (rs.next()) {
                        tableModel.addRow(new Object[]{
                                rs.getInt("order_id"),
                                rs.getInt("warehouse_manager_id"),
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
    }

    private void updateOrderStatus(String status) {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow != -1) {
            int orderId = (int) tableModel.getValueAt(selectedRow, 0);
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "UPDATE orders SET order_status = ? WHERE order_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, status);
                    stmt.setInt(2, orderId);
                    stmt.executeUpdate();
                    loadPendingOrders(); // Refresh the table
                    JOptionPane.showMessageDialog(this, "Order " + status + ".");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "No order selected.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SupplierDashboard(1)); // Replace 1 with actual user ID
    }
}