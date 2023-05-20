package org.example;

import java.sql.*;
import java.util.*;

public class Academics {
    static Connection conn = null;
    static Statement stmt = null;
    static PreparedStatement pstmt = null;

    static boolean Insert_course(int academicsChoice, int course_id, String course_name, String credit_structure) {
        System.out.println("You chose to insert a course into the course_catalog.");

        try {
            conn = ConnectionUtil.getConnection();
            pstmt = conn.prepareStatement(
                    "INSERT INTO course_catalog (course_id, course_name, credit_structure) VALUES (?, ?, ?)");

            pstmt.setInt(1, course_id);
            pstmt.setString(2, course_name);
            pstmt.setString(3, credit_structure);

            pstmt.executeUpdate();

            System.out.println("Data inserted successfully!");

            pstmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException se2) {

            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {

                se.printStackTrace();

            }

        }
        return true;
    }

    static boolean Delete_course(int course_id) {

        try {
            // Establishing a database connection
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db", "postgres",
                    "1234");

            // Preparing the delete statement
            String deleteQuery = "DELETE FROM course_catalog WHERE course_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
            pstmt.setInt(1, course_id);

            // Executing the delete statement
            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted == 0)
                System.out.println("Course with course_id " + course_id + " does not exist");
            else
                System.out.println("Course with course_id " + course_id + " deleted successfully");

            // Closing the statement and database connection
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return true;
    }

    static boolean Show_course() {

        try {
            conn = ConnectionUtil.getConnection();
            stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM course_catalog");

            while (rs.next()) {
                int course_id = rs.getInt("course_id");
                String course_name = rs.getString("course_name");
                String credit_structure = rs.getString("credit_structure");

                System.out.println("\nCourse ID: " + course_id);
                System.out.println("Course Name: " + course_name);
                System.out.println("Credit Structure: " + credit_structure);
                System.out.println();
            }

            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return true;

    }

    static boolean start_sem() {
        try {

            conn = ConnectionUtil.getConnection();
            stmt = conn.createStatement();

            PreparedStatement pstmt = conn
                    .prepareStatement("UPDATE admin SET sem_start = ? WHERE email = ?");
            // Set the values for the prepared statement
            pstmt.setBoolean(1, true);
            pstmt.setString(2, "admin@example.com");

            String sql = "UPDATE students SET curr_sem = curr_sem + 1";
            stmt.executeUpdate(sql);

            // Execute the update query
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated");
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

//         Csvtodb.fun();
        return true;
    }

    static boolean end_sem() {
        try {

            conn = ConnectionUtil.getConnection();
            stmt = conn.createStatement();

            PreparedStatement pstmt = conn
                    .prepareStatement("UPDATE admin SET sem_start = ? WHERE email = ?");
            // Set the values for the prepared statement
            pstmt.setBoolean(1, false);
            pstmt.setString(2, "admin@example.com");

            // Execute the update query
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated");
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    static boolean view_grad(String email) {
        try {

            // Establishing a database connection
            conn = ConnectionUtil.getConnection();
            stmt = conn.createStatement();

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

    static boolean gradcheck(String email) {
        try {
            Connection conn = ConnectionUtil.getConnection();

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
                return true;
            } else {
                System.out.println("No! Student is not eligible for graduation");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
