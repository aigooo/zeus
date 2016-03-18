package com.zeus.persist.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FK {

    String refColumn();
    /**
     * @Desc 关联类型
     * @createTime 2015年9月20日 上午9:52:33
     */
    RefType refType() default RefType.many_to_many;
    
    
    LazyType lazy() default LazyType.No_Proxy;
    
	/**
	 * @Desc 保存到数据库时保存的格式，针对many_2_many使用，当many_to_many时，为表名称
	 * @createTime 2015年9月20日 上午9:51:18
	 */
	String store() default "";
	
	
	enum RefType{
        one_to_many("one-to-many"),
        many_to_one("many-to-one"),
        one_to_one("one-to-one"),
        many_to_many("many-to-many"),
        
        /**
         * 针对多对多的映射，用一个字段保存的情况
         */
    	many_2_many("many_2_many");

        private String value;
        RefType(String value){
            this.value = value;
        }

        public String getValue(){
            return value;
        }
    }
    
    enum LazyType{
    	False("false"),No_Proxy("no-proxy"),Proxy("proxy"),TRUE("true");
    	
    	String value;
    	
    	LazyType(String value){
    		this.value = value;
    	}

		public String getValue() {
			return value;
		}
    }
	
}
