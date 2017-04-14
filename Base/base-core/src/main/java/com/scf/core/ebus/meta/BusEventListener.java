package com.scf.core.ebus.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author wub
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface BusEventListener {

    /**
     * 监听事件总线的名称
     *
     * @return
     */
    public String busname() default "global";
}
