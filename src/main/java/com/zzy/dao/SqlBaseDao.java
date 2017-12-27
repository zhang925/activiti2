package com.zzy.dao;

import java.sql.*;

public class SqlBaseDao {

	private String className="com.mysql.jdbc.Driver";
	//本地
	private String url="jdbc:mysql://127.0.0.1:3306/activiti?user=root&password=root";
	//网络
	//private String url="jdbc:mysql://w.rdc.sae.sina.com.cn:3307/app_bestsunny?user=k1k350xj0n&password= zxwi33zylzy10zl50hh13x2lhw522k51wjii451j";
	
	//private String user="zhang";
	//private String pwd="root";
	/**
	 * 数据库连接的方法
	 */
	public Connection getConnection(){
		//加载驱动
		Connection conn = null;
		try {
			//Class.forName(className);//SqlServer
			Class.forName(className).newInstance();//MySQL
			//conn=DriverManager.getConnection(url,user,pwd);
			conn=DriverManager.getConnection(url); 
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return conn;
	}
	/**
	 * 关闭数据库的方法
	 */
	public void close(Connection conn,Statement stmt,PreparedStatement pstmt,ResultSet rs){
		//关闭有顺序，根据先开启后关闭的原则关闭
		 try {
			 //关闭连接对象conn
			 if(conn!=null){
				conn.close();
			 }
			if(stmt!=null){
				stmt.close();
			}
			if(pstmt!=null){
				pstmt.close();
			}
			//关闭处理结果
			if(rs!=null){
				rs.close();
				}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	/**
	 * 只适合 增加 		删除	 	修改	操作
	 * 执行SQL语句的方法
	 */
	 public void execute(String sql,Object param[]) {
		 Connection conn=null;
		 PreparedStatement ps=null;
		 conn=this.getConnection();
		 try {
			ps=conn.prepareStatement(sql);
			 if(param!=null && param.length!=0){
				 for(int i=0;i<param.length;i++){
					 	//把每一个对象param放入数组中
						ps.setObject((i+1), param[i]);	
					} 
			 }
			 ps.execute();//执行SQL语句
			 this.close(conn, null, ps, null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	 }
	
}
