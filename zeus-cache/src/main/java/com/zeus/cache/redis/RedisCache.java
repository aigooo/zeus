package com.zeus.cache.redis;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.zeus.cache.Cache;
import com.zeus.common.util.ConfigUtil;
import com.zeus.common.util.JsonUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@SuppressWarnings("deprecation")
public class RedisCache implements Cache{
	
	private static final Logger logger = Logger.getLogger(RedisCache.class);
	private JedisPool pool;
	
	public static RedisCache instance = new RedisCache();
	
	private RedisCache(){
		JedisPoolConfig cofing = new JedisPoolConfig();
		RedisConfig redisConfig = ConfigUtil.readConfig(RedisConfig.class, "init.properties","redis.properties","redis.config");
		if(redisConfig!=null){
			Integer port = redisConfig.getPort()!=null?redisConfig.getPort():6379;
			String host = StringUtils.isNotBlank(redisConfig.getHost())?redisConfig.getHost():"127.0.0.1";
			Integer maxTotal = redisConfig.getMaxTotal()!=null?redisConfig.getMaxTotal():100;
			Integer maxIdle = redisConfig.getMaxIdle()!=null?redisConfig.getMaxIdle():600;
			Integer maxWaitMillis = redisConfig.getMaxWaitMillis()!=null?redisConfig.getMaxWaitMillis():1000;
			Integer minIdle = redisConfig.getMinIdle()!=null?redisConfig.getMinIdle():600;
			Boolean testOnBorrow = redisConfig.getTestOnBorrow()!=null?redisConfig.getTestOnBorrow()!=null:true;
			Boolean testOnReturn = redisConfig.getTestOnReturn()!=null?redisConfig.getTestOnReturn()!=null:true;
			
			cofing.setMaxTotal(maxTotal);   
			cofing.setMaxIdle(maxIdle);  
			cofing.setMinIdle(minIdle);
			cofing.setMaxWaitMillis(maxWaitMillis);  
			cofing.setTestOnBorrow(testOnBorrow);   
			cofing.setTestOnReturn(testOnReturn);
			
			pool = new JedisPool(cofing, host, port);
			logger.info("created redis pool : " + redisConfig + "," + JsonUtil.toJSONString(cofing));
		}
	}
	
	public <T> T get(String key,Class<T> clazz){
		Jedis resource = pool.getResource();
		try{
			logger.debug("Redis pool active: " + pool.getNumActive() + ",idle: " + pool.getNumIdle());
			String value = resource.get(key);
			return JsonUtil.parseObject(value, clazz);
		}finally{
			pool.returnBrokenResource(resource);
		}
	}
	
	public String get(String key){
		Jedis resource = pool.getResource();
		try{
			logger.debug("Redis pool active: " + pool.getNumActive() + ",idle: " + pool.getNumIdle());
			return resource.get(key);
		}finally{
			pool.returnBrokenResource(resource);
		}
	}
	
	public int expire(String key,int timeout){
		Jedis resource = pool.getResource();
		try{
			logger.debug("Redis pool active: " + pool.getNumActive() + ",idle: " + pool.getNumIdle());
			return resource.expire(key, timeout).intValue();
		}finally{
			pool.returnBrokenResource(resource);
		}
	}
	
	public int putAndExpire(String key,Object object,int timeout){
		Jedis resource = pool.getResource();
		try{
			logger.debug("Redis pool active: " + pool.getNumActive() + ",idle: " + pool.getNumIdle());
			resource.set(key, JsonUtil.toJSONString(object));
			return resource.expire(key, timeout).intValue();
		}finally{
			pool.returnBrokenResource(resource);
		}
	}
	
	
	public <T> List<T> getList(String key,Class<T> clazz){
		Jedis resource = pool.getResource();
		try{
			logger.debug("Redis pool active: " + pool.getNumActive() + ",idle: " + pool.getNumIdle());
			return JsonUtil.parseObjects(resource.get(key),clazz);
		}finally{
			pool.returnBrokenResource(resource);
		}
	}
	
	public int put(String key,Object object){
		Jedis resource = pool.getResource();
		try{
			logger.debug("Redis pool active: " + pool.getNumActive() + ",idle: " + pool.getNumIdle());
			resource.set(key, JsonUtil.toJSONString(object));
			return 1;
		} catch(Exception e){
			logger.error("put " + key + " : " + object + "failed!",e);
			return 0;
		}finally{
			pool.returnBrokenResource(resource);
		}
	}
	
	public boolean exist(String key){
		Jedis resource = pool.getResource();
		try{
			logger.debug("Redis pool active: " + pool.getNumActive() + ",idle: " + pool.getNumIdle());
			return resource.exists(key);
		} catch(Exception e){
			return false;
		}finally{
			pool.returnBrokenResource(resource);
		}
	}
	
	public long delete(String pattern){
		Jedis resource = pool.getResource();
		try{
			logger.debug("Redis pool active: " + pool.getNumActive() + ",idle: " + pool.getNumIdle());
			return resource.keys(pattern).stream().filter(key->{
				return resource.del(key)>0;
			}).count();
		}finally{
			pool.returnBrokenResource(resource);
		}
	}
}
