package com.zeus.server.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.annotation.JSONField;
import com.zeus.cache.memory.MemoryCache;
import com.zeus.common.util.JsonUtil;

public class SingleSession implements Session {
	
	private String id;
	private long timeout;
	private Map<String,Object> dataMap = new HashMap<>();
	
	@Override
	public Session loadSession() {
		Session session = MemoryCache.instance.get(id, SingleSession.class);
		if(session==null) {
			return null;
		}
		else return session;
	}
	
	public SingleSession() {
	}

	public SingleSession(String id, long timeout) {
		this.id = id;
		this.timeout = timeout;
	}
	
	public SingleSession(String id) {
		super();
		this.id = id;
	}

	public void removeSession() {
		MemoryCache.instance.delete(id);
	}

	@Override
	public boolean exist() {
		return MemoryCache.instance.exist(id);
	}

	@Override
	public String id() {
		if(id==null) return UUID.randomUUID().toString();
		else return id;
	}

	@Override
	public Session putSession(Session session) {
		MemoryCache.instance.put(id,session);
		return session;
	}

	@Override
	@JSONField(serialize = false)
	public Object load(String key,Class<?> clazz) throws SessionTimeoutException{
		Session session = loadSession();
		if(session==null){
			throw new SessionTimeoutException("session is timeout!");
		}
		session.touchSession();
		return JsonUtil.parseObject(JsonUtil.toJSONString(session.data().get(key)), clazz);
	}
	
	@Override
	public Session put(String key, Object obj) throws SessionTimeoutException{
		Session session = loadSession();
		if(session==null){
			throw new SessionTimeoutException("session is timeout!");
		}
		session.data().put(key,obj);
		putSession(session);
		return session;
	}

	@Override
	public Session remove(String key) throws SessionTimeoutException {
		Session session = loadSession();
		if(session==null){
			throw new SessionTimeoutException("session is timeout!");
		}
		session.data().remove(key);
		putSession(session);
		return session;
	}

	@Override
	public Map<String, Object> data() {
		return dataMap;
	}

	@Override
	public long lastAccessed() {
		return 0;
	}

	@Override
	public void destroy() {
		removeSession();
	}

	@Override
	public long timeout() {
		return timeout;
	}

	@Override
	public void setAccessed() {

	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	@Override
	public Session touchSession() {
		putSession(loadSession());
		return this;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public long getTimeout() {
		return timeout;
	}
}
