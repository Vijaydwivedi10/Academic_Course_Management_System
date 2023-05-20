package org.example;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginTest {

    private static Connection conn;

    @BeforeAll
    static void setup() throws SQLException {
        // Establishing a database connection
        java.sql.Connection conn = ConnectionUtil.getConnection();
        java.sql.Statement stmt = conn.createStatement();
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Creating a test user
        stmt.executeUpdate("INSERT INTO users (email, password, name, address, phone, role, token) VALUES ('john2@example.com', 'password123', 'John Doe', '123 Main St', '555-1234', 'student', 'token123')");

    }

    @AfterAll
    static void cleanup() throws SQLException {
        // Deleting the test user

        java.sql.Connection conn = ConnectionUtil.getConnection();
        java.sql.Statement stmt = conn.createStatement();

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.executeUpdate("DELETE FROM users WHERE email = 'john2@example.com'");


        // Closing the database connection
        conn.close();
    }

    @Test
    void login_val_should_return_true_for_valid_credentials() {
        assertTrue(Login.Login_val("john2@example.com", "password123", "student"));
    }

    @Test
    void login_val_should_return_false_for_invalid_credentials() {
        assertFalse(Login.Login_val("john2@example.com", "wrong-password", "admin"));
        assertFalse(Login.Login_val("john2@example.com", "password123", "instructor"));
    }
}
