package com.zeus.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegxUtil {

	public static boolean match(String target, String pattern) {

		if (target.contains(pattern)) {
			return true;
		}
		Pattern pat = Pattern.compile(pattern);

		Matcher mat = pat.matcher(target);

		return mat.matches();
	}
	
	public static void main(String[] args) {
		System.out.println(RegxUtil.match("/fafdsa/fdasf/fdsadfadsa.js", "^.*\\.html|.*\\.css$"));
	}
}
