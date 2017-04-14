
/*   
 * @Title: SystemException.java 
 * @Package: com.scf.core.exception.sys 
 * @author wubin  
 * @date 2016年8月3日 上午11:34:06 
 * @version 1.3.1 
 */


package com.scf.core.exception.sys;

import com.scf.core.exception.AppException;
import com.scf.core.exception.ExCode;

/** 
 * 系统通用异常
 * @author wubin
 * @date 2016年8月3日 上午11:34:06 
 * @version V1.1.0
 */

public class SystemException extends AppException {


    
    /** @Fields serialVersionUID: */
        
    private static final long serialVersionUID = 3112363884205879412L;

    
    public SystemException(int code,String message, Throwable cause) {
        super(code,message,cause);
    }
    
    
    
    public SystemException(int code,String message) {
        super(code,message);
    }
    /**
     * 系统默认异常
     * @author wubin
     * @param message 后台日志shortmessage
     * @param cause
     * @return
     */
    public static SystemException defaultSystemException(String message,Throwable cause){
        SystemException systemException = new SystemException(ExCode.SYS_001,"系统默认异常： "+(message == null?"":message),cause);
        return systemException;
    }
    
    /**
     * 系统配置异常
     * @author wubin
     * @param message
     * @param cause
     * @return
     */
    public static SystemException configException(String message,Throwable e){
        SystemException memcacheException = new SystemException(ExCode.SYS_002,"系统配置异常： "+(message == null?"":message),e);
        return memcacheException;
    }
    
    /**
     * 系统配置异常
     * @author wubin
     * @param message
     * @param cause
     * @return
     */
    public static SystemException configException(String message){
        return SystemException.configException(message,null);
    }
    
    /**
     * 系统参数配置不能为空异常异常
     * @author wubin
     * @param message
     * @param cause
     * @return
     */
    public static SystemException configEmptyException(String... propertys){
        StringBuffer sb = new StringBuffer();
        for(String propertyTmp  : propertys){
            sb.append(propertyTmp+"||");
        }
        if(sb.length()>0){
            sb.substring(0, sb.length()-2);
        }
        return SystemException.configException(sb.toString()+" 不能为空！",null);
    }
    
    /**
     * 系统加密异常
     * @author wubin
     * @param message
     * @param cause
     * @return
     */
    public static SystemException encryptException(String message,Throwable e){
        SystemException memcacheException = new SystemException(ExCode.SYS_003,"系统加密异常： "+(message == null?"":message),e);
        return memcacheException;
    }
    
    /**
     * 系统解析文件异常
     * @author wubin
     * @param message
     * @param cause
     * @return
     */
    public static SystemException resolveFileException(String message,Throwable e){
        SystemException memcacheException = new SystemException(ExCode.SYS_004,"系统解析文件异常： "+(message == null?"":message),e);
        return memcacheException;
    }
    
    
    /**
     * 与第三方系统交互异常
     * @author wubin
     * @param message
     * @param cause
     * @return
     */
    public static SystemException  thirdException(String message,Throwable e){
        SystemException memcacheException = new SystemException(ExCode.SYS_005,"与第三方系统交互异常： "+(message == null?"":message),e);
        return memcacheException;
    }
    
    
    /**
     * 系统上下文异常
     * @author wubin
     * @param message
     * @param cause
     * @return
     */
    public static SystemException contextException(String message,Throwable e){
        SystemException memcacheException = new SystemException(ExCode.SYS_006,"系统上下文异常： "+(message == null?"":message),e);
        return memcacheException;
    }
    
    

    /**
     * 系统参数异常
     * @author wubin
     * @param message
     * @param cause
     * @return
     */
    public static SystemException paramException(String message){
        SystemException memcacheException = new SystemException(ExCode.PARAM_001,"系统参数异常： "+(message == null?"":message));
        return memcacheException;
    }
    
    
    
    


}
