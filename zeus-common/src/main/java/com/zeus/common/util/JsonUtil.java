package com.zeus.common.util;

import java.nio.ByteBuffer;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtil {
	
	public static String toJSONString(Object object){
		if(object==null){
			return "";
		}
		return JSON.toJSONString(object,new PersistPropertyPreFilter(),SerializerFeature.DisableCircularReferenceDetect);
	}
	
	public static String toJSONString(Object object,PropertyPreFilter filter){
		if(object==null){
			return "";
		}
		return JSON.toJSONString(object,filter,SerializerFeature.DisableCircularReferenceDetect);
	}
	
	public static <T> T parseObject(String jsonString,Class<T> clazz){
		if(!StringUtils.isNotBlank(jsonString)){
			return null;
		}
		return JSON.parseObject(jsonString,clazz);
	}
	
	public static <T> List<T> parseObjects(String jsonString,Class<T> clazz){
		if(!StringUtils.isNotBlank(jsonString)){
			return null;
		}
		return JSON.parseArray(jsonString,clazz);
	}
	
	public static JSONObject parseObject(String jsonString){
		return JSON.parseObject(jsonString);
	}
	
	public static ByteBuffer toByteBuffer(Object object){
		return ByteBuffer.wrap(JSON.toJSONBytes(object,SerializerFeature.NotWriteRootClassName));
	}
}
