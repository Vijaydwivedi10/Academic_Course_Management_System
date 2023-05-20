package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GradeUpdater {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/db";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "1234";

    static boolean GradeUpdaterfun() {
        try {
            // Establishing a database connection
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Starting a transaction
            conn.setAutoCommit(false);

            // Reading the CSV file
            try (BufferedReader reader = new BufferedReader(new FileReader("E:\\Project\\Images\\fuckoff\\src\\main\\java\\org\\example\\grades.csv"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    String email = data[0];
                    int course_id = Integer.parseInt(data[1]);
                    int semester = Integer.parseInt(data[2]);
                    int grade = Integer.parseInt(data[3]);

                    // Checking if the grade needs to be updated
                    PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM grades WHERE email=? AND course_id=? AND semester=? AND grade=-1");
                    pstmt.setString(1, email);
                    pstmt.setInt(2, course_id);
                    pstmt.setInt(3, semester);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        // Updating the grade
                        PreparedStatement updateStmt = conn.prepareStatement("UPDATE grades SET grade=? WHERE email=? AND course_id=? AND semester=?");
                        updateStmt.setInt(1, grade);
                        updateStmt.setString(2, email);
                        updateStmt.setInt(3, course_id);
                        updateStmt.setInt(4, semester);
                        updateStmt.executeUpdate();
                        updateStmt.close();
                    }

                    pstmt.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Committing the transaction
            conn.commit();

            // Checking if there are any students with -1 grade
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM grades WHERE grade=-1");
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Reverting all the updates
                Statement rollbackStmt = conn.createStatement();
                rollbackStmt.execute("ROLLBACK");
                rollbackStmt.close();
                System.out.println("Some grades could not be updated.");
            } else {
                System.out.println("All grades updated successfully.");
            }

            // Closing the database connection and statement
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        return true;
    }

}
