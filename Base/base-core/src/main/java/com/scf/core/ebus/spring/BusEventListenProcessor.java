package com.scf.core.ebus.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.scf.core.ebus.EventBusRepository;
import com.scf.core.ebus.meta.BusEventListener;
import com.scf.utils.ClassUtilies;

/**
 * @author wub
 *
 */
public class BusEventListenProcessor implements BeanPostProcessor, ApplicationContextAware {

    /**
     *
     */
    private static final Logger _logger = LoggerFactory.getLogger(BusEventListenProcessor.class);

    /**
     *
     */
    @SuppressWarnings("unused")
    private ApplicationContext context;

    /**
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    /**
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        BusEventListener ann = (BusEventListener) ClassUtilies.getRealClass(bean).getAnnotation(BusEventListener.class);
        if (ann != null) {
            String busName = ann.busname();
            if (EventBusRepository.exists(busName)) {
                EventBusRepository.getEventBus(busName).addEventListener(bean);
                _logger.info("Regiested eventbus listener for " + beanName + " on bus name " + busName);
            } else {
                _logger.warn("Can not regiest eventbus listener for " + beanName + " on bus name " + busName);
            }
        }
        return bean;
    }

    /**
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
