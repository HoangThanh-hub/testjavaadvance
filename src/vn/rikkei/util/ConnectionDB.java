package vn.rikkei.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/phone_store";
    private static final String USER = "root";
    private static final String PASS = "thanh2126";

    public static Connection openConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

}
