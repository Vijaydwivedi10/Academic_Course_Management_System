package org.example;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CurriculumUploader {
    public static void uploadCurriculum() {
        String line;
        String cvsSplitBy = ",";
        Connection conn = null;

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("C:\\Users\\vijay\\IdeaProjects\\fuckoff\\src\\main\\java\\org\\example\\curriculum.csv"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }



        try  {
            conn = ConnectionUtil.getConnection();
            PreparedStatement pstmt = null;
//            br.readLine();
            // Read the CSV file line by line
            while (true) {
                // Split the line by commas
               line=br.readLine();
               if(line.equals(""))break;
                String[] data = line.split(cvsSplitBy);

                System.out.println(line);

//                 Get the batch, semester, department, course ID, and course type from the data array
                int batch =Integer.parseInt (data[0]);
                int semester =Integer.parseInt( data[1]);
                System.out.println(data[1]);
                String department = data[2];
                int course_id = Integer.parseInt(data[3]);
                String course_type = data[4];
//
//                 Check if the course exists in the course_catalog table
                String courseQuery = "SELECT course_name FROM course_catalog WHERE course_id = ?";
                pstmt = conn.prepareStatement(courseQuery);
                pstmt.setInt(1, 103);
                ResultSet rs = pstmt.executeQuery();

                if (!rs.next()) {
                    System.out.println("Course with ID " + course_id + " does not exist in the course catalog.");
                    continue;
                }

                String course_name = rs.getString("course_name");

                // Insert the curriculum into the course_curriculum table
                String curriculumQuery = "INSERT INTO course_curriculum (batch, semester, department, course_id, course_type) VALUES (?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(curriculumQuery);
                pstmt.setInt(1, batch);
                pstmt.setInt(2, semester);
                pstmt.setString(3, department);
                pstmt.setInt(4, course_id);
                pstmt.setString(5, course_type);
                pstmt.executeUpdate();

                System.out.println("Curriculum for course with ID " + course_id + " uploaded for batch " + batch + " and semester " + semester + ".");
            }

            pstmt.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("SQL error occurred while closing connection: " + e.getMessage());
            }
        }
    }
}
