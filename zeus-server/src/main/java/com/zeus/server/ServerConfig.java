package com.zeus.server;

import com.zeus.common.annotation.Config;

@Config(name="server")
public class ServerConfig {
	
	private int port;
	private String rootPackage;
	private String webroot;
	private int idleTimeout;
	private int sessionTimeout = 10*60;
	private String sessionHolder = "redis";
	private boolean resControl = true;
	private String allowOrigan;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getRootPackage() {
		return rootPackage;
	}

	public void setRootPackage(String rootPackage) {
		this.rootPackage = rootPackage;
	}

	public String getWebroot() {
		return webroot;
	}

	public void setWebroot(String webroot) {
		this.webroot = webroot;
	}

	public int getIdleTimeout() {
		return idleTimeout;
	}

	public void setIdleTimeout(int idleTimeout) {
		this.idleTimeout = idleTimeout;
	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public boolean isResControl() {
		return resControl;
	}

	public void setResControl(boolean resControl) {
		this.resControl = resControl;
	}

	public String getAllowOrigan() {
		return allowOrigan;
	}

	public void setAllowOrigan(String allowOrigan) {
		this.allowOrigan = allowOrigan;
	}

	public String getSessionHolder() {
		return sessionHolder;
	}

	public void setSessionHolder(String sessionHolder) {
		this.sessionHolder = sessionHolder;
	}
}
