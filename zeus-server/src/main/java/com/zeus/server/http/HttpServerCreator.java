package com.zeus.server.http;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.zeus.common.util.JsonUtil;
import com.zeus.common.util.PersistPropertyPreFilter;
import com.zeus.server.RespBody;
import com.zeus.server.ServerConfig;
import com.zeus.server.session.Session;
import com.zeus.server.session.SessionFactory;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class HttpServerCreator {
	
	private static Logger logger = Logger.getLogger(HttpServerCreator.class);
	
	public static HttpServer create(Vertx vertx,List<RouteInfo> routeInfos,ServerConfig config){
		HttpServer server = vertx.createHttpServer(new HttpServerOptions().setIdleTimeout(config.getIdleTimeout()));
		Router router = Router.router(vertx);
		
		router.route("/*").handler(CookieHandler.create());
		router.route("/*").handler(BodyHandler.create());
		
		/**
		 * 实际路径为config.getWebroot() + * 
		 * 配置路径为/res/ + * 
		 */
		if(StringUtils.isNotBlank(config.getWebroot())){
			router.routeWithRegex("^.*\\.html|.*\\.css|.*\\.js|.*\\.jpg|.*\\.gif|.*\\.png|.*\\.jpeg|.*\\.woff|.*\\.ttf$").handler(StaticHandler.create(config.getWebroot()).setCachingEnabled(false));
		}else{
			router.routeWithRegex("^.*\\.html|.*\\.css|.*\\.js|.*\\.jpg|.*\\.gif|.*\\.png|.*\\.jpeg|.*\\.woff|.*\\.ttf$").handler(StaticHandler.create().setCachingEnabled(false));
		}
		
		routeInfos.forEach(routeInfo->{
			logger.info("regist path: " + routeInfo.getPath() + ",consume: " + Arrays.toString(routeInfo.getConsume())
							+ ",produce: " + routeInfo.getProduce() + ",method: " + Arrays.toString(routeInfo.getHttpMethods()));
			Route route = router.route(routeInfo.getPath());
			if(routeInfo.getConsume()!=null&&routeInfo.getConsume().length>0){
				for(String consume:routeInfo.getConsume()){
					route.consumes(consume);
				}
			}
			
			route.produces(routeInfo.getProduce());
			
			if(routeInfo.getHttpMethods()!=null&&routeInfo.getHttpMethods().length>0){
				for(HttpMethod httpMethod:routeInfo.getHttpMethods()){
					route.method(httpMethod);
				}
			}
			route.blockingHandler(routingContext -> {
				try {
					long beginTime = System.currentTimeMillis();
					String sessionId,responseBody = "";
					Cookie cookie  = routingContext.getCookie(Session.SESSION_ID);
					if(cookie==null) {
						sessionId = routingContext.request().params().get(Session.SESSION_ID);
						if(StringUtils.isNotBlank(sessionId)){
							cookie = Cookie.cookie(Session.SESSION_ID, sessionId);
						}else{
							sessionId = UUID.randomUUID().toString();
							cookie = Cookie.cookie(Session.SESSION_ID, sessionId);
						}
					}
					sessionId = cookie.getValue();
					Session session = SessionFactory.createSession(config,sessionId, config.getSessionTimeout());
					if(config.isResControl()){
						session = session.loadSession();
						if(session==null){
							session = SessionFactory.createSession(config,sessionId, config.getSessionTimeout());
							session.putSession(session);
						}else{
							session.touchSession();
						}
					}
					if(config.isResControl()&&routeInfo.getNeedLogin()&&session.load("user", JSONObject.class)==null){
						responseBody = JsonUtil.toJSONString(RespBody.logout());
						logger.warn("current session don't contains user, and " + routeInfo.getPath() + " need login.");
					}else{
						routingContext.addCookie(cookie);
						Object result = routeInfo.getMethod().invoke(routeInfo.getTarget(), ParamsConstructor.convertParams(routingContext,session,routeInfo.getMethod()));
						if(routeInfo.getProduce().equalsIgnoreCase("application/json")){
							responseBody = JsonUtil.toJSONString(result,new PersistPropertyPreFilter());
						}else if(routeInfo.getProduce().equalsIgnoreCase("text/html")){
							responseBody = (String) result;
						}
					}
					routingContext.response().setStatusCode(200)
								.putHeader("Content-Length", "" + responseBody.getBytes().length)
								.putHeader("Content-Type", "application/json")
								.putHeader("Cache-Control", "no-cache")
								.putHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
								.putHeader("Access-Control-Allow-Headers", "Content-Type, X-Requested-With, X-authentication, X-client")
								.putHeader("Access-Control-Allow-Origin", "*")
				          	.write(responseBody);
					routingContext.next();
					logger.info("handle request: " + routeInfo.getPath() + " in " + (System.currentTimeMillis() - beginTime) + " ms.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		});
		server.requestHandler(router::accept);
		return server;
	}
}
