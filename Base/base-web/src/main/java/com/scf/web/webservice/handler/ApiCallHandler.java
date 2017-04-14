package com.scf.web.webservice.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.scf.core.exception.AppException;
import com.scf.core.exception.ExCode;
import com.scf.utils.EncryptUtils;
import com.scf.utils.HttpClientUtilies;
import com.scf.utils.JacksonObjectMapper;
import com.scf.web.webservice.handler.extend.ApiCallHandlerBasic;
import com.scf.web.webservice.param.OutputParameter;
import com.scf.web.webservice.param.key.WebServiceKey;

/**
 * 接口调用抽象类
 * 
 * 参数说明： sourceSystem为原系统 targetSystem目标系统，url调用路径，password为调用秘钥，bizMap为业务参数 OutputParameter返回值类型
 * 
 * @author jiangxia
 * @version [版本号, 2015年6月24日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class ApiCallHandler
{
    private static final Logger logger = LoggerFactory.getLogger(ApiCallHandlerBasic.class);

    /** 
     * 接口入餐合法性校验
     * 
    * @Title: fillWebServiceKey 
    * @param  sourceSystem 原系统
    * @param  targetSystem 目标系统
    * @param  url  访问地址
    * @param  apiId  接口id
    * @return WebServiceKey    返回类型 
    * @throws 
    */
    protected boolean checkInputIllegal(String sourceSystem, String targetSystem, String url, String apiId)
    {
        if (logger.isInfoEnabled())
        {
            logger.info("ApiCallHandler==sourceSystem:" + sourceSystem + "==targetSystem:"
                + targetSystem + "==url:" + url + "==apiId:" + apiId );
        }
        if (StringUtils.isEmpty(sourceSystem) || StringUtils.isEmpty(targetSystem) || StringUtils.isEmpty(url)
            || StringUtils.isEmpty(apiId))
        {
            if (logger.isWarnEnabled())
            {
                logger.warn("ApiCallHandler===必要参数不能为空");
            }
            return false;
        }
        return true;
    }
   
    /** 
     * 组装WebServiceKey对象 
     * 
    * @Title: fillWebServiceKey 
    * @param  sourceSystem 原系统
    * @param  apiId  接口id
    * @return WebServiceKey    返回类型 
    * @throws 
    */
    protected WebServiceKey fillWebServiceKey(String sourceSystem, String apiId)
    {
        WebServiceKey webServiceKey = new WebServiceKey();
        webServiceKey.setSystem(sourceSystem);
        webServiceKey.setKey(EncryptUtils.MD5Encode(apiId));
        return webServiceKey;
    }
    
    /** 
     * 
     * 调用接口核心方法
     * 
    * @Title: afterApiCall 
    * @param  url  访问地址
    * @param  inputParameterStr 入参的json串 
    * @param  typeRef    返回的类型 
    * @return OutputParameter
    * @throws 
    */
    protected <O> OutputParameter<O> apiCall(String url, String inputParameterStr, TypeReference<OutputParameter<O>> typeRef)
    {
        
        logger.info("ApiCallHandler===inputParameterStr==={}",inputParameterStr);
        
        String outPutString = HttpClientUtilies.doPostJson(url, inputParameterStr);
        logger.info("ApiCallHandler===OutputParameter==={}",outPutString);
        if (outPutString == null)
        {
            logger.error("ApiCallHandler===10105：与第三方系统交互异常（未知）");
            throw new AppException(ExCode.SYS_005);
        }
        
        OutputParameter<O> outputParameter = (OutputParameter<O>)JacksonObjectMapper.fromJsonString(outPutString, typeRef);
        
        if(!StringUtils.isEmpty(outputParameter.getCode()) && outputParameter.getCode().startsWith("ex.")){
                logger.error("ApiCallHandler===10105：与第三方系统交互异常(对方系统产生系统异常)");
                throw new AppException(ExCode.SYS_005);
        }
        return outputParameter;
    }
    
}

