/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.module.sms.logger;

/**
 * 短信日志记录器
 * @author wubin
 * @date 2016年9月20日 下午4:35:26 
 * @version V1.1.0
 */
public interface ILoger {

    public void log(String mobiles, String content, String signer, String response);
}
