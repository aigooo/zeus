package com.zeus.server;

public class RespBody<T> {
	
	public final static String CODE_SUCCESS = "0";
	public final static String CODE_FIALED = "1";
	public final static String CODE_LOGOUT = "2";
	
	private String code;
	private String message;
	private T data;
	
	public RespBody(String code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
	public static <T> RespBody<T> logout(){
		return create(CODE_LOGOUT,"登录信息超时，请重新登录",null);
	}
	
	public static <T> RespBody<T> success(){
		return create(CODE_SUCCESS,"",null);
	}
	
	public static <T> RespBody<T> success(String message){
		return create(CODE_SUCCESS,message,null);
	}
	
	public static <T> RespBody<T> success(T data){
		return create(CODE_SUCCESS,"",data);
	}
	
	public static <T> RespBody<T> success(String message,T data){
		return create(CODE_SUCCESS,message,data);
	}
	
	public static <T> RespBody<T> fail(){
		return create(CODE_FIALED,"",null);
	}
	
	public static <T> RespBody<T> fail(String message){
		return create(CODE_FIALED,message,null);
	}
	
	public static <T> RespBody<T> fail(String message,T data){
		return create(CODE_FIALED,message,data);
	}
	
	public static <T> RespBody<T> create(String code,String message,T data){
		return new RespBody<T>(code,message,data);
	}
}
