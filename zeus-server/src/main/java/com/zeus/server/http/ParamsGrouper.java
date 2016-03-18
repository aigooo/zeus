package com.zeus.server.http;

import java.util.HashMap;
import java.util.Map;

import io.vertx.core.MultiMap;

public class ParamsGrouper {
	
	public static Map<String,Map<String,String>> group(MultiMap params){
		Map<String,Map<String,String>> paramsGroup = new HashMap<>();
		
		params.entries().forEach(entry->{
			String[] prefixs = getPrefixs(entry.getKey());
			for(String prefix:prefixs){
				Map<String,String> map = paramsGroup.get(prefix);
				if(map==null){
					map = new HashMap<String, String>();
					paramsGroup.put(prefix, map);
				}
				map.put(entry.getKey(), entry.getValue());
			}
		});
		return paramsGroup;
	}
	
	private static String[] getPrefixs(String key){
		String[] items = key.split("\\.");
		if(items.length==1){
			return new String[]{""};
		}
		String[] results = new String[items.length-1];
		for(int i=1;i<items.length;i++){
			String result = "";
			for(int j=0;j<i;j++){
				result = result + items[j] + ".";
			}
			results[i-1] = result;
		}
		return results;
	}
}
