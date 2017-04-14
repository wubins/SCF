package com.scf.web.webservice.param;

import java.io.Serializable;

import com.scf.utils.JacksonObjectMapper;
import com.scf.web.webservice.param.key.WebServiceKey;

/**
 * 系统间webservice接口输入参数 基类，包含验证对象
 * 
 * @author wubin
 */
public abstract class InputParameter implements Serializable
{
    private static final long serialVersionUID = 3613126383711929421L;
    
    private WebServiceKey key; // 验证对象
    
    public WebServiceKey getKey()
    {
        return key;
    }
    
    public void setKey(WebServiceKey key)
    {
        this.key = key;
    }
    
    @Override
    public String toString()
    {
        return JacksonObjectMapper.toJsonString(this);
    }
    
}
