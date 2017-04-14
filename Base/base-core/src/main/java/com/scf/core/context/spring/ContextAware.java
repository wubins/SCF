/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.core.context.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 容器上下文感知器
 * @author wubin
 */
public class ContextAware implements ApplicationContextAware {

    /**
     *
     */
    private static ApplicationContext applicationContext;

    /**
     *
     * @param ac
     * @throws BeansException
     */
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        applicationContext = ac;
    }

    /**
     *
     * @param idOrName
     * @return
     */
    public static Object getBean(String idOrName) {
        Object object = applicationContext.getBean(idOrName);
        return object;
    }
}
