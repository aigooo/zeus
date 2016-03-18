package com.zeus.server.http;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.zeus.common.util.TypeUtil;
import com.zeus.server.annotation.Param;
import com.zeus.server.session.Session;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

/**
 * 根据request组装方法参数 
 * 		request参数格式要求
 * 			instance.attribute   --->instance参数，值为instance内attribute属性值
 * 			attribute            --->attribute参数
 * 			instance.attribute.attribute  --->instance参数，值为instance内attribute内attribute属性值
 * 			instance.attribute.@1     --->针对attribute是Collections的情况
 * 			instance.attribute.@1.attribute  --->针对attribute是Collections的情况
 * 			instance.attribute.@1.key   --->针对attribute是Map的情况
 * 			instance.attribute.@1.value   --->针对attribute是Map的情况
 * 			instance.attribute.@1.value.attribute   --->针对attribute是Map value为对象的的情况
 * 			instance.@1.attribute    --->针对对象类型数组
 * 			instance.@1
 * 			attribute...    --->针对普通类型的数组
 * @author XIAZS
 */
public class ParamsConstructor {
	
	private static Logger logger = Logger.getLogger(ParamsConstructor.class);

	public static Object[] convertParams(RoutingContext routingContext,Session session,Method method){
		Object[] paramsValues = new Object[method.getParameterCount()];
		Parameter[] parameters = method.getParameters();
		Map<String,Map<String,String>> params = ParamsGrouper.group(routingContext.request().formAttributes().addAll(routingContext.request().params()));
		for(int i=0;i<parameters.length;i++){
			Parameter parameter = parameters[i];
			String name = TypeUtil.toInstanceName(parameter.getType().getSimpleName());
			Param param = parameter.getAnnotation(Param.class);
			if(param!=null){
				name = param.name();
			}
			Object instance;
			if(Collection.class.isAssignableFrom(parameter.getType())){  //集合类型参数
			}else if(parameter.getType().isArray()){   //数组类型参数
			}else if(Map.class.isAssignableFrom(parameter.getType())){   //键值类参数
			}else if(HttpServerRequest.class.isAssignableFrom(parameter.getType())){   //HttpServerRequest
				paramsValues[i] = routingContext.request();
			}else if(HttpServerResponse.class.isAssignableFrom(parameter.getType())){  //HttpServerResponse
				paramsValues[i] = routingContext.response();
			}else if(RoutingContext.class.isAssignableFrom(parameter.getType())){  //RoutingContext
				paramsValues[i] = routingContext;
			}else if(Session.class.isAssignableFrom(parameter.getType())){  //HttpServerSession
				paramsValues[i] = session;
			}else{//普通类型参数
				try {
					if(parameter.getType().isPrimitive()||String.class.isAssignableFrom(parameter.getType())
							||Number.class.isAssignableFrom(parameter.getType())||Date.class.isAssignableFrom(parameter.getType())
							||Boolean.class.isAssignableFrom(parameter.getType())){
						paramsValues[i] = params.get("")==null?null:TypeUtil.convertToType(params.get("").get(name), parameter.getType());
					}else{
						instance = parameter.getType().newInstance();
						setValues4Object(instance,name + "." ,params);
						paramsValues[i] = instance;
					}
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return paramsValues;
	}
	
	private static void setValues4Object(Object obj,String prefix,Map<String,Map<String,String>> params){
		Field[] fields = obj.getClass().getDeclaredFields();
		for(Field field:fields){
			try {
				if(field.getType().isPrimitive()||String.class.isAssignableFrom(field.getType())||Number.class.isAssignableFrom(field.getType())
						||Date.class.isAssignableFrom(field.getType())||Boolean.class.isAssignableFrom(field.getType())){
					obj.getClass().getMethod(TypeUtil.attributeToSetter(field.getName()), field.getType())
						.invoke(obj, TypeUtil.convertToType(params.get(prefix).get(prefix+field.getName()), field.getType()));
				}else if(Collection.class.isAssignableFrom(field.getType())){
					setValue4Collection(newInstance4Attribute(obj,field.getName()),field.getGenericType().getClass(),prefix + field.getName(),params);
				}else if(Map.class.isAssignableFrom(field.getType())){
					setValue4Map(newInstance4Attribute(obj,field.getName()),field.getClass(),prefix + field.getName(),params);
				}else if(field.getType().isArray()){
					setValue4Array(newInstance4Attribute(obj,field.getName()),field.getClass(),prefix + field.getName(),params);
				}else{
					if(params.get(prefix + field.getName()+".")!=null){
						Object instance = newInstance4Attribute(obj,field.getName());
						setValues4Object(instance,prefix + field.getName()+".",params);
						obj.getClass().getMethod(TypeUtil.attributeToSetter(field.getName()), field.getType()).invoke(obj, instance);
					}
				}
			} catch (Exception e) {
				logger.debug("set value for " + obj.getClass() + " field " + field.getName() + " failed!");
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes"})
	private static void setValue4Collection(Collection obj,Class<?> internalType,String prefix,Map<String,Map<String,String>> params) throws Exception{
	}
	
	@SuppressWarnings({ "rawtypes"})
	private static void setValue4Map(Map obj,Class<?> type,String prefix,Map<String,Map<String,String>> params){
	}
	
	private static void setValue4Array(Object[] obj,Class<?> internalType,String prefix,Map<String,Map<String,String>> params){
		
	}
	
	public static void setAttribute4Object(Object obj,String attrName,String prefix,Map<String,Map<String,String>> params){
		
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T newInstance4Attribute(Object target,String attrName) throws Exception{
		Field field = target.getClass().getDeclaredField(attrName);
		return (T)field.getType().newInstance();
	}
}
