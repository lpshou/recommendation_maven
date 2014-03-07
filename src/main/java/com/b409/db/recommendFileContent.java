package com.b409.db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.b409.commonTool.splitString;
import com.mysql.jdbc.PreparedStatement;

public class recommendFileContent {
	public static String driver = "com.mysql.jdbc.Driver";
	public static String url = "jdbc:mysql://192.168.0.87:3306/mcloud";
	public static String user = "root";
	public static String password = "123456";
	//插入文章摘要信息
	public static Integer insert_into_filemanage_recommend_file_content(String file_path, String file_contents){
		String file_content="";
		if(file_contents.length()>500){
			file_content = file_contents.substring(0, 500);
			
		}else{
			file_content = file_contents;
		}
		Integer flagInteger=0;
		try{
			//加载驱动程序
			Class.forName(driver);
			
			//连接数据库
			java.sql.Connection conn = DriverManager.getConnection(url, user, password);
			if(conn.isClosed())
				System.out.println("连接数据库...failed！");
			

			Statement statement = conn.createStatement();
			String sql = "select * from filemanage_recommend_file_content where file_path = '"
						+ file_path + "' and content = '"
					    + file_content + "'";
			ResultSet rs = statement.executeQuery(sql);
			
			//如果不存在keyword记录，则添加一条记录
			if(!rs.next()){
				String sqlInsert = "insert into filemanage_recommend_file_content(file_path,content) values(?,?)";
				PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sqlInsert);
				ps.setString(1, file_path);
				ps.setString(2, file_content);
				ps.executeUpdate();
			}else{
				//不执行操作
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
