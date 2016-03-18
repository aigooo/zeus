package com.zeus.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.zeus.common.annotation.Config;

public class ConfigUtil {

	/**
	 * 读取配置文件，按照configs文件的先后顺序读取配置文件
	 * @param clazz
	 * @param configs
	 * @return
	 */
	public static <T> T readConfig(Class<T> clazz, String... configs) {
		T t = null;
		for(String config:configs){
			if(config.endsWith("properties")||config.endsWith("prop")){
				t = convertFromProperties(config,clazz);
			}
			if(t!=null){
				return t;
			}
		}
		return null;
	}
	
	public static <T> Map<String,T> readConfigs(Class<T> clazz, String... configs){
		Map<String,T> map = new HashMap<>();
		for(String config:configs){
			if(config.endsWith("config")||config.endsWith("json")){
				map = convertFromJson(config,clazz);
			}
			if(map!=null){
				return map;
			}
		}
		return null;
	}
	
	/**
	 * 从json文件中读取配置信息
	 * @param fPath
	 * @param clazz
	 * @return
	 */
	public static <T> Map<String,T> convertFromJson(String fPath,Class<T> clazz){
		Map<String,T> map = new HashMap<>();
		try {
			JSONObject jsonObject = JsonUtil.parseObject(FileUtil.fileContent(fPath));
			Iterator<String> iterator = jsonObject.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				String value = jsonObject.getString(key);
				T t = JsonUtil.parseObject(value, clazz);
				map.put(key, t);
			}
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 从properties文件中读取配置信息
	 * @param fPath
	 * @param clazz
	 * @return
	 */
    public static <T> T convertFromProperties(String fPath,Class<T> clazz){
    	Properties propertie = new Properties();
    	try {
    		T t = clazz.newInstance();
    		Config classConfig = clazz.getAnnotation(Config.class);
    		String classPrefix = classConfig!=null?classConfig.name():"";
    		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fPath);
    		if(is==null){
    			return null;
    		}
			propertie.load(is);
			Enumeration<Object> em = propertie.keys();
			Field[] fields = clazz.getDeclaredFields();
			while(em.hasMoreElements()){
				String key = (String) em.nextElement();
				String value = propertie.getProperty(key);
				for(Field field:fields){
					Config config = field.getAnnotation(Config.class);
					Method method = clazz.getMethod(TypeUtil.attributeToSetter(field.getName()), field.getType());
					String type = field.getType().getName();
					String fieldConfigName = StringUtils.isNotBlank(classPrefix)?classPrefix+"."+field.getName():(config!=null?config.name():"");
					if(StringUtils.isNotBlank(fieldConfigName)&&fieldConfigName.equals(key)){
						if(type.toUpperCase().contains("INT")){
							if(StringUtils.isNotBlank(value))
								method.invoke(t, Integer.parseInt(value));
						} else if(type.toUpperCase().contains("LONG")){
							if(StringUtils.isNotBlank(value))
								method.invoke(t, Long.parseLong(value));
						} else if(type.toUpperCase().contains("DOUBLE")){
							if(StringUtils.isNotBlank(value))
								method.invoke(t, Double.parseDouble(value));
						} else if(type.toUpperCase().contains("FLOAT")){
							if(StringUtils.isNotBlank(value))
								method.invoke(t, Float.parseFloat(value));
						} else if(type.toUpperCase().contains("BOOL")){
							if(StringUtils.isNotBlank(value))
								method.invoke(t, Boolean.parseBoolean(value));
						} else{
							method.invoke(t, value);
						}
					}
				}
			}
			return t;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
    	return null;
    }
}
