package com.scf.web.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import com.scf.core.exception.AppException;
import com.scf.core.exception.ExCode;
import com.scf.core.persistence.db.dao.DaoException;
import com.scf.utils.ExceptionsUtilies;
import com.scf.utils.JacksonObjectMapper;
import com.scf.utils.UserAgentUtils;
import com.scf.utils.WebUtilies;
import com.scf.web.comp.ace.ResponseMessage;
import com.scf.web.i18n.MessageResolver;

/**
 *
 * 统一异常处理 默认优先级最高
 * 
 * @author wub
 * @version [版本号, 2015年6月23日]
 * 
 * @see org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver
 * @see AppException
 * @since [产品/模块版本]
 */
public class CommonHandlerExceptionResolver extends AbstractHandlerExceptionResolver
{
    
    private final static String CONTENTTYPE_JSON = "application/json";
    
    /** 编码 */
    private String encoding = "UTF-8";
    
    private int codeExceptionBase = ExCode.PARAM_001; // 111000
    
    private boolean sameCode = true;
    
    private int codeBindException;
    
    private int codeHttpMessageNotReadableException;
    
    private int codeMissingServletRequestPartException;
    
    private int codeMissingServletRequestParameterException;
    
    private int codeTypeMismatchException = codeExceptionBase;
    
    private int codeMethodArgumentNotValidException = codeExceptionBase;
    {
        // reCountExceptionCode
        this.reCountExceptionCode();
    }
    
