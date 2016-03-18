package com.zeus.service.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.zeus.service.rpc.RpcCleint;
import com.zeus.service.rpc.RpcConfig;

public class RPCInvocationHandler implements InvocationHandler{
	
	RpcCleint rpcCleint;
	
	public static RPCInvocationHandler createHandler(RpcConfig config){
		return new RPCInvocationHandler(new RpcCleint(config));
	}
	
	public RPCInvocationHandler(RpcCleint rpcCleint) {
		this.rpcCleint = rpcCleint;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return rpcCleint.invoke(proxy.getClass().getInterfaces()[0], method, args);
	}
}
