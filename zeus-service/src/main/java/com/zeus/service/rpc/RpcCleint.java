package com.zeus.service.rpc;

import java.lang.reflect.Method;

public class RpcCleint {
	
	RpcConnection connection;
	
	public RpcCleint(RpcConfig config){
		this(new RpcConnection(config));
	}
	
	public RpcCleint(RpcConnection connection) {
		this.connection = connection;
	}

	public Object invoke(Class<?> itf,Method method,Object[] args){
		return invoke(itf.getName(),method.getName(),args,method.getReturnType());
	}
	
	public Object invoke(String interfaceName,String methodName,Object[] args,Class<?> resType){
		Class<?>[] classes = null;
		if(args!=null){
			classes = new Class<?>[args.length];
			for(int i=0;i<args.length;i++) classes[i] = args[i].getClass();
		}
		return invoke(interfaceName,methodName,classes,args,resType);
	}
	
	public Object invoke(String interfaceName,String methodName,Class<?>[] classes, Object[] args,Class<?> resType){
		return invoke(new RpcRequest(interfaceName,methodName,classes,args),resType);
	}
	
	public <T> T invoke(RpcRequest request,Class<T> resType){
		//TODO 异常处理，异常code定义
		//return connection.send(request,resType).getResponse();
		return null;
	}

	public static RpcCleint getCleit(RpcConnection connetion) {
		return new RpcCleint(connetion);
	}
}
