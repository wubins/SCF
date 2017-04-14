package com.scf.web.webservice.param;

import java.io.Serializable;

import com.scf.utils.JacksonObjectMapper;
import com.scf.web.enums.ApiResponseStatus;

/**
 * 系统间webservice接口输出参数
 * 
 * @author wubin
 */

public class OutputParameter<T> implements Serializable
{
    private static final long serialVersionUID = 9114108592248628653L;
    
    /**
     * 默认操作成功
     */
    private boolean success ;
    
    /**
     * 返回码
     */
    private String code;
    
    /**
     * 返回提示信息
     */
    private String msg;
    
    /**
     * 返回业务对象
     */
    private T biz;
    
    public OutputParameter(){
        super();
    }
    
    @SuppressWarnings("unused")
    private OutputParameter(boolean success, String code, String message,T biz)
    {
        this.success = success;
        this.code = code;
        this.msg = message;
        this.biz = biz;
    }
    
    private OutputParameter(boolean success, String message, T biz )
    {
        this.success = success;
        this.msg = message;
        this.biz = biz;
    }
    
    private OutputParameter(boolean success,T biz)
    {
        this.success = success;
        this.biz = biz;
    }
    
    private OutputParameter(boolean success, String code, String message)
    {
        this.success = success;
        this.code = code;
        this.msg = message;
    }
    
    private OutputParameter(boolean success, String message)
    {
        this.success = success;
        this.msg = message;
    }
    
    private OutputParameter(boolean success)
    {
        this.success = success;
    }
    
    
    public static <T> OutputParameter<T> success(T biz)
    {
        return new OutputParameter<T>(true, biz);
    }
    //返回类型为string 的业务参数，为了区分开 success(String message)方法
    public static  <T> OutputParameter<T> successStringBiz(T biz)
    {
        return new OutputParameter<T>(true, biz);
    } 
    
    public static <T> OutputParameter<T> success(String message, T biz)
    {
        return new OutputParameter<T>(true, message, biz);
    }
    
    public static  <T> OutputParameter<T> success(String message)
    {
        return new OutputParameter<T>(true, message);
    }
    /**
     * 接口返回成功
     * @author wubin
     * @return
     */
    public static <T> OutputParameter<T> success()
    {
        return new OutputParameter<T>(true);
    }
    
    /**
     * 接口非受信访问
     * @author wubin
     * @return
     */
    public static <T> OutputParameter<T> noPermission(String reason)
    {
         return new OutputParameter<T>(false, ApiResponseStatus.AUTH_FAIL.getCode()+"",ApiResponseStatus.AUTH_FAIL.getMsg()+": "+reason);
    }
    /**
     * 接口非受信ip
     * @author wubin
     * @return
     */
    public static <T> OutputParameter<T> noPermissionIp(String sourceIp)
    {
        return new OutputParameter<T>(false, ApiResponseStatus.IP_AUTH_FAIL.getCode()+"",ApiResponseStatus.IP_AUTH_FAIL.getMsg()+": "+sourceIp);
    }
    
    
    /**
     * 接口不合法
     * @author wubin
     * @return
     */
    
    public static <T> OutputParameter<T> interfaceIllegal(String reason)
    {
        return new OutputParameter<T>(false, ApiResponseStatus.INTERFACE_ILLEGAL.getCode()+"",ApiResponseStatus.INTERFACE_ILLEGAL.getMsg()+": "+reason);
    }
    
    
    /**
     * 接口返回错误
     * @author wubin
     * @param code
     * @param message
     * @return
     */
    public static <T> OutputParameter<T> error(String code,String message)
    {
        return new OutputParameter<T>(false, code,message);
    }
    
    /**
     * 默认未知错误
     * @author wubin
     * @param code
     * @param message
     * @return
     */
    
    public static <T> OutputParameter<T> error()
    {
        return new OutputParameter<T>(false, ApiResponseStatus.UNKNOWN_ERROR.getCode()+"",ApiResponseStatus.UNKNOWN_ERROR.getMsg());
    }
    
    /**
     * @return 返回 code
     */
    public String getCode()
    {
        return code;
    }
    
    /**
     * @param 对code进行赋值
     */
    public void setCode(String code)
    {
        this.code = code;
    }
    
    
    /**
     * @return success
     */
    
    public boolean isSuccess() {
        return success;
    }

    
    /** 
     * @param success 设置 success 
     */
    
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * @return msg
     */
    
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }
    
    public T getBiz()
    {
        return biz;
    }
    
    public void setBiz(T biz)
    {
        this.biz = biz;
    }
    
    @Override
    public String toString()
    {
        return JacksonObjectMapper.toJsonString(this);
    }
}
