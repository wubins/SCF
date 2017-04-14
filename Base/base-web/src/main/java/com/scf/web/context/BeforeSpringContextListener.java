/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.web.context;

import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.scf.core.constant.CommonConstants;
import com.scf.core.context.ContextHolder;
import com.scf.core.context.app.cfg.IBootstartupInitialization;
import com.scf.core.context.app.cfg.ResourceInitor;
import com.scf.core.context.app.cfg.initor.RootConfigInitor;
import com.scf.core.context.app.cfg.module.ModuleConfigHandler;
import com.scf.core.ebus.EventBusRepository;
import com.scf.utils.ClassUtilies;

/**
 * 容器启动前置初始化
 * @author wubin
 * @date 2016年7月27日 下午4:10:16 
 * @version V1.1.0
 */
public abstract class BeforeSpringContextListener implements ServletContextListener {

    private static final Logger _logger = LoggerFactory.getLogger(BeforeSpringContextListener.class);

    /**
     *
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
            sce.getServletContext().log("BeforeSpringContextListener  contextInitialized  begin ....");
            //注册同步和异步事件
            EventBusRepository.regGlobalAsyncBus();
            EventBusRepository.regGlobalSyncBus();
            
            //初始化web上下文
            ContextHolder.initIdentiyContext(new DefaultWebContext());
            //init root Resource
            initRootResource(sce);
            
            //init resource
            initResource(sce);
            
          //init module config
            ModuleConfigHandler.loadModuleConfigs(getOverrideModuleConfigProperties());
            //init currentSystem
            setCurrentSystemDomain();
            
          //init currentSystemEnv
            setCurrentSystemEnv();
            
            sce.getServletContext().log("BeforeSpringContextListener contextInitialized end ....");
    }

    /**
     *
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    /**
     *
     * @param sce
     * @throws Exception
     */
    private void initRootResource(ServletContextEvent sce) {
        String rootInitClass = sce.getServletContext().getInitParameter(ResourceInitor.ROOT_RESOURCE_CLASS_PARAM);
        if (rootInitClass != null) {
            _logger.info("Fined project root config param '" + rootInitClass + "', initing.");
            IBootstartupInitialization initor = (IBootstartupInitialization) ClassUtilies.newInstance(rootInitClass);
            ResourceInitor.init(initor);
            _logger.info("Project root config param inited. ");
        } else {
            _logger.warn("Cann't find project root config param.");
        }
    }
    
    private void initResource(ServletContextEvent sce) {
        _logger.info("Initing other resource.");
        ResourceInitor.load();
        _logger.info("Init resource loaded.");
    }
    
    /** 
     * 参数覆盖
     */
    protected    Properties getOverrideModuleConfigProperties()
    {
        return  RootConfigInitor.PARAMS.getParams();
    }
    
    /** 
     * 设置当前系统域，可由子系统来实现,请设置赋值给CommonConstants.CURRENT_SYSTEM
     */
    
    protected abstract void setCurrentSystemDomain();
        
    
    

    /**
     * 设置当前系统的环境：test/dev/demo/product
     */
    private void setCurrentSystemEnv() {
        try {
            CommonConstants.CURRENT_ENVIRONMENT = (String) RootConfigInitor.PARAMS.get("system.env", "");
            _logger.info("system.env", CommonConstants.CURRENT_ENVIRONMENT);
            Assert.hasLength(CommonConstants.CURRENT_SYSTEM, "system.name");
        } catch (Exception e) {
            _logger.error("", e);
        }
    }

}
