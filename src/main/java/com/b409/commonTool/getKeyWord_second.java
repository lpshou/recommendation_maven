package com.b409.commonTool;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class getKeyWord_second {
	//读取文件内容
	public static String readFile(String file)
	{
		Tika tika=new Tika();
		String content=null;
		try {
			content=tika.parseToString(new File(file));
		} catch (TikaException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		return content;
	}
	
	/*分词函数*/
	public static ArrayList<String> cutWords(String file) 
	{
		ArrayList<String> words = new ArrayList<String>();
		String text = readFile(file);
		StringReader reader = new StringReader(text);
		
		IKSegmenter ik = new IKSegmenter(reader, true);
		Lexeme lex = null;
		try {
			while((lex=ik.next())!=null){
				words.add(lex.getLexemeText());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return words;	
		
	}
	
	//  计算一个文件中某词条出现的次数
    //	若是英文，则统计时全部转化为小写统计
	public static HashMap<String,Integer> wordFrequency(ArrayList<String> cutwords)
	{
		HashMap<String,Integer> hm = new HashMap<String, Integer>();
		for(int i = 0; i <cutwords.size(); i ++){
			String word = cutwords.get(i).toLowerCase();
			if(!hm.containsKey(word)){
				hm.put(word, 1);
			}
			else{
				hm.put(word, hm.get(word)+1);
				
			}
			}
		return hm;
		}
	
	public static String sort(HashMap<String, Integer> map){
		String keys="";
		List<Map.Entry<String, Integer>> infoIds = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());    
		//排序前    
		for (int i = 0; i < infoIds.size(); i++) {    
			String id = infoIds.get(i).toString();    
			//System.out.println(id);    
		}    
		//排序    
		Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {    
		public int compare(Map.Entry<String, Integer> o1,    
			Map.Entry<String, Integer> o2) {    
				return (o2.getValue() - o1.getValue());    
			}    
			});    
		//排序后    
		for (int i = 0; i < infoIds.size()&& i<10; i++) {    
			//String id = infoIds.get(i).toString();    
			String id = infoIds.get(i).getKey();
			keys=keys+id+",";
			//System.out.println(id);    
		}  
		return keys;
	}
	
	//得到关键词
	public static String getKeyWords(String path){
		ArrayList<String> cutWordsArrayList=cutWords(path);
		HashMap<String, Integer> wordFrequencyHashMap = wordFrequency(cutWordsArrayList);
		String topNResult = sort(wordFrequencyHashMap);
		return topNResult;
		
	}
	public static void main(String[] args) {
		
		System.out.println(getKeyWords(noChangeVariable.PATH));
	}
}
