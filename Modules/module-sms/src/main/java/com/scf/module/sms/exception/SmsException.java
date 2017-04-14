

package com.scf.module.sms.exception;

import com.scf.core.exception.ExCode;
import com.scf.core.exception.sys.SystemException;

/** 
 * 短信发送异常
 * @author wubin
 * @date 2016年8月2日 上午11:17:23 
 * @version V1.1.0
 */

public class SmsException extends SystemException {


    
    /** @Fields serialVersionUID: */
        
    private static final long serialVersionUID = 3123363884205879412L;

    
    public SmsException(int code,String message, Throwable cause) {
        super(code,message,cause);
    }
    
    
    
    public SmsException(int code,String message) {
        super(code,message);
    }
    
    
    /**
     * 默认短信发送异常
     * @author wubin
     * @param message 后台日志shortmessage
     * @param cause
     * @return
     */
    public static SmsException defaultException(){
        return SmsException.defaultException(null);
    }
    
    /**
     * 默认短信发送异常
     * @author wubin
     * @param message 后台日志shortmessage
     * @param cause
     * @return
     */
    public static SmsException defaultException(Throwable cause){
        SmsException smsException = new SmsException(ExCode.SMS_001,ExCode.SMS_001+"",cause);
        return smsException;
    }
    
  
    /**
     * 接受手机发送验证码频繁，不准发送
     * @author wubin
     * @param message 后台日志shortmessage
     * @param cause
     * @return
     */
    public static SmsException outputMobileSendException(){
        SmsException smsException = new SmsException(ExCode.SMS_002,ExCode.SMS_002+"");
        return smsException;
    }
    
    
    /**
     * 未到重发间隔，不准发送
     * @author wubin
     * @param message 后台日志shortmessage
     * @param cause
     * @return
     */
    public static SmsException shortIntervalException(){
        SmsException smsException = new SmsException(ExCode.SMS_003,ExCode.SMS_003+"");
        return smsException;
    }
    
    /**
     * 发送者（ip）发送验证码频繁，不准发送
     * @author wubin
     * @param message 后台日志shortmessage
     * @param cause
     * @return
     */
    public static SmsException outputAddressSendException(){
        SmsException smsException = new SmsException(ExCode.SMS_004,ExCode.SMS_004+"");
        return smsException;
    }
    
    

}
