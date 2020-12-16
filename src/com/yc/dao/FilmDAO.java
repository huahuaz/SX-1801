package com.yc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yc.bean.Film;
import com.yc.commons.DbHelper;
import com.yc.utils.DbUtil;
/**
 * 
 * @author 14243
 *
 */

public class FilmDAO {
	DbHelper db=new DbHelper();
	
	
	//��ӵ�Ӱ
	public int addFilm(String fname,String ftype,String fyear,String flength,
			String fdate,String director,String actors,String fids,String farea,
			String imagepath) throws Exception{
		//��ȡ����
		Connection conn = DbUtil.getConn();
		int result =0;
		String sql="insert into tb_film values(null,?,?,?,?,?,?,?,?,?,?)";
		//��ȡԤ�������
		PreparedStatement pstmt= conn.prepareStatement(sql);
		//���ò���
		pstmt.setObject(1, fname);
		pstmt.setObject(2, ftype);
		pstmt.setObject(3, fyear);
		pstmt.setObject(4, flength);
		pstmt.setObject(5, fdate);
		pstmt.setObject(6, director);
		pstmt.setObject(7, actors);
		pstmt.setObject(8, fids);
		pstmt.setObject(9, farea);
		pstmt.setObject(10, imagepath);
		//ִ�и��²���
		result=pstmt.executeUpdate();
		//�ر���Դ
		DbUtil.closeAll(conn, pstmt, null);
		return result;
		
		
	}
	
	
	//�鿴���е�Ӱ��Ϣ
	//һ����Ӱ��¼����һ��map ������Ӱ��¼ ���map �洢list������  List-->ArrayList
	public List<Map<String,Object> > findAll() throws Exception{
		//sql����м�¼�������� order by �ֶ���[desc |asc]����ָ�����ֶν���desc������|asc���� Ĭ�ϵ�
		String sql="select fid,fname,ftype,fyear,flength,fdate,director,actors,fdis,farea,fimage from tb_film order by fid desc";
		//�����ϼ�����
		List<Map<String,Object>> list = new ArrayList<Map<String,Object> >();
		Map<String,Object> map =null;
		Connection conn=DbUtil.getConn();
		PreparedStatement pstmt=conn.prepareStatement(sql);
		ResultSet rs=pstmt.executeQuery();
		//���ض�����¼ ʹ��ѭ��
		while(rs.next()){
			//ÿһ����¼������map
			map=new HashMap<String,Object>();
			map.put("fid", rs.getObject("fid"));
			map.put("fname", rs.getObject("fname"));
			
			map.put("ftype", rs.getObject("ftype"));
		
			map.put("fyear", rs.getObject("fyear"));
			map.put("flength", rs.getObject("flength"));
			map.put("fdate", rs.getObject("fdate"));
			map.put("director", rs.getObject("director"));
			map.put("actors", rs.getObject("actors"));
			map.put("fdis", rs.getObject("fdis"));
			map.put("farea", rs.getObject("farea"));
			map.put("fimage", rs.getObject("fimage"));
			//��map��ӵ�������add()
			list.add(map);
			
		}
		//�ر���Դ
		DbUtil.closeAll(conn, pstmt, rs);
		//���ؼ���
		return list;
		
		
	}
	
	
	//���ݵ�Ӱ��Ų鿴��Ӱ��Ϣ
	public Map<String,Object> findByFid(String fid) throws Exception{
		String sql="select fid,fname,ftype,fyear,flength,fdate,director,actors,fdis,farea,fimage from tb_film where fid =?";
		Map<String,Object> map=null;
		Connection conn =DbUtil.getConn();
		PreparedStatement pstmt = conn.prepareStatement(sql);
		//���ò���
		pstmt.setObject(1, fid);
		//ִ�в�ѯ����
		ResultSet rs = pstmt.executeQuery();
		//�Խ�������д���
		if(rs.next()){
			//����map����
			map=new HashMap<String,Object>();
			//map�м�ֵ�Դ洢�������ֶ��� ֵ��ͨ���ֶ����ӽ�����л�ȡ��ֵ
			map.put("fid", rs.getObject("fid"));
			map.put("fname", rs.getObject("fname"));
			map.put("ftype", rs.getObject("ftype"));
			map.put("fyear", rs.getObject("fyear"));
			map.put("flength", rs.getObject("flength"));
			map.put("fdate", rs.getObject("fdate"));
			map.put("director", rs.getObject("director"));
			map.put("actors", rs.getObject("actors"));
			map.put("fdis", rs.getObject("fdis"));
			map.put("farea", rs.getObject("farea"));
			map.put("fimage", rs.getObject("fimage"));
			
		}
		//�ر���Դ
		DbUtil.closeAll(conn, pstmt, rs);
		return map;

	}
	
	
	
	//��ӵ�Ӱ
	public int deleteFilm(int fid) throws Exception{
			//��ȡ����
		Connection conn = DbUtil.getConn();
		int result =0;
		String sql="delete from tb_film where fid=? ";
		//��ȡԤ�������
		PreparedStatement pstmt= conn.prepareStatement(sql);
		//���ò���
		pstmt.setObject(1, fid);
		//ִ�и��²���
		result=pstmt.executeUpdate();
		//�ر���Դ
		DbUtil.closeAll(conn, pstmt, null);
		return result;
	}
		
	
	
	
	
	
	/**
	 * ��ҳ��ѯ
	 * @param f
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<Film> findByPage(Integer pageNum,Integer pageSize) throws Exception{
		StringBuffer sb=new StringBuffer();
		sb.append("select *from tb_film ");
		
		sb.append(" order by fid desc ");
		if(pageSize!=null&&pageNum!=null){
			//����һ����ʼ������������������ҳ�������
			sb.append(" limit "+(pageNum-1)*pageSize+","+pageSize );	
		}
		System.out.println(sb.toString());
		return db.findMutipl(sb.toString(), null, Film.class);
	}



	
	
	public int totalRows() throws Exception{
		StringBuffer sb=new StringBuffer();
		sb.append("select count(*) from tb_film");
		return (int)db.getPolymer(sb.toString(), null);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	public static void main(String[] args) throws Exception {
//		//���춨��ķ�����û��ʹ��static�ؼ��� ���嶼�ǳ�Ա���� ���ڶ��� �ȴ���������ٵ�
//				FilmDAO filmdao=new FilmDAO();
//				
//				List<Film> film=filmdao.findByPage(2, 2);
//				for(Film f:film) {
//					System.out.println(f.toString());
//				}
//				
//	}
	
	
}
