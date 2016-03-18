package com.zeus.service;

import java.util.HashMap;
import java.util.Map;

import com.zeus.service.rpc.RpcConfig;

public class AbstractBootstrap {
	
	public static Map<String,RpcConfig> configs = new HashMap<>();
	
	public void startup(){
		this.preProcesser().startup0().postProcessor();
		
		//隐含了RpcBeanPostProcessor的执行
//		applicationContext = new AnnotationConfigApplicationContext(ARpcConfig.class,CService.class,BService.class,RpcBeanPostProcessor.class);
//		BService bService = applicationContext.getBean(BService.class);
//		bService.print();
	}
	
	private AbstractBootstrap startup0(){
		return this;
	}
	
	public AbstractBootstrap preProcesser(){
		return this;
	}
	
	public AbstractBootstrap postProcessor(){
		return this;
	}

}
