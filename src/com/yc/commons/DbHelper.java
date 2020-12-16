package com.yc.commons;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.lang.reflect.Field;;
//import oracle.sql.BLOB;
//import oracle.sql.CLOB;

public class DbHelper {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	//当类第一次加载时执行
	static{
		try {
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			Class.forName(MyProperties.getInstance().getProperty("driverClass"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 获取连接对象
	 * @throws SQLException 
	 */
	public Connection getConn() throws SQLException{
		//conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","scott","tph");
		conn=DriverManager.getConnection(MyProperties.getInstance().getProperty("url"),MyProperties.getInstance());
		return conn;
		
		//使用sql包下的数据源获取连接对象
		/*Context ic;
		try {
			 ic=new InitialContext();
			DataSource source=(DataSource) ic.lookup("java:comp/env/jdbc/fresh");//配置在context.xml，资源name名称jdbc/fresh
			conn=source.getConnection();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;*/
		
		
		
		/*try {
			
			DataSource source=(DataSource)BasicDataSourceFactory.createDataSource(Env.getInstance());
			conn=source.getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	
		*/
	}
	
	//关闭所有资源
	public void closeAll(Connection conn,Statement stmt,ResultSet rs){
		 
		 try {
			 //关闭结果集
		 		if(null!=rs){
				rs.close();
		 		}
		 		 //关闭编译对象
				 if(null!=stmt){
					  stmt.close();
				 }
				//关闭连接对象
				if(null!=conn){
					  conn.close();
				}
			 } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
	}
	
	
	
	/**
	 * 单条sql语句更新操作
	 * sql	sql语句
	 * params	参数集合	参数添加顺序必须和？顺序一致
	 * @throws SQLException 
	 */
	public int update(String sql,List<Object> params) throws SQLException{
		int result=0;
		//获取连接对象
		conn=this.getConn();
		//获取预编译对象
		pstmt=conn.prepareStatement(sql);
		//设置参数

		setParams(pstmt,params);
		result=pstmt.executeUpdate();//INSERT ， UPDATE ，或DELETE

		this.closeAll(conn, pstmt, rs);
		return result;
	}

	
	/**
	 * 带有事务更新操作
	 * sqls 多条更新语句
	 * params	多条sql语句对应的参数集合，一条sql语句的对应一个小的list
	 * 集合，多个对应大的。sql语句添加的顺序必须和参数小List集合顺序一致的
	 * @param sqls
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public int update(List<String> sqls,List<List<Object>> params) throws SQLException{
		int result=0;
		try {
			conn=getConn();
			//查看API	conn模式自动提交
			//将事务设置为自动提交
			conn.setAutoCommit(false);
			for(int i=0;i<sqls.size();i++){
				pstmt=conn.prepareStatement(sqls.get(i));
				//设置参数
				List<Object> param=params.get(i);
				setParams(pstmt,param);
				result=pstmt.executeUpdate();
				if(result<=0){
					conn.rollback();//事务回滚
					return result;
				}
			}
			conn.commit();//事务提交
		} catch (Exception e) {
			conn.rollback();//事务回滚
			e.printStackTrace();
		}finally{
			conn.setAutoCommit(true);
			closeAll(conn,pstmt,null);
		}
		return result;
	}
	
	
	
	
	/**
	 * 设置参数
	 * pstmt	预编译对象
	 * params	参数集合
	 * @throws SQLException 
	 */
	private void setParams(PreparedStatement pstmt,List<Object> params) throws SQLException{
		if(null==params||params.size()<0){
			return;
		}
		for(int i=0;i<params.size();i++){
			pstmt.setObject(i+1,params.get(i));//问号索引号是从1开始的
		}
		
	}
	
	
	
	
	/**
	 * 查询操作	select *from tb_name where id=?	返回一条记录
	 * sql	sql语句
	 * params	参数
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public Map<String,Object> findSingle(String sql,List<Object> params) throws SQLException, IOException{
		Map<String,Object> map=null;
		
		try {
			//获取连接对象
			conn=this.getConn();
			//获取预编译对象
			pstmt=conn.prepareStatement(sql);
			//设置参数
			setParams(pstmt,params);

			//执行查询操作
			rs=pstmt.executeQuery();
			//获取所有列名
			List<String> columnNames=getColumnNames(rs);
			if(rs.next()){
				//创建Map对象
				map=new HashMap<String,Object>();
				//map.put("",rs.getObject(""))
				//循环列名
				for(String name:columnNames){
					//获取到值
					Object obj=rs.getObject(name);
					if(obj==null){
						continue;//执行下一次循环
					}
					String typeName=obj.getClass().getName();
					//System.out.println(typeName);
					/*if("oracle.sql.BLOB".equals(typeName)){
						//图片	以字节数组的形式存储到map中
						BLOB blob=(BLOB) rs.getBlob(name);
						InputStream in= blob.getBinaryStream();
						byte []bt=new byte[(int) blob.length()];
						in.read(bt);
						in.close();
						map.put(name, bt);
					}else if("oracle.sql.CLOB".equals(typeName)){
						CLOB clob=(CLOB) rs.getClob(name);
						Reader rd=clob.getCharacterStream();
						char []cs=new char[(int) clob.length()];
						rd.read(cs);
						String str=new String(cs);
						rd.close();
						map.put(name, str);					
					}else{
						map.put(name, rs.getObject(name));
					}*/
				}
			}
		} finally{
			this.closeAll(conn, pstmt, rs);
		}
		return map;
	}
		
		
		
		
		/**
		 * 根据结果集获取所有的列名
		 * @throws SQLException 
		 */
		public List<String> getColumnNames(ResultSet rs) throws SQLException{
			List<String> columnNames=new ArrayList<String>();
			ResultSetMetaData data=rs.getMetaData();
			int count=data.getColumnCount();
			//获取列名
			for(int i=1;i<=count;i++){
				columnNames.add(data.getColumnName(i));//获取列名，添加到list集合中
			}
			return columnNames;
		}
		
		
		
		
		
