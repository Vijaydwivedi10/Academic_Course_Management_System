package org.example;

import java.sql.*;
import java.io.*;

public class Student {

    static Connection conn = null;
    static Statement stmt = null;

    static PreparedStatement pstmt = null;

    static boolean Reg_course(int course_id, String email) {
        try {
            conn = ConnectionUtil.getConnection();
            stmt = conn.createStatement();

            String query = "SELECT curr_sem FROM students WHERE email = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            int curr_sem = 0;
            if (rs.next()) {
                curr_sem = rs.getInt("curr_sem");
            }

            String query2 = "SELECT course_id FROM course_catalog WHERE course_id = ?";
            pstmt = conn.prepareStatement(query2);
            pstmt.setInt(1, course_id);
            rs = pstmt.executeQuery();

            boolean courseExists = false;
            if (rs.next()) {
                courseExists = true;
            }

            if (courseExists) {
                // insert grade with -1 value for current semester and chosen course
                String insertQuery = "INSERT INTO grades (email, course_id, semester, grade) VALUES (?, ?, ?, ?)";
                pstmt = conn.prepareStatement(insertQuery);
                pstmt.setString(1, email);
                pstmt.setInt(2, course_id);
                pstmt.setInt(3, curr_sem);
                pstmt.setInt(4, -1);
                pstmt.executeUpdate();

                System.out.println("Grade with value -1 inserted successfully.");
            } else {
                System.out.println("Chosen course is not available in course catalog.");
            }

            // Close the result set and statement
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());

        }

        return true;

    }

    static boolean dereg_course(int course_id) {

        try {
            conn = ConnectionUtil.getConnection();
            stmt = conn.createStatement();

            String selectQuery = "SELECT email FROM users WHERE token IS NOT NULL";
            PreparedStatement pstmt = conn.prepareStatement(selectQuery);

            // Executing the select statement
            ResultSet rs = pstmt.executeQuery();

            // Iterating over the result set and printing the usernames
            String email = "e";
            if (rs.next()) {
                email = rs.getString("email");

                System.out.println("--------" + email);

            }

            String deleteQuery = "DELETE FROM grades WHERE email = ? AND course_id = ? AND grade = -1";

            pstmt = conn.prepareStatement(deleteQuery);
            pstmt.setString(1, email);
            pstmt.setInt(2, course_id);
            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted == 0)
                System.out
                        .println("No course with grade -1 found for email: " + email + " and course_id: " + course_id);
            else
                System.out.println("Course with grade -1 deleted successfully for email: " + email + " and course_id: "
                        + course_id);

            // Close the result set and statement
            rs.close();
            pstmt.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
            return false;
        }

        return true;
    }

    static boolean view_grade(String email) {

        try {
            // Establishing a database connection
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db", "postgres",
                    "1234");

            // Preparing the select statement
            String selectQuery = "SELECT course_id, semester, grade FROM grades WHERE email = ?";
            PreparedStatement pstmt = conn.prepareStatement(selectQuery);
            pstmt.setString(1, email);

            // Executing the select statement
            ResultSet rs = pstmt.executeQuery();

            int x = 0;

            // Iterating over the result set and printing the grades
            while (rs.next()) {
                x = 1;
                int courseId = rs.getInt("course_id");
                int semester = rs.getInt("semester");
                int grade = rs.getInt("grade");

                System.out.println("Course ID: " + courseId + " | Semester: " + semester + " | Grade: " + grade);
            }

            if (x == 0)
                System.out.println("Student with " + email + " does not exist");

            // Closing the result set, statement, and database connection
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }

        return true;
    }

    static boolean update_profile(String address, String phone, String email) {

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db", "postgres",
                    "1234");
            // Update the address and phone of the user with email "example@example.com"

            String updateQuery = "UPDATE users SET address = ?, phone = ? WHERE email = ?";
            pstmt = conn.prepareStatement(updateQuery);

            // Set the new address and phone values
            pstmt.setString(1, address);
            pstmt.setString(2, phone);

            // Set the email of the user to update
            pstmt.setString(3, email);

            // Execute the update statement
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("User details updated successfully");
            } else {
                System.out.println("No user found with the specified email");
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception: " + e.getMessage());
            }
        }

        return true;
    }

    static boolean change_pass(String password) {

        try {
            conn = ConnectionUtil.getConnection();
            stmt = conn.createStatement();

            Currentuser c = new Currentuser();
            String email = c.getEmail();

            String updateQuery = "UPDATE users SET password = ? WHERE email = ?";
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setString(1, password);
            pstmt.setString(2, email);

            pstmt.executeUpdate();

            // Close the result set and statement

            pstmt.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
            return false;
        }

        return true;
    }

    static boolean case6() {

        return true;
    }

    static boolean TranscriptGenerator(String studentEmail) {
        try {
            conn = ConnectionUtil.getConnection();
            stmt = conn.createStatement();

            String sql = "SELECT s.entry_number, s.batch, s.department, s.cgpa, g.course_id, g.semester, g.grade "
                    + "FROM students s JOIN grades g ON s.email = g.email WHERE s.email = ? ORDER BY g.semester";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, studentEmail);

                ResultSet rs = pstmt.executeQuery();

                String filename = "transcript.txt";
                FileWriter writer = new FileWriter(filename);

                writer.write("Transcript for Student: " + studentEmail + "\n\n");
                writer.write(String.format("| %-15s | %-10s | %-20s | %-10s |\n", "Entry Number", "Batch", "Department",
                        "CGPA"));

                if (rs.next()) {
                    String entryNumber = rs.getString("entry_number");
                    int batch = rs.getInt("batch");
                    String department = rs.getString("department");
                    float cgpa = rs.getFloat("cgpa");

                    writer.write(String.format("| %-15s | %-10d | %-20s | %-10.2f |\n\n", entryNumber, batch,
                            department, cgpa));

                    writer.write(String.format("| %-10s | %-10s | %-10s |\n", "Course ID", "Semester", "Grade"));

                    do {
                        int courseId = rs.getInt("course_id");
                        int semester = rs.getInt("semester");
                        int grade = rs.getInt("grade");

                        writer.write(String.format("| %-10d | %-10d | %-10d |\n", courseId, semester, grade));
                    } while (rs.next());

                } else {
                    writer.write("No grades found for student " + studentEmail);
                }

                writer.close();
                System.out.println("Transcript generated successfully.");

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

}
