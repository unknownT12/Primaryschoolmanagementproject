#!/bin/bash

# Database Setup Script for Primary School Admin
# This script will create the database and all required tables

echo "=========================================="
echo "Primary School Admin - Database Setup"
echo "=========================================="
echo ""

# Check if MySQL is installed
if ! command -v mysql &> /dev/null; then
    echo "❌ MySQL is not installed or not in PATH"
    echo "Please install MySQL first: https://dev.mysql.com/downloads/mysql/"
    exit 1
fi

echo "✅ MySQL found"
echo ""

# Prompt for MySQL root password
echo "Enter MySQL root password (or press Enter if no password):"
read -s MYSQL_PASSWORD

# If password is empty, try without password
if [ -z "$MYSQL_PASSWORD" ]; then
    MYSQL_CMD="mysql -u root"
else
    MYSQL_CMD="mysql -u root -p$MYSQL_PASSWORD"
fi

echo ""
echo "Creating database and tables..."
echo ""

# Run the SQL script
if $MYSQL_CMD < setup_database.sql; then
    echo ""
    echo "✅ Database setup completed successfully!"
    echo ""
    echo "Database: schooldb"
    echo "Tables created:"
    echo "  - users"
    echo "  - students"
    echo "  - teachers"
    echo "  - subjects"
    echo "  - grades"
    echo "  - classes"
    echo ""
    echo "Next steps:"
    echo "1. Make sure your db.properties file has the correct MySQL credentials"
    echo "2. Create an admin user through the registration page or directly in the database"
    echo "3. Start your application server"
else
    echo ""
    echo "❌ Database setup failed!"
    echo "Please check:"
    echo "  - MySQL is running (try: mysql.server start)"
    echo "  - MySQL root password is correct"
    echo "  - You have permission to create databases"
    exit 1
fi

