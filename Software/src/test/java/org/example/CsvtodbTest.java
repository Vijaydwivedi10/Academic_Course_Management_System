package org.example;

import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class CsvtodbTest {

    @Test
    void testFun() throws SQLException {
        java.sql.Connection conn = ConnectionUtil.getConnection();
        java.sql.Statement stmt = conn.createStatement();
        Csvtodb.fun();
        String query = "SELECT COUNT(*) FROM users";
        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();
        assertTrue(rs.next());
        assertEquals(3, rs.getInt(1));
      //  pstmt.executeQuery("Delete from users;");
        pstmt.close();
        rs.close();
    }
}
