package com.zeus.common.util;

import java.util.HashMap;
import java.util.Map;

public class DictionaryUtil{
	
	private static Map<String,Map<String,String>> context;
	
	private static DictionaryUtil dictionary = new DictionaryUtil();
	
	private DictionaryUtil(){
		
	}
	public static DictionaryUtil init(Map<String,Map<String,String>> context){
		DictionaryUtil.context = context;
		return dictionary;
	}
	
	public static DictionaryUtil reload(Map<String,Map<String,String>> context){
		DictionaryUtil.context = context;
		return dictionary;
	}
	
	public static DictionaryUtil reloadByIndex(String index,Map<String,String> items){
		DictionaryUtil.context.put(index, items);
		return dictionary;
	}
	
	public static DictionaryUtil removeByIndex(String index){
		DictionaryUtil.context.remove(index);
		return dictionary;
	}
	
	public static Map<String,String> getByIndex(String index){
		Map<String,String> item = context.get(index);
		return item!=null?item:new HashMap<>();
	}
	
	public static String getByIndexAndItem(String index,String item){
		if(context.get(index)!=null){
			return context.get(index).get(item);
		}else{
			return "";
		}
	}
	
	public static String getByIndexAndItem(String indexItem){
		if(indexItem.contains("#")){
			String index = indexItem.split("#")[0];
			String item = indexItem.split("#")[1];
			return getByIndexAndItem(index,item);
		}else{
			return "";
		}
	}
}
