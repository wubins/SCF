package com.scf.web.webservice.param.extend;

import java.io.Serializable;

import com.scf.utils.JacksonObjectMapper;
import com.scf.web.webservice.param.InputParameter;

/**
 * 系统间webservice接口输入参数,只含有一个参数：one
 * 
 * @author wubin
 */
public class InputParameterBasic<One> extends InputParameter  implements Serializable
{
    private static final long serialVersionUID = 3613126383711929421L;
    
    private One conditionOne ; //第一个条件
    
    
    public One getConditionOne() {
		return conditionOne;
	}

	public void setConditionOne(One conditionO) {
		this.conditionOne = conditionO;
	}
    
    @Override
    public String toString()
    {
        return JacksonObjectMapper.toJsonString(this);
    }
    
}
