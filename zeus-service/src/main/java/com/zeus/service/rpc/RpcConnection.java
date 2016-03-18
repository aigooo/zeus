package com.zeus.service.rpc;

public class RpcConnection {
	
	public RpcConnection(){
		
	}
	
	public RpcConnection(RpcConfig rpcConfig){
		
	}

	public <T> RpcResponse<T> send(RpcRequest request,Class<T> resType){
		return null;
	}
	
	public static RpcConnection getConnection(RpcConfig rpcConfig){
		return null;
	}
}
