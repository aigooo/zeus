package com.zeus.persist.util;

import javax.persistence.Table;

public class MapUtil {
	
	/**
	 * 根据实体类型称获取对于的表名称
	 * @param clazz
	 * @return
	 */
	public static <T> String objectToTable(Class<T> clazz){
		Table table = clazz.getAnnotation(Table.class);
		if(table!=null){
			return table.name();
		}else{
			return objectToTable(clazz.getSimpleName());
		}
	}
	
	/**
	 * 根据实体名称获取表名称
	 * @param className
	 * @return
	 */
	public static String objectToTable(String className){
		char[] classShortName = className.substring(className.lastIndexOf("\\.")+1,className.length()).toCharArray();
		StringBuilder tableName = new StringBuilder();

		for(int i=0;i<classShortName.length;i++){
			char ch = classShortName[i];
			if(i!=0 && ch>='A' && ch<='Z'){
				tableName.append("_");
			}
			tableName.append(ch);
		}
		return tableName.toString();
	}
	
	/**
	 * 根据field名称获取表名称
	 * @param name
	 * @return
	 */
	public static String fieldToColumn(String name){
		char[] fieldName = name.toCharArray();
		StringBuilder columnName = new StringBuilder();

		for(int i=0;i<fieldName.length;i++){
			char ch = fieldName[i];
			if(i!=0 && ch>='A' && ch<='Z'){
				columnName.append("_");
			}
			columnName.append(ch);
		}
		return columnName.toString();
	}
}
