USE inventory_db;

-- Suppliers table
CREATE TABLE suppliers (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact_info VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL
);

-- Products table
CREATE TABLE products (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    supplier_id VARCHAR(50),
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE SET NULL
);

-- Stock Orders table
CREATE TABLE stock_orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id VARCHAR(50),
    quantity INT NOT NULL,
    total_cost DECIMAL(10,2) NOT NULL,
    order_date VARCHAR(30) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Sample Test Records (Suppliers)
INSERT INTO suppliers (id, name, contact_info, address) VALUES
('SUP-001', 'Apex Electronics Ltd', '555-0192', '102 Silicon Valley, CA'),
('SUP-002', 'Global Hardware Inc', '555-8831', '44 Industrial Pkwy, TX'),
('SUP-003', 'Prime Office Supplies', '555-3342', '789 Commerce St, NY');

-- Sample Test Records (Products)
INSERT INTO products (id, name, category, quantity, supplier_id, price) VALUES
('PRD-101', 'Wireless Ergonomic Mouse', 'Electronics', 45, 'SUP-001', 29.99),
('PRD-102', 'Mechanical RGB Keyboard', 'Electronics', 12, 'SUP-001', 89.50),
('PRD-103', 'Adjustable Desk Chair', 'Furniture', 8, 'SUP-002', 249.99),
('PRD-104', 'Stainless Steel Tumbler', 'Kitchenware', 85, 'SUP-003', 19.99),
('PRD-105', 'LED Desk Lamp', 'Electronics', 5, 'SUP-001', 34.99);

-- Sample Test Records (Stock Orders)
INSERT INTO stock_orders (product_id, quantity, total_cost, order_date) VALUES
('PRD-101', 20, 599.80, '2026-03-01'),
('PRD-103', 5, 1249.95, '2026-03-02');

