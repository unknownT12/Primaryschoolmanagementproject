#!/bin/bash

# Database Export Script for Primary School Admin
# This script exports the database structure and data

echo "=========================================="
echo "Primary School Admin - Database Export"
echo "=========================================="
echo ""

# Check if MySQL is installed
if ! command -v mysql &> /dev/null; then
    echo "❌ MySQL is not installed or not in PATH"
    exit 1
fi

echo "✅ MySQL found"
echo ""

# Prompt for MySQL root password
echo "Enter MySQL root password (or press Enter if no password):"
read -s MYSQL_PASSWORD

# Create export directory
EXPORT_DIR="database_export"
mkdir -p "$EXPORT_DIR"

TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
STRUCTURE_FILE="$EXPORT_DIR/schooldb_structure_$TIMESTAMP.sql"
DATA_FILE="$EXPORT_DIR/schooldb_data_$TIMESTAMP.sql"
FULL_DUMP_FILE="$EXPORT_DIR/schooldb_full_$TIMESTAMP.sql"

# If password is empty, try without password
if [ -z "$MYSQL_PASSWORD" ]; then
    MYSQL_CMD="mysql -u root"
    MYSQLDUMP_CMD="mysqldump -u root"
else
    MYSQL_CMD="mysql -u root -p$MYSQL_PASSWORD"
    MYSQLDUMP_CMD="mysqldump -u root -p$MYSQL_PASSWORD"
fi

echo ""
echo "Exporting database..."

# Export structure only (no data)
echo "1. Exporting database structure..."
$MYSQLDUMP_CMD --no-data schooldb > "$STRUCTURE_FILE" 2>&1

# Export data only (no structure)
echo "2. Exporting database data..."
$MYSQLDUMP_CMD --no-create-info schooldb > "$DATA_FILE" 2>&1

# Export full dump (structure + data)
echo "3. Exporting full database dump..."
$MYSQLDUMP_CMD schooldb > "$FULL_DUMP_FILE" 2>&1

# Check if exports were successful
if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Database export completed successfully!"
    echo ""
    echo "Exported files in '$EXPORT_DIR' directory:"
    echo "  📄 Structure only: $STRUCTURE_FILE"
    echo "  📄 Data only: $DATA_FILE"
    echo "  📄 Full dump: $FULL_DUMP_FILE"
    echo ""
    echo "You can use the full dump to restore the database:"
    echo "  mysql -u root -p schooldb < $FULL_DUMP_FILE"
else
    echo ""
    echo "❌ Database export failed!"
    echo "Please check:"
    echo "  - MySQL is running"
    echo "  - MySQL root password is correct"
    echo "  - Database 'schooldb' exists"
    exit 1
fi



