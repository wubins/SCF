/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.web.context.spring;

import java.io.FileNotFoundException;

import javax.servlet.ServletContext;

import org.springframework.util.Log4jConfigurer;
import org.springframework.util.StringUtils;
import org.springframework.web.util.Log4jWebConfigurer;

/**
 * log4j配置加载器，具有定时刷新的功能，在web.xml中可以配置定时刷新的时间间隔
 * @author wubin
 * @date 2016年7月27日 下午4:16:38 
 * @version V1.1.0
 */
public class ProjectLog4jWebConfigurer extends Log4jWebConfigurer {

    public static void initLogging(ServletContext servletContext, String location) {

        if (location != null) {
            try {
                // Write log message to server log.
                servletContext.log("Initializing log4j from [" + location + "]");

                // Check whether refresh interval was specified.
                String intervalString = servletContext.getInitParameter(REFRESH_INTERVAL_PARAM);
                if (StringUtils.hasText(intervalString)) {
                    // Initialize with refresh interval, i.e. with log4j's watchdog thread,
                    // checking the file in the background.
                    try {
                        long refreshInterval = Long.parseLong(intervalString);
                        Log4jConfigurer.initLogging(location, refreshInterval);
                    } catch (NumberFormatException ex) {
                        throw new IllegalArgumentException("Invalid 'log4jRefreshInterval' parameter: " + ex.getMessage());
                    }
                } else {
                    // Initialize without refresh check, i.e. without log4j's watchdog thread.
                    Log4jConfigurer.initLogging(location);
                }
            } catch (FileNotFoundException ex) {
                throw new IllegalArgumentException("Invalid 'log4jConfigLocation' parameter: " + ex.getMessage());
            }
        }
    }
}
