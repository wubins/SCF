package com.scf.web.webservice.param.extend;

import com.scf.utils.JacksonObjectMapper;

/**
 * 系统间webservice接口输入参数，,含有二个参数，继承one以及自己的two
 * 
 * @author jiangxia
 */
public class InputParameterJunior<One, Two> extends InputParameterBasic<One>
{
	private static final long serialVersionUID = 3613126383711929422L;
	
    private Two conditionTwo ; //第二个条件
    
	public Two getConditionTwo() {
		return conditionTwo;
	}

	public void setConditionTwo(Two conditionTwo) {
		this.conditionTwo = conditionTwo;
	}

	@Override
    public String toString()
    {
        return JacksonObjectMapper.toJsonString(this);
    }
    
}
