package com.scf.web.aspect;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scf.core.context.app.cfg.initor.RootConfigInitor;
import com.scf.utils.ClassUtilies;
import com.scf.web.annotation.CheckSpecialChar;
import com.scf.web.comp.ace.ResponseMessage;
import com.scf.web.comp.ace.datatable.DataTableParam;
import com.scf.web.enums.ApiResponseStatus;
import com.scf.web.webservice.param.OutputParameter;
import com.scf.web.webservice.param.extend.InputParameterBasic;

/**
 * action 切面，做一些特殊字符处理之类
 * @author wubin
 */
public class ActionAspect
{
    private static final Logger logger = LoggerFactory.getLogger(ActionAspect.class);
    
    public Object doAround(ProceedingJoinPoint pjp, CheckSpecialChar check)
        throws Throwable
    {
        
        boolean enableFlag = RootConfigInitor.PARAMS.getBoolean("scf.web.checkSpecialChar.enable", false);
        if (!enableFlag)
        {
            logger.info("系统未开启信息消毒检查");
            return pjp.proceed();
        }
        logger.info("提交信息检查开始");
        int[] cols = check.args();
        Object[] args = pjp.getArgs();
        boolean flag = false;
        boolean isNotBus = false;
        for (int i : cols)
        {
            Object obj = args[i];
            if (obj == null)
            {
                continue;
            }
            else if (isSpecialClassType(obj))
            {
                // 特殊类型不处理
                continue;
            }
            else if (obj instanceof String)
            {
                // 字符串
                flag = checkStr(obj.toString());
                if (flag)
                {
                    break;
                }
            }
            else if (ClassUtilies.isBasicClassType(obj.getClass()))
            {
                // 基础类型不处理
                continue;
            }
            else if (obj.getClass().isArray())
            {
                // 数组
                flag = checkStr(obj.toString());
                if (flag)
                {
                    break;
                }
            }
            else if (obj instanceof InputParameterBasic)
            {
                isNotBus = true;
                flag = checkStr(obj.toString());
                if (flag)
                {
                    break;
                }
            }
            else
            {
                //把obj的getXxx属性设置进map中
                Map<String, String> map = BeanUtils.describe(obj);
                for (Entry<String, String> entry : map.entrySet())
                {
                    flag = checkStr(entry.getValue());
                    if (flag)
                    {
                        break;
                    }
                }
            }
        }
        
        logger.info("提交信息检查结束");
        if (!flag)
        {
            return pjp.proceed();
        }
        
        String errorMsg = "传入参数包含非法字符“<”及“>”";
        // 判断是否接口
        if (isNotBus)
        {
            return OutputParameter.error(ApiResponseStatus.PARAM_ILLEGAL.getCode() + "", errorMsg);
        }
        return ResponseMessage.error(errorMsg,errorMsg);
        
    }
    
    private boolean isSpecialClassType(Object obj)
    {
        if (obj instanceof HttpServletRequest || obj instanceof HttpServletResponse || obj instanceof HttpSession
            || obj instanceof DataTableParam)
        {
            return true;
        }
        return false;
    }
    
    private boolean checkStr(String param)
    {
        if (StringUtils.indexOf(param, "<") >= 0 || StringUtils.indexOf(param, ">") >= 0)
        {
            return true;
        }
        return false;
    }
}
