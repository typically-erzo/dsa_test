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

        int startY = 360;
        int lblX = 30, txtX = 140, width = 280, height = 25;

        add(createLabel("Product ID:", lblX, startY));
        prodIdField = new JTextField();
        prodIdField.setBounds(txtX, startY, width, height);
        add(prodIdField);

        add(createLabel("Quantity:", lblX + 460, startY));
        qtyField = new JTextField();
        qtyField.setBounds(txtX + 460, startY, width, height);
        add(qtyField);

        startY += 40;
        add(createLabel("Total Cost ($):", lblX, startY));
        costField = new JTextField();
        costField.setBounds(txtX, startY, width, height);
        add(costField);

        startY += 60;
        int btnW = 180, btnH = 35;

        orderButton = new JButton("Place Stock Order");
        orderButton.setBounds(30, startY, btnW, btnH);
        orderButton.addActionListener(this);
        add(orderButton);

        refreshButton = new JButton("Refresh Report");
        refreshButton.setBounds(230, startY, btnW, btnH);
        refreshButton.addActionListener(this);
        add(refreshButton);

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
                JOptionPane.showMessageDialog(this, "Invalid order details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == refreshButton) {
            refreshTable();
        }
    }
}