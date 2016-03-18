package com.zeus.cache.status;

public class Status {
	
	/**
	 * 启动中，未开始执行
	 */
	public final static String STARTING = "0";
	
	/**
	 * 执行中
	 */
	public final static String DOING = "1";
	
	/**
	 * 执行成功
	 */
	public final static String SUCCESS = "2";
	
	/**
	 * 执行失败
	 */
	public final static String FAILED = "3";
	
	/**
	 * 等待取消中
	 */
	public final static String CANCELING = "4";
	
	/**
	 * 已取消
	 */
	public final static String CANCELED = "5";
	
	private String status;
	private String message;
	
	public String getStatus() {
		return status;
	}
	public Status setStatus(String status) {
		this.status = status;
		return this;
	}
	public String getMessage() {
		return message;
	}
	public Status setMessage(String message) {
		this.message = message;
		return this;
	}
}
