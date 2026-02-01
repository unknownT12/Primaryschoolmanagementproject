package util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utility to fetch a Connection from the container-managed JNDI pool.
 * Uses the Resource name "jdbc/schooldb" as defined in context.xml.
 */
public class DBConnection {

    private static DataSource dataSource;
    private static String jdbcUrl;
    private static String jdbcUser;
    private static String jdbcPassword;

    static {
        try {
            // Obtain JNDI initial context
            Context initContext = new InitialContext();
            // java:comp/env is required when looking up resources in webapp context
            Context envContext = (Context) initContext.lookup("java:comp/env");
            // Look up the DataSource; the name must match context.xml Resource name
            dataSource = (DataSource) envContext.lookup("jdbc/schooldb");
            System.out.println("[DBConnection] Using container-managed DataSource jdbc/schooldb");
        } catch (NamingException e) {
            // JNDI lookup failed – fall back to a simple DriverManager configuration
            System.err.println("[DBConnection] JNDI lookup failed (" + e.getMessage() + "). Falling back to db.properties");
            loadFallbackProperties();
        }
    }

    private static void loadFallbackProperties() {
        Properties props = new Properties();
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new IllegalStateException("db.properties not found on classpath");
            }
            props.load(in);
            String driver = props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
            jdbcUrl = props.getProperty("db.url");
            jdbcUser = props.getProperty("db.username");
            jdbcPassword = props.getProperty("db.password");

            if (jdbcUrl == null || jdbcUser == null) {
                throw new IllegalStateException("db.url and db.username must be provided in db.properties");
            }

            Class.forName(driver);
            System.out.println("[DBConnection] Initialized fallback DriverManager connection for " + jdbcUrl);
        } catch (IOException | ClassNotFoundException ex) {
            throw new ExceptionInInitializerError("Failed to load fallback DB settings: " + ex.getMessage());
        }
    }

    /**
     * Get a pooled connection. Caller must close it (connection.close()),
     * which returns it to the pool rather than physically closing it.
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource != null) {
            return dataSource.getConnection();
        }
        if (jdbcUrl != null) {
            return DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
        }
        throw new SQLException("No datasource or JDBC configuration available");
    }
}
