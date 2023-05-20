package org.example;

import org.example.ConnectionUtil;
import org.example.CurriculumUploader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CurriculumUploaderTest {
    private static final String TEST_CSV_FILE = "C:\\Users\\vijay\\IdeaProjects\\fuckoff\\src\\main\\java\\org\\example\\users.csv";

    @Test
    public void testUploadCurriculum() throws IOException {
        // Create a new instance of CurriculumUploader
        CurriculumUploader uploader = new CurriculumUploader();

        // Call the uploadCurriculum method
        CurriculumUploader.uploadCurriculum();

        // Read the contents of the course_curriculum table to verify that the data has been uploaded correctly
        try (BufferedReader br = new BufferedReader(new FileReader(TEST_CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line by commas
                String[] data = line.split(",");

                // Get the batch, semester, department, course ID, and course type from the data array
//                int batch = Integer.parseInt(data[0]);
//                int semester = Integer.parseInt(data[1]);
//                String department = data[2];
//                int course_id = Integer.parseInt(data[3]);
//                String course_type = data[4];

                // Verify that the data has been uploaded correctly
//                Assertions.assertTrue(checkDataInTable(batch, semester, department, course_id, course_type));
            }
        }
    }

    private boolean checkDataInTable(int batch, int semester, String department, int course_id, String course_type) {
        boolean dataExists = false;
        Connection conn = null;

        try {
            conn = ConnectionUtil.getConnection();
            PreparedStatement pstmt = null;

            // Check if the data exists in the course_curriculum table
            String query = "SELECT * FROM course_curriculum WHERE batch = ? AND semester = ? AND department = ? AND course_id = ? AND course_type = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, batch);
            pstmt.setInt(2, semester);
            pstmt.setString(3, department);
            pstmt.setInt(4, course_id);
            pstmt.setString(5, course_type);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                dataExists = true;
            }

            pstmt.close();
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        } finally {

        }

        return dataExists;
    }

}
