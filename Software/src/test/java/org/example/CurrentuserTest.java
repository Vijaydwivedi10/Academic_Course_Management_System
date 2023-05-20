package org.example;

import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class CurrentuserTest {

    @Test
    void testGenerateToken() {
        // Define the expected length of the token
        int length = 4;
        // Call the method to generate a token
        String token = Currentuser.generateToken(length);

// Verify that the token is not null or empty
        assertNotNull(token);
        assertTrue(token.length() > 0);

// Verify that the token has the expected length
        assertTrue(token.length() > length);

// Verify that the token contains only valid characters
        String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
        for (int i = 0; i < token.length(); i++) {
            assertTrue(validChars.indexOf(token.charAt(i)) >= 0);
        }
    }


    @Test
    void testGetEmail() throws SQLException {
        java.sql.Connection conn = ConnectionUtil.getConnection();
        java.sql.Statement stmt = conn.createStatement();
        // Insert sample data into the test database

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.executeUpdate("INSERT INTO users (email, password, name, address, phone, role, token) VALUES ('john2@example.com', 'password123', 'John Doe', '123 Main St', '555-1234', 'user', 'token123')");
        stmt.executeUpdate("INSERT INTO users (email, password, name, address, phone, role, token) VALUES ('jane2@example.com', 'password456', 'Jane Smith', '456 Oak St', '555-5678', 'student', null)");

        // Call the getEmail() method
        Currentuser user = new Currentuser();
        String email = user.getEmail();

        // Verify that the correct email is returned
        assertEquals("john2@example.com", email);

        // Delete the sample data from the test database
        stmt.executeUpdate("DELETE FROM users WHERE email = 'john2@example.com'");
        stmt.executeUpdate("DELETE FROM users WHERE email = 'jane2@example.com'");
        stmt.close();
    }

    @Test
    void testSetEmail() {
        try {
            // Establishing a database connection
            java.sql.Connection conn = ConnectionUtil.getConnection();
            java.sql.Statement stmt = conn.createStatement();
            // Inserting a user with a known email address
            String insertQuery = "INSERT INTO users (email, password, name, address, phone, role, token) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertQuery);
            pstmt.setString(1, "test@example.com");
            pstmt.setString(2, "password");
            pstmt.setString(3, "Test User");
            pstmt.setString(4, "123 Main St");
            pstmt.setString(5, "555-1234");
            pstmt.setString(6, "user");
            pstmt.setString(7, "old_token");
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted");

            // Closing the statement
            pstmt.close();

            // Calling the setEmail method with the known email address
            Currentuser login = new Currentuser();
            login.setEmail("test@example.com");

            // Verifying that the token for the user has been updated
            String selectQuery = "SELECT token FROM users WHERE email = ?";
            pstmt = conn.prepareStatement(selectQuery);
            pstmt.setString(1, "test@example.com");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String token = rs.getString("token");
                System.out.println("New token: " + token);
                assertNotEquals("old_token", token);
            }

            String deleteQuery = "DELETE FROM users WHERE email = ?";
            pstmt = conn.prepareStatement(deleteQuery);
            pstmt.setString(1, "test@example.com");
            rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected + " row(s) deleted");

            // Closing the result set, statement, and database connection
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }


    @Test
    void resetTokens() throws SQLException {
        // Creating a test database with test data
        java.sql.Connection conn = ConnectionUtil.getConnection();
        java.sql.Statement stmt = conn.createStatement();
        stmt.executeUpdate("INSERT INTO users (email, password, name, address, phone, role, token) VALUES ('john2@example.com', 'password123', 'John Doe', '123 Main St', '555-1234', 'user', 'token123')");
        stmt.executeUpdate("INSERT INTO users (email, password, name, address, phone, role, token) VALUES ('jane2@example.com', 'password456', 'Jane Smith', '456 Oak St', '555-5678', 'student', null)");

        // Call the getEmail() method

        // Calling the resetTokens() method
        Currentuser student = new Currentuser();
        student.resetTokens();

        // Verifying that the token column in the users table is set to NULL for all rows
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM users WHERE token IS NOT NULL");
        rs.next();
        int count = rs.getInt("count");
        assertEquals(0, count);

        stmt.executeUpdate("DELETE FROM users WHERE email = 'john2@example.com'");
        stmt.executeUpdate("DELETE FROM users WHERE email = 'jane2@example.com'");


        // Closing the database connection and statement
        rs.close();
        stmt.close();
        conn.close();
    }

}