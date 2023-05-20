package org.example;

import static org.example.Instructor.conn;
import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.*;

import org.junit.jupiter.api.Test;

public class InstructorTest {
    @Test
    void testCase1() {
        // Set up test data
        String email = "john@example.com";
        int course_id = 104;
        int semester = 2;
        int newGrade = 9;

        // Call the function under test
        boolean result = Instructor.upload_grades(email, course_id, semester, newGrade);

        // Assert the expected result
        assertTrue(result);
    }

    @Test
    void update_grad() {

    }

    @Test
    void testCase3() {
        String instructorEmail = "jane@example.com";
        assertTrue(Instructor.view_grad(instructorEmail));
    }

    @Test
    void testCase4() {

        boolean result = Instructor.update_profile("new address", "345678", "jane@example.com");

        // Check that the update was successful
        assertTrue(result);

        // Check that the updated data is correct
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db", "postgres",
                "1234")) {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE email = ?");
            pstmt.setString(1, "jane@example.com");
            ResultSet rs = pstmt.executeQuery();
            assertTrue(rs.next());
            assertEquals("new address", rs.getString("address"));
            assertEquals("345678", rs.getString("phone"));
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }

    }

    @Test
    void testCase5() {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Statement stmt = null;
        // Set up test data
        String newPassword = "np";

        // Call the method being tested
        boolean result = Instructor.change_pass(newPassword);

        // Check the result
        assertTrue(result, "Failed to update password");

        // Verify that the password was updated in the database
        try {
            conn = ConnectionUtil.getConnection();
            stmt = conn.createStatement();

            Currentuser c = new Currentuser();
            String email = c.getEmail();

            String selectQuery = "SELECT password FROM users WHERE email = ?";
            pstmt = conn.prepareStatement(selectQuery);
            pstmt.setString(1, email);

            rs = pstmt.executeQuery();
            rs.next();
            String actualPassword = rs.getString("password");

            assertEquals(newPassword, actualPassword, "Password was not updated in the database");

            // Clean up
            pstmt.close();
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddCourseOffering() throws SQLException {
        // Create a test course, instructor, and CGPA constraint
        int testCourseId = 105;
        String testInstructorEmail = "jane@example.com";
        float testCgpaConstraint = 3.0f;



        // Add the test course offering
        assertTrue(Instructor.addCourseOffering(testCourseId, testInstructorEmail, testCgpaConstraint));

        // Try to add the course offering again with the same course ID and instructor email
        assertFalse(Instructor.addCourseOffering(testCourseId, testInstructorEmail, testCgpaConstraint));

        // Clean up the test data
        // Get the instructor ID
        String instructorQuery = "SELECT instructor_id FROM instructors WHERE email = ?";
        PreparedStatement pstmt = conn.prepareStatement(instructorQuery);
        pstmt.setString(1, testInstructorEmail);
        ResultSet rs = pstmt.executeQuery();

        int instructor_id = 0;
        if (rs.next()) {
            instructor_id = rs.getInt("instructor_id");
        } else {
            System.out.println("Instructor with email " + testInstructorEmail + " does not exist.");
        }

        // Delete the course from course_offerings
        String deleteCourseQuery = "DELETE FROM course_offerings WHERE course_id = ? AND instructor_id = ?";
        pstmt = conn.prepareStatement(deleteCourseQuery);
        pstmt.setInt(1, testCourseId);
        pstmt.setInt(2, instructor_id);
        int rowsDeleted = pstmt.executeUpdate();
    }

}
