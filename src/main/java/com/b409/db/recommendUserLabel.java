package com.b409.db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.jdbc.PreparedStatement;

public class recommendUserLabel {
	public static String driver = "com.mysql.jdbc.Driver";
	public static String url = "jdbc:mysql://192.168.0.87:3306/mcloud";
	public static String user = "root";
	public static String password = "123456";
	
	//查询
	public static void query_user_label(){
		try{
			//加载驱动程序
			Class.forName(driver);
			
			//连接数据库
			java.sql.Connection conn = DriverManager.getConnection(url, user, password);
			if(conn.isClosed())
				System.out.println("连接数据库...failed！");
			Statement statement = conn.createStatement();
			String sql = "select * from recommend_user_label";
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()){
				String user_id = rs.getString("user_id");
				String keyword = rs.getString("keyword");
				Integer count = rs.getInt("count");
//				System.out.println(user_id+"  "+keyword+"  "+count);
				
			}
			rs.close();
			conn.close();
		}catch(ClassNotFoundException e) {   
			System.out.println("Sorry,can`t find the Driver!");   
			e.printStackTrace();   
			} catch(SQLException e) {   
			e.printStackTrace();   
			} catch(Exception e) {   
			e.printStackTrace();   
			}  
	}
	
	//插入
	public static Integer insert_into_recommend_user_label(String user_id, String keywords){
		Integer flagInteger=0;
		ArrayList<String> keywordList = recommendFileKeywords.split(keywords);
		
		try{
			//加载驱动程序
			Class.forName(driver);
			
			//连接数据库
			java.sql.Connection conn = DriverManager.getConnection(url, user, password);
			if(conn.isClosed())
				System.out.println("连接数据库...failed！");
			for(int i=0;i<keywordList.size();i++){
				//针对每一个keyword进行操作
				String keyword = keywordList.get(i);
				Statement statement = conn.createStatement();
				String sql = "select * from recommend_user_label where user_id = '"+user_id +"' and keyword = '"+keyword+"'";
				ResultSet rs = statement.executeQuery(sql);
				
				//如果不存在keyword记录，则添加一条记录
				if(!rs.next()){
					System.out.println("keyword: '" + keyword + "'不存在....所以...你懂得，添加进去！");
					
					String sqlInsert = "insert into recommend_user_label(user_id,keyword,count) values(?,?,?)";
					PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sqlInsert);
					ps.setString(1, user_id);
					ps.setString(2, keyword );
					ps.setInt(3, 1);
					ps.executeUpdate();
				//如果记录存在，则将count值加1
				}else{
				
					System.out.println("keyword: '" + keyword + "'存在....所以...你懂得，将该关键字的次数加1");
					int temp=1;
					String sqlUpdate = "update recommend_user_label set count= count+1 where user_id = '"+user_id +"' and keyword = '"+keyword+"'";
					Statement stat = conn.createStatement();
					stat.executeUpdate(sqlUpdate);
				}
			}
			conn.close();
		}catch(ClassNotFoundException e) {   
			System.out.println("Sorry,can`t find the Driver!");   
			e.printStackTrace();   
			} catch(SQLException e) {   
			e.printStackTrace();   
			} catch(Exception e) {   
			e.printStackTrace();   
			}  
		return flagInteger;
	}


}
