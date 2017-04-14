
/*
 * @Title: SmsLimiter.java
 * 
 * @Package: com.scf.module.sms
 * 
 * @author wubin
 * 
 * @date 2016年9月21日 上午11:51:29
 * 
 * @version 1.3.1
 */

package com.scf.module.sms.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.scf.core.context.app.cfg.module.ModuleConfigHandler;
import com.scf.core.context.spring.memcache.cleint.MemCachedOperation;
import com.scf.module.sms.exception.SmsException;
import com.scf.utils.DatetimeUtilies;

/**
 * 发送限制器
 * 
 * @author wubin
 * @date 2016年9月21日 上午11:51:29
 * @version V1.1.0
 */

public class SmsLimiter {

    private static Logger logger = LoggerFactory.getLogger(SmsLimiter.class);

    /**
     * 接受验证码的手机次数限制
     */
    public static final String SMS_MOBILE_ACCOUNT_PREFIX = "SMS_SENDER_LIMIT_SMS_MOBILE_ACCOUNT_";

    /**
     * 发送验证码的机器ip地址发送次数限制
     */
    public static final String SMS_ADDRESS_ACCOUNT_PREFIX = "SMS_SENDER_LIMIT_SMS_ADDRESS_ACCOUNT_";

    /**
     * 验证码发送间隔
     */
    public static final String SMS_MOBILE_RETRY_PREFIX = "SMS_SENDER_LIMIT_SMS_MOBILE_RETRY_";

    /**
     * 验证码有效时间
     */
    public static final Integer VERIFYCODE_EXPIRY = Integer
            .parseInt(ModuleConfigHandler.getSimpleProp("sms", "verifyCodeExpiry"));

    /**
     * 验证码重新发送时间间隔
     */
    public static final Integer VERIFYCODE_RETRY = Integer
            .parseInt(ModuleConfigHandler.getSimpleProp("sms", "verifyCodeRetry"));

    /**
     * 同一IP地址当天发送次数限制
     */
    public static final Integer ADDRESS_COUNT = Integer
            .parseInt(ModuleConfigHandler.getSimpleProp("sms", "verifyCodeAddressCount"));

    /**
     * 同一手机号当天发送次数限制
     */
    public static final Integer MOBILE_COUNT = Integer
            .parseInt(ModuleConfigHandler.getSimpleProp("sms", "verifyCodeMobileCount"));

    /**
     * 发送前处理器，校验发送限制
     * 
     * @author wubin
     */
    public static void preSend(String mobile, String ipAddress) {

        Integer sendADDRESS_COUNT = 0;
        Integer sendMobileCount = 0;
        if (MemCachedOperation.containsKey(SMS_MOBILE_ACCOUNT_PREFIX + mobile)) {
            sendMobileCount = (Integer) MemCachedOperation.get(SMS_MOBILE_ACCOUNT_PREFIX + mobile);
            logger.info("===有验证码调用的发送短信方法=sendVerifyCode=ipAddress:{},authCode{},短信发送次数:{}",
                    new Object[] { ipAddress, "", sendMobileCount });
            // TODO 验证码超过次数，一般需要图片验证码的。
            if (sendMobileCount >= MOBILE_COUNT) {
                // 手机发送验证码频繁，不准发送
                throw SmsException.outputMobileSendException();
            }
        }
        if (MemCachedOperation.containsKey(SMS_MOBILE_RETRY_PREFIX + mobile)) {
            // 未到重发间隔，不准发送
            throw SmsException.shortIntervalException();
        }
        if (!StringUtils.isEmpty(ipAddress)) {
            if (MemCachedOperation.containsKey(SMS_ADDRESS_ACCOUNT_PREFIX + ipAddress)) {
                sendADDRESS_COUNT = (Integer) MemCachedOperation.get(SMS_ADDRESS_ACCOUNT_PREFIX + ipAddress);
                if (sendADDRESS_COUNT >= ADDRESS_COUNT) {
                    // IP地址请求验证码频繁，不准发送
                    throw SmsException.outputAddressSendException();
                }
            }
        }

    }

    /**
     * 发送后处理器,记录发送次数限制
     */
    public static void postSend(String mobile, String verifyCode, String ipAddress) {
        Integer sendADDRESS_COUNT = (Integer) MemCachedOperation.get(SmsLimiter.SMS_ADDRESS_ACCOUNT_PREFIX + ipAddress);
        if(sendADDRESS_COUNT == null){
            sendADDRESS_COUNT = 0;
        }
        sendADDRESS_COUNT++;
        Integer sendMobileCount = (Integer) MemCachedOperation.get(SmsLimiter.SMS_MOBILE_ACCOUNT_PREFIX + mobile);
        
        if(sendMobileCount == null){
            sendMobileCount = 0;
        }
        sendMobileCount++;
        MemCachedOperation.set(SmsLimiter.SMS_MOBILE_RETRY_PREFIX + mobile, verifyCode, SmsLimiter.VERIFYCODE_RETRY);
        MemCachedOperation.set(SmsLimiter.SMS_MOBILE_ACCOUNT_PREFIX + mobile, sendMobileCount,
                DatetimeUtilies.getRemainSeconds());
        if (!StringUtils.isEmpty(ipAddress)) {
            MemCachedOperation.set(SmsLimiter.SMS_ADDRESS_ACCOUNT_PREFIX + ipAddress, sendADDRESS_COUNT,
                    DatetimeUtilies.getRemainSeconds());
        }
    }

}
