import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection.java
 *
 * Single responsibility: open (and hand out) a JDBC connection to our
 * PostgreSQL database. Keeping this logic in one place means that if
 * the DB URL/credentials ever change, we only edit ONE file instead of
 * hunting through the whole codebase.
 *
 * NOTE: For a real production app, credentials should come from
 * environment variables or a config file, NOT be hard-coded like
 * below. They are hard-coded here only to keep this learning project
 * simple to run.
 */
public class DBConnection {

    // ---- Update these 3 values to match your local PostgreSQL setup ----
    private static final String URL = "jdbc:postgresql://localhost:5432/employee_management";
    private static final String USER = "postgres";
    private static final String PASSWORD = "your_password_here";

    /**
     * Opens and returns a new JDBC Connection.
     *
     * We open a fresh connection each time this is called (simple
     * approach for a learning project). In bigger apps you'd normally
     * use a connection pool like HikariCP instead of opening a new
     * connection every time.
     *
     * @return an open java.sql.Connection to the PostgreSQL database
     * @throws SQLException if the connection cannot be established
     *         (wrong URL, wrong password, DB not running, etc.)
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Explicitly loading the driver class. Modern JDBC (4.0+)
            // usually auto-loads it, but doing this explicitly makes
            // errors clearer if the PostgreSQL driver jar is missing
            // from the classpath.
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC driver not found. " +
                    "Make sure postgresql-<version>.jar is on the classpath.", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
