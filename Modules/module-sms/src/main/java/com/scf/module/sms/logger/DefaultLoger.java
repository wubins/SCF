package com.scf.module.sms.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认短信日志记录器
 * @author wubin
 * @date 2016年9月20日 下午4:35:49 
 * @version V1.1.0
 */
public class DefaultLoger implements ILoger {
    
    private static final Logger _logger = LoggerFactory.getLogger(DefaultLoger.class);
    
    @Override
    public void log(String mobiles, String content, String signer, String response) {
        
        _logger.info("Sended sms, mobiles={}, content={}, signer={}, response={}"  ,mobiles,content,signer,response);
    }
    
}
