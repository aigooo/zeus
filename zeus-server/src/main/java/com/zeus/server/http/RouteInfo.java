package com.zeus.server.http;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.zeus.common.util.ArrayUtil;
import com.zeus.server.annotation.HTTP;

import io.vertx.core.http.HttpMethod;

public class RouteInfo {

	private String path;
	private String[] consume;
	private String produce;
	private HttpMethod[] httpMethods;
	private Method method;
	private Object target;
	private int order;
	private Boolean needLogin;

	public static List<RouteInfo> findRoutes(List<Class<?>> classes) {
		final List<RouteInfo> routeInfos = new ArrayList<>();
		classes.forEach(clazz -> {
			List<RouteInfo> routeInfo = findRoute(clazz);
			if (routeInfo != null&& !routeInfo.isEmpty())
				routeInfos.addAll(routeInfo);
		});
		return routeInfos;
	}

	public static List<RouteInfo> findRoute(Class<?> clazz) {
		List<RouteInfo> routeInfos = new ArrayList<>();
		HTTP classHttp = clazz.getAnnotation(HTTP.class);
		RouteInfo classRouteInfo = new RouteInfo();     //class级别的RouteInfo
		Boolean allOpen = Boolean.FALSE;
		if (classHttp != null) {
			classRouteInfo.path = classHttp.path()!=null?classHttp.path():"";
			classRouteInfo.consume = classHttp.consume();
			classRouteInfo.produce = classHttp.produce();
			classRouteInfo.httpMethods = classHttp.method();
			classRouteInfo.needLogin = classHttp.needLogin();
			allOpen = Boolean.TRUE;
		}
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if(method.getName().contains("lambda$")){
				continue;
			}
			HTTP methodHttp = method.getAnnotation(HTTP.class);
			RouteInfo routeInfo = new RouteInfo();
			if (methodHttp != null) {
				routeInfo.path = (classRouteInfo.path!=null?classRouteInfo.path:"") + methodHttp.path();
				routeInfo.consume = ArrayUtil.merge(classRouteInfo.consume, methodHttp.consume());
				routeInfo.produce = StringUtils.isNotBlank(methodHttp.produce())?methodHttp.produce():classRouteInfo.produce;
				routeInfo.httpMethods = methodHttp.method()!=null&&methodHttp.method().length>0?methodHttp.method():classRouteInfo.httpMethods;
				routeInfo.needLogin = classRouteInfo.needLogin&&methodHttp.needLogin();
				routeInfo.order = methodHttp.order();
				routeInfo.method = method;
			} else if (allOpen) {
				routeInfo.method = method;
			}
			routeInfos.add(routeInfo);
		}
		return routeInfos;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String[] getConsume() {
		return consume;
	}

	public void setConsume(String[] consume) {
		this.consume = consume;
	}

	public String getProduce() {
		return produce;
	}

	public void setProduce(String produce) {
		this.produce = produce;
	}

	public HttpMethod[] getHttpMethods() {
		return httpMethods;
	}

	public void setHttpMethods(HttpMethod[] httpMethods) {
		this.httpMethods = httpMethods;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Boolean getNeedLogin() {
		return needLogin;
	}

	public void setNeedLogin(Boolean needLogin) {
		this.needLogin = needLogin;
	}
}
