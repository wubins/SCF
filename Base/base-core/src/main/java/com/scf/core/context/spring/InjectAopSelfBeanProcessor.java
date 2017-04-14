/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.core.context.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 注入代理对象 后处理器
 * @author wubin
 */
public class InjectAopSelfBeanProcessor implements BeanPostProcessor, ApplicationContextAware {

    /**
     *
     */
    private static final Logger _logger = LoggerFactory.getLogger(InjectAopSelfBeanProcessor.class);

    /**
     *
     */
    private ApplicationContext context;

    /**
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    /**
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!(bean instanceof AopSelfBeanAware)) {
            return bean;
        }
        _logger.info("Set self aop proxy object for '" + beanName + "'.");
        if (AopUtils.isAopProxy(bean)) {
            ((AopSelfBeanAware) bean).setSelf(bean);
        } else {
            ((AopSelfBeanAware) bean).setSelf(context.getBean(beanName));
        }
        return bean;
    }

    /**
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
    
}
