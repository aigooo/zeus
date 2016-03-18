package com.zeus.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class FileUtil {

	public static void createFile(String filePath,String content){
        File file = new File(filePath);
        try {
            if(!file.exists()){
                if(!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes(Charset.forName("UTF-8")));
            fos.flush();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String findLine(String all,String... keywords){
    	String[] lines = all.split("\n");
    	for(String line : lines){
    		Boolean isThis = Boolean.TRUE;
    		for(String keyword:keywords){
    			if(!line.contains(keyword)){
    				isThis = Boolean.FALSE;
    				break;
    			}
    		}
    		if(isThis){
    			return line;
    		}
    	}
    	return "";
    }
    
    public static String fileContent(String filePath) throws IOException{
    	String all="",line = "";
    	InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(filePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		while ((line = br.readLine()) != null) {
			all = all + line + "\n";
		}
		return all;
    }
    
    public static String fileContent(File file) throws IOException{
    	String all="",line = "";
    	InputStream is = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		while ((line = br.readLine()) != null) {
			all = all + line + "\n";
		}
		br.close();
		return all;
    }
}
