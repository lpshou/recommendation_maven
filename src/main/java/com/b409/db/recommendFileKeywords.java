package com.b409.db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.b409.commonTool.databaseConfig;
import com.b409.commonTool.splitString;
import com.mysql.jdbc.PreparedStatement;

public class recommendFileKeywords implements databaseConfig {

	//插入用户关键词，
	//如果该用户的该篇文章对应的keywords已经存在，则将次数加1
	//如果该用户的该篇文章对应的keywords不存在，则添加一条记录
	public static Integer insert_into_filemanage_recommend_file_keywords(String user_id, String file_path, 
			String keywords,String file_name,String file_acl){
		Integer flagInteger=0;
		ArrayList<String> keywordList = splitString.getArrayListFromString(keywords, ",");
		try{
			//加载驱动程序
			Class.forName(driver);
			
			//连接数据库
			java.sql.Connection conn = DriverManager.getConnection(url, user, password);
			if(conn.isClosed())
				System.out.println("连接数据库...failed！");
			
			for(int i=0;i<keywordList.size();i++){
				//首先查询判断该用户的该篇文章对应的keywords是否已经存在
				//针对每一个keyword进行操作
				String keyword = keywordList.get(i);
				Statement statement = conn.createStatement();
				String sql = "select * from filemanage_recommend_file_keywords where user_id = '"
							+ user_id + "' and keyword = '"
						    + keyword + "' and file_path = '"
						    +file_path+"' and file_acl = '"
						    +file_acl+"'";
				ResultSet rs = statement.executeQuery(sql);
				
				//如果不存在keyword记录，则添加一条记录
				if(!rs.next()){
					String sqlInsert = "insert into filemanage_recommend_file_keywords(user_id,file_path,keyword,file_name,file_acl) values(?,?,?,?,?)";
					PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sqlInsert);
					ps.setString(1, user_id);
					ps.setString(2, file_path);
					ps.setString(3, keyword);
					ps.setString(4, file_name);
					ps.setString(5, file_acl);
					ps.executeUpdate();
				}else{
					//不执行操作
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
