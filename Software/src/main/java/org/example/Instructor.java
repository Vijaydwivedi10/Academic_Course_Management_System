package org.example;

import java.sql.*;
import java.util.*;

public class Instructor {
    static Scanner input = new Scanner(System.in);

    static Connection conn = null;
    static Statement stmt = null;

    static boolean upload_grades(String email, int course_id, int semester, int newGrade) {
        try {
            conn = ConnectionUtil.getConnection();
            stmt = conn.createStatement();

            String updateQuery = "UPDATE grades SET grade = ? WHERE email = ? AND course_id = ? AND semester = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateQuery);
            pstmt.setInt(1, newGrade);
            pstmt.setString(2, email);
            pstmt.setInt(3, course_id);
            pstmt.setInt(4, semester);
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated == 0) {
                System.out.println("No record found to update for email: " + email + ", course_id: " + course_id
                        + ", and semester: " + semester);
            } else {
                System.out.println("Grade updated successfully for email: " + email + ", course_id: " + course_id
                        + ", and semester: " + semester);
            }

            // Close the result set and statement
            pstmt.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
            return false;
        }

        return true;
    }

    static boolean update_grad() {

        return true;
    }

    static boolean view_grad(String instructorEmail) {

        try {
            conn = ConnectionUtil.getConnection();
            stmt = conn.createStatement();
            // Prepare the SQL query to get all courses offered by the instructor
            String sql = "SELECT c.course_id, c.course_name FROM course_catalog c, course_offerings o, instructors i "
                    + "WHERE c.course_id = o.course_id AND o.instructor_id = i.instructor_id AND i.email = ?";

            // Create a PreparedStatement object and set the parameter
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, instructorEmail);

            // Execute the query and get the ResultSet object
            ResultSet rs = pstmt.executeQuery();

            // Loop through the result set and print the courses offered by the instructor
            while (rs.next()) {
                int courseId = rs.getInt("course_id");
                String courseName = rs.getString("course_name");
                System.out.println("Course ID: " + courseId + ", Course Name: " + courseName);
            }

            // Close the ResultSet and PreparedStatement objects
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
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
            PreparedStatement pstmt = conn.prepareStatement(updateQuery);
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

    static boolean update_profile(String address, String phone, String email) {

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db", "postgres",
                    "1234");
            // Update the address and phone of the user with email "example@example.com"

            String updateQuery = "UPDATE users SET address = ?, phone = ? WHERE email = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateQuery);

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

                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception: " + e.getMessage());
            }
        }

        return true;
    }

    public static boolean addCourseOffering(int course_id, String instructor_email, float cgpa_constraint) {
        try {
            conn = ConnectionUtil.getConnection();
            stmt = conn.createStatement();

            // Check if the instructor exists
            String instructorQuery = "SELECT instructor_id FROM instructors WHERE email = ?";
            PreparedStatement pstmt = conn.prepareStatement(instructorQuery);
            pstmt.setString(1, instructor_email);
            ResultSet rs = pstmt.executeQuery();

            int instructor_id = 0;
            if (rs.next()) {
                instructor_id = rs.getInt("instructor_id");
            } else {
                System.out.println("Instructor with email " + instructor_email + " does not exist.");
                return false;
            }

            // Check if the course already exists in the course_offerings table
            String existingCourseQuery = "SELECT course_id FROM course_offerings WHERE course_id = ? AND instructor_id = ?";
            pstmt = conn.prepareStatement(existingCourseQuery);
            pstmt.setInt(1, course_id);
            pstmt.setInt(2, instructor_id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Course with ID " + course_id + " is already offered by instructor with email " + instructor_email + ".");
                return false;
            }

            // Add the course to course_offerings
            String addCourseQuery = "INSERT INTO course_offerings (course_id, instructor_id, cgpa_constraint, min_credits) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(addCourseQuery);
            pstmt.setInt(1, course_id);
            pstmt.setInt(2, instructor_id);
            pstmt.setFloat(3, cgpa_constraint);
            pstmt.setInt(4, 0); // Dummy value for min_credits
            pstmt.executeUpdate();

            System.out.println("Course with ID " + course_id + " added to course offerings for instructor with email " + instructor_email + ".");

            // Close the result set and statement
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
            return false;
        }

        return true;
    }



}
