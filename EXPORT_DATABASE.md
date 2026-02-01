# Database Export Guide

This guide shows you how to export your `schooldb` database in multiple ways.

## Method 1: Using the Export Script (Easiest)

Run the export script:

```bash
./export_database.sh
```

Enter your MySQL root password when prompted. This will create:
- **Structure only**: Database schema without data
- **Data only**: Just the data without table structures
- **Full dump**: Complete database backup (structure + data)

Files will be saved in the `database_export` directory.

## Method 2: Using Command Line

### Export Full Database
```bash
mysqldump -u root -p schooldb > schooldb_backup.sql
```

### Export Structure Only (no data)
```bash
mysqldump -u root -p --no-data schooldb > schooldb_structure.sql
```

### Export Data Only (no structure)
```bash
mysqldump -u root -p --no-create-info schooldb > schooldb_data.sql
```

### If you don't have a password
```bash
mysqldump -u root schooldb > schooldb_backup.sql
```

## Method 3: Using IntelliJ IDEA

### View Database in IntelliJ

1. **Open Database Tool Window**:
   - View → Tool Windows → Database
   - Or press `Alt + 1` (Windows/Linux) or `Cmd + 1` (Mac)

2. **Add Data Source**:
   - Click the `+` button → Data Source → MySQL
   - Or right-click in Database tool window → New → Data Source → MySQL

3. **Configure Connection**:
   ```
   Host: 127.0.0.1 (or localhost)
   Port: 3306
   Database: schooldb
   User: root
   Password: MyNewPassword123!  (or your MySQL root password)
   ```

4. **Test Connection**:
   - Click "Test Connection"
   - If it asks to download MySQL driver, click "Download"

5. **View Database**:
   - Once connected, expand `schooldb` to see all tables
   - Double-click a table to view its data

### Export from IntelliJ

1. **Right-click on database or table** in the Database tool window
2. **Select**: `SQL Scripts` → `Export to File`
3. **Choose export options**:
   - Export structure (CREATE statements)
   - Export data (INSERT statements)
   - Or both
4. **Choose file location** and click OK

### Export Specific Tables

1. Select the tables you want (hold `Cmd`/`Ctrl` to select multiple)
2. Right-click → `SQL Scripts` → `Export to File`
3. Choose options and export

## Method 4: Export via MySQL Workbench

If you have MySQL Workbench installed:

1. Open MySQL Workbench
2. Connect to your MySQL server
3. Go to: Server → Data Export
4. Select `schooldb` database
5. Choose tables to export (or select all)
6. Choose export options:
   - Export to Self-Contained File
   - Include Create Schema
7. Click "Start Export"

## Restore Database from Export

To restore a database from an exported file:

```bash
mysql -u root -p schooldb < schooldb_backup.sql
```

Or create a new database and restore:

```bash
mysql -u root -p -e "CREATE DATABASE schooldb_backup;"
mysql -u root -p schooldb_backup < schooldb_backup.sql
```

## Quick Export Commands

### Export everything (recommended for backup)
```bash
mysqldump -u root -p schooldb > schooldb_$(date +%Y%m%d_%H%M%S).sql
```

### Export with compression (smaller file)
```bash
mysqldump -u root -p schooldb | gzip > schooldb_$(date +%Y%m%d_%H%M%S).sql.gz
```

### Restore compressed backup
```bash
gunzip < schooldb_backup.sql.gz | mysql -u root -p schooldb
```

## Export Specific Tables

```bash
mysqldump -u root -p schooldb users students teachers > specific_tables.sql
```

## View Database Schema (Structure)

### Using Command Line
```bash
mysql -u root -p -e "USE schooldb; SHOW TABLES;"
mysql -u root -p -e "USE schooldb; DESCRIBE students;"
```

### Using IntelliJ
- Expand database → Expand table
- Right-click table → Modify Table (to see structure)
- Or open SQL console and run: `DESCRIBE table_name;`

## Troubleshooting

### "Access Denied" Error
- Make sure you're using the correct MySQL root password
- Try: `mysql -u root -p` and enter password interactively

### "Database doesn't exist" Error
- Verify database exists: `mysql -u root -p -e "SHOW DATABASES;"`
- If missing, run `setup_database.sql` again

### IntelliJ Can't Connect
- Make sure MySQL server is running
- Check firewall settings
- Verify connection settings match `db.properties`
- Download MySQL driver in IntelliJ if prompted

## Current Database Configuration

Based on your `db.properties`:
- **Host**: 127.0.0.1
- **Port**: 3306
- **Database**: schooldb
- **User**: root
- **Password**: MyNewPassword123!

Make sure your export uses the same credentials!

