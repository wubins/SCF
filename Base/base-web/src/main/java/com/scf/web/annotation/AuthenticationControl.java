package com.scf.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * api接口需要验证权限
 * 可修饰类或方法
 * 
 * 
 * @author  wub
 * @version  [版本号, 2015年7月1日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthenticationControl
{
    
}