    public CommonHandlerExceptionResolver()
    {
        setOrder(Ordered.HIGHEST_PRECEDENCE);
    }
    
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
        Exception ex)
    {
        try
        {
            if (ex instanceof DataAccessException)
            {
                // DataAccessException
                return handleApp( DaoException.defaultDBException(ex), request, response, handler);
            }
            else if (ex instanceof AppException)
            {
                // AppException
                return handleApp((AppException)ex, request, response, handler);
            }
            else if (ex instanceof TypeMismatchException)
            {
                // 参数类型不匹配
                return handleTypeMismatch((TypeMismatchException)ex, request, response, handler);
            }
            else if (ex instanceof MethodArgumentNotValidException)
            {
                // 参数校验失败
                return handleMethodArgumentNotValidException((MethodArgumentNotValidException)ex,
                    request,
                    response,
                    handler);
            }
            else if (ex instanceof MissingServletRequestParameterException)
            {
                // 缺少必须的参数
                return handleMissingServletRequestParameter((MissingServletRequestParameterException)ex,
                    request,
                    response,
                    handler);
            }
            else if (ex instanceof HttpMessageNotReadableException)
            {
                // 未知的请求体
                return handleHttpMessageNotReadable((HttpMessageNotReadableException)ex, request, response, handler);
            }
            else if (ex instanceof MissingServletRequestPartException)
            {
                // 缺少部分请求内容
                return handleMissingServletRequestPartException((MissingServletRequestPartException)ex,
                    request,
                    response,
                    handler);
            }
            else if (ex instanceof ServletRequestBindingException || ex instanceof BindException)
            {
                // 请求不合法
                return handleBindException((BindException)ex, request, response, handler);
            }else{
                //其他异常处理
                return handleOther(ex, request, response, handler);
            }
        }
        catch (Exception handlerException)
        {
            if (logger.isWarnEnabled())
            {
                logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
            }
        }
        // for default processing
        return null;
    }
    
    /**
     * ajax to json<br/>
     * from to ResponseMapping ERROR
     * 
     * @param result
     * @param httpStatus
     * @param ex
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws IOException
     */
    private ModelAndView handleException(ResponseMessage result, int httpStatus, Exception ex,
        HttpServletRequest request, HttpServletResponse response, Object handler)
        throws IOException
    {
        
        //移动设备或者浏览器的ajax则使用rest返回
        if(UserAgentUtils.isMobileOrTablet(request) || WebUtilies.isAjaxRequest(request) || isHandlerMethodRest(handler)){
            response.setStatus(httpStatus);
            response.setContentType(CONTENTTYPE_JSON);
            response.setCharacterEncoding(encoding);
            PrintWriter out = response.getWriter();
            out.print( JacksonObjectMapper.toJsonString(result)); // output  String
            // 记录异常日志
            if (logger.isErrorEnabled())
            {
                logger.error(new StringBuffer("通用异常处理器 ：Catch a ").append(ex.getClass().getSimpleName())
                    .append(" and write to response json errors = ")
                    .append(JacksonObjectMapper.toJsonString(result))
                    .append("\r\n==异常堆栈信息如下：\r\n========")
                    .append(ExceptionsUtilies.exceptionToString(ex))
                    .toString());
            }
            out.flush();
            return new ModelAndView();
        }else{//其他非rest请求留给剩下的其他异常处理器处理
            return null;  
        }
        
    }
    
    /**
     * 判断是否HandlerMethod方式的rest请求
     * @author wubin
     * @return
     */
    private boolean isHandlerMethodRest(Object handler){
        boolean isIt = false;
        if(HandlerMethod.class.isAssignableFrom(handler.getClass())){
            HandlerMethod hm= HandlerMethod.class.cast(handler );
            ResponseBody  mResponseBody = hm.getMethod().getAnnotation(ResponseBody.class);
            ResponseBody  cResponseBody = hm.getBean().getClass().getAnnotation(ResponseBody.class);
            RestController restController = hm.getBean().getClass().getAnnotation(RestController.class);
            if(mResponseBody != null || cResponseBody != null || restController != null){
                isIt = true;
            }
        }
        return isIt;
    }
    
   
    
    // handle area
    /**
     * 500 {ex.code, message{ex.code}}
     * 
     * @param ex
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws IOException
     */
    private ModelAndView handleApp(AppException ex, HttpServletRequest request, HttpServletResponse response,
        Object handler)
        throws IOException
    {
        // 记录内部调试异常日志
        if (logger.isErrorEnabled())
        {
            logger.error(new StringBuffer("通用异常处理器 ：系统日志调试错误信息 AppException->ExDetail->message: ").append(ex.getExMessage()));
        }
        ResponseMessage result =
            ResponseMessage.error(ex.getExCode() + "",
                MessageResolver.getMessage(request, "ex." + ex.getExCode(), ex.getDetail().getPlaceholders()));
        int httpStatus = HttpServletResponse.SC_INTERNAL_SERVER_ERROR; // 500
        return handleException(result, httpStatus, ex, request, response, handler);
    }
    
    /**
     * 处理其他异常
     * 
     * @author wubin
     * @param ex
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws IOException
     */
    private ModelAndView handleOther(Exception ex, HttpServletRequest request, HttpServletResponse response,
            Object handler)
            throws IOException
        {
            ResponseMessage result =
                ResponseMessage.error();
            int httpStatus = HttpServletResponse.SC_INTERNAL_SERVER_ERROR; // 500
            return handleException(result, httpStatus, ex, request, response, handler);
        }
    
    private ModelAndView handleMissingServletRequestPartException(MissingServletRequestPartException ex,
        HttpServletRequest request, HttpServletResponse response, Object handler)
        throws IOException
    {
        int code = sameCode ? codeExceptionBase : codeMissingServletRequestPartException;
        ResponseMessage result = ResponseMessage.error(code + "", MessageResolver.getMessage(request, "ex." + code));
        int httpStatus = HttpServletResponse.SC_BAD_REQUEST; // 400
        return handleException(result, httpStatus, ex, request, response, handler);
    }
    
    private ModelAndView handleBindException(BindException ex, HttpServletRequest request,
        HttpServletResponse response, Object handler)
        throws IOException
    {
        int code = sameCode ? codeExceptionBase : codeBindException;
        ResponseMessage result = ResponseMessage.error(code + "", MessageResolver.getMessage(request, "ex." + code));
        int httpStatus = HttpServletResponse.SC_BAD_REQUEST; // 400
        return handleException(result, httpStatus, ex, request, response, handler);
    }
    
    private ModelAndView handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
        HttpServletRequest request, HttpServletResponse response, Object handler)
        throws IOException
    {
        int code = sameCode ? codeExceptionBase : codeMethodArgumentNotValidException;
        ResponseMessage result = ResponseMessage.error(code + "", MessageResolver.getMessage(request, "ex." + code));
        int httpStatus = HttpServletResponse.SC_BAD_REQUEST; // 400
        return handleException(result, httpStatus, ex, request, response, handler);
    }
    
    private ModelAndView handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request,
        HttpServletResponse response, Object handler)
        throws IOException
    {
        int code = sameCode ? codeExceptionBase : codeHttpMessageNotReadableException;
        ResponseMessage result = ResponseMessage.error(code + "", MessageResolver.getMessage(request, "ex." + code));
        int httpStatus = HttpServletResponse.SC_BAD_REQUEST; // 400
        return handleException(result, httpStatus, ex, request, response, handler);
    }
    
    private ModelAndView handleTypeMismatch(TypeMismatchException ex, HttpServletRequest request,
        HttpServletResponse response, Object handler)
        throws IOException
    {
        int code = sameCode ? codeExceptionBase : codeTypeMismatchException;
        ResponseMessage result = ResponseMessage.error(code + "", MessageResolver.getMessage(request, "ex." + code));
        int httpStatus = HttpServletResponse.SC_BAD_REQUEST; // 400
        return handleException(result, httpStatus, ex, request, response, handler);
    }
    
    private ModelAndView handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
        HttpServletRequest request, HttpServletResponse response, Object handler)
        throws IOException
    {
        int code = sameCode ? codeExceptionBase : codeMissingServletRequestParameterException;
        ResponseMessage result = ResponseMessage.error(code + "", MessageResolver.getMessage(request, "ex." + code));
        int httpStatus = HttpServletResponse.SC_BAD_REQUEST; // 400
        return handleException(result, httpStatus, ex, request, response, handler);
    }
    
    // set property
    private void reCountExceptionCode()
    {
        this.codeBindException = codeExceptionBase + 1; // BindException result code
        this.codeHttpMessageNotReadableException = codeExceptionBase + 2; // HttpMessageNotReadableException result code
        this.codeMissingServletRequestPartException = codeExceptionBase + 3; // MissingServletRequestPartException
                                                                             // result code
        this.codeMissingServletRequestParameterException = codeExceptionBase + 4; // MissingServletRequestParameterException
                                                                                  // result code
        this.codeTypeMismatchException = codeExceptionBase + 5; // TypeMismatchException result code
        this.codeMethodArgumentNotValidException = codeExceptionBase + 6; // MethodArgumentNotValidException result code
    }
    
    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }
    
    public void setCodeExceptionBase(int codeExceptionBase)
    {
        this.codeExceptionBase = codeExceptionBase;
        
        // reCountExceptionCode
        this.reCountExceptionCode();
    }
    
    public void setSameCode(boolean sameCode)
    {
        this.sameCode = sameCode;
    }
    
    public void setCodeTypeMismatchException(int codeTypeMismatchException)
    {
        this.codeTypeMismatchException = codeTypeMismatchException;
    }
    
    public void setCodeHttpMessageNotReadableException(int codeHttpMessageNotReadableException)
    {
        this.codeHttpMessageNotReadableException = codeHttpMessageNotReadableException;
    }
    
    public void setCodeMissingServletRequestPartException(int codeMissingServletRequestPartException)
    {
        this.codeMissingServletRequestPartException = codeMissingServletRequestPartException;
    }
    
    public void setCodeMethodArgumentNotValidException(int codeMethodArgumentNotValidException)
    {
        this.codeMethodArgumentNotValidException = codeMethodArgumentNotValidException;
    }
    
    public void setCodeMissingServletRequestParameterException(int codeMissingServletRequestParameterException)
    {
        this.codeMissingServletRequestParameterException = codeMissingServletRequestParameterException;
    }
    
    public void setCodeBindException(int codeBindException)
    {
        this.codeBindException = codeBindException;
    }
    
}
