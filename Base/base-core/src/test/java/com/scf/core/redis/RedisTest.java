
/*   
 * @Title: RedisTest.java 
 * @Package: com.scf.core.redis 
 * @author wubin  
 * @date 2016年8月2日 上午10:34:37 
 * @version 1.3.1 
 */


package com.scf.core.redis;

import java.io.FileNotFoundException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.util.Log4jConfigurer;

import com.scf.core.context.spring.redis.cleint.RedisOperation;

/** 
 * @author wubin
 * @date 2016年8月2日 上午10:34:37 
 * @version V1.1.0
 */

@ContextConfiguration(locations = { "classpath*:scf/spring-ctx.xml","classpath*:scf/spring-ctx-redis.xml"})
public class RedisTest extends AbstractJUnit4SpringContextTests {
    @BeforeClass
    public static void setUpClass() {
        try {
            Log4jConfigurer.initLogging("classpath:log4j.properties");
        } catch (FileNotFoundException ex) {
        }
    }
    @Test
    public void testSetAndGet()
        throws Exception
    {
        RedisOperation.setString("aa", "123");
        String a =RedisOperation.getString("aa");
        System.out.println("===1231====="+"123".equals(a));
        
    }
}
