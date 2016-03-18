package com.zeus.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyPreFilter;

public class PersistPropertyPreFilter implements PropertyPreFilter {
    
	private final Class<?>    clazz;
    private final Set<String> includes = new HashSet<String>();
    private final Set<String> excludes = new HashSet<String>();

    public PersistPropertyPreFilter(String... properties){
        this(null, properties);
    }

    public PersistPropertyPreFilter(Class<?> clazz, String... properties){
        super();
        this.clazz = clazz;
        for (String item : properties) {
            if (item != null) {
                this.includes.add(item);
            }
        }
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Set<String> getIncludes() {
        return includes;
    }

    public Set<String> getExcludes() {
        return excludes;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean apply(JSONSerializer serializer, Object source, String name) {
        if (source == null) {
            return true;
        }
        
        try {
        	Object value;
        	if(Map.class.isAssignableFrom(source.getClass())){
        		value = ((Map)source).get(name);
        		value = JsonUtil.parseObject(JsonUtil.toJSONString(value), value.getClass());
        		((Map)source).put(name,value);
        	}else{
        		try{
	        		Field field = source.getClass().getDeclaredField(name);
	        		field.setAccessible(true);
	        		value = field.get(source);
        		}catch( NoSuchFieldException e){
        			try {
						value = source.getClass().getMethod(TypeUtil.attributeToGetter(name)).invoke(source);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
							| NoSuchMethodException | SecurityException e1) {
						return true;
					}
        		} catch (Exception e) {
					e.printStackTrace();
					return true;
				}
        	}
			if(value!=null){
				String fieldClassName = value.getClass().getName();
				if((fieldClassName.contains("org.hibernate")&&fieldClassName.contains("Persistent"))
						||fieldClassName.contains("_$$_")){
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

        if (clazz != null && !clazz.isInstance(source)) {
            return true;
        }

        if (this.excludes.contains(name)) {
            return false;
        }

        if (includes.size() == 0 || includes.contains(name)) {
            return true;
        }

        return false;
    }
    
}