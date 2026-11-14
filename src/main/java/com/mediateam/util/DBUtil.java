package com.mediateam.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * JDBC 数据库连接工具类
 */
public final class DBUtil {

    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    static {
        try (InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties props = new Properties();
            if (in != null) {
                props.load(in);
            }
            driver = props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
            url = props.getProperty("db.url",
                    "jdbc:mysql://localhost:3306/media_team_db?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true");
            username = props.getProperty("db.username", "root");
            password = props.getProperty("db.password", "123456");
            Class.forName(driver);
        } catch (Exception e) {
            throw new ExceptionInInitializerError("数据库配置初始化失败: " + e.getMessage());
        }
    }

    private DBUtil() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static void close(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ignored) {
        }
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException ignored) {
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ignored) {
        }
    }
}
