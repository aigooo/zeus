package com.zeus.common.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Properties;

import com.zeus.common.annotation.Config;

public class PropertiesUtil {

    private Properties propertie = null;

    private String filePath;
    
    private PropertiesUtil(String fPath){
    	filePath = fPath;
    }
    
    public static PropertiesUtil init(String fPath){
    	PropertiesUtil util = new PropertiesUtil(fPath);
    	util.propertie = new Properties();
    	util.filePath = fPath;
    	try {
    		InputStream is = PropertiesUtil.class.getResourceAsStream(fPath);
    		if(is==null){
    			return null;
    		}
    		util.propertie.load(is);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    	return util;
    }
    

    public PropertiesUtil reload(String fPath) {
    	this.propertie = new Properties();
    	this.filePath = fPath;
    	try {
    		this.propertie.load(PropertiesUtil.class.getResourceAsStream(fPath));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    	return this;
    }

    public String getValue(String key) {
        if (propertie.containsKey(key)) {
            String value = propertie.getProperty(key);
            return value;
        } else {
            return null;
        }
    }
    
    public static <T> T init(String fPath,Class<T> clazz){
    	Properties propertie = new Properties();
    	try {
    		T t = clazz.newInstance();
			propertie.load(PropertiesUtil.class.getResourceAsStream(fPath));
			Enumeration<Object> em = propertie.keys();
			Field[] fields = clazz.getDeclaredFields();
			while(em.hasMoreElements()){
				String key = (String) em.nextElement();
				String value = propertie.getProperty(key);
				for(Field field:fields){
					String type = "";
					Config config = field.getAnnotation(Config.class);
					Method method = clazz.getMethod(TypeUtil.attributeToSetter(field.getName()), field.getType());
					type = field.getType().getName();
					if(config==null){
						if(key.equals(field.getName())){
							if(type.toUpperCase().contains("INT")){
								method.invoke(t, Integer.parseInt(value));
							} else if(type.toUpperCase().contains("LONG")){
								method.invoke(t, Long.parseLong(value));
							} else if(type.toUpperCase().contains("DOUBLE")){
								method.invoke(t, Double.parseDouble(value));
							} else if(type.toUpperCase().contains("BOOL")){
								method.invoke(t, Boolean.parseBoolean(value));
							} else{
								method.invoke(t, value);
							}
						}
					}else{
						if(config.name().equals(key)){
							if(type.toUpperCase().contains("INT")){
								method.invoke(t, Integer.parseInt(value));
							} else if(type.toUpperCase().contains("LONG")){
								method.invoke(t, Long.parseLong(value));
							} else if(type.toUpperCase().contains("DOUBLE")){
								method.invoke(t, Double.parseDouble(value));
							} else if(type.toUpperCase().contains("BOOL")){
								method.invoke(t, Boolean.parseBoolean(value));
							} else{
								method.invoke(t, value);
							}
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

	public Properties getPropertie() {
		return propertie;
	}

	public void setPropertie(Properties propertie) {
		this.propertie = propertie;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
