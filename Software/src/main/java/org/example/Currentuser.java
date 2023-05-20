package org.example;

import java.security.SecureRandom;
import java.sql.*;
import java.util.Base64;

public class Currentuser {

    private static final SecureRandom random = new SecureRandom();

    public static String generateToken(int length) {
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }


    private String email;
    private String password;

    public String getEmail() throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        try {
            // Preparing the select statement
            String selectQuery = "SELECT email FROM users WHERE token IS NOT NULL";
            PreparedStatement pstmt = conn.prepareStatement(selectQuery);

            // Executing the select statement
            ResultSet rs = pstmt.executeQuery();

            // Iterating over the result set and printing the usernames
            if (rs.next()) {
                email = rs.getString("email");

                System.out.println(email);

            }

            // Closing the result set, statement, and database connection
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return email;
    }

    public void setEmail(String email) {
        try {
            // Establishing a database connection
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db", "postgres",
                    "1234");

            // Preparing the update statement
            String updateQuery = "UPDATE users SET token = ? WHERE email = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateQuery);
            pstmt.setString(1, generateToken(16));
            pstmt.setString(2, email);

            // Executing the update statement
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated");

            // Closing the database connection and statement
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }

        this.email = email;
    }

    public void resetTokens() {
        try {
            // Establishing a database connection
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db", "postgres", "1234");

            // Preparing the update statement
            String updateQuery = "UPDATE users SET token = NULL";
            PreparedStatement pstmt = conn.prepareStatement(updateQuery);

            // Executing the update statement
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated");

            // Closing the database connection and statement
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }

}
