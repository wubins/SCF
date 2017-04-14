package com.scf.module.security.meta;

import java.io.Serializable;

import com.scf.module.security.matcher.RequestMatcher;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 权限对象
 * @author wubin
 * @date 2016年8月3日 上午9:30:39 
 * @version V1.1.0
 */
@XStreamAlias("permission")
public class Permission implements Serializable{
    
    /**  */
    private static final long serialVersionUID = 169547859821321383L;
    
    /**
     * 资源key
     */
    @XStreamAlias("code")
    private String code;
    /**
     * 资源url
     */
    @XStreamAlias("url")
    private String url;
    
    /**
     * 匹配器
     */
    @XStreamOmitField
    private RequestMatcher requestMatcher;
    
    
    
    /**
     * Getter method for property <tt>requestMatcher</tt>.
     * 
     * @return property value of requestMatcher
     */
    public RequestMatcher getRequestMatcher() {
        return requestMatcher;
    }
    /**
     * Setter method for property <tt>requestMatcher</tt>.
     * 
     * @param requestMatcher value to be assigned to property requestMatcher
     */
    public void setRequestMatcher(RequestMatcher requestMatcher) {
        this.requestMatcher = requestMatcher;
    }
    /**
     * Getter method for property <tt>code</tt>.
     * 
     * @return property value of code
     */
    public String getCode() {
        return code;
    }
    /**
     * Setter method for property <tt>code</tt>.
     * 
     * @param code value to be assigned to property code
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * Getter method for property <tt>url</tt>.
     * 
     * @return property value of url
     */
    public String getUrl() {
        return url;
    }
    /**
     * Setter method for property <tt>url</tt>.
     * 
     * @param url value to be assigned to property url
     */
    public void setUrl(String url) {
        this.url = url;
    }
    
    
    
    
    
    
    

}
