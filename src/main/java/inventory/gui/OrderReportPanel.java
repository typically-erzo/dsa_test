package inventory.gui;

import inventory.model.StockOrder;
import inventory.service.StockReportService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class OrderReportPanel extends JPanel implements ActionListener {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField prodIdField, qtyField, costField;
    private JButton orderButton, refreshButton;
    private StockReportService reportService;

    public OrderReportPanel() {
        reportService = new StockReportService();
        setLayout(null);
        setBounds(0, 0, 960, 680);

        tableModel = new DefaultTableModel(new String[]{"Order ID", "Product ID", "Quantity", "Total Cost ($)", "Order Date"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 30, 900, 300);
        add(scrollPane);

        // Form Fields (Row 1 - Y: 360)
        JLabel lblProdId = new JLabel("Product ID:");
        lblProdId.setBounds(30, 360, 100, 25);
        add(lblProdId);

        prodIdField = new JTextField();
        prodIdField.setBounds(140, 360, 280, 25);
        add(prodIdField);

        JLabel lblQty = new JLabel("Quantity:");
        lblQty.setBounds(490, 360, 100, 25);
        add(lblQty);

        qtyField = new JTextField();
        qtyField.setBounds(600, 360, 280, 25);
        add(qtyField);

        // Form Fields (Row 2 - Y: 400)
        JLabel lblCost = new JLabel("Total Cost ($):");
        lblCost.setBounds(30, 400, 100, 25);
        add(lblCost);

        costField = new JTextField();
        costField.setBounds(140, 400, 280, 25);
        add(costField);

        // Action Buttons (Y: 460)
        orderButton = new JButton("Place Stock Order");
        orderButton.setBounds(30, 460, 180, 35);
        orderButton.addActionListener(this);
        add(orderButton);

        refreshButton = new JButton("Refresh Report");
        refreshButton.setBounds(230, 460, 180, 35);
        refreshButton.addActionListener(this);
        add(refreshButton);

        refreshTable();
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        try {
            List<StockOrder> orders = reportService.getAllOrders();
            for (StockOrder o : orders) {
                tableModel.addRow(new Object[]{
                        o.getOrderId(), o.getProductId(), o.getQuantity(), o.getTotalCost(), o.getOrderDate()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading orders: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == orderButton) {
            try {
                StockOrder order = new StockOrder(
                        0,
                        prodIdField.getText().trim(),
                        Integer.parseInt(qtyField.getText().trim()),
                        Double.parseDouble(costField.getText().trim()),
                        LocalDate.now().toString()
                );
                reportService.placeOrder(order);
                refreshTable();
                prodIdField.setText("");
                qtyField.setText("");
                costField.setText("");
                JOptionPane.showMessageDialog(this, "Stock order placed & inventory updated successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid order details: Fill all missing Fields ", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == refreshButton) {
            refreshTable();
        }
    }
}