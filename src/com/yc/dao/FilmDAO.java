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
	
	
	//添加电影
	public int addFilm(String fname,String ftype,String fyear,String flength,
			String fdate,String director,String actors,String fids,String farea,
			String imagepath) throws Exception{
		//获取连接
		Connection conn = DbUtil.getConn();
		int result =0;
		String sql="insert into tb_film values(null,?,?,?,?,?,?,?,?,?,?)";
		//获取预编译对象
		PreparedStatement pstmt= conn.prepareStatement(sql);
		//设置参数
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
		//执行更新操作
		result=pstmt.executeUpdate();
		//关闭资源
		DbUtil.closeAll(conn, pstmt, null);
		return result;
		
		
	}
	
	
	//查看所有电影信息
	//一条电影记录放在一个map 多条电影纪录 多个map 存储list集合中  List-->ArrayList
	public List<Map<String,Object> > findAll() throws Exception{
		//sql语句中记录进行排序 order by 字段名[desc |asc]根据指定的字段进行desc（降序）|asc升序 默认的
		String sql="select fid,fname,ftype,fyear,flength,fdate,director,actors,fdis,farea,fimage from tb_film order by fid desc";
		//创建合集对象
		List<Map<String,Object>> list = new ArrayList<Map<String,Object> >();
		Map<String,Object> map =null;
		Connection conn=DbUtil.getConn();
		PreparedStatement pstmt=conn.prepareStatement(sql);
		ResultSet rs=pstmt.executeQuery();
		//返回多条记录 使用循环
		while(rs.next()){
			//每一条记录都是新map
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
			//将map添加到集合中add()
			list.add(map);
			
		}
		//关闭资源
		DbUtil.closeAll(conn, pstmt, rs);
		//返回集合
		return list;
		
		
	}
	
	
	//根据电影编号查看电影信息
	public Map<String,Object> findByFid(String fid) throws Exception{
		String sql="select fid,fname,ftype,fyear,flength,fdate,director,actors,fdis,farea,fimage from tb_film where fid =?";
		Map<String,Object> map=null;
		Connection conn =DbUtil.getConn();
		PreparedStatement pstmt = conn.prepareStatement(sql);
		//设置参数
		pstmt.setObject(1, fid);
		//执行查询操作
		ResultSet rs = pstmt.executeQuery();
		//对结果集进行处理
		if(rs.next()){
			//创建map对象
			map=new HashMap<String,Object>();
			//map中键值对存储键名：字段名 值：通过字段名从结果集中获取的值
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
		//关闭资源
		DbUtil.closeAll(conn, pstmt, rs);
		return map;

	}
	
	
	
	//添加电影
	public int deleteFilm(int fid) throws Exception{
			//获取连接
		Connection conn = DbUtil.getConn();
		int result =0;
		String sql="delete from tb_film where fid=? ";
		//获取预编译对象
		PreparedStatement pstmt= conn.prepareStatement(sql);
		//设置参数
		pstmt.setObject(1, fid);
		//执行更新操作
		result=pstmt.executeUpdate();
		//关闭资源
		DbUtil.closeAll(conn, pstmt, null);
		return result;
	}
		
	
	
	
	
	
	/**
	 * 分页查询
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
			//参数一：开始的行数，参数二：该页面的行数
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
//		//今天定义的方法都没有使用static关键字 定义都是成员方法 归于对象 先创建对象后再调
//				FilmDAO filmdao=new FilmDAO();
//				
//				List<Film> film=filmdao.findByPage(2, 2);
//				for(Film f:film) {
//					System.out.println(f.toString());
//				}
//				
//	}
	
	
}
