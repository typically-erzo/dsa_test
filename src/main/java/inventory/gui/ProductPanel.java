package inventory.gui;

import inventory.model.Product;
import inventory.service.InventoryService;
import inventory.ds.ProductBST;
import inventory.ds.InventoryLinkedList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class ProductPanel extends JPanel implements ActionListener {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, categoryField, qtyField, supplierField, priceField;
    private JButton addButton, updateButton, deleteButton, sortBstButton, clearButton;
    private InventoryService inventoryService;

    public ProductPanel() {
        inventoryService = new InventoryService();
        setLayout(null); // Absolute layout
        setBounds(0, 0, 960, 680);

        // Table & ScrollPane
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Category", "Quantity", "Supplier ID", "Price ($)"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 30, 900, 300);
        add(scrollPane);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    idField.setText(tableModel.getValueAt(row, 0).toString());
                    nameField.setText(tableModel.getValueAt(row, 1).toString());
                    categoryField.setText(tableModel.getValueAt(row, 2).toString());
                    qtyField.setText(tableModel.getValueAt(row, 3).toString());
                    supplierField.setText(tableModel.getValueAt(row, 4).toString());
                    priceField.setText(tableModel.getValueAt(row, 5).toString());
                    idField.setEnabled(false); // ID cannot be edited during update
                }
            }
        });

        // Form Fields
        int startY = 350;
        int lblX = 30, txtX = 140, width = 280, height = 25;

        add(createLabel("Product ID:", lblX, startY));
        idField = new JTextField();
        idField.setBounds(txtX, startY, width, height);
        add(idField);

        add(createLabel("Name:", lblX + 460, startY));
        nameField = new JTextField();
        nameField.setBounds(txtX + 460, startY, width, height);
        add(nameField);

        startY += 40;
        add(createLabel("Category:", lblX, startY));
        categoryField = new JTextField();
        categoryField.setBounds(txtX, startY, width, height);
        add(categoryField);

        add(createLabel("Quantity:", lblX + 460, startY));
        qtyField = new JTextField();
        qtyField.setBounds(txtX + 460, startY, width, height);
        add(qtyField);

        startY += 40;
        add(createLabel("Supplier ID:", lblX, startY));
        supplierField = new JTextField();
        supplierField.setBounds(txtX, startY, width, height);
        add(supplierField);

        add(createLabel("Price ($):", lblX + 460, startY));
        priceField = new JTextField();
        priceField.setBounds(txtX + 460, startY, width, height);
        add(priceField);

        // Action Buttons
        startY += 60;
        int btnW = 150, btnH = 35;

        addButton = new JButton("Add Product");
        addButton.setBounds(30, startY, btnW, btnH);
        addButton.addActionListener(this);
        add(addButton);

        updateButton = new JButton("Update Selected");
        updateButton.setBounds(190, startY, btnW, btnH);
        updateButton.addActionListener(this);
        add(updateButton);

        deleteButton = new JButton("Delete Product");
        deleteButton.setBounds(350, startY, btnW, btnH);
        deleteButton.addActionListener(this);
        add(deleteButton);

        sortBstButton = new JButton("Sort by Price (BST)");
        sortBstButton.setBounds(510, startY, 180, btnH);
        sortBstButton.addActionListener(this);
        add(sortBstButton);

        clearButton = new JButton("Clear Form");
        clearButton.setBounds(700, startY, 140, btnH);
        clearButton.addActionListener(this);
        add(clearButton);

        refreshTable();
    }

    private JLabel createLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 100, 25);
        return lbl;
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        try {
            List<Product> products = inventoryService.getAllProducts();
            
            // Populate custom Linked List for dynamic memory demonstration
            InventoryLinkedList list = new InventoryLinkedList();
            for (Product p : products) {
                list.add(p);
            }

            for (Product p : list.toList()) {
                tableModel.addRow(new Object[]{
                        p.getId(), p.getName(), p.getCategory(), p.getQuantity(), p.getSupplierId(), p.getPrice()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            try {
                Product p = new Product(
                        idField.getText().trim(),
                        nameField.getText().trim(),
                        categoryField.getText().trim(),
                        Integer.parseInt(qtyField.getText().trim()),
                        supplierField.getText().trim(),
                        Double.parseDouble(priceField.getText().trim())
                );
                inventoryService.addProduct(p);
                refreshTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Product added successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == updateButton) {
            try {
                Product p = new Product(
                        idField.getText().trim(),
                        nameField.getText().trim(),
                        categoryField.getText().trim(),
                        Integer.parseInt(qtyField.getText().trim()),
                        supplierField.getText().trim(),
                        Double.parseDouble(priceField.getText().trim())
                );
                inventoryService.updateProduct(p);
                refreshTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Product updated successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == deleteButton) {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a product to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                inventoryService.deleteProduct(id);
                refreshTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Product deleted successfully.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting product: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == sortBstButton) {
            try {
                List<Product> products = inventoryService.getAllProducts();
                ProductBST bst = new ProductBST();
                for (Product p : products) {
                    bst.insert(p);
                }
                List<Product> sorted = bst.inOrderTraversal();
                tableModel.setRowCount(0);
                for (Product p : sorted) {
                    tableModel.addRow(new Object[]{
                            p.getId(), p.getName(), p.getCategory(), p.getQuantity(), p.getSupplierId(), p.getPrice()
                    });
                }
                JOptionPane.showMessageDialog(this, "Products sorted by price using Binary Search Tree (BST).");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error sorting: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == clearButton) {
            clearForm();
        }
    }

    private void clearForm() {
        idField.setText("");
        idField.setEnabled(true);
        nameField.setText("");
        categoryField.setText("");
        qtyField.setText("");
        supplierField.setText("");
        priceField.setText("");
        table.clearSelection();
    }
}