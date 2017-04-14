/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.web.context;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.core.io.Resource;
import org.springframework.web.context.support.ServletContextResource;

import com.scf.core.context.IdentityContext;
import com.scf.utils.FileUtilies;

/**
 * web上下文 抽象类，子类可以随意包装、扩展
 * @author wubin
 * @date 2016年7月27日 下午4:47:37 
 * @version V1.1.0
 */
public abstract class WebContext implements IdentityContext
{
    
    /**
     * 
     */
    public final static String SESSION_IDENTITY = "SESSION_IDENTITY";
    
    /**
     * 
     */
    private static String WEB_ROOT_PATH = null;
    /**
     * 删除整个会话
     * 
     */
    
    public  abstract void destorySession();
    /**
     * 
     * @return
     */
    protected abstract HttpSession getSession();
    
    /**
     * 
     * @return
     */
    protected abstract HttpServletRequest getHttpServletRequest();
    
    /**
     * 
     * @return
     */
    protected abstract HttpServletResponse getHttpServletResponse();
    
    /**
     * 
     * @return
     */
    protected abstract ServletContext getServletContext();
    
    /**
     *
     * @return
     */
    public String getWebRootPath()
    {
        if (WEB_ROOT_PATH == null)
        {
            try
            {
                Resource resource = new ServletContextResource(getServletContext(), "/");
                WEB_ROOT_PATH = resource.getFile().getAbsolutePath();
                if (WEB_ROOT_PATH == null)
                {
                    WEB_ROOT_PATH = resource.getFile().getAbsoluteFile().getAbsolutePath();
                }
                WEB_ROOT_PATH = FileUtilies.fixPath(WEB_ROOT_PATH);
                WEB_ROOT_PATH += "/";
            }
            catch (NullPointerException e1)
            {
                throw new IllegalAccessError("Can not init system root path. please check.");
            }
            catch (IOException e2)
            {
                throw new IllegalAccessError("Can not init system root path. please check.");
            }
        }
        return WEB_ROOT_PATH;
    }
    
}