		public List<Map<String,Object>> findMutiple(String sql,List<Object> params) throws Exception{
			List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();

			Map<String,Object> map=null;
			map=new HashMap<String,Object>();
			try {
				//获取连接对象
				conn=this.getConn();
				//获取预编译对象
				pstmt=conn.prepareStatement(sql);
				//设置参数
				setParams(pstmt,params);

				//执行查询操作
				rs=pstmt.executeQuery();
				//获取所有列名

				List<String> columnNames=getColumnNames(rs);

				while(rs.next()){
					//创建Map对象
					map=new HashMap<String,Object>();
					//map.put("",rs.getObject(""))
					//循环列名
					for(String name:columnNames){
						//获取到值
						Object obj=rs.getObject(name);
						if(obj==null){
							continue;//执行下一次循环
						}
						String typeName=obj.getClass().getName();
						//System.out.println(typeName);
						/*if("oracle.sql.BLOB".equals(typeName)){
							//图片	以字节数组的形式存储到map中
							//BLOB blob=(BLOB) rs.getBlob(name);
							InputStream in= blob.getBinaryStream();
							byte []bt=new byte[(int) blob.length()];
							in.read(bt);
							in.close();
							map.put(name, bt);
						}else if("oracle.sql.CLOB".equals(typeName)){
							CLOB clob=(CLOB) rs.getClob(name);
							Reader rd=clob.getCharacterStream();
							char []cs=new char[(int) clob.length()];
							rd.read(cs);
							String str=new String(cs);
							rd.close();
							map.put(name, str);	
						}else{
							map.put(name, rs.getObject(name));
						}*/
						map.put(name,rs.getObject(name));
					}
					list.add(map);
				}
			} finally{
				this.closeAll(conn, pstmt, rs);
			}
			return list;
		}




	


		/*public static void main(String[] args) throws SQLException{
			DbHelper db=new DbHelper();
			
			System.out.println(db.getConn().getClass().getName());
		}*/
		
