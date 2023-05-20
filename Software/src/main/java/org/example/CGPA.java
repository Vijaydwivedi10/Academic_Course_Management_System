package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CGPA {
    public static void computeCGPA() {
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;

        try {
            conn = ConnectionUtil.getConnection();

            // Get all students
            pstmt1 = conn.prepareStatement("SELECT email, curr_sem FROM students");
            rs1 = pstmt1.executeQuery();

            // Loop through each student
            while (rs1.next()) {
                String email = rs1.getString("email");
                int curr_sem = rs1.getInt("curr_sem");

                // Get all courses for this student
                pstmt2 = conn.prepareStatement(
                        "SELECT grades.course_id, grades.grade, course_catalog.credit_structure " +
                                "FROM grades JOIN course_catalog " +
                                "ON grades.course_id = course_catalog.course_id " +
                                "WHERE email = ? AND grade >= 0"
                );

                pstmt2.setString(1, email);
                rs2 = pstmt2.executeQuery();

                // Compute weighted average of grades
                double total_weighted_grade = 0;
                double total_credits = 0;
                while (rs2.next()) {
                    String credit_structure = rs2.getString("credit_structure");
                    int grade = rs2.getInt("grade");

                    int credits = Integer.parseInt(credit_structure.split("-")[2]);
                    total_credits += credits;

                    total_weighted_grade += credits * grade;
                }

                // Compute CGPA
                double cgpa = total_weighted_grade / total_credits;

                // Update CGPA in the database
                pstmt2 = conn.prepareStatement("UPDATE students SET cgpa = ? WHERE email = ?");
                pstmt2.setDouble(1, cgpa);
                pstmt2.setString(2, email);
                pstmt2.executeUpdate();

                System.out.println("CGPA : "+cgpa);
            }

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs2 != null)
                    rs2.close();
            } catch (SQLException se2) {
            }
            try {
                if (pstmt2 != null)
                    pstmt2.close();
            } catch (SQLException se2) {
            }
            try {
                if (rs1 != null)
                    rs1.close();
            } catch (SQLException se2) {
            }
            try {
                if (pstmt1 != null)
                    pstmt1.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
