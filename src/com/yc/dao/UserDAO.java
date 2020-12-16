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
 * �����û���tb_user
 * @author Lenovo
 *
 */
public class UserDAO {
	
	DbHelper db=new DbHelper();
	/**
	 * �û�ע��
	 * @param uname
	 * @param email
	 * @param pwd
	 * @return
	 * @throws SQLException
	 */
	public static int register(String uname,String email,String pwd) throws SQLException {
		//�������ͱ���
		int result=0;
		//��ȡ���Ӷ���
		//getConn()������һ����̬�ķ�����static�ؼ������εķ�����ͨ��������������())
		Connection conn=DbUtil.getConn();
		//4.��дsql���
		//ʹ��	?������紫���ֵ
		String sql="insert into tb_user values(null,?,?,?,1)";
		//��ȡԤ�������
		PreparedStatement pstmt=conn.prepareStatement(sql);
		//6.���ò���
		pstmt.setObject(1, uname);//1��ʾ�ڼ����ʺ�
		pstmt.setObject(2, email);
		pstmt.setObject(3, pwd);
		
		//7.ִ�и��²���
		result=pstmt.executeUpdate();
		//����Ԥ�������
		if(null!=pstmt) {
			pstmt.close();
		}
		
		//�رն���
		DbUtil.closeAll(conn, pstmt, null);
		return result;
	}
	
	
	/**
	 * �޸�����
	 * @param uid
	 * @param pwd
	 * @return
	 * @throws SQLException
	 */
	public static int updatePwd(int uid,String pwd) throws SQLException {
		//�������ͱ���
		int result=0;
		//��ȡ���Ӷ���
		//getConn()������һ����̬�ķ�����static�ؼ������εķ�����ͨ��������������())
		Connection conn=DbUtil.getConn();
		//4.��дsql���
		//ʹ��	?������紫���ֵ
		String sql="update tb_user set upwd=? where uid=? ";
		//��ȡԤ�������
		PreparedStatement pstmt=conn.prepareStatement(sql);
		//6.���ò���
		pstmt.setObject(1, pwd);//1��ʾ�ڼ����ʺ�
		pstmt.setObject(2, uid);
		
				
		//7.ִ�и��²���
		result=pstmt.executeUpdate();
		//����Ԥ�������
		if(null!=pstmt) {
			pstmt.close();
		}
				
		//�رն���
		DbUtil.closeAll(conn, pstmt, null);
		return result;
	}
	
	
	public static Map<String,Object> login(String email,String pwd) throws SQLException{
		
		
		//key,value		ʹ�ñ����ֶ�������Ϊ��
		//map.put("uid",1)
		//����map����
		Map<String,Object> map=null;
		
		//��ȡ���Ӷ���
		//getConn������һ����̬�ķ���(static�ؼ������εķ�����ͨ��������������())
		Connection conn=DbUtil.getConn();
		//��дsql���
		String sql="select uid,uname,upwd,email,state from tb_user where email=? and upwd=? and state=1 ";
		//��ȡԤ�������
		PreparedStatement pstmt=conn.prepareStatement(sql);
		//������ֵ
		pstmt.setObject(1, email);
		pstmt.setObject(2, pwd);
		///ִ�в�ѯ���������ؽ��������
		ResultSet rs=pstmt.executeQuery();
		//�Խ�������д���
		if(rs.next()) {
			//next()������ƶ�����һ�е���ǰ�棬�жϸ����Ƿ������ݣ���true��û��false
			//��ѯ�����
			//����map����
			map=new HashMap<String,Object>();
			//��Ӧ�ֶ�ֵ���õ�map
			//������������ṩһ����������ȡ��Ӧ�ֶε�ֵ		rs.getObject(�ֶ�����)
			map.put("uid", rs.getObject("uid"));
			map.put("uname", rs.getObject("uname"));
			map.put("email", rs.getObject("email"));
			map.put("upwd", rs.getObject("upwd"));
			map.put("state", rs.getObject("state"));
		}
		//�ر���Դ
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
     *�����û�������������
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
