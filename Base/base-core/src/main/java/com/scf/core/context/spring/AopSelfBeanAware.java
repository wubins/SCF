/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.core.context.spring;
/**
 * aop aware
 * @author wubin
 */
public interface AopSelfBeanAware {

    public void setSelf(Object proxyBean);
}
