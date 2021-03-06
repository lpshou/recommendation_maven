package com.b409.db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.b409.commonTool.databaseConfig;
import com.b409.commonTool.splitString;
import com.mysql.jdbc.PreparedStatement;

public class recommendUserLabel implements databaseConfig{
	
	//插入
	public static Integer insert_into_recommend_user_label(String user_id, String keywords){
		Integer flagInteger=0;
//		ArrayList<String> keywordList = recommendFileKeywords.split(keywords);
		ArrayList<String> keywordList = splitString.getArrayListFromString(keywords, ",");
		
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
				String sql = "select * from filemanage_recommend_user_label where user_id = '"+user_id +"' and keyword = '"+keyword+"'";
				ResultSet rs = statement.executeQuery(sql);
				
				//如果不存在keyword记录，则添加一条记录
				if(!rs.next()){
					String sqlInsert = "insert into filemanage_recommend_user_label(user_id,keyword,count) values(?,?,?)";
					PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sqlInsert);
					ps.setString(1, user_id);
					ps.setString(2, keyword );
					ps.setInt(3, 1);
					ps.executeUpdate();
				//如果记录存在，则将count值加1
				}else{
					int temp=1;
					String sqlUpdate = "update filemanage_recommend_user_label set count= count+1 where user_id = '"+user_id +"' and keyword = '"+keyword+"'";
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
