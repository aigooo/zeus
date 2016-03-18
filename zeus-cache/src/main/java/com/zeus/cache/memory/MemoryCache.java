package com.zeus.cache.memory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.zeus.cache.Cache;
import com.zeus.common.util.JsonUtil;

public class MemoryCache  implements Cache{
	
	Map<String,String> cache = new ConcurrentHashMap<>();
	public static MemoryCache instance = new MemoryCache();
	
	private MemoryCache(){
		
	}

	@Override
	public String get(String key) {
		return cache.get(key);
	}

	@Override
	public <T> T get(String key, Class<T> clazz) {
		return JsonUtil.parseObject(cache.get(key), clazz);
	}

	@Override
	public <T> List<T> getList(String key, Class<T> clazz) {
		return JsonUtil.parseObjects(cache.get(key), clazz);
	}

	@Override
	public int put(String key, Object object) {
		cache.put(key,JsonUtil.toJSONString(object));
		return 1;
	}

	@Override
	public long delete(String pattern) {
		cache.remove(pattern);
		return 1;
	}

	@Override
	public boolean exist(String key) {
		return cache.containsKey(key);
	}

}
