package com.scf.web.comp.ace;

import java.io.Serializable;

/**
 * 普通 rest请求响应
 * @author wubin
 * @date 2016年7月27日 下午5:23:02 
 * @version V1.1.0
 */
public class ResponseMessage implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -3035285950649126234L;
    
    
    private ResponseMessage()
    {
        
    }
    /**
     * 未登陆
     */
    public static final String ERR_NOT_LOGIN = "err.not.login";
    /**
     * 未授权
     */
    public static final String ERR_UNAUTHORIZED = "err.unauthorized";
    /**
     * 未注册
     */
    public static final String ERR_NOT_REGIST = "err.not.regist";
    /**
     * 默认未知错误
     */
    public static final String ERR_DEFAULT = "err.default";
    
    /**
     *  成功失败 标志
     */
    private boolean success;
    /**
     * 错误code
     */
    private String code;
    
    /**
     * code对应的内容
     */
    private String message;
    /**
     *  返回的对象
     */
    private Object obj;
    
    public static ResponseMessage success(Object obj)
    {
        return new ResponseMessage(true, obj);
    }
    
    public static ResponseMessage success(String code, String message)
    {
        return new ResponseMessage(true, code,message);
    }
    
    public static ResponseMessage success(String message, Object obj)
    {
        return new ResponseMessage(true, message, obj);
    }
    
    public static ResponseMessage success(String message)
    {
        return new ResponseMessage(true, message);
    }
    
    //返回类型为string 的业务参数，为了区分开 success(String message)方法
    public static ResponseMessage successStringBiz(String biz)
    {
        return new ResponseMessage(true, (Object)biz);
    }
    
    
    public static ResponseMessage success()
    {
        return new ResponseMessage(true);
    }
    
    
    public static ResponseMessage error(String code, String message)
    {
        return new ResponseMessage(false, code, message);
    }
    
    public static ResponseMessage error()
    {
        return new ResponseMessage(false, ERR_DEFAULT, "默认未知错误");
    }
    
    public static ResponseMessage noLogin(String message)
    {
        return new ResponseMessage(false, ERR_NOT_LOGIN, message);
    }
    
    public static ResponseMessage noPermission(String message)
    {
        return new ResponseMessage(false, ERR_UNAUTHORIZED, message);
    }
    
    public static ResponseMessage noPermission(String message,Object obj)
    {
         return new ResponseMessage(false, ERR_UNAUTHORIZED, message, obj);
    }
    
    public static ResponseMessage noRegist(String message)
    {
        return new ResponseMessage(false, ERR_NOT_REGIST, message);
    }
    

    public static ResponseMessage error(String message, Object obj)
    {
        return new ResponseMessage(false, message, obj);
    }
    
    public static ResponseMessage error(int code)
    {
        return new ResponseMessage(false, code);
    }
    
    private ResponseMessage(boolean success, String message, Object obj)
    {
        this.success = success;
        this.message = message;
        this.obj = obj;
    }
    
    private ResponseMessage(boolean success, String code, String message)
    {
        this.success = success;
        this.code = code;
        this.message = message;
    }
    
    private ResponseMessage(boolean success, String code, String message,Object obj)
    {
        this.success = success;
        this.code = code;
        this.message = message;
        this.obj = obj;
    }
    
    private ResponseMessage(boolean success, Object obj)
    {
        this.success = success;
        this.obj = obj;
    }
    
    private ResponseMessage(boolean success, String message)
    {
        this.success = success;
        this.message = message;
    }
    
    private ResponseMessage(boolean success)
    {
        this.success = success;
    }
    
    public boolean isSuccess()
    {
        return success;
    }
    
    public void setSuccess(boolean success)
    {
        this.success = success;
    }
    
    public String getCode()
    {
        return code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public Object getObj()
    {
        return obj;
    }
    
    public void setObj(Object obj)
    {
        this.obj = obj;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
}
