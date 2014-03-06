package com.b409.commonTool;

import java.io.File;
import java.io.IOException;

import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.utils.ParseUtils;

public class getContextFromFile {

	//从文件中读取文件内容，返回文件内容值
	public static String getContext(String path) {  
        String result = "";  
        TikaConfig tikaConfig = TikaConfig.getDefaultConfig();  
        try {  
            result = ParseUtils.getStringContent(new File(path), tikaConfig);  
        }catch (Exception e) {  
           // log.debug("[by ninja.hzw]" + e);  
        }  
        return result;  
    } 
	
	//从文件中读取文件内容，返回文件内容值(方法2,功能相同)
	public static String getContent2(File file){ 
		Tika tika=new Tika();
		String content=null;
		try {
			content=tika.parseToString(file);
		} catch (TikaException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		return content;
	}
}
