package com.example.selltrainticket;

import android.annotation.SuppressLint;
import android.os.StrictMode;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
    String db;
    public DBUtils(String db) {
        this.db=db;
    }

    private static String driver="com.mysql.cj.jdbc.Driver";
    private static String url="jdbc:mysql://169.254.89.44/trainticketusermessege"
            + "?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false&allowPublicKeyRetrieval=true";

    private static String username="root";
    private static String password="yzk1259341271999";

    public static Connection open() {


        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        return null;
    }

    public static void close(Connection con) {
        while(con!=null) {
            try {
                con.close();
            } catch (SQLException e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }
        }
    }
}
