package com.zeus.cache.redis;

import com.zeus.common.annotation.Config;
import com.zeus.common.util.JsonUtil;

public class RedisConfig {
	
	@Config(name="redis.host")
	private String host;
	
	@Config(name="redis.port")
	private Integer port;
	
	@Config(name="redis.maxTotal")
	private Integer maxTotal;
	
	@Config(name="redis.maxIdle")
	private Integer maxIdle;
	
	@Config(name="redis.maxWaitMillis")
	private Integer maxWaitMillis;
	
	@Config(name="redis.minIdle")
	private Integer minIdle;
	
	@Config(name="redis.testOnBorrow")
	private Boolean testOnBorrow;
	
	@Config(name="redis.testOnReturn")
	private Boolean testOnReturn;
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public Integer getMaxTotal() {
		return maxTotal;
	}
	public void setMaxTotal(Integer maxTotal) {
		this.maxTotal = maxTotal;
	}
	public Integer getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}
	public Integer getMaxWaitMillis() {
		return maxWaitMillis;
	}
	public void setMaxWaitMillis(Integer maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}
	public Integer getMinIdle() {
		return minIdle;
	}
	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}
	public Boolean getTestOnBorrow() {
		return testOnBorrow;
	}
	public void setTestOnBorrow(Boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}
	public Boolean getTestOnReturn() {
		return testOnReturn;
	}
	public void setTestOnReturn(Boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}
	@Override
	public String toString() {
		return JsonUtil.toJSONString(this);
	}
}
