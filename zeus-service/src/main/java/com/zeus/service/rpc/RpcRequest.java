package com.zeus.service.rpc;

public class RpcRequest {
	
	private String serviceName;
	private String methodName;
	private Class<?>[] classes;
	private Object[] args;
	
	public RpcRequest(String serviceName, String methodName, Class<?>[] classes, Object[] args) {
		this.serviceName = serviceName;
		this.methodName = methodName;
		this.classes = classes;
		this.args = args;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Class<?>[] getClasses() {
		return classes;
	}
	public void setClasses(Class<?>[] classes) {
		this.classes = classes;
	}
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
}
