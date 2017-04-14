package com.scf.core.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/** 
 * 操作日志注解，在需要记录操作日志的方法上标注这个注解
 * 
* @author wub 
 *  */  
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface OperationLog {  
	/**
	 * 模块名
	 * @return
	 */
    public   String  module() default "";
      
      
      /**
  	 * 操作名
  	 * @return
  	 */
    public  String  name() default "";
}  

