package com.scf.web.webservice.param.key;

import java.io.Serializable;

import com.scf.utils.JacksonObjectMapper;

/**
 * 系统间webservice接口校验对象
 * @author wubin
 */
public class WebServiceKey implements Serializable
{
    private static final long serialVersionUID = -1954272612830574768L;
    /**
     * 当前系统的code
     */
    private String system;
    /**
     * 秘钥
     */
    private String key;
    
    public String getSystem()
    {
        return system;
    }
    
    public void setSystem(String system)
    {
        this.system = system;
    }
    
    public String getKey()
    {
        return key;
    }
    
    public void setKey(String key)
    {
        this.key = key;
    }
    
    @Override
    public String toString()
    {
        return JacksonObjectMapper.toJsonString(this);
    }
}
