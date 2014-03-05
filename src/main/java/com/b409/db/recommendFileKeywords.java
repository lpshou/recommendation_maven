package com.b409.db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.mysql.jdbc.PreparedStatement;

public class recommendFileKeywords {
	public static String driver = "com.mysql.jdbc.Driver";
	public static String url = "jdbc:mysql://192.168.0.87:3306/mcloud";
	public static String user = "root";
	public static String password = "123456";
	
	//将keywords切分成单个单词放入数组
	public static ArrayList<String> split(String keywords){
		ArrayList<String> keywordsArrayList = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(keywords,",");
		while(tokenizer.hasMoreTokens()){
			String strTemp = tokenizer.nextToken();
			keywordsArrayList.add(strTemp);
			System.out.println(strTemp);
		}
		return keywordsArrayList;
	}
	
	//查询
	public static void query_recommend_file_keywords(){
		try{
			//加载驱动程序
			Class.forName(driver);
			
			//连接数据库
			java.sql.Connection conn = DriverManager.getConnection(url, user, password);
			if(!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			Statement statement = conn.createStatement();
			String sql = "select * from recommend_file_keywords";
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()){
				int user_id = rs.getInt("user_id");
				String file_path = rs.getString("file_path");
				String keyword =  rs.getString("keyword");
				System.out.println(user_id+"  "+keyword+"  "+file_path);
				
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
	public static Integer insert_into_recommend_file_keywords(String user_id, String file_path, String keywords){
		Integer flagInteger=0;
		ArrayList<String> keyword = recommendFileKeywords.split(keywords);
		
		try{
			//加载驱动程序
			Class.forName(driver);
			
			//连接数据库
			java.sql.Connection conn = DriverManager.getConnection(url, user, password);
			if(!conn.isClosed())
				System.out.println("连接数据库...Ok！");

			//不自动commit
			conn.setAutoCommit(false);
			String sql = "insert into recommend_file_keywords(user_id,file_path,keyword) values(?,?,?)";

			PreparedStatement statement = (PreparedStatement) conn.prepareStatement(sql);
			for(int i=0;i<keyword.size();i++){
				statement.setString(1, user_id);
				statement.setString(2, file_path);
				statement.setString(3, keyword.get(i));
				statement.addBatch();		
			}
			//批量执行
			int []counts = statement.executeBatch();
			conn.commit();
	
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
	
	
	public static void main(String[] args){
//		query_recommend_file_keywords();
		insert_into_recommend_file_keywords("0", "D:/A.txt", "喜剧");
	}

}
