package org.example;

import java.sql.PreparedStatement;
import java.sql.*;

public class Login {
    static boolean Login_val(String username, String password, String role) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = ConnectionUtil.getConnection();
            stmt = conn.createStatement();
            // --------------------

            PreparedStatement pstmt = conn.prepareStatement("select role from users where email=? and password=?;");
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            String role2 = "";
            if (rs.next()) {
                role2 = rs.getString("role");
            }
            if (role2.equals(role)) {
                return true;
            } else {
                return false;
            }

            // -------------------------

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
        return false;

    }
}
