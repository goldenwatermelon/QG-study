package com.QG.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {

    public static final String url = "jdbc:mysql://localhost:3306/db1?useSSL=false";
    public static final String user = "root";
    public static final String password = "12345";

    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(url,user,password);
        }catch (SQLException e){
            System.out.println("数据库错误" + e.getMessage());
            return null;
        }
    }

    public static void close(Connection connection){
        if (connection != null){
            try {
                connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
}
