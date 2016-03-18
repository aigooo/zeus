package com.zeus.persist.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO 增加DataSource配置信息，或结合spring添加数据源信息
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DataSource {
	
	DataSourceType type() default DataSourceType.ORACLE;
	
	enum DataSourceType {
		ORACLE,MYSQL,POSTGRE,DB2
	}
}
