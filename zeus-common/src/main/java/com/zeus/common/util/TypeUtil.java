package com.zeus.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TypeUtil {
	
	public static int DOUBLE_BYTE = 4;
	
	/**
	 * 将字符串转化为基本类型
	 * @param str 被转化的字符串
	 * @param clazz 基本类型
	 * @return
	 * @addBy Administrator
	 * @addTime 上午9:37:54
	 */
	public static Object convertToType(String str,Class<?> clazz){
		if(str!=null){
			try{
				if(clazz.getName().equals("java.lang.Integer")||clazz.getName().equals("int")){
					return Integer.parseInt(str.trim());
				}
				if(clazz.getName().equals("java.lang.String")){
					return str.trim();
				}
				if(clazz.getName().equals("java.lang.Double")||clazz.getName().equals("double")){
					return Double.parseDouble(str.trim());
				}
				if(clazz.getName().equals("java.lang.Long")||clazz.getName().equals("long")){
					return Long.parseLong(str.trim());
				}
				if(clazz.getName().equals("java.lang.Boolean")||clazz.getName().equals("bool")){
					if("null".equalsIgnoreCase(str)) return null;
					boolean result = str.equalsIgnoreCase("TRUE")||str.equalsIgnoreCase("1")||str.equalsIgnoreCase("YES");
					result = !(str.equalsIgnoreCase("FALSE")||str.equalsIgnoreCase("0")||str.equalsIgnoreCase("NO"));
					return result;
				}
				if(clazz.getName().equals("java.lang.Float")||clazz.getName().equals("float")){
					return Float.parseFloat(str.trim());
				}
				if(clazz.getName().equals("java.util.Date")){
					return DateUtil.stringToDate((str.trim()));
				}
			}	catch(Exception e){
				return null;
			}
		}
		return null;
	}

	/**
	 * 将页面提价的字符数组转化为对应的对象列表
	 * @param listString 字符数组串
	 * @param clazz  对象类型
	 * @param proertyName 字符串值对应对象的属性名称
	 * @param tclass  字符串值对应对象的属性类型
	 * @return
	 * @addBy Administrator
	 * @addTime 上午9:40:01
	 */
	public static <T,E> List<T> covertListStringToList(String listString,Class<T> clazz,String proertyName,Class<E> tclass){
		List<T> list = new ArrayList<>();
		String[] strs = listString.split(",");
		for(String str:strs){
			try {
				T t = clazz.getConstructor().newInstance();
				Method method = clazz.getMethod(TypeUtil.attributeToSetter(proertyName), tclass);
				method.invoke(t, convertToType(str,tclass));
				list.add(t);
			} catch (NoSuchMethodException | InstantiationException | SecurityException |
					IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	/**
	 * @Desc 将字符串转化为数字类型
	 * @author xiazs
	 * @createTime 2015年9月21日 下午3:26:13
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> covertListStringToList(String listString,Class<T> clazz){
		List<T> list = new ArrayList<>();
		String[] strs = listString.split(",");
		for(String str:strs){
			list.add((T) convertToType(str,clazz));
		}
		return list;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List arrayToList(Object[] hyCycles) {
		List list = new ArrayList();
		Collections.addAll(list, hyCycles);
		return list;
	}

	public static String doubleToString(double d) {
		int length;
		if (d < 0.0) {
			length = DOUBLE_BYTE;
		} else {
			length = DOUBLE_BYTE - 1;
		}
		double db = 1.0;
		for (int i = 1; i < length; i++) {
			db = db * 10.0;
		}
		double nd = (double) Math.round(d * db) / db;
		String str = "" + nd;
		if (str.length() < length + 1) {
			for (int i = 0; i < length + 1 - str.length(); i++) {
				str = str + "0";
			}
		} else {
			str = str.substring(0, length + 1);
		}
		return str;
	}

	public static String doubleToString100(double d) {
		return TypeUtil.doubleToString(d * 100.0) + "%";
	}
	
	public static String arrayToString(String[] strs, String separator) {
		String result = "";
		for (String str : strs) {
			result = result + str + separator;
		}
		return result.substring(0, result.length() - 1);
	}


	public static String attributeToSelection(String attr){
		return attr + "Selections";
	}

	public static String attributeToUp(String attr){
		return "up" + ("" + attr.charAt(0)).toUpperCase()
				+ attr.substring(1);
	}

	public static String attributeToDown(String attr){
		return "dn" + ("" + attr.charAt(0)).toUpperCase()
				+ attr.substring(1);
	}

	public static String getterToAttribute(String getter) {
		String attribute = getter.replace("get", "");
		attribute = ("" + attribute.charAt(0)).toLowerCase()
				+ attribute.substring(1);
		return attribute;
	}

	public static String attributeToGetter(String attr) {
		if (attr.length() < 2) {
			return "get" + (attr.toUpperCase());
		} else {
			String getter;

			if (("" + attr.charAt(1)).toUpperCase().equals(
					("" + attr.charAt(1)))) {
				getter = "get" + attr;
			} else {
				getter = ("get" + ("" + attr.charAt(0)).toUpperCase())
						+ attr.substring(1);
			}
			return getter;
		}
	}
	public static String attributeToSetter(String attr) {
		if (attr.length() < 2) {
			return "set" + (attr.toUpperCase());
		} else {
			String setter;

			if (("" + attr.charAt(1)).toUpperCase().equals(
					("" + attr.charAt(1)))) {
				setter = "set" + attr;
			} else {
				setter = ("set" + ("" + attr.charAt(0)).toUpperCase())
						+ attr.substring(1);
			}
			return setter;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map listGroupToMap(List list,String key){
		Map map = new HashMap();
		String[] attrs = key.split("\\.");
		String[] methodNames = new String[attrs.length];
		for (int i = 0; i < attrs.length; i++) {
			methodNames[i] = attributeToGetter(attrs[i]);
		}
		for (Object aList : list) {
			try {
				Object obj = aList;
				Class clazz = aList.getClass();
				for (int j = 0; j < attrs.length; j++) {
					String className = clazz.getName();

					clazz = clazz.getMethod(methodNames[j]).invoke(obj)
							.getClass();

					Class clazzTemp = Thread.currentThread()
							.getContextClassLoader().loadClass(className);
					obj = clazzTemp.getMethod(methodNames[j]).invoke(obj);
				}
				if(map.containsKey(obj)){
					List groupList = (List) map.get(obj);
					groupList.add(aList);
					map.put(obj,groupList);
				}else{
					List groupList = new ArrayList<>();
					groupList.add(aList);
					map.put(obj,groupList);
				}
			} catch (IllegalArgumentException|SecurityException|IllegalAccessException|InvocationTargetException
					|NoSuchMethodException|ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map listToMap(List list, String key) {
		Map map = new HashMap();
		String[] attrs = key.split("\\.");
		String[] methodNames = new String[attrs.length];
		for (int i = 0; i < attrs.length; i++) {
			methodNames[i] = attributeToGetter(attrs[i]);
		}

		for (Object aList : list) {
			try {
				Object obj = aList;
				Class clazz = aList.getClass();
				for (int j = 0; j < attrs.length; j++) {
					String className = clazz.getName();
					clazz = clazz.getMethod(methodNames[j]).invoke(obj)
							.getClass();

					Class clazzTemp = Thread.currentThread()
							.getContextClassLoader().loadClass(className);
					obj = clazzTemp.getMethod(methodNames[j]).invoke(obj);
				}
				map.put(obj, aList);
			} catch (IllegalArgumentException|SecurityException|IllegalAccessException|InvocationTargetException
					|NoSuchMethodException|ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		return map;
	}

	/**
	 * 清理Object
	 * @param obj
	 * @return
	 */
	public static Object trim(Object obj) {
		if (obj == null) {
			return null;
		}
		try {
			@SuppressWarnings("unchecked")
			Class<Object> clazz = (Class<Object>) obj.getClass();
			Method[] methods = clazz.getMethods();
			for (Method m : methods) {
				if (m.getName().startsWith("get")
						&& m.getReturnType().getName()
								.equals("java.lang.String")) {
					String mName = "set"
							+ m.getName().substring(3, m.getName().length());

					Method setter = clazz.getMethod(mName, String.class);

					String attribute = (String) m.invoke(obj);
					if (attribute != null) {
						if (attribute.indexOf("null") != -1) {
							setter.invoke(obj, (Object) null);
						} else {
							if (attribute.contains(",")) {
								String[] values = attribute.split(",");
								for (int i = 0; i < values.length; i++) {
									values[i] = values[i].trim();
								}
								attribute = arrayToString(values, ",");
							}
							setter.invoke(obj, attribute.trim());
						}
					}
				}
			}

			return obj;
		} catch (SecurityException|NoSuchMethodException|IllegalArgumentException
				|IllegalAccessException|InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String toInstanceName(String simpleName) {
		return (""+simpleName.charAt(0)).toLowerCase() + simpleName.substring(1);
	}

}
