/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.core.ebus;

import com.google.common.eventbus.Subscribe;
import com.scf.core.ebus.EventBusRepository;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.util.Log4jConfigurer;

/**
 *
 * @author wub
 */
@ContextConfiguration(locations = {"classpath*:scf/spring-ctx.xml", "classpath*:scf/spring-ctx-ds.xml", "classpath*:scf/spring-ctx-tx.xml", "classpath*:scf/spring-ctx-cache.xml"})
public class GuavaEventBusTest extends AbstractTransactionalJUnit4SpringContextTests {

    @BeforeClass
    public static void setUpClass() {
        try {
            Log4jConfigurer.initLogging("classpath:log4j.properties");
        } catch (Exception ex) {
        }
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void testEvent() {
        EventBusRepository.regGlobalBus(false);
        EventListener el = new EventListener();
        EventBusRepository.getGlobalBus().addEventListener(el);
        Object e = "abc";
        EventBusRepository.getGlobalBus().dispatchEvent(e);
        Assert.assertTrue(e.equals(el.testEvent));
        EventBusRepository.getGlobalBus().dispatchEvent(123);
        EventBusRepository.unRegGlobalBus();
    }

    @Test
    public void testAsyncEvent() {
        EventBusRepository.regGlobalBus(true);
        EventListener el = new EventListener();
        EventBusRepository.getGlobalBus().addEventListener(el);
        Object e = "abc";
        EventBusRepository.getGlobalBus().dispatchEvent(e);
        try {
            Thread.sleep(500);
        } catch (Exception ex) {
        }
        Assert.assertTrue(e.equals(el.testEvent));
        EventBusRepository.getGlobalBus().dispatchEvent(123);
        EventBusRepository.unRegGlobalBus();
    }

    class EventListener {

        String testEvent = null;

        @Subscribe
        public void handlerEvent(String event) {
            testEvent = event;
        }
    }

}