		/**
		 * 查询	返回多条记录   封装javaBean对象中
		 */
		public <T> List<T> findMutipl(String sql,List<Object> params,Class<T> cls) throws Exception{
			List<T> list=new ArrayList<T>();
			T t=null;
			
			try{
				conn=getConn();
				pstmt=conn.prepareStatement(sql);
				//设置参数
				setParams(pstmt, params);
				rs=pstmt.executeQuery();
				//获取所有的方法和字段
				Method []methods=cls.getDeclaredMethods();
				
				//Field []fields=cls.getDeclaredFields();
				List<String> cloumnNames=getColumnNames(rs);
				while(rs.next()){
					//创建对象，根据反射创建
					t=cls.newInstance();
					
					for(Method m:methods){
						/*for(Field f:fields){
							String fieldName=f.getName();*/
						//循环结果集列名
						
						for(String name:cloumnNames){//数据库表中有的字段最后才封装到对象中
						//set与字段名拼接，称为setNam，与方法名进行比较
							if(("set"+name).equalsIgnoreCase(m.getName())){
								String type=m.getParameterTypes()[0].getName();
								if("int".equals(type)||"java.lang.Integer".equals(type)){
									m.invoke(t, rs.getInt(name));		
								}else if("double".equals(type)||"java.lang.Double".equals(type)){
									m.invoke(t, rs.getDouble(name));		
								}else if("float".equals(type)||"java.lang.Float".equals(type)){
									m.invoke(t, rs.getFloat(name));		
								}else if("long".equals(type)||"java.lang.Long".equals(type)){
									m.invoke(t, rs.getLong(name));		
								}else if("java.lang.String".equals(type)){
									m.invoke(t, rs.getString(name));		
								}else{
									//后期扩展
								}
							}
						}
					}
					list.add(t);//对象添加到list集合中
				}
		}finally{
			closeAll(conn,pstmt,rs);
		}		
		return list;
	}
	
		
	/**
	 * 查询	返回一条记录   封装javaBean对象中
	 */
	public <T> T find(String sql,List<Object> params,Class<T> cls) throws Exception{
		T t=null;
		try{
			conn=getConn();
			pstmt=conn.prepareStatement(sql);
			//设置参数
			setParams(pstmt, params);
			rs=pstmt.executeQuery();
			//获取所有的方法和字段
			Method []methods=cls.getDeclaredMethods();
			//Field []fields=cls.getDeclaredFields();
			List<String> cloumnNames=getColumnNames(rs);
			if(rs.next()){
				//创建对象，根据反射创建
				t=cls.newInstance();
				for(Method m:methods){
					/*for(Field f:fields){
					String fieldName=f.getName();*/
				//循环结果集列名
				for(String name:cloumnNames){//数据库表中有的字段最后才封装到对象中
				//set与字段名拼接，称为setNam，与方法名进行比较
						//set与字段名拼接，称为setNam，与方法名进行比较
					if(("set"+name).equalsIgnoreCase(m.getName())){
						String type=m.getParameterTypes()[0].getName();
						if("int".equals(type)||"java.lang.Integer".equals(type)){
							m.invoke(t, rs.getInt(name));		
						}else if("double".equals(type)||"java.lang.Double".equals(type)){
							m.invoke(t, rs.getDouble(name));		
						}else if("float".equals(type)||"java.lang.Float".equals(type)){
							m.invoke(t, rs.getFloat(name));		
						}else if("long".equals(type)||"java.lang.Long".equals(type)){
							m.invoke(t, rs.getLong(name));		
						}else if("java.lang.String".equals(type)){
							m.invoke(t, rs.getString(name));		
						}else{
							//后期扩展
						}
					}
				}
			}
				
		}
	}finally{
		closeAll(conn,pstmt,rs);
	}		
	return t;
	}
	
	
	
	
	/**
	 * 单条sql语句的更新，不定长数组
	 * 必须和？顺序一致
	 */
	public int update(String sql,Object...params)throws Exception{
		int result=0;
		try{
			//获取连接对象
			conn=this.getConn();
			//获取预编译对象
			pstmt=conn.prepareStatement(sql);
			//设置参数
			//数组
			if(null!=params){
				for(int i=0;i<params.length;i++){
					pstmt.setObject(1+i, params[i]);//设置参数
				}
			}
			result=pstmt.executeUpdate();
		}finally{
			this.closeAll(conn, pstmt, null);
		}
		return result;
	}
	
	/**
	 * 聚合函数查询		返回一列的值	sum,avg,count,max,min
	 * @throws SQLException 
	 */
	public double getPolymer(String sql,List<Object> params) throws SQLException{
		double result=0;
		try{
			//获取连接对象
			conn=getConn();
			//获取预编译对象
			pstmt=conn.prepareStatement(sql);
			//设置参数
			setParams(pstmt, params);
			//执行所有查询操作,返回结果集
			rs=pstmt.executeQuery();
			//获取所有的列名
			List<String> columnNames=getColumnNames(rs);
			if(rs.next()){
				//结果集的序号从1开始
				//当标签移动到第一行数据前，判断该行是否有数据
				result=rs.getDouble(1);//只有一列
				//从结果集里面，以double的格式取出来某一列的数据
			}
		}finally {
			this.closeAll(conn, pstmt, rs);
		}
			return result;
		}
	}

	
	                  	
	
	

