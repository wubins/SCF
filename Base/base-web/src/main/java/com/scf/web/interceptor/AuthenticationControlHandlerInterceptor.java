package com.scf.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.scf.utils.JacksonObjectMapper;
import com.scf.web.webservice.IApi;
import com.scf.web.webservice.param.OutputParameter;

/**
 * 
 * api接口鉴权拦截器
 * 
 * @author wub
 * @version [版本号, 2015年7月1日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class AuthenticationControlHandlerInterceptor extends HandlerInterceptorAdapter implements HandlerInterceptor
{
    
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationControlHandlerInterceptor.class);
    
    @Override
    public final boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
        final Object handler)
        throws Exception
    {
        response.setContentType("application/json;charset=utf-8");
        
        final HandlerMethod handlerMethod = (HandlerMethod)handler;
        if (!(handlerMethod.getBean() instanceof IApi))
        {
            logger.error("接口实现类未实现IApi");
            response.getWriter().write(JacksonObjectMapper.toJsonString(OutputParameter.error()));
            return false;
        }
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception
    {
        super.afterCompletion(request, response, handler, ex);
    }
}
