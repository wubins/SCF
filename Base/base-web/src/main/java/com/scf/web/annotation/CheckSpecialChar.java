package com.scf.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求入参校验
 * 
 * @author wub
 * @version [版本号, 2015年10月27日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckSpecialChar
{
    /**
     * 需过滤参数位置，已0开始，默认值为{0}
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    int[] args() default {0};
}
