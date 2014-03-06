package com.b409.recommendation;

import com.b409.commonTool.getKeyword;
import com.b409.db.recommendFileKeywords;
import com.b409.db.recommendFilesToUsers;
import com.b409.db.recommendUserLabel;

public class createKeyWordsForFile {
	public static void createKeyWordsForFile(String user_id,String file_source_path,String file_target_path){
		
		//获得关键词
		String keywords = getKeyword.getKeywordInString(file_source_path);
		System.out.println("成功获取文章"+file_source_path+"的关键词!");
		
		//更新文件关键字表
		recommendFileKeywords.insert_into_recommend_file_keywords(user_id, file_target_path, keywords);	
		System.out.println("成功更新文章"+file_target_path+"的关键词!");
		
		//更新该用户标签表
		recommendUserLabel.insert_into_recommend_user_label(user_id, keywords);
		System.out.println("成功更新用户"+user_id+"标签");

		//更新用户推荐表
		recommendFilesToUsers.get_recommend_files_to_user(user_id);
		System.out.println("成功生成推荐列表");
	}

	public static void main(String[] args){
		createKeyWordsForFile("test","C:/Users/lpshou2/Desktop/计算机相关.docx","d:/test/");
	}

}
