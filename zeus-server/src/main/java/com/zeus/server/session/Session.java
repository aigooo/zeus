package com.zeus.server.session;

import java.util.Map;

public interface Session {
	
	public String SESSION_ID = "zsessionId";
	
	void removeSession();
	
	Session touchSession();
	
	Session loadSession();
	
	Session putSession(Session session);
	
	boolean exist();

	String id();

	public Session put(String key, Object obj) throws SessionTimeoutException;

	Object load(String key,Class<?> clazz) throws SessionTimeoutException;

	Session remove(String key) throws SessionTimeoutException;

	Map<String, Object> data();

	long lastAccessed();

	void destroy();

	long timeout();

	void setAccessed();
}