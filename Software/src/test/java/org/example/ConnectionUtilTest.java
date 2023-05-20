package org.example;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionUtilTest {

    @Test
    void getConnection() {
        Connection conn = null;
        try {
            conn = ConnectionUtil.getConnection();
            assertNotNull(conn);
        } catch (SQLException e) {
            fail("Unexpected SQLException: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                fail("Unexpected SQLException while closing connection: " + e.getMessage());
            }
        }
    }
    }


