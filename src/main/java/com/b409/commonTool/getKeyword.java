package com.b409.commonTool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.utils.ParseUtils;
import org.wltea.analyzer.lucene.IKQueryParser;


/**
 * 自动提取关键字
 * 步骤：
 * 1：使用中文分词技术，得到所以词组
 * 2：根据每个词组在文章中检索出现的次数
 * 3：采用HashMap,进行词频统计（注意：HashMap效率不是很高.网上有很多词频统计算法，这里不一一列举）
 * @author zhugf 2010-04-13
 * 采用的开源代码，部分修正，by liupeng
 *
 */
public class getKeyword {
	
	static Map<String,Integer> map =new HashMap<String,Integer>();
	
	
	//从title中通过分词获得词组
	public static String getSplitString(String title){
		String keyword="";
		try {
			//使用IKQueryParser查询分析器构造Query对象
			Query query1 = IKQueryParser.parse("", title);
			String str=query1.toString();
			str=str.replace(" ", "+");
			str=str.replace("(", "");
			str=str.replace(")", "");
			str=str.replace("++", "+");
			String[] words=str.split("\\+");/** \\+ **/
			for(int j=0;j<words.length;j++){
				if(words[j].length()>1){
					keyword=keyword+words[j]+",";
					//查找字符串key在words中出现的次数的程序
					getCount(title,words[j]);
					//System.out.println(words[j]);
				}
			}
			return keyword;
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return keyword;
	}
	
	public static int getCount(String words,String key){
        int count=0;
        int temp;
        if(words.length()>=key.length()){
            for(int i=0;i<=words.length();i++){
                temp=words.indexOf(key);
                if(temp>=0){
                    ++count;
                    words=words.substring(temp+1);
                }
            }
        }
        map.put(key,count);
        return count;
    }
	
	//获取按照词频排序的结果
	public static String getTopN(){
		String keys="";
		List<Map.Entry<String, Integer>> infoIds = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());    
		//排序前    
		for (int i = 0; i < infoIds.size(); i++) {    
			String id = infoIds.get(i).toString();    
		}    
		//排序    
		Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {    
		public int compare(Map.Entry<String, Integer> o1,    
			Map.Entry<String, Integer> o2) {    
				return (o2.getValue() - o1.getValue());    
			}    
			});    

		//排序后    （获取top5结果）
		for (int i = 0; i < infoIds.size()&& i<10; i++) {    
			String id = infoIds.get(i).getKey();
			keys=keys+id+",";
		}  
		return keys;
	}
	
	//从文章中提取关键词
	public static String getKeywordInString(String filePath){
		String context = getContextFromFile.getContext(filePath);
		BooleanQuery.setMaxClauseCount(10000);
		getSplitString(context);
		String keyword = getTopN();
		return keyword;
	}
	
}
