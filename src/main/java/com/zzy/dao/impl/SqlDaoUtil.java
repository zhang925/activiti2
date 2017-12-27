package com.zzy.dao.impl;


import java.sql.*;
import java.util.*;
import com.zzy.dao.BaseDao;
import com.zzy.dao.SqlBaseDao;
import com.zzy.model.MsgModel;
import com.zzy.model.TempModel;

public class SqlDaoUtil extends SqlBaseDao{

	public List getAllTables() {
		String sql="SELECT *  FROM a_test";
		List list=new ArrayList();
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			conn=this.getConnection();
			ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
			while(rs.next()){
				Map map = new HashMap();
				map.put("id",rs.getInt("id"));
				map.put("name",rs.getString("name"));
				list.add(map);
			}	
			this.close(conn, null, ps, rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
	
		}	
		return list;
	}

	/*public User getBook(int id) {
		String sql="SELECT * FROM users where id=";
		StringBuffer sb=new StringBuffer();
		sb=sb.append(sql);
		sb=sb.append(id);
		sql=sb.toString();
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		User u=new User();
		try {
			conn=this.getConnection();
			ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
				while(rs.next()){
					u.setId(rs.getInt("id"));
					u.setUid(rs.getString("uid"));
					u.setName(rs.getString("name"));
					u.setUsername(rs.getString("username"));
					u.setPassword(rs.getString("password"));
					u.setPasswordmd5(rs.getString("passwordmd5"));
				}
				//关闭连接
				this.close(conn, null,ps, rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return u;
	}

	public User login(String username, String password) {
		String sql="SELECT * FROM users where username='"+username+"' and password='"+password+"'";
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		User u=new User();
		try {
			conn=this.getConnection();
			ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
				while(rs.next()){
					u.setId(rs.getInt("id"));
					u.setUid(rs.getString("uid"));
					u.setName(rs.getString("name"));
					u.setUsername(rs.getString("username"));
					u.setPassword(rs.getString("password"));
					u.setPasswordmd5(rs.getString("passwordmd5"));
				}
				//关闭连接
				this.close(conn, null,ps, rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return u;
	}*/

}
