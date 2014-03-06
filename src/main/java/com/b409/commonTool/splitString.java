package com.b409.commonTool;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class splitString {
	//将获取字符串str中以,分割的词组，放入arraylist中
	public static ArrayList<String> getArrayListFromString(String str,String flag){
		ArrayList<String> keywordsArrayList = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(str,flag);
		while(tokenizer.hasMoreTokens()){
			String strTemp = tokenizer.nextToken();
			keywordsArrayList.add(strTemp);
		}
		return keywordsArrayList;
	}
}
