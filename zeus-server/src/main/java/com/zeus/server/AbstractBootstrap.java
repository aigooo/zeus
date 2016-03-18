package com.zeus.server;

import java.util.List;

import org.apache.log4j.Logger;

import com.zeus.common.util.ClassScanner;
import com.zeus.common.util.ConfigUtil;
import com.zeus.common.util.JsonUtil;
import com.zeus.server.annotation.HTTP;
import com.zeus.server.http.HttpServerCreator;
import com.zeus.server.http.RouteInfo;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

public abstract class AbstractBootstrap{
	
	public Vertx vertx = Vertx.vertx();
	private Logger logger = Logger.getLogger(AbstractBootstrap.class);

	public void startup(){
		this.preProcesser().startup0().postProcessor();
	}
	
	private AbstractBootstrap startup0(){
		logger.info("httpserver starting... ");
		ServerConfig config = ConfigUtil.readConfig(ServerConfig.class,"init.properties","init.config","server.properties","server.config");
		logger.info("httpserver config: " + JsonUtil.toJSONString(config));
		List<RouteInfo> routeInfos = RouteInfo.findRoutes(ClassScanner.findByAnnotation(config.getRootPackage(), HTTP.class));
		initTarget(routeInfos);
		HttpServer httpServer = HttpServerCreator.create(vertx,routeInfos,config);
		httpServer.listen(config.getPort());
		logger.info("httpserver is listening at " + config.getPort());
		return this;
	}
	
	public void initTarget(List<RouteInfo> routeInfos){
		routeInfos.forEach(routeInfo->{
			try {
				routeInfo.setTarget(routeInfo.getMethod().getDeclaringClass().newInstance());
			} catch (Exception e) {
				logger.error("initialize " + routeInfo.getMethod().getClass() + " failed! Path " + routeInfo.getPath() + " disabled!",e);
			}
		});
	}
	
	public abstract AbstractBootstrap preProcesser();
	
	public abstract AbstractBootstrap postProcessor();
}
