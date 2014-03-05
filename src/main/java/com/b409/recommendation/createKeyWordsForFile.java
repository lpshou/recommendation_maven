package com.b409.recommendation;

import java.io.File;
import java.util.ArrayList;

import com.b409.commonTool.getContextFromFile;
import com.b409.commonTool.getKeyword;
import com.b409.commonTool.noChangeVariable;
import com.b409.db.DBUtil;
import com.b409.db.recommendFileKeywords;
import com.b409.db.recommendFilesToUsers;
import com.b409.db.recommendUserLabel;
import com.b409.recommendation_maven.test;

public class createKeyWordsForFile {
	public static void createKeyWordsForFile(String user_id,String file_source_path,String file_target_path){
		
		//获得关键词
		String keywords = getKeyword.getKeywordInString(file_source_path);
		System.out.println(keywords);
		
		//更新文件关键字表
		recommendFileKeywords.insert_into_recommend_file_keywords(user_id, file_target_path, keywords);	
//		
//		//更新该用户标签表
		recommendUserLabel.insert_into_recommend_user_label(user_id, keywords);
//		
//		//更新用户推荐表
		recommendFilesToUsers.get_recommend_files_to_user(user_id);
	}
	public static void test(String v1,String v2,String v3){
		System.out.println(v1+"  "+v2+"  "+v3);
	}
	public static void main(String[] args){
//		createKeyWordsForFile(args[0], args[1], args[3]);
		test(args[0], args[1], args[2]);
		
	}

}
