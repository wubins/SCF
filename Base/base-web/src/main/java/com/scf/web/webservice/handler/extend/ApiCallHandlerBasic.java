package com.scf.web.webservice.handler.extend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.scf.utils.JacksonObjectMapper;
import com.scf.web.webservice.handler.ApiCallHandler;
import com.scf.web.webservice.param.OutputParameter;
import com.scf.web.webservice.param.extend.InputParameterBasic;
import com.scf.web.webservice.param.key.WebServiceKey;

/**
 * 接口调用封装类
 * 
 * 参数说明： sourceSystem为原系统 targetSystem目标系统，url调用路径，password为调用秘钥，bizMap为业务参数 OutputParameter返回值类型
 * 
 * @author jiangxia
 * @version [版本号, 2015年6月24日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ApiCallHandlerBasic extends ApiCallHandler
{
	private static final ApiCallHandlerBasic single = new ApiCallHandlerBasic();
	

	/** 
	* 单个参数接口调用
	* 
	* @Title: apiCall 
	* @Description: apicall
	* @param  sourceSystem 源系统
	* @param  targetSystem 目标系统
	* @param  url  访问地址
	* @param  apiid接口id
	* @param  inputParameterStr 入参的json串 
	* @param  typeRef    返回的类型 
	* @param  param 入参
	* @return OutputParameter
	* @throws 
	*/
	public <One, O> OutputParameter<O> apiCall(String sourceSystem, String targetSystem, String url, String apiId,
			TypeReference<OutputParameter<O>> typeRef, One param)
	{
		if(!super.checkInputIllegal(sourceSystem, targetSystem, url, apiId))
		{
			return null;
		}
		WebServiceKey webServiceKey = super.fillWebServiceKey(sourceSystem, apiId);
		
		InputParameterBasic<One> inputParameter = new InputParameterBasic<One>();
        inputParameter.setKey(webServiceKey);
        inputParameter.setConditionOne(param);
        
        return super.apiCall(url, JacksonObjectMapper.toJsonString(inputParameter), typeRef);
	}
	
    public static ApiCallHandlerBasic getInstance() {  
        return single; 
    }
}

