package com.zeus.common.sql;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.zeus.common.util.DateUtil;

public class SqlUtil {
	
	public static String stringsInSQL(String str) {
		String rstr = "";
		String[] items = str.split(",");
		for (String item : items) {
			rstr = rstr + "'" + item.trim() + "',";
		}
		return rstr.substring(0, rstr.length() - 1);
	}

	public static String stringInSQL(String str) {
		return "'" + str + "'";
	}

	public static String typeInSql(String type,Object obj){
		if("long,int,short,double,float,Integer,Long,Short,Double,Integer".contains(type)){
			return obj.toString();
		}else if("String".contains(type)){
			return stringInSQL(obj.toString());
		}else if(obj instanceof List<?>){
			String partSQL = "";
			for(Object one:(List<?>) obj){
				Class<?> innerType = one.getClass();
				partSQL = partSQL + typeInSql(innerType.getSimpleName(), one) + ",";
			}
			if(partSQL.length()>=0){
				partSQL = partSQL.substring(0,partSQL.length()-1);
			}
			return partSQL;
		}else if(obj instanceof Set<?>){
			String partSQL = "";
			Iterator<?> it = ((Set<?>) obj).iterator();
			while(it.hasNext()){
				Object one = it.next();
				partSQL = partSQL + typeInSql(one.getClass().getSimpleName(), one) + ",";
			}
			if(partSQL.length()>=0){
				partSQL = partSQL.substring(0,partSQL.length()-1);
			}
			return partSQL;
		}else if(obj instanceof Date){
			return dateInSQL((Date) obj);
		}else{
			return obj.toString();
		}
	}

	public static String typesInSql(String type,String obj){
		String partSQL = "";
		String[] objs = obj.split(",");
		for(String one:objs)
			partSQL = partSQL + typeInSql(type,one)+",";
		if(partSQL.length()>=0){
			partSQL = partSQL.substring(0,partSQL.length()-1);
		}
		return partSQL;
	}

	public static String dateInSQL(Date date) {
		return "'" +DateUtil.timeToString(date) +"'";
	}

}
