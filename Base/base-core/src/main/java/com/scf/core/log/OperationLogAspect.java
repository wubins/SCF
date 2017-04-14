package com.scf.core.log;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.scf.core.context.SwitchContext;
import com.scf.core.context.app.cfg.initor.RootConfigInitor;
import com.scf.core.exception.AppException;
import com.scf.core.exception.BusinessException;
import com.scf.core.exception.ExCode;
import com.scf.core.meta.OperationLog;
import com.scf.core.mq.TextMessageSender;
import com.scf.utils.ExceptionsUtilies;
import com.scf.utils.JacksonObjectMapper;

/**
 * 业务操作日志AOP
 * 
 * @author wubin
 *
 */
public class OperationLogAspect implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(OperationLogAspect.class);

    private TextMessageSender messageSender;

    @Resource
    private IOperationSupport iOperationSupport;

    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        logger.info("操作执行开始");
        Map<String, Object> logMsg = new HashMap<String, Object>();
        // 方法参数
        String params = "";
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        // 调用方法
        Method method = signature.getMethod();
        logMsg.put("method", method.getName());
        String className = method.getDeclaringClass().getName();
        logMsg.put("className", className);
        // 输入参数
        Object[] args = pjp.getArgs();
        for (Object obj : args) {
            params += obj.toString();
        }
        logMsg.put("params", params);

        // 获取到这个类上的注解
        OperationLog annotation = method.getAnnotation(OperationLog.class);
        String moduleName = annotation.module();
        logMsg.put("moduleName", moduleName);
        String operationName = annotation.name();
        logMsg.put("operationName", operationName);
        String currentSystem = iOperationSupport.getCurrentSystem();
        logMsg.put("currentSystem", currentSystem);
        String currentOperator = iOperationSupport.getCurrentOperator();
        logMsg.put("currentOperator", currentOperator);
        logMsg.put("operationTime", new Date().getTime());
        // 执行前记录接口日志
        Object returnResult = null;
        // 操作执行结果状态，1成功,0失败
        Integer operationStatus = 1;
        String operationCode = "OK";
        try {
            returnResult = pjp.proceed();
        } catch (Throwable e) {
            operationStatus = 0;
            operationCode = ExCode.SYS_001 + "";
            if (e instanceof BusinessException) {
                BusinessException be = (BusinessException) e;
                operationCode = be.getValue();
                logger.warn("业务执行错误，BusinessException业务异常:异常码：{}", operationCode);
            } else if (e instanceof AppException) {
                AppException appException = (AppException) e;
                operationCode = appException.getExCode() + "";
                logger.error("业务执行错误，AppException系统异常:异常码：{}，原因:{}", operationCode,
                        ExceptionsUtilies.exceptionToString(appException));
            } else {
                logger.error("业务执行错误，出现了未知异常", e);
            }

            // 异常继续抛出
            throw e;
        } finally {
            logMsg.put("operationStatus", operationStatus);
            logMsg.put("operationCode", operationCode);
            sendLog(logMsg);
            logger.info("操作执行结束");
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
        if (SwitchContext.isOpen("business.operation.log")) {
            // 发送至MQ
            String msg = JacksonObjectMapper.toJsonString(logMsg);
            logger.info("业务操作信息：" + msg);
            messageSender.sendToQueue(msg, RootConfigInitor.PARAMS.getString("mq.queue.operation.log", ""));
        }

    }

    public TextMessageSender getMessageSender() {
        return messageSender;
    }

    public void setMessageSender(TextMessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public IOperationSupport getiOperationSupport() {
        return iOperationSupport;
    }

    public void setiOperationSupport(IOperationSupport iOperationSupport) {
        this.iOperationSupport = iOperationSupport;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (iOperationSupport == null) {
            throw new IllegalArgumentException("iOperationSupport is null!!!!!");
        }

    }

}
