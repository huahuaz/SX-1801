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
	
	//�����һ�μ���ʱִ��
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
	 * ��ȡ���Ӷ���
	 * @throws SQLException 
	 */
	public Connection getConn() throws SQLException{
		//conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","scott","tph");
		conn=DriverManager.getConnection(MyProperties.getInstance().getProperty("url"),MyProperties.getInstance());
		return conn;
		
		//ʹ��sql���µ�����Դ��ȡ���Ӷ���
		/*Context ic;
		try {
			 ic=new InitialContext();
			DataSource source=(DataSource) ic.lookup("java:comp/env/jdbc/fresh");//������context.xml����Դname����jdbc/fresh
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
	
	//�ر�������Դ
	public void closeAll(Connection conn,Statement stmt,ResultSet rs){
		 
		 try {
			 //�رս����
		 		if(null!=rs){
				rs.close();
		 		}
		 		 //�رձ������
				 if(null!=stmt){
					  stmt.close();
				 }
				//�ر����Ӷ���
				if(null!=conn){
					  conn.close();
				}
			 } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
	}
	
	
	
	/**
	 * ����sql�����²���
	 * sql	sql���
	 * params	��������	�������˳�����ͣ�˳��һ��
	 * @throws SQLException 
	 */
	public int update(String sql,List<Object> params) throws SQLException{
		int result=0;
		//��ȡ���Ӷ���
		conn=this.getConn();
		//��ȡԤ�������
		pstmt=conn.prepareStatement(sql);
		//���ò���

		setParams(pstmt,params);
		result=pstmt.executeUpdate();//INSERT �� UPDATE ����DELETE

		this.closeAll(conn, pstmt, rs);
		return result;
	}

	
	/**
	 * ����������²���
	 * sqls �����������
	 * params	����sql����Ӧ�Ĳ������ϣ�һ��sql���Ķ�Ӧһ��С��list
	 * ���ϣ������Ӧ��ġ�sql�����ӵ�˳�����Ͳ���СList����˳��һ�µ�
	 * @param sqls
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public int update(List<String> sqls,List<List<Object>> params) throws SQLException{
		int result=0;
		try {
			conn=getConn();
			//�鿴API	connģʽ�Զ��ύ
			//����������Ϊ�Զ��ύ
			conn.setAutoCommit(false);
			for(int i=0;i<sqls.size();i++){
				pstmt=conn.prepareStatement(sqls.get(i));
				//���ò���
				List<Object> param=params.get(i);
				setParams(pstmt,param);
				result=pstmt.executeUpdate();
				if(result<=0){
					conn.rollback();//����ع�
					return result;
				}
			}
			conn.commit();//�����ύ
		} catch (Exception e) {
			conn.rollback();//����ع�
			e.printStackTrace();
		}finally{
			conn.setAutoCommit(true);
			closeAll(conn,pstmt,null);
		}
		return result;
	}
	
	
	
	
	/**
	 * ���ò���
	 * pstmt	Ԥ�������
	 * params	��������
	 * @throws SQLException 
	 */
	private void setParams(PreparedStatement pstmt,List<Object> params) throws SQLException{
		if(null==params||params.size()<0){
			return;
		}
		for(int i=0;i<params.size();i++){
			pstmt.setObject(i+1,params.get(i));//�ʺ��������Ǵ�1��ʼ��
		}
		
	}
	
	
	
	
	/**
	 * ��ѯ����	select *from tb_name where id=?	����һ����¼
	 * sql	sql���
	 * params	����
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public Map<String,Object> findSingle(String sql,List<Object> params) throws SQLException, IOException{
		Map<String,Object> map=null;
		
		try {
			//��ȡ���Ӷ���
			conn=this.getConn();
			//��ȡԤ�������
			pstmt=conn.prepareStatement(sql);
			//���ò���
			setParams(pstmt,params);

			//ִ�в�ѯ����
			rs=pstmt.executeQuery();
			//��ȡ��������
			List<String> columnNames=getColumnNames(rs);
			if(rs.next()){
				//����Map����
				map=new HashMap<String,Object>();
				//map.put("",rs.getObject(""))
				//ѭ������
				for(String name:columnNames){
					//��ȡ��ֵ
					Object obj=rs.getObject(name);
					if(obj==null){
						continue;//ִ����һ��ѭ��
					}
					String typeName=obj.getClass().getName();
					//System.out.println(typeName);
					/*if("oracle.sql.BLOB".equals(typeName)){
						//ͼƬ	���ֽ��������ʽ�洢��map��
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
		 * ���ݽ������ȡ���е�����
		 * @throws SQLException 
		 */
		public List<String> getColumnNames(ResultSet rs) throws SQLException{
			List<String> columnNames=new ArrayList<String>();
			ResultSetMetaData data=rs.getMetaData();
			int count=data.getColumnCount();
			//��ȡ����
			for(int i=1;i<=count;i++){
				columnNames.add(data.getColumnName(i));//��ȡ��������ӵ�list������
			}
			return columnNames;
		}
		
		
		
		
		
		public List<Map<String,Object>> findMutiple(String sql,List<Object> params) throws Exception{
			List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();

			Map<String,Object> map=null;
			map=new HashMap<String,Object>();
			try {
				//��ȡ���Ӷ���
				conn=this.getConn();
				//��ȡԤ�������
				pstmt=conn.prepareStatement(sql);
				//���ò���
				setParams(pstmt,params);

				//ִ�в�ѯ����
				rs=pstmt.executeQuery();
				//��ȡ��������

				List<String> columnNames=getColumnNames(rs);

				while(rs.next()){
					//����Map����
					map=new HashMap<String,Object>();
					//map.put("",rs.getObject(""))
					//ѭ������
					for(String name:columnNames){
						//��ȡ��ֵ
						Object obj=rs.getObject(name);
						if(obj==null){
							continue;//ִ����һ��ѭ��
						}
						String typeName=obj.getClass().getName();
						//System.out.println(typeName);
						/*if("oracle.sql.BLOB".equals(typeName)){
							//ͼƬ	���ֽ��������ʽ�洢��map��
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
		 * ��ѯ	���ض�����¼   ��װjavaBean������
		 */
		public <T> List<T> findMutipl(String sql,List<Object> params,Class<T> cls) throws Exception{
			List<T> list=new ArrayList<T>();
			T t=null;
			
			try{
				conn=getConn();
				pstmt=conn.prepareStatement(sql);
				//���ò���
				setParams(pstmt, params);
				rs=pstmt.executeQuery();
				//��ȡ���еķ������ֶ�
				Method []methods=cls.getDeclaredMethods();
				
				//Field []fields=cls.getDeclaredFields();
				List<String> cloumnNames=getColumnNames(rs);
				while(rs.next()){
					//�������󣬸��ݷ��䴴��
					t=cls.newInstance();
					
					for(Method m:methods){
						/*for(Field f:fields){
							String fieldName=f.getName();*/
						//ѭ�����������
						
						for(String name:cloumnNames){//���ݿ�����е��ֶ����ŷ�װ��������
						//set���ֶ���ƴ�ӣ���ΪsetNam���뷽�������бȽ�
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
									//������չ
								}
							}
						}
					}
					list.add(t);//������ӵ�list������
				}
		}finally{
			closeAll(conn,pstmt,rs);
		}		
		return list;
	}
	
		
	/**
	 * ��ѯ	����һ����¼   ��װjavaBean������
	 */
	public <T> T find(String sql,List<Object> params,Class<T> cls) throws Exception{
		T t=null;
		try{
			conn=getConn();
			pstmt=conn.prepareStatement(sql);
			//���ò���
			setParams(pstmt, params);
			rs=pstmt.executeQuery();
			//��ȡ���еķ������ֶ�
			Method []methods=cls.getDeclaredMethods();
			//Field []fields=cls.getDeclaredFields();
			List<String> cloumnNames=getColumnNames(rs);
			if(rs.next()){
				//�������󣬸��ݷ��䴴��
				t=cls.newInstance();
				for(Method m:methods){
					/*for(Field f:fields){
					String fieldName=f.getName();*/
				//ѭ�����������
				for(String name:cloumnNames){//���ݿ�����е��ֶ����ŷ�װ��������
				//set���ֶ���ƴ�ӣ���ΪsetNam���뷽�������бȽ�
						//set���ֶ���ƴ�ӣ���ΪsetNam���뷽�������бȽ�
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
							//������չ
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
	 * ����sql���ĸ��£�����������
	 * ����ͣ�˳��һ��
	 */
	public int update(String sql,Object...params)throws Exception{
		int result=0;
		try{
			//��ȡ���Ӷ���
			conn=this.getConn();
			//��ȡԤ�������
			pstmt=conn.prepareStatement(sql);
			//���ò���
			//����
			if(null!=params){
				for(int i=0;i<params.length;i++){
					pstmt.setObject(1+i, params[i]);//���ò���
				}
			}
			result=pstmt.executeUpdate();
		}finally{
			this.closeAll(conn, pstmt, null);
		}
		return result;
	}
	
	/**
	 * �ۺϺ�����ѯ		����һ�е�ֵ	sum,avg,count,max,min
	 * @throws SQLException 
	 */
	public double getPolymer(String sql,List<Object> params) throws SQLException{
		double result=0;
		try{
			//��ȡ���Ӷ���
			conn=getConn();
			//��ȡԤ�������
			pstmt=conn.prepareStatement(sql);
			//���ò���
			setParams(pstmt, params);
			//ִ�����в�ѯ����,���ؽ����
			rs=pstmt.executeQuery();
			//��ȡ���е�����
			List<String> columnNames=getColumnNames(rs);
			if(rs.next()){
				//���������Ŵ�1��ʼ
				//����ǩ�ƶ�����һ������ǰ���жϸ����Ƿ�������
				result=rs.getDouble(1);//ֻ��һ��
				//�ӽ�������棬��double�ĸ�ʽȡ����ĳһ�е�����
			}
		}finally {
			this.closeAll(conn, pstmt, rs);
		}
			return result;
		}
	}

	
	                  	
	
	

