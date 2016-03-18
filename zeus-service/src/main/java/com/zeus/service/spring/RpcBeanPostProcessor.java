package com.zeus.service.spring;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.zeus.common.util.TypeUtil;
import com.zeus.service.AbstractBootstrap;
import com.zeus.service.annotation.API;
import com.zeus.service.annotation.Token;
import com.zeus.service.proxy.BeanProxyFactory;
import com.zeus.service.proxy.RPCInvocationHandler;
import com.zeus.service.rpc.RpcConfig;

@Component
public class RpcBeanPostProcessor implements BeanPostProcessor{
	
	private static final Logger logger = Logger.getLogger(RpcBeanPostProcessor.class);

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		Class<?> clazz = bean.getClass();
		if(RpcConfig.class.isAssignableFrom(clazz)){
			Token token = clazz.getAnnotation(Token.class);
			if(token!=null){
				AbstractBootstrap.configs.put(token.value(), (RpcConfig) bean);
				logger.info(" mapped token " + token.value() + " with " + bean);
			}else{
				logger.warn(" cann't find token for " + bean + ", discard it!");
			}
			return bean;
		}
		
		logger.info("begin dependency injection for bean " + beanName);
		Field[] fields = clazz.getDeclaredFields();
		for(Field field:fields){
			logger.debug("inject property "+field.getName() + " in " + beanName );
			Class<?> fieldClass = field.getType();
			API api = fieldClass.getAnnotation(API.class);
			Token token = fieldClass.getAnnotation(Token.class);
			if(api!=null&&token!=null){
				RpcConfig config = AbstractBootstrap.configs.get(token.value());
				if(config==null){
					logger.warn(" cann't find token for " + token.value() + "!");
				}
				Object proxy = BeanProxyFactory.create(fieldClass, RPCInvocationHandler.createHandler(config));
				try {
					field.set(bean, proxy);
				} catch (IllegalArgumentException e){
					logger.error("",e);
				} catch (IllegalAccessException e){
					logger.debug("Illegal Access for "+ beanName +"'s property "+field.getName()+",try it's setter");
					try {
						Method method = clazz.getMethod(TypeUtil.attributeToSetter(field.getName()), fieldClass);
						method.invoke(bean, proxy);
					} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
						logger.error("cann't inject property " + field.getName() + " in " + beanName ,e1);
					}
				}
			}
		}
		return bean;
	}

}
