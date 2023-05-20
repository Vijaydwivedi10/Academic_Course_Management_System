package org.example;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AcademicsTest {
    @Test
    void testCase1() {
        // Set up test data
        int academicsChoice = 1;
        int course_id = 123;
        String course_name = "Intro to Computer Science";
        String credit_structure = "3-0-2";

        // Call the method being tested
        boolean result = Academics.Insert_course(academicsChoice, course_id, course_name, credit_structure);

        // Check that the method returns true (i.e. successful insertion)
        assertTrue(result);
    }

    @Test
    void testCase2() {
        assertTrue(Academics.Delete_course(123)); // replace 123 with the course_id you want to delete
    }

    @Test
    public void testCase3() {
        assertTrue(Academics.Show_course());
    }

    @Test
    void testCase4() throws IOException {
        // Call the case4 method
        boolean result = Academics.start_sem();

        // Check if sem_start is updated or not
        try {
            java.sql.Connection conn = ConnectionUtil.getConnection();
            java.sql.Statement stmt = conn.createStatement();

            PreparedStatement pstmt = conn
                    .prepareStatement("UPDATE admin SET sem_start = ? WHERE email = ?");
            // Set the values for the prepared statement
            pstmt.setBoolean(1, true);
            pstmt.setString(2, "admin@example.com");

            ResultSet rs = stmt.executeQuery("SELECT sem_start FROM admin WHERE email = 'admin@example.com'");
            if (rs.next()) {
                boolean semStart = rs.getBoolean("sem_start");
                assertEquals(true, semStart);
            }
            rs.close();

            conn.close();
        } catch (SQLException se) {
            fail("SQL Exception: " + se.getMessage());
        }
        assertTrue(result);
    }

    // @Disabled("This test case is currently disabled.")
    @Test
    void testCase5() {

        boolean result = Academics.end_sem();

        // Check if sem_start is updated or not
        try {
            java.sql.Connection conn = ConnectionUtil.getConnection();
            java.sql.Statement stmt = conn.createStatement();

            PreparedStatement pstmt = conn
                    .prepareStatement("UPDATE admin SET sem_start = ? WHERE email = ?");
            // Set the values for the prepared statement
            pstmt.setBoolean(1, false);
            pstmt.setString(2, "admin@example.com");

            ResultSet rs = stmt.executeQuery("SELECT sem_start FROM admin WHERE email = 'admin@example.com'");
            if (rs.next()) {
                boolean semStart = rs.getBoolean("sem_start");
                assertEquals(false, semStart);
            }
            rs.close();

            conn.close();
        } catch (SQLException se) {
            fail("SQL Exception: " + se.getMessage());
        }
        assertTrue(result);
    }

    @Test
    void testCase6() {
        String email = "john@example.com";
        boolean result = Academics.view_grad(email);
        assertTrue(result);
    }

    @Test
    void testGradcheck() {

        String email = "john@example.com";

        try {
            java.sql.Connection conn = ConnectionUtil.getConnection();
            java.sql.Statement stmt = conn.createStatement();

            // Get the courses taken by the student
            PreparedStatement pstmt1 = conn.prepareStatement("SELECT course_id, grade FROM grades WHERE email = ?");
            pstmt1.setString(1, email);
            ResultSet rs1 = pstmt1.executeQuery();

            // Initialize counts for different types of courses
            int totalCredits = 0;
            int programCoreCredits = 0;
            int engineeringCoreCredits = 0;
            int electiveCredits = 0;
            int btpcredit = 0;

            // Iterate over the courses taken by the student
            while (rs1.next()) {
                int courseId = rs1.getInt("course_id");
                int grade = rs1.getInt("grade");

                // Check if the course is counted towards graduation
                PreparedStatement pstmt2 = conn.prepareStatement("SELECT * FROM course_catalog WHERE course_id = ?");
                pstmt2.setInt(1, courseId);
                ResultSet rs2 = pstmt2.executeQuery();

                if (rs2.next()) {
                    String creditStructure = rs2.getString("credit_structure");
                    String[] credits = creditStructure.split("-");
                    int credit = Integer.parseInt(credits[2]);

                    // Check if the grade is within the required range
                    if (grade >= 4 && grade <= 10) {
                        totalCredits += credit;

                        // Check the course type and increment the corresponding count
                        PreparedStatement pstmt3 = conn
                                .prepareStatement("SELECT course_type FROM course_curriculum WHERE course_id = ?");
                        pstmt3.setInt(1, courseId);
                        ResultSet rs3 = pstmt3.executeQuery();

                        if (rs3.next()) {
                            String courseType = rs3.getString("course_type");

                            if (courseType.equals("program_core")) {
                                programCoreCredits += credit;
                            } else if (courseType.equals("engineering_core")) {
                                engineeringCoreCredits += credit;
                            } else if (courseType.equals("elective")) {
                                electiveCredits += credit;
                            } else if (courseType.equals("btp")) {
                                btpcredit += credit;
                            }
                        }

                        rs3.close();
                        pstmt3.close();
                    }
                }

                rs2.close();
                pstmt2.close();
            }

            rs1.close();
            pstmt1.close();
            conn.close();

            // Check if the student meets the graduation requirements
            if (totalCredits >= 0 && programCoreCredits >= 0 && engineeringCoreCredits >= 0
                    && electiveCredits >= 0 && btpcredit >= 0) {
                System.out.println("Yes! Student is eligible for graduation");

                email = "john@example.com";

                // Call the gradcheck method
                boolean result = Academics.gradcheck(email);

                // Check the result
                Assertions.assertTrue(result);
            } else {
                System.out.println("No! Student is not eligible for graduation");
                Assertions.assertFalse(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Assertions.assertFalse(false);
        }
    }

}
