CREATE DATABASE IF NOT EXISTS inventory_db;
USE inventory_db;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS stock_orders;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS suppliers;
DROP TABLE IF EXISTS users;
SET FOREIGN_KEY_CHECKS = 1;

-- Users / Admin table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- Pre-seed strictly the default admin
INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'ADMIN');

