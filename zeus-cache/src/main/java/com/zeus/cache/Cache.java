package com.zeus.cache;

import java.util.List;

public interface Cache {
	
	public String get(String key);
	
	public <T> T get(String key,Class<T> clazz);
	
	public <T> List<T> getList(String key,Class<T> clazz);
	
	public int put(String key,Object object);
	
	public long delete(String pattern);
	
	public boolean exist(String key);
	
}
