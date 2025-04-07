package com.example.horse_racing.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL = "jdbc:postgresql://localhost:5432/Horse_racing";
    private static final String USER = "postgres";
    private static final String PASSWORD = "135VlAdIsLaV";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("DB Connection success!");
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
