package com.zeus.server.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.vertx.core.http.HttpMethod;

@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE,ElementType.METHOD})
public @interface HTTP {
	
	public String path() default "";
	
	public String[] consume() default {};
	
	public String produce() default "application/json";
	
	public HttpMethod[] method() default {HttpMethod.GET,HttpMethod.POST};
	
	public int order() default 0;
	
	public boolean needLogin() default true;
}
