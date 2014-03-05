package com.b409.db;

import java.sql.*;




public class DBUtil {
	public static String driver = "com.mysql.jdbc.Driver";
	public static String url = "jdbc:mysql://192.168.0.87:3306/mcloud";
	public static String user = "root";
	public static String password = "123456";

	public static void insertIntodb(String targetPath,String keywords){
		
	}
	
	public static void main(String[] args){
		try{
			//加载驱动程序
			Class.forName(driver);
			
			//连接数据库
			java.sql.Connection conn = DriverManager.getConnection(url, user, password);
			if(!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			Statement statement = conn.createStatement();
			String sql = "select * from recommend_user";
			ResultSet rs = statement.executeQuery(sql);
			String name="";
			while(rs.next()){
				name = rs.getString("username");
				System.out.println(name);
				
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
	
}
