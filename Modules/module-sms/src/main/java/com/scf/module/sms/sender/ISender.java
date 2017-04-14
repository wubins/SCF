/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.module.sms.sender;

import java.util.Map;

import com.scf.module.sms.logger.ILoger;

/**
 * 短信发送器
 * @author wubin
 * @date 2016年9月20日 下午4:34:52 
 * @version V1.1.0
 */
public interface ISender {
    
    /**
     * 验证码占位符名称：${code}，请业务线调用着注意！
     */
    public static final String CODE_NAME="code";

    /**
     *  初始化
     * @author wubin
     */
    public void init();

    /**
     * 发送短信
     * @author wubin
     * @param mobiles
     * @param signName
     * @param templateName
     * @param params
     * @param ipAddress 发送者ip
     * @return
     */
    public void send(String mobiles, String signName, String templateName, Map<String, String> params,String ipAddress);

    /**
     * 日志
     * @author wubin
     * @param loger
     */
    public void setLoger(ILoger loger);
    /**
     *  销毁钩子
     * @author wubin
     */
    public void destory();
}
