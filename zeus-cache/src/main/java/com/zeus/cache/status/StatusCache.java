package com.zeus.cache.status;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class StatusCache{
	
	static ConcurrentHashMap<String, Status> map = new ConcurrentHashMap<>();

	public static Status getStatus(String key){
		return map.get(key);
	}
	
	public static void success(String key){
		if(map.containsKey(key))
			map.get(key).setStatus(Status.SUCCESS);;
	}
	
	public static void success(String key,String message){
		if(map.containsKey(key))
			map.get(key).setStatus(Status.SUCCESS).setMessage(message);
	}

	public static void doing(String key){
		if(map.containsKey(key))
			map.get(key).setStatus(Status.DOING);
	}
	
	public static void doing(String key,String message){
		if(map.containsKey(key))
			map.get(key).setStatus(Status.DOING).setMessage(message);
	}
	
	public static void failed(String key){
		if(map.containsKey(key))
			map.get(key).setStatus(Status.FAILED);;
	}
	
	public static void failed(String key,String message){
		if(map.containsKey(key))
			map.get(key).setStatus(Status.FAILED).setMessage(message);
	}
	
	public static void cancal(String key){
		if(map.containsKey(key))
			map.get(key).setStatus(Status.CANCELING);;
	}
	
	public static void cancaled(String key){
		if(map.containsKey(key))
			map.get(key).setStatus(Status.CANCELED);
	}
	
	public static String getKey(){
		String key = "" + (new Date()).getTime() + Math.round(Math.random()*10000);
		map.put(key, new Status().setStatus(Status.STARTING));
		return key;
	}
}
