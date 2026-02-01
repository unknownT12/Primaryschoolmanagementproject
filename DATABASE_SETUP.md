# Database Setup Guide

This guide will help you set up the MySQL database for the Primary School Admin application.

## Prerequisites

- MySQL 8.0 or later installed
- MySQL server running
- Root access to MySQL (or a user with CREATE DATABASE privileges)

## Quick Setup

### Option 1: Using the Setup Script (Recommended)

1. Make sure MySQL server is running:
   ```bash
   mysql.server start
   ```

2. Run the setup script:
   ```bash
   ./setup_database.sh
   ```

3. Enter your MySQL root password when prompted (or press Enter if no password)

### Option 2: Manual Setup

1. Start MySQL server (if not running):
   ```bash
   mysql.server start
   ```

2. Run the SQL script:
   ```bash
   mysql -u root -p < setup_database.sql
   ```
   (Enter your MySQL root password when prompted)

   Or if you don't have a password:
   ```bash
   mysql -u root < setup_database.sql
   ```

## Verify Database Setup

After running the setup, verify the database was created:

```bash
mysql -u root -p -e "USE schooldb; SHOW TABLES;"
```

You should see these tables:
- users
- students
- teachers
- subjects
- grades
- classes

## Database Configuration

The application uses the following database settings (configured in `src/main/resources/db.properties`):

- **Database Name**: `schooldb`
- **Host**: `127.0.0.1:3306`
- **Username**: `root`
- **Password**: `MyNewPassword123!` (update this in `db.properties` if different)

### Update Database Credentials

If your MySQL root password is different, update `src/main/resources/db.properties`:

```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://127.0.0.1:3306/schooldb?useSSL=false&serverTimezone=UTC
db.username=root
db.password=YOUR_PASSWORD_HERE
```

## Create Admin User

After setting up the database, you need to create an admin user. You can do this in two ways:

### Option 1: Through the Application
1. Start your application server
2. Navigate to the registration page
3. Register a new user with role "admin"

### Option 2: Directly in Database
1. Connect to MySQL:
   ```bash
   mysql -u root -p schooldb
   ```

2. Insert admin user (you'll need to hash the password using your PasswordUtil class):
   ```sql
   INSERT INTO users (username, password, role, full_name) 
   VALUES ('admin', 'hashed_password_here', 'admin', 'System Administrator');
   ```

   **Note**: The password must be hashed using BCrypt. Use your `PasswordUtil` class to generate the hash, or use an online BCrypt generator.

## Troubleshooting

### MySQL Server Not Running
```bash
# Start MySQL server
mysql.server start

# Check if MySQL is running
mysql.server status
```

### Permission Denied
Make sure you have the correct MySQL root password, or create a new MySQL user with appropriate privileges:

```sql
CREATE USER 'schooladmin'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON schooldb.* TO 'schooladmin'@'localhost';
FLUSH PRIVILEGES;
```

Then update `db.properties` to use this user instead of root.

### Database Already Exists
If the database already exists and you want to start fresh:

```bash
mysql -u root -p -e "DROP DATABASE IF EXISTS schooldb;"
```

Then run the setup script again.

## Database Schema

The database includes the following tables:

- **users**: User accounts (admin, teacher, parent)
- **students**: Student information and records
- **teachers**: Teacher information and qualifications
- **subjects**: Subject catalog
- **grades**: Student grades/marks for subjects
- **classes**: Class names (e.g., "Standard 1", "Standard 2")

## Next Steps

1. ✅ Database created
2. ✅ Tables created
3. ⬜ Create admin user
4. ⬜ Start application server
5. ⬜ Test login

Good luck with your Primary School Admin application!

