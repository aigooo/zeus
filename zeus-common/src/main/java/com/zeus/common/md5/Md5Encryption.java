package com.zeus.common.md5;

import java.security.MessageDigest;

/**
 * 基于MD5算法加密器
 */
public class Md5Encryption implements Encryption {
	
	private String key;
	private int length;

	@Override
	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public void setCutLength(int length) {
		this.length = length;
	}

	@Override
	public String encrypt(String password) {
		char[] echar = new char[]{'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X'
				,'Y','Z','0','1','2','3','4','5','6','7','8','9'};
		try {
			byte[] passwordBytes = password.getBytes("UTF-8");
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(passwordBytes);
			byte[] passwordMd5 = messageDigest.digest();
			
			byte[] keyBytes = key.getBytes("UTF-8");
			messageDigest.update(keyBytes);
			byte[] keyMd5 = messageDigest.digest();
			
			int i = passwordMd5.length>keyMd5.length?keyMd5.length:passwordMd5.length;
			
			char[] encryptBytes = new char[i];
			for(int j=0;j<i;j++){
				encryptBytes[j] =echar[Math.abs((keyMd5[j]>>3)*(passwordMd5[j]>>2))%36];
			}
			String encryptString = new String(encryptBytes);
			return encryptString.length()>length?encryptString.substring(0,length):encryptString;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean check(String password, String encrypted) {
		String encryptString = encrypt(password);
		return encrypted.equals(encryptString);
	}

	public void setLength(int length) {
		this.length = length;
	}
	
	public static void main(String[] args) {
		Md5Encryption md5Encryption = new Md5Encryption();
		md5Encryption.setKey("ibookstar#res");
		md5Encryption.setLength(99);
		System.out.println(md5Encryption.encrypt("123456"));
	}
}
