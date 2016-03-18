package com.zeus.server.session;

import com.zeus.server.ServerConfig;

public class SessionFactory {
	
	public static Session createSession(ServerConfig config){
		if("redis".equalsIgnoreCase(config.getSessionHolder())){
			return new DistributedSession();
		}else{
			return new SingleSession();
		}
	}
	
	public static Session createSession(ServerConfig config,String id){
		if("redis".equalsIgnoreCase(config.getSessionHolder())){
			return new DistributedSession(id);
		}else{
			return new SingleSession(id);
		}
	}
	
	public static Session createSession(ServerConfig config,String id,long timeout){
		if("redis".equalsIgnoreCase(config.getSessionHolder())){
			return new DistributedSession(id,timeout);
		}else{
			return new SingleSession(id);
		}
	}
}
