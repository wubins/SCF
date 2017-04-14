package com.scf.core.context.spring.memcache;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.utils.AddrUtil;

/**
 * memcached客户端生成 包装类
 * @author wubin
 * @date 2016年7月27日 下午3:12:48 
 * @version V1.1.0
 */
public class MemcacheWrapper extends XMemcachedClientBuilder
{
    private static Logger _logger = LoggerFactory.getLogger(MemcacheWrapper.class);
    
    private boolean enable;
    
    
    public MemcacheWrapper(String address,String userName, String password)
    {
        super(address);
        if(!StringUtils.isEmpty(userName)&&!StringUtils.isEmpty(password)){
        	   super.addAuthInfo(AddrUtil.getOneAddress(address), AuthInfo.plain(userName,password));
        }
    }
    
    public MemcachedClient build() throws IOException
    {
        _logger.info("is memcache enable:{}", enable);
        if (enable)
        {

            return  super.build();
        }
        return null;
    }
    
    public boolean isEnable()
    {
        return enable;
    }
    
    public void setEnable(boolean enable)
    {
        this.enable = enable;
    }
    
    
}
