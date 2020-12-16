package com.yc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yc.bean.User;
import com.yc.commons.DbHelper;
import com.yc.utils.DbUtil;

/**
 * 操作用户表tb_user
 * @author Lenovo
 *
 */
public class UserDAO {
	
	DbHelper db=new DbHelper();
	/**
	 * 用户注册
	 * @param uname
	 * @param email
	 * @param pwd
	 * @return
	 * @throws SQLException
	 */
	public static int register(String uname,String email,String pwd) throws SQLException {
		//声明整型变量
		int result=0;
		//获取连接对象
		//getConn()方法是一个静态的方法（static关键字修饰的方法，通过类名，方法名())
		Connection conn=DbUtil.getConn();
		//4.编写sql语句
		//使用	?代替外界传入的值
		String sql="insert into tb_user values(null,?,?,?,1)";
		//获取预编译对象
		PreparedStatement pstmt=conn.prepareStatement(sql);
		//6.设置参数
		pstmt.setObject(1, uname);//1表示第几个问号
		pstmt.setObject(2, email);
		pstmt.setObject(3, pwd);
		
		//7.执行更新操作
		result=pstmt.executeUpdate();
		//关于预编译对象
		if(null!=pstmt) {
			pstmt.close();
		}
		
		//关闭对象
		DbUtil.closeAll(conn, pstmt, null);
		return result;
	}
	
	
	/**
	 * 修改密码
	 * @param uid
	 * @param pwd
	 * @return
	 * @throws SQLException
	 */
	public static int updatePwd(int uid,String pwd) throws SQLException {
		//声明整型变量
		int result=0;
		//获取连接对象
		//getConn()方法是一个静态的方法（static关键字修饰的方法，通过类名，方法名())
		Connection conn=DbUtil.getConn();
		//4.编写sql语句
		//使用	?代替外界传入的值
		String sql="update tb_user set upwd=? where uid=? ";
		//获取预编译对象
		PreparedStatement pstmt=conn.prepareStatement(sql);
		//6.设置参数
		pstmt.setObject(1, pwd);//1表示第几个问号
		pstmt.setObject(2, uid);
		
				
		//7.执行更新操作
		result=pstmt.executeUpdate();
		//关于预编译对象
		if(null!=pstmt) {
			pstmt.close();
		}
				
		//关闭对象
		DbUtil.closeAll(conn, pstmt, null);
		return result;
	}
	
	
	public static Map<String,Object> login(String email,String pwd) throws SQLException{
		
		
		//key,value		使用表中字段名称作为键
		//map.put("uid",1)
		//声明map变量
		Map<String,Object> map=null;
		
		//获取连接对象
		//getConn方法是一个静态的方法(static关键字修饰的方法，通过类名，方法名())
		Connection conn=DbUtil.getConn();
		//编写sql语句
		String sql="select uid,uname,upwd,email,state from tb_user where email=? and upwd=? and state=1 ";
		//获取预编译对象
		PreparedStatement pstmt=conn.prepareStatement(sql);
		//参数设值
		pstmt.setObject(1, email);
		pstmt.setObject(2, pwd);
		///执行查询操作，返回结果集对象
		ResultSet rs=pstmt.executeQuery();
		//对结果集进行处理
		if(rs.next()) {
			//next()将光标移动到下一行的最前面，判断改行是否有数据，有true，没有false
			//查询出结果
			//创建map对象
			map=new HashMap<String,Object>();
			//对应字段值设置到map
			//结果集对象中提供一个方法，获取对应字段的值		rs.getObject(字段名称)
			map.put("uid", rs.getObject("uid"));
			map.put("uname", rs.getObject("uname"));
			map.put("email", rs.getObject("email"));
			map.put("upwd", rs.getObject("upwd"));
			map.put("state", rs.getObject("state"));
		}
		//关闭资源
		DbUtil.closeAll(conn, pstmt, rs);
		return map;
	}
	
	
	public static void main(String[] args) throws Exception {
//		int result=register("zhangsan", "zhangsan@qq.com", "a");
//		System.out.println(result);
		
		
//		int result=updatePwd(5, "123");
//		System.out.println(result);
		
//		Map<String,Object> map=login("zhangsan@qq.com", "123");
//		System.out.println(map);
//		int result=UserDAO.updateMessage("aihua", "111@qq.com", "aaa");
//		System.out.println("result:"+result);
		UserDAO dao=new UserDAO();
		
			List<User> user=dao.findMessage();
			System.out.println(user);
		
			// TODO Auto-generated catch block
		
	}


	public static Map<String, Object> findByEmail(String email) throws SQLException {
		Map<String,Object> map=null;
		Connection conn=DbUtil.getConn();
		String sql="select uid,uname,upwd,email,state from tb_user where email=? ";
		PreparedStatement pstmt=conn.prepareStatement(sql);
		pstmt.setObject(1, email);
		ResultSet rs=pstmt.executeQuery();
		if(rs.next()) {
			map=new HashMap<String,Object>();
			map.put("uid", rs.getObject("uid"));
			map.put("uname", rs.getObject("uname"));
			map.put("email", rs.getObject("email"));
			map.put("upwd", rs.getObject("upwd"));
			map.put("state", rs.getObject("state"));
		}
		
		DbUtil.closeAll(conn, pstmt, rs);
		return map;
	}
	
	/**
     *根据用户名和邮箱留言
     */
    public static int updateMessage(String uname,String email,String message) throws SQLException {
    	System.out.println("uname:"+uname);
    	System.out.println("email:"+email);
    	System.out.println("message:"+message);
    	int result = 0;
        Connection conn = DbUtil.getConn();
        String sql = "update tb_user set message=? where uname=? and email=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1,message);
        ps.setObject(2,uname);
        ps.setObject(3,email);

        result = ps.executeUpdate();
        DbUtil.closeAll(conn, ps,null);
        return result;
    }
    
    
    public List<User> findMessage() throws Exception {
    	StringBuffer sb=new StringBuffer();
    	sb.append("select *from tb_user where message is not null order by uid desc");
    	System.out.println(sb.toString());
    	List<User> user=db.findMutipl(sb.toString(), null, User.class);
    	System.out.println(user);
    	return user;
    }
    
    
    
  
}
