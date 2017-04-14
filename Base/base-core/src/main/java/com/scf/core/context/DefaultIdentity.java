/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.core.context;

import java.util.HashMap;
import java.util.Map;

/**
 * 唯一标示默认实现
 * @author wub
 *
 */
public class DefaultIdentity implements Identity
{
    private static final long serialVersionUID = -589281732614547936L;

    /**
     * 标示id
     * 
     */
    private String id;
    
    /**
     * 标示中的数据
     */
    private final Map<String, Object> datas = new HashMap<String, Object>();
    
    @Override
    public String getId()
    {
        return id;
    }
    
    @Override
    public Map<String, Object> getDatas()
    {
        return datas;
    }
    
    @Override
    public void setId(String id)
    {
        this.id = id;
    }
    
    @Override
    public void put(String key, Object value)
    {
        datas.put(key, value);
    }
    
    @Override
    public Object getData(String key)
    {
        return datas.get(key);
    }
    
}
