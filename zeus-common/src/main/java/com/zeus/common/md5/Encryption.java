package com.zeus.common.md5;

/**
 * 加密器
 * @author xiazs
 * @createTime 2014-5-15
 */
public interface Encryption {
	/**
	 * 设置加密器的密码
	 * @param key
	 * @addBy Administrator
	 * @addTime 下午3:02:01
	 */
	void setKey(String key);
	
	/**
	 * 设置加密后截取位数防止恢复
	 * @param length
	 * @return
	 * @addBy Administrator
	 * @addTime 下午3:03:22
	 */
	void setCutLength(int length);
	
	 /**
     * 为password加密
     * @param password 密码明文
     * @return 加密后的密文
     */
	 String encrypt(String password);

    /**
     * 检查密码是否匹配
     * @param password  需要检查的密码明文
     * @param encrypted 密码密文
     */
	boolean check(String password, String encrypted);

}
