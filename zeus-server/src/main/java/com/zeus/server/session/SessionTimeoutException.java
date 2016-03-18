package com.zeus.server.session;

public class SessionTimeoutException extends Exception{
	
	private static final long serialVersionUID = -5880427416460969790L;
	
	private String message;

	public String getMessage() {
		return message;
	}

	public SessionTimeoutException(String message){
		this.message = message;
	}

}
