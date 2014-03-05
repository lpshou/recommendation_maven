package com.b409.db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import java.util.HashSet;
import java.util.Set;

import com.b409.commonTool.splitString;
import com.mysql.jdbc.PreparedStatement;

public class recommendFilesToUsers {
	public static String driver = "com.mysql.jdbc.Driver";
	public static String url = "jdbc:mysql://192.168.0.87:3306/mcloud";
	public static String user = "root";
	public static String password = "123456";

	//查询用户标签
	public static String query_top_n_label(String user_id,int n){
		String label="";
		try{
			//加载驱动程序
			Class.forName(driver);
			
			//连接数据库
			java.sql.Connection conn = DriverManager.getConnection(url, user, password);
			if(!conn.isClosed())
				System.out.println("连接数据库...Ok！");

			Statement statement = conn.createStatement();
			String sql = "select * from recommend_user_label where user_id='"+user_id+"' order by count desc limit " + n;
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()){
				String keywordss = rs.getString("keyword");
				if(label.equals("")){
					label=label+keywordss;
				}else{
					label=label+","+keywordss;
				}
			}
			System.out.println(label);
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
		
		return label;
	}
	
	//查询拥有某个标签且不属于某个用户的所有文件
	public static Set query_files_contains_label(String user_id,String keyword){
		Set files = new HashSet();
		try{
			//加载驱动程序
			Class.forName(driver);
			
			//连接数据库
			java.sql.Connection conn = DriverManager.getConnection(url, user, password);
			if(!conn.isClosed())
				System.out.println("连接数据库...Ok！");
			Statement statement = conn.createStatement();
			String sql = "select * from recommend_file_keywords where keyword='"+keyword+"' and user_id != '"+user_id+"'";
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()){
				String file_path = rs.getString("file_path");
				files.add(file_path);	
			}
			System.out.println(files);
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
		return files;
	}
	
	//插入
	public static Integer insert_into_recommend_files_to_users(String user_id, String recommendfiles){
		Integer flagInteger=0;
//		ArrayList<String> keyword = recommendFileKeywords.split(keywords);
		
		try{
			//加载驱动程序
			Class.forName(driver);
			
			//连接数据库
			java.sql.Connection conn = DriverManager.getConnection(url, user, password);
			if(!conn.isClosed())
				System.out.println("连接数据库...Ok！");

			String sql = "insert into recommend_files_to_users(user_id,recommendfiles) values(?,?)";

			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setString(1, user_id);
			ps.setString(2, recommendfiles);
			
			int i = ps.executeUpdate();
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
	//根据用户标签分析得到用户喜欢的且不属于自己的文件（用户标签个数默认为3）
	public static String get_recommend_files_to_user(String user_id){
		String recommend_files="";
		String user_labels = query_top_n_label(user_id, 3);
		ArrayList<String> user_label_arraylistArrayList = splitString.getArrayListFromString(user_labels, ",");
		System.out.println(user_label_arraylistArrayList.size());
		
		//根据每一个标签查询数据库，得到拥有此标签的所有文件
		
		return recommend_files;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		insert_into_recommend_files_to_users("0", "c:/b.txt");
//		query_top_n_label("0",3);
//		get_recommend_files_to_user("0");
		query_files_contains_label("0", "恢复");
		
		
	}

}
