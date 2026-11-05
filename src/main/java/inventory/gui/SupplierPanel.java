package inventory.gui;

import inventory.model.Supplier;
import inventory.service.SupplierRegistryService;
import inventory.ds.SupplierHashTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class SupplierPanel extends JPanel implements ActionListener {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, contactField, addressField;
    private JButton addButton, updateButton, deleteButton, clearButton;
    private SupplierRegistryService supplierService;

    public SupplierPanel() {
        supplierService = new SupplierRegistryService();
        setLayout(null);
        setBounds(0, 0, 960, 680);

        tableModel = new DefaultTableModel(new String[]{"Supplier ID", "Name", "Contact Info", "Address"}, 0);
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
                    contactField.setText(tableModel.getValueAt(row, 2).toString());
                    addressField.setText(tableModel.getValueAt(row, 3).toString());
                    idField.setEnabled(false);
                }
            }
        });

        int startY = 350;
        int lblX = 30, txtX = 140, width = 300, height = 25;

        add(createLabel("Supplier ID:", lblX, startY));
        idField = new JTextField();
        idField.setBounds(txtX, startY, width, height);
        add(idField);

        add(createLabel("Name:", lblX + 460, startY));
        nameField = new JTextField();
        nameField.setBounds(txtX + 460, startY, width, height);
        add(nameField);

        startY += 40;
        add(createLabel("Contact:", lblX, startY));
        contactField = new JTextField();
        contactField.setBounds(txtX, startY, width, height);
        add(contactField);

        add(createLabel("Address:", lblX + 460, startY));
        addressField = new JTextField();
        addressField.setBounds(txtX + 460, startY, width, height);
        add(addressField);

        startY += 60;
        int btnW = 160, btnH = 35;

        addButton = new JButton("Add Supplier");
        addButton.setBounds(30, startY, btnW, btnH);
        addButton.addActionListener(this);
        add(addButton);

        updateButton = new JButton("Update Selected");
        updateButton.setBounds(210, startY, btnW, btnH);
        updateButton.addActionListener(this);
        add(updateButton);

        deleteButton = new JButton("Delete Supplier");
        deleteButton.setBounds(390, startY, btnW, btnH);
        deleteButton.addActionListener(this);
        add(deleteButton);

        clearButton = new JButton("Clear Form");
        clearButton.setBounds(570, startY, 140, btnH);
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
            List<Supplier> suppliers = supplierService.getAllSuppliers();
            
            // Populate custom Hash Table to demonstrate hashing lookups
            SupplierHashTable hashTable = new SupplierHashTable();
            for (Supplier s : suppliers) {
                hashTable.put(s);
            }

            for (Supplier s : hashTable.getAllSuppliers()) {
                tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getContactInfo(), s.getAddress()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading suppliers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String contact = contactField.getText().trim();
            String address = addressField.getText().trim();

            // Prevent blank inputs from being inserted
            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Supplier ID and Name cannot be empty!", 
                    "Validation Error", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                Supplier s = new Supplier(idField.getText().trim(), nameField.getText().trim(), contactField.getText().trim(), addressField.getText().trim());
                supplierService.addSupplier(s);
                refreshTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Supplier added successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error adding supplier: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == updateButton) {
            try {
                Supplier s = new Supplier(idField.getText().trim(), nameField.getText().trim(), contactField.getText().trim(), addressField.getText().trim());
                supplierService.updateSupplier(s);
                refreshTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Supplier updated successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating supplier: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == deleteButton) {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a supplier to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                supplierService.deleteSupplier(id);
                refreshTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Supplier deleted successfully.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting supplier: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == clearButton) {
            clearForm();
        }
    }

    private void clearForm() {
        idField.setText("");
        idField.setEnabled(true);
        nameField.setText("");
        contactField.setText("");
        addressField.setText("");
        table.clearSelection();
    }
}