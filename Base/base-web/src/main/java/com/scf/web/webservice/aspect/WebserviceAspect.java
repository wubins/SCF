package com.scf.web.webservice.aspect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scf.core.context.ContextHolder;
import com.scf.core.context.SwitchContext;
import com.scf.core.context.app.cfg.initor.RootConfigInitor;
import com.scf.core.exception.AppException;
import com.scf.core.exception.BusinessException;
import com.scf.core.exception.ExCode;
import com.scf.core.mq.TextMessageSender;
import com.scf.utils.EncryptUtils;
import com.scf.utils.JacksonObjectMapper;
import com.scf.utils.WebUtilies;
import com.scf.web.annotation.InterfaceLog;
import com.scf.web.context.DefaultWebContext;
import com.scf.web.enums.ApiResponseStatus;
import com.scf.web.i18n.MessageResolver;
import com.scf.web.webservice.IApi;
import com.scf.web.webservice.param.OutputParameter;
import com.scf.web.webservice.param.extend.InputParameterBasic;

/**
 * webservice切面，接口规则校验、接口鉴权、记录访问日志、接口系统异常处理操作,鉴权和发送日志都有单独开关
 * 
 * @author wubin
 */
public class WebserviceAspect {
    private static final Logger logger = LoggerFactory.getLogger(WebserviceAspect.class);

