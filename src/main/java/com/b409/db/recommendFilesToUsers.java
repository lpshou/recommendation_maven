package com.b409.db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.b409.commonTool.databaseConfig;
import com.b409.commonTool.splitString;
import com.mysql.jdbc.PreparedStatement;

public class recommendFilesToUsers implements databaseConfig{

	//查询用户标签
	public static String query_top_n_label(String user_id,int n){
		String label="";
		try{
			//加载驱动程序
			Class.forName(driver);
			
			//连接数据库
			java.sql.Connection conn = DriverManager.getConnection(url, user, password);
			if(conn.isClosed())
				System.out.println("连接数据库...failed！");

			Statement statement = conn.createStatement();
			String sql = "select * from filemanage_recommend_user_label where user_id='"+user_id+"' order by count desc limit " + n;
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()){
				String keywordss = rs.getString("keyword");
				if(label.equals("")){
					label=label+keywordss;
				}else{
					label=label+","+keywordss;
				}
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
		return label;
	}
	
	//查询拥有某个标签的其他用户共享的所有文件
	//三个限制条件：1）有某个标签；2）其他用户的；3）共享的
	public static Set query_files_contains_label(String user_id,String keyword){
		Set files = new HashSet();
		try{
			//加载驱动程序
			Class.forName(driver);
			
			//连接数据库
			java.sql.Connection conn = DriverManager.getConnection(url, user, password);
			if(conn.isClosed()){
				System.out.println("连接数据库...failed！");
				return files;
			}
			
			Statement statement = conn.createStatement();
			String sql = "select * from filemanage_recommend_file_keywords where keyword='"
					+keyword+"' and user_id != '"
					+user_id+"' and file_acl = 'public'";
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()){
				String file_path = rs.getString("file_path");
				files.add(file_path);	
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
		return files;
	}
	
	//插入
	public static Integer insert_into_recommend_files_to_users(String user_id, String recommendfiles){
		Integer flagInteger=0;
		try{
			//加载驱动程序
			Class.forName(driver);
			
			//连接数据库
			java.sql.Connection conn = DriverManager.getConnection(url, user, password);
			if(conn.isClosed()){
				System.out.println("连接数据库...failed！");
				return 0;
			}
			
			Statement statement = conn.createStatement();
			String sql = "select * from filemanage_recommend_files_to_users where user_id = '"+user_id+"'";
			ResultSet rs = statement.executeQuery(sql);
			//如果不存在记录
			if(!rs.next()){
				String sql1 = "insert into filemanage_recommend_files_to_users(user_id,recommendfiles) values(?,?)";
				PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql1);
				ps.setString(1, user_id);
				ps.setString(2, recommendfiles);
				int i = ps.executeUpdate();
			}else{
			//如果存在记录，则更新用户推荐文件
				String sqlUpdate = "update filemanage_recommend_files_to_users set recommendfiles='"+recommendfiles+"' where user_id = '"+user_id +"'";
				Statement stat = conn.createStatement();
				stat.executeUpdate(sqlUpdate);
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
	//根据用户标签分析得到用户喜欢的且不属于自己的文件（用户标签个数默认为3）
	public static String get_recommend_files_to_user(String user_id){
		
		//recommend_files存放推荐文件
		//首先存放包含三个关键词的文件
		//其次存放包含二个关键词的文件
		//最后存放包含一个关键词的文件
		String recommend_files="";
		String user_labels = query_top_n_label(user_id, 15);//设定为15，取前3个重点分析
//		System.out.println(user_labels);
		ArrayList<String> user_label_arraylistArrayList = splitString.getArrayListFromString(user_labels, ",");
	
		Set set1=new HashSet();
		Set set2=new HashSet();
		Set set3=new HashSet();
		//得到每个关键词对应的文件，结果放入set中
		if(user_label_arraylistArrayList.size()>=1){
			set1=query_files_contains_label(user_id, user_label_arraylistArrayList.get(0));
		}
		if(user_label_arraylistArrayList.size()>=2){
			set2=query_files_contains_label(user_id, user_label_arraylistArrayList.get(1));
		}
		if(user_label_arraylistArrayList.size()>=3){
			set3=query_files_contains_label(user_id, user_label_arraylistArrayList.get(2));
		}
		
		
		//将后12个关键词对应的文件记录下来，
		ArrayList<String> recommend_files_of_the_last_twelve = new ArrayList();
		for(int i=3;i<user_label_arraylistArrayList.size();i++){
			Set set_temp = new HashSet();
			set_temp=query_files_contains_label(user_id, user_label_arraylistArrayList.get(i));
			Iterator<String>iter_temp = set_temp.iterator();
			while(iter_temp.hasNext()){
				recommend_files_of_the_last_twelve.add(iter_temp.next());
			}
		}
		
		//将set转化为list保存
		List list1=new ArrayList(set1);
		List list2=new ArrayList(set2);
		List list3=new ArrayList(set3);

		//recommend_files_list_temp用于临时保存推荐文件
		List recommend_files_list_tempList = new ArrayList();
		//获取包含三个关键词的文件
		set1.retainAll(set2);
		set1.retainAll(set3);
		Iterator<String> iter = set1.iterator();
		while(iter.hasNext()){
			recommend_files_list_tempList.add(iter.next());
		}
		
		//获取包含两个关键词的文件
		set1=new HashSet(list1);
		set2=new HashSet(list2);
		set1.retainAll(set2);
		Iterator<String> iterator1 = set1.iterator();
		while(iterator1.hasNext()){
			recommend_files_list_tempList.add(iterator1.next());
		}
		set1=new HashSet(list1);
		set3=new HashSet(list3);
		set1.retainAll(set3);
		Iterator<String> iterator2 = set1.iterator();
		while(iterator2.hasNext()){
			recommend_files_list_tempList.add(iterator2.next());
		}
		set3=new HashSet(list3);
		set2=new HashSet(list2);
		set3.retainAll(set2);
		Iterator<String> iterator3 = set3.iterator();
		while(iterator3.hasNext()){
			recommend_files_list_tempList.add(iterator3.next());
		}
		
		//获取包含一个关键词的文件
		set1=new HashSet(list1);
		set2=new HashSet(list2);
		set3=new HashSet(list3);
		Iterator<String> itera1 = set1.iterator();
		while(itera1.hasNext()){
			recommend_files_list_tempList.add(itera1.next());
		}
		Iterator<String> itera2 = set2.iterator();
		while(itera2.hasNext()){
			recommend_files_list_tempList.add(itera2.next());
		}
		Iterator<String> itera3 = set3.iterator();
		while(itera3.hasNext()){
			recommend_files_list_tempList.add(itera3.next());
		}
		
		//获取后面12个关键词包含的文件
		for(int j=0;j<recommend_files_of_the_last_twelve.size();j++){
			recommend_files_list_tempList.add(recommend_files_of_the_last_twelve.get(j));
		}
		
		//重新整理recommend_files_list_tempList，去掉重复元素
		List list_final = new ArrayList<String>();
		for(int i=0;i<recommend_files_list_tempList.size();i++){
			if(!list_final.contains(recommend_files_list_tempList.get(i))){
				list_final.add(recommend_files_list_tempList.get(i));
			}
		}
		
		//recommend_files_list_tempList 变为字符串
		for(int i=0;i<list_final.size();i++){
			if(recommend_files.equals("")){
				recommend_files=recommend_files+list_final.get(i);
			}else{
				recommend_files=recommend_files+","+list_final.get(i);
			}
		}
		
		//将向用户推荐的文件插入数据库
		insert_into_recommend_files_to_users(user_id, recommend_files);
		return recommend_files;
	}
	

}
