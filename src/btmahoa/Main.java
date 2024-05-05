package btmahoa;
import java.sql.*;

public class Main {
    // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/users";
    private static final String USERNAME = "your_username";
    private static final String PASSWORD = "your_password";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            // Create table if not exists
            createTableIfNotExists(conn);

            // Example: Register a new user
            registerUser(conn, "user1", "password123");

            // Example: Login
            boolean loginResult = login(conn, "user1", "password123");
            if (loginResult) {
                System.out.println("Login successful. Welcome!");
            } else {
                System.out.println("Login failed. Invalid username or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTableIfNotExists(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS users (username VARCHAR(50) PRIMARY KEY, password VARCHAR(255))");
        }
    }

    private static void registerUser(Connection conn, String username, String password) throws SQLException {
        // Hash the password before storing
        String hashedPassword = hashPassword(password);

        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.executeUpdate();
        }
    }

    private static boolean login(Connection conn, String username, String password) throws SQLException {
        // Hash the password before comparing
        String hashedPassword = hashPassword(password);

        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // If result set is not empty, login is successful
            }
        }
    }

    private static String hashPassword(String password) {
        // You can use any secure hashing algorithm here, like bcrypt or SHA-256
        // For simplicity, let's just return the password as is (not recommended for real-world applications)
        return password;
    }
}
