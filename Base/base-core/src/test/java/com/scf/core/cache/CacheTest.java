package com.scf.core.cache;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.scf.core.context.spring.cache.CacheContext;
import com.scf.utils.AssertUtilies;
/**
 * 
 * @author wubin
 */
@ContextConfiguration(locations = {"classpath*:scf/spring-ctx.xml" ,"classpath*:scf/spring-ctx-memcache.xml","classpath*:scf/spring-ctx-cache.xml"})
public class CacheTest extends AbstractJUnit4SpringContextTests {
    @Test
    public void testSetAndGet()
        throws Exception
    {
        CacheContext.put("aa", 123);
        Integer a =CacheContext.get("aa");
        AssertUtilies.isTrue((a == 123));
    }
}
