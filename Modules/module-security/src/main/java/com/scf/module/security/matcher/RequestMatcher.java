package com.scf.module.security.matcher;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求matcher
 * @author wubin
 * @date 2016年8月3日 上午10:21:20 
 * @version V1.1.0
 */
public interface RequestMatcher {

    boolean matches(HttpServletRequest request);

}
