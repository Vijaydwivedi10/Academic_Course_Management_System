package org.example;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class Csvtodb {
    public static boolean fun() {
        String csvFile = "C:\\Users\\vijay\\IdeaProjects\\fuckoff\\src\\main\\java\\org\\example\\users.csv";

        String jdbcUrl = "jdbc:postgresql://localhost:5432/db";
        String jdbcUsername = "postgres";
        String jdbcPassword = "1234";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword)) {
            CSVReader reader = new CSVReader(new FileReader(csvFile));
            List<String[]> rows = reader.readAll();
            for (String[] row : rows) {
                String email = row[0];
                String password = row[1];
                String name = row[2];
                String address = row[3];
                String phone = row[4];
                String role = row[5];
                String token = row[6];

                String insertQuery = "INSERT INTO users (email, password, name, address, phone, role, token) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(insertQuery);
                if (token.isEmpty()) {
                    pstmt.setNull(7, Types.VARCHAR);
                } else {
                    pstmt.setString(7, token);
                }
                pstmt.setString(1, email);
                pstmt.setString(2, password);
                pstmt.setString(3, name);
                pstmt.setString(4, address);
                pstmt.setString(5, phone);
                pstmt.setString(6, role);
                pstmt.executeUpdate();
                pstmt.close();

            }
            return true;
        } catch (IOException | CsvException | SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
}
