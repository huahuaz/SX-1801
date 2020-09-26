package util;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接的工具类
 */
public class DbUtil {
    //加载驱动
    static{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //获取连接的方法
    public static Connection getConn() throws SQLException {
        String url = "jdbc:mysql://120.26.177.155:3306/db_film";
        String user = "root";
        String password = "Tph123456";
        Connection conn = DriverManager.getConnection(url,user,password);
        return conn;
    }

    /**
     * 关闭连接
     */
    public static void close(Closeable... closers) {
        //closers 是一个数组 类型是Closeable[]
        for (Closeable closer : closers) {
            if (closer != null) {
                try {
                    closer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
