package com.scf.module.verifycode.service.impl;

import java.io.File;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.scf.module.verifycode.service.impl.SimpleVerifyCodeService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:scf/module/verifycode/spring-ctx.xml"})
public class SimpleVerifyCodeServiceTest
{
    private static Logger _logger = LoggerFactory.getLogger(SimpleVerifyCodeServiceTest.class);
    
    @Resource
    private SimpleVerifyCodeService instance;
    
    @Test
    public void testGenerateVerifyCode()
    {
        String code = instance.generateVerifyCode(5);
        _logger.info("output verify code : {} ", code);
        Assert.assertNotNull(code);
    }
    
    @Test
    public void testGenerateSourceVerifyCode()
    {
        String resource = "23456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjklmnopqrstuvwxyz";
        String code = instance.generateVerifyCode(resource, 4);
        _logger.info("output verify code : {} ", code);
        Assert.assertNotNull(code);
    }
    
    @Test
    public void testOutputVerifyImage()
    {
        File dir = new File(".");
        File file = new File(dir, "verifyCode.jpg");
        String code = instance.outputVerifyImage(100, 30, file, 4);
        _logger.info("output verify code : {} , filepath : {} ", code, file.getPath());
    }
    
    public SimpleVerifyCodeService getInstance()
    {
        return instance;
    }
    
    public void setInstance(SimpleVerifyCodeService instance)
    {
        this.instance = instance;
    }
    
}
