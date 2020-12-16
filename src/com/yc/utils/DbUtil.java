package com.yc.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbUtil {
	
	
	//驱动注册（加载驱动）
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public static Connection getConn() {
		Connection conn=null;
		
		
		/**
		 * url:连接数据库的地址	jdbc:mysql://数据库服务器IP：数据库端口号/数据库名
		 * user:登录数据库的用户名 
		 * password:登录数据库的密码
		 */
		
		try {
			conn=DriverManager.getConnection("jdbc:mysql://120.26.177.155:3306/db_film","root","Tph123456");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
	
	
	public static void closeAll(Connection conn,PreparedStatement pstmt,ResultSet rs) throws SQLException {
		
		
		//关闭结果集对象
		if(null!=pstmt) {
			pstmt.close();
		}
		
		//关闭结果集对象
		if(null!=pstmt) {
			pstmt.close();
		}
		
		//关闭连接对象
		if(null!=conn) {
			conn.close();
		}
				
		
	}
}
