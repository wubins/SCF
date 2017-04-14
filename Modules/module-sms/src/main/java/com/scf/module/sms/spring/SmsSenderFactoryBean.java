package com.scf.module.sms.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.scf.module.sms.sender.ISender;
import com.scf.utils.ClassUtilies;

/**
 * 短信发送器工厂类
 * @author wubin
 * @date 2016年9月20日 下午4:33:27 
 * @version V1.1.0
 */
public class SmsSenderFactoryBean implements FactoryBean<ISender>, InitializingBean {

    private static final Log _logger = LogFactory.getLog(SmsSenderFactoryBean.class);

    /**
     *短信发送器
     */
    private ISender smsSender;
    private Class<ISender> senderClass;

    /**
     *
     * @return @throws Exception
     */
    @Override
    public ISender getObject() throws Exception {
        return smsSender;
    }

    /**
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        smsSender = (ISender) ClassUtilies.newInstance(senderClass);
        _logger.info("Sms sender is " + smsSender.getClass());
    }

    public void init() {
        smsSender.init();
    }

    public void destory() {
        smsSender.destory();
    }

    /**
     *
     * @return
     */
    @Override
    public Class<ISender> getObjectType() {
        return ISender.class;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * @return the senderClass
     */
    public Class<ISender> getSenderClass() {
        return senderClass;
    }

    /**
     * @param senderClass the senderClass to set
     */
    public void setSenderClass(Class<ISender> senderClass) {
        this.senderClass = senderClass;
    }
}
