package com.hanning.bms.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("缺少数据库驱动包");
        }
    }


    public static Connection conn;

    public static Connection getConn() {
        try {
            if (conn == null) {
                String url = "jdbc:mysql://localhost:3306/bms?user=root&password=root&useUnicode=true&characterEncoding=utf8";
                conn = DriverManager.getConnection(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
