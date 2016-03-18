package com.zeus.common.math;

public class MathUtil {

	/**
	 * 获取数字列表的中位数的位置
	 * @param rang 数字列表
	 * @param total 数字列表和值
	 * @return 中位数的位置
	 */
	public static int getMid(Integer[] rang,int total) {
		int sum = 0;
		for(int i=0;i<rang.length;i++){
			sum = sum + rang[i];
			if(sum>total*0.5){
				return i;
			}
		}
		return 0;
	}
}
