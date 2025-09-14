package com.example.projekpbobd.util;

import java.sql.*;

public class ConnectionManager {
    public static Connection getConnection() throws SQLException {
        final String url = "jdbc:postgresql://localhost:5432/projek_pbo_bd?user=postgres&password=david1410";
        Connection conn = DriverManager.getConnection(url);
        System.out.println("Connected");
        return  conn;
    }

    public static void closeConnection(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void close (PreparedStatement preparedStatement) {
        try {
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
