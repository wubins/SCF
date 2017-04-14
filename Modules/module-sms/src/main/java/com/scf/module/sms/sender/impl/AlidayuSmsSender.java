package com.scf.module.sms.sender.impl;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.scf.core.context.app.cfg.module.ModuleConfigHandler;
import com.scf.module.sms.exception.SmsException;
import com.scf.module.sms.logger.DefaultLoger;
import com.scf.module.sms.logger.ILoger;
import com.scf.module.sms.sender.ISender;
import com.scf.utils.JacksonObjectMapper;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

/**
 * 
 * @author wubin
 * @date 2016年9月20日 下午4:34:47 
 * @version V1.1.0
 */
public class AlidayuSmsSender implements ISender {

    
    private String url;
    private String appkey;
    private String secret;

    @SuppressWarnings("unused")
    private static final Log _logger = LogFactory.getLog(AlidayuSmsSender.class);

    private ILoger loger;

    /**
     *
     */
    private TaobaoClient client = null;

    /**
     *
     */
    @Override
    public void init() {
        loger = new DefaultLoger();
        readParams();
        client = new DefaultTaobaoClient(url, appkey, secret);
    }

    /**
     *
     */
    @Override
    public void destory() {
    }

    /**
     *获取配置参数
     */
    protected void readParams() {
        url = ModuleConfigHandler.getSimpleProp("sms", "sms.alidayu.url");
        appkey = ModuleConfigHandler.getSimpleProp("sms", "sms.alidayu.appkey");
        secret = ModuleConfigHandler.getSimpleProp("sms", "sms.alidayu.secret");
        if(StringUtils.isEmpty(url) || StringUtils.isEmpty(appkey) || StringUtils.isEmpty(secret)){
            throw SmsException.configEmptyException("url","appkey","secret");
        }
    }

    /**
     *发送短信
     * @param mobiles
     * @param signName
     * @param templateName
     * @param params
     * @return
     */
    @Override
    public void send(String mobiles, String signName, String templateName, Map<String,String> params,String addressIp) {
        
        
        AlibabaAliqinFcSmsNumSendResponse rsp = null;
        do{
            if(StringUtils.isEmpty(mobiles) ||StringUtils.isEmpty(signName) || StringUtils.isEmpty(templateName) || MapUtils.isEmpty(params)){
                throw SmsException.configEmptyException("mobiles","signName","templateName","params");
            }
            String ps = convertParmas(params);
           
            AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
            req.setExtend("");
            req.setSmsType("normal");
            req.setSmsFreeSignName(signName);
            req.setSmsParamString(ps);
            req.setRecNum(mobiles);
            req.setSmsTemplateCode(templateName);
            try {
                rsp = client.execute(req);
            } catch (ApiException ex) {
                throw SmsException.defaultException(ex);
            }finally{
                if(rsp != null){
                    loger.log(mobiles, ps, signName, rsp.getBody());
                }else{
                    loger.log(mobiles, ps, signName, "调用阿里大于发送短信失败！");
                }
            }
            if(!rsp.isSuccess()){
                throw SmsException.defaultException();
            }
            
        }while(false);
        
      
    }

    /**
     *
     * @param params
     * @return
     */
    protected String convertParmas(Map<String,String> params) {
        return JacksonObjectMapper.toJsonString(params);
    }

    /**
     * @return the loger
     */
    public ILoger getLoger() {
        return loger;
    }

    /**
     * @param loger the loger to set
     */
    @Override
    public void setLoger(ILoger loger) {
        this.loger = loger;
    }

}
