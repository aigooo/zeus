package com.zeus.service.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class BeanProxyFactory {
	
	@SuppressWarnings("unchecked")
	public static <T> T create(Class<T> clazz,InvocationHandler handler){
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, handler);
	}

}
