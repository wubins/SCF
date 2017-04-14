
/*   
 * @Title: SenderDelegate.java 
 * @Package: com.scf.module.sms.support 
 * @author wubin  
 * @date 2016年9月21日 下午12:32:00 
 * @version 1.3.1 
 */

package com.scf.module.sms.support;

import java.util.Map;

import com.scf.module.sms.logger.ILoger;
import com.scf.module.sms.sender.ISender;
import com.scf.utils.StringUtilies;

/** 
 * 短信发送传播起
 * @author wubin
 * @date 2016年9月21日 下午12:32:00 
 * @version V1.1.0
 */

public class SenderDelegate implements ISender {

    /**
     * 短信发送器
     */
    private ISender sender;
    
    
    
    /**
     * @return sender
     */
    
    public ISender getSender() {
        return sender;
    }

    
    /** 
     * @param sender 设置 sender 
     */
    
    public void setSender(ISender sender) {
        this.sender = sender;
    }

    /**
     *  
     * @see com.scf.module.sms.ISender#init() 
     */

    @Override
    public void init() {
    }

    /**
     * @param mobiles
     * @param signName
     * @param templateName
     * @param params 
     * @see com.scf.module.sms.ISender#send(java.lang.String, java.lang.String, java.lang.String, java.util.Map) 
     */

    @Override
    public void send(String mobiles, String signName, String templateName, Map<String, String> params,String ipAddress) {
        
        SmsLimiter.preSend(mobiles, ipAddress);
        
        String verifyCode = params.get(ISender.CODE_NAME);
        
        sender.send(mobiles, signName, templateName, params,ipAddress);
        //可能存在无验证码的场景
        if(!StringUtilies.isNullOrEmpty(verifyCode))
        {
            SmsLimiter.postSend(mobiles, verifyCode, ipAddress);
        }
    }

    /**
     * @param loger 
     * @see com.scf.module.sms.ISender#setLoger(com.scf.module.sms.ILoger) 
     */

    @Override
    public void setLoger(ILoger loger) {
    }

    /**
     *  
     * @see com.scf.module.sms.ISender#destory() 
     */

    @Override
    public void destory() {
    }

}