    private TextMessageSender messageSender;

    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        logger.info("接口执行开始");
        OutputParameter<?> result = null;
        String error = "";
        Map<String, Object> logMsg = new HashMap<String, Object>();
        // 获取上下文
        HttpServletRequest request = ((DefaultWebContext) ContextHolder.getIdentityContext()).getRequest();
        // 访问者IP
        String apiIp = WebUtilies.getFirstIpAddr(request);
        logMsg.put("ip", apiIp);
        // 访问地址
        String uri = request.getRequestURI();
        logMsg.put("uri", uri);
        // 调用方法名
        String methodName = pjp.getSignature().getName();
        Method method = null;
        String inputStr = null;
        int repeatFlag = 0;
        for (Method m : pjp.getSignature().getDeclaringType().getMethods()) {
            if (StringUtils.equals(methodName, m.getName())) {
                method = m;
                logMsg.put("method", method.getDeclaringClass().getName() + "." + method.getName());
                repeatFlag++;
            }
        }
        //必须实现IApi接口
        if(IApi.class.equals(pjp.getSignature().getDeclaringType().getSuperclass()!=null) ){
                logger.error("接口类未实现IApi接口:{}",pjp.getSignature().getDeclaringType().getClass().getSimpleName());
                result = OutputParameter.error(String.valueOf(ApiResponseStatus.INTERFACE_ILLEGAL.getCode()),ApiResponseStatus.INTERFACE_ILLEGAL.getMsg());
        }
        else if (repeatFlag > 1) {//方法重复
            error += "接口执行错误，方法存在重复";
            logger.error("接口执行错误，方法存在重复，执行方法【" + logMsg.get("method") + "】");
            result = OutputParameter.error();
        } else {
            // 输入参数
            InputParameterBasic<?> input = null;
            Object[] args = pjp.getArgs();
            for (Object obj : args) {
                if (obj instanceof InputParameterBasic) {
                    input = (InputParameterBasic<?>) obj;
                    break;
                }
            }

            inputStr = input.toString();
            logMsg.put("params", StringUtils.substring(inputStr, 0, 1000));

            // IP鉴权
            if (!checkIPHasAuth(apiIp, uri))
            {
                error = "授权可访问IP范围未包含访问IP;";
                result = OutputParameter.noPermissionIp(apiIp);
            }

            // 获取到这个类上的注解
            String interfaceId = "UNKOWN";
            InterfaceLog annotation = method.getAnnotation(InterfaceLog.class);
            if (annotation == null) {
                // 记录无注解，鉴权失败，记录日志
                logger.warn("方法无InterfaceLog注解，执行方法【" + logMsg.get("method") + "】");
                error += "方法无InterfaceLog注解;";
                result = OutputParameter.interfaceIllegal(error);
            } else if (StringUtils.isEmpty(annotation.id())) {
                // 记录无无接口ID，鉴权失败，记录日志
                logger.warn("InterfaceLog注解未填写id，执行方法【" + logMsg.get("method") + "】");
                error += "InterfaceLog注解未填写id;";
                result = OutputParameter.interfaceIllegal(error);
            } else {
                interfaceId = annotation.id();
            }
            logMsg.put("interfaceId", interfaceId);
            String key = EncryptUtils.MD5Encode(interfaceId);
            if (!StringUtils.equals(key, input.getKey().getKey())) {
                // 记录调用不匹配，鉴权失败，记录日志
                logger.warn("被访问接口key与访问key不符，执行方法【" + logMsg.get("method") + "】，接口编码【" + interfaceId + "】，我方key【" + key
                        + "】,对方key【" + input.getKey().getKey() + "】");
                error += "被访问接口key与访问key不符;";

                result = OutputParameter.noPermission(error);
            }

        }
        // 如果接口校验失败则记录日志，并且中断访问
        if (StringUtils.isNotEmpty(error)) {
            logMsg.put("accessStatus", result.isSuccess());
            logMsg.put("results", result.toString());
            sendLog(logMsg);
            return result;
        }
        // 接口返回
        OutputParameter<?> returnResult = null;
        String resultStr = null;
        try {
            // 执行目标方法
            returnResult = (OutputParameter<?>) pjp.proceed();
        }
        // 异常处理
        catch (Throwable e) {
            String code = null;
            String msg = null;

            if (e instanceof BusinessException) {
                BusinessException be = (BusinessException) e;
                code = be.getValue();
                msg = MessageResolver.getMessage(request, code, be.getPlaceholders());
            } else if (e instanceof AppException) {
                AppException appException = (AppException) e;
                code = "ex." + appException.getExCode();
                msg = appException.getMessage();
                if (msg == null) {
                    msg = MessageResolver.getMessage(request, code, appException.getDetail().getPlaceholders());
                }
                logger.error("系统异常，本次接口调用的传入参数【" + inputStr + "】", e);
            } else {
                // 其他未知系统异常定义为约定默认系统异常
                code = "ex." + ExCode.SYS_001;
                msg = e.getLocalizedMessage();
                logger.error("未知异常，本次接口调用的传入参数【" + inputStr + "】", e);
            }
            logger.warn("接口执行错误，本次接口调用的传入参数【" + inputStr + "】，出现异常【" + code + " || " + msg + "】");

            returnResult = OutputParameter.error(code, msg);
            return returnResult;
        } finally {
            resultStr = StringUtils.substring(returnResult.toString(), 0, 1000);
            logMsg.put("accessStatus", returnResult.isSuccess());
            logMsg.put("results", resultStr);
            // 记录执行接口日志
            sendLog(logMsg);
            logger.info("接口执行状态{},执行结果：【{}】", returnResult.isSuccess(), resultStr);
        }
        return returnResult;
    }

    /**
     * 向MQ发送日志消息
     * 
     * @param logMsg
     * @see [类、类#方法、类#成员]
     */
    private void sendLog(Map<String, Object> logMsg) {

        if (SwitchContext.isOpen("api.interface.log")) {
            // 发送至MQ
            String msg = JacksonObjectMapper.toJsonString(logMsg);
            logger.info("接口调用信息：" + msg);
            messageSender.sendToQueue(msg, RootConfigInitor.PARAMS.getString("mq.queue.interface.log", null));
        }
    }

    /**
     * 检查该接口调用是否不能通过IP鉴权校验
     * 
     * @param apiIp 访问者IP
     * @param uri 访问接口相对路径
     * @return 鉴权结果，true：未通过鉴权、false：通过鉴权校验
     * @see [类、类#方法、类#成员]
     */
    private boolean checkIPHasAuth(String apiIp, String uri) {
        // 是否开启IP鉴权
        boolean flag = SwitchContext.isOpen("api.auth.ip");
        if (!flag) {
            logger.warn("未开启IP授权检测");
            return true;
        }

        List<String> ipList = new ArrayList<String>();
        String ipAuthPrefix = "api.auth.ip.";
        String _uri = uri.replace("/", "_");
        String ipAuthStr = null;
        // 先判断该接口是否有独立鉴权方案
        ipAuthStr = RootConfigInitor.PARAMS.getString(ipAuthPrefix + _uri, null);
        // 获取通用鉴权方案
        if (StringUtils.isEmpty(ipAuthStr)) {
            ipAuthStr = RootConfigInitor.PARAMS.getString(ipAuthPrefix + "all", "127.0.0.1");
        }
        ipList.addAll(Arrays.asList(ipAuthStr.split(",")));
        // 开始鉴权
        if (ipList.contains(apiIp)) {
            return true;
        }
        return false;
    }

    public TextMessageSender getMessageSender() {
        return messageSender;
    }

    public void setMessageSender(TextMessageSender messageSender) {
        this.messageSender = messageSender;
    }
}
