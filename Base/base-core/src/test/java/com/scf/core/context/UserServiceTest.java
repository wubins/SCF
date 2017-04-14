package com.scf.core.context;

import java.io.FileNotFoundException;
import java.util.Properties;

import javax.annotation.Resource;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.util.Log4jConfigurer;

import com.scf.core.context.app.cfg.module.ModuleConfigHandler;
import com.scf.core.context.service.UserService;

@ContextConfiguration(locations = { "classpath*:scf/spring-ctx.xml",  "classpath*:spring-ctx-ds.xml", "classpath*:spring-ctx-tx.xml"})
@TransactionConfiguration( defaultRollback = false)
public class UserServiceTest  extends AbstractTransactionalJUnit4SpringContextTests {
	@Resource
	private UserService userService;
	
	  //protected DataFactory dataFactory = new DataFactory();

	    public UserServiceTest() {
	        //dataFactory.randomize(RandomUtils.nextInt());
	    }

	    @BeforeClass
	    public static void setUpClass() {
	        try {
	            Log4jConfigurer.initLogging("classpath:log4j.properties");
	        } catch (FileNotFoundException ex) {
	            throw new RuntimeException("Can't init log4j configurer.", ex);
	        }
	        ModuleConfigHandler.loadModuleConfigs(new Properties());
	    }

	    @Test
	    public void testInit() {
	        userService.deleteAll();
	    }
}
