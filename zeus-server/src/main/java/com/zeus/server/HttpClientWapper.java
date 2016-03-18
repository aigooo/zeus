package com.zeus.server;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.impl.HttpClientImpl;
import io.vertx.core.impl.VertxInternal;

public class HttpClientWapper extends HttpClientImpl implements HttpClient{
	
	private String host;
	private Integer port;
	private Boolean keepAlive;
	private String secretKey;
	
	public HttpClient create(Vertx vertx){
		HttpClientOptions options = new HttpClientOptions();
		options.setDefaultHost(host).setDefaultPort(port).setKeepAlive(keepAlive);
		return vertx.createHttpClient(options);
	}
	
	public HttpClientWapper(VertxInternal vertx, HttpClientOptions options) {
		super(vertx, options);
	}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public Boolean getKeepAlive() {
		return keepAlive;
	}
	public void setKeepAlive(Boolean keepAlive) {
		this.keepAlive = keepAlive;
	}
	
	public <T> RespBody<T> get(String requestURI,Class<T> clazz) {
		super.get(requestURI, response->{
			
		}).putHeader("secret", secretKey);
		return null;
	}

	@Override
	public HttpClientRequest get(String requestURI, Handler<HttpClientResponse> responseHandler) {
		return super.get(requestURI, responseHandler).putHeader("secret", secretKey);
	}

	@Override
	public HttpClient getNow(String requestURI, Handler<HttpClientResponse> responseHandler) {
		return super.getNow(requestURI, responseHandler);
	}

	@Override
	public HttpClientRequest post(String requestURI, Handler<HttpClientResponse> responseHandler) {
		return super.post(requestURI, responseHandler);
	}

	@Override
	public HttpClientRequest put(String requestURI, Handler<HttpClientResponse> responseHandler) {
		// TODO Auto-generated method stub
		return super.put(requestURI, responseHandler);
	}
	
	
}
