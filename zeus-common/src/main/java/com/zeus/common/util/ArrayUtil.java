package com.zeus.common.util;

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;

public class ArrayUtil {
	
	/**
	 * 合并两个数组，剔除相同元素
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static String[] merge(String[] array1,String[] array2){
		if(array1==null||array2==null){
			return (String[]) ArrayUtils.addAll(array1, array2);
		}
		String[] all = new String[array1.length + array2.length];
		Integer length = array1.length;
		System.arraycopy(array1,0,all,0,array1.length);
		for(int j=0;j<array2.length;j++){
			Boolean add = Boolean.TRUE;
			for(int i=0;i<array1.length;i++){
				if(array2[j].equals(array1[i])){
					add = Boolean.FALSE;
					break;
				}
			}
			if(add){
				all[array1.length + j] = array2[j];
				length++;
			}
		}
		return Arrays.copyOf(all, length);
	}
	
	/**
	 * 合并两个数组，剔除相同元素
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static Object[] merge(Object[] array1,Object[] array2){
		if(array1==null||array2==null){
			return ArrayUtils.addAll(array1, array2);
		}
		Object[] all = new Object[array1.length + array2.length];
		Integer length = array1.length;
		System.arraycopy(array1,0,all,0,array1.length);
		for(int j=0;j<array2.length;j++){
			Boolean add = Boolean.TRUE;
			for(int i=0;i<array1.length;i++){
				if(array2[j].equals(array1[i])){
					add = Boolean.FALSE;
					break;
				}
			}
			if(add){
				all[array1.length + j] = array2[j];
				length++;
			}
		}
		return Arrays.copyOf(all, length);
	}
}
