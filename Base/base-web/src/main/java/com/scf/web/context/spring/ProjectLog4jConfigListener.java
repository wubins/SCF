/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.web.context.spring;

import java.io.File;
import javax.servlet.ServletContextEvent;
import org.springframework.web.util.Log4jConfigListener;

/**
 * log4j监听器，web.xml中可以配置外链文件，不配置默认使用classpath下的log4j.properties
 * @author wubin
 * @date 2016年7月27日 下午4:12:03 
 * @version V1.1.0
 */
public class ProjectLog4jConfigListener extends Log4jConfigListener {
    
    private static final String LOG4J_OUT_LINK_FILE = "log4jOutlinkFile";
    
    @Override
    public void contextInitialized(ServletContextEvent event) {
        String outLink = event.getServletContext().getInitParameter(LOG4J_OUT_LINK_FILE);
        if (outLink != null) {
            event.getServletContext().log("Looked a out link log4j file: " + outLink);
            File file = new File(outLink);
            if (file.exists()) {
                ProjectLog4jWebConfigurer.initLogging(event.getServletContext(), outLink);
            } else {
                super.contextInitialized(event);
            }
        } else {
            super.contextInitialized(event);
        }
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        super.contextDestroyed(event);
    }
}
