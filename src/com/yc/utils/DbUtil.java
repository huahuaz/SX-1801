package com.yc.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbUtil {
	
	
	//����ע�ᣨ����������
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
		 * url:�������ݿ�ĵ�ַ	jdbc:mysql://���ݿ������IP�����ݿ�˿ں�/���ݿ���
		 * user:��¼���ݿ���û��� 
		 * password:��¼���ݿ������
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
		
		
		//�رս��������
		if(null!=pstmt) {
			pstmt.close();
		}
		
		//�رս��������
		if(null!=pstmt) {
			pstmt.close();
		}
		
		//�ر����Ӷ���
		if(null!=conn) {
			conn.close();
		}
				
		
	}
}
