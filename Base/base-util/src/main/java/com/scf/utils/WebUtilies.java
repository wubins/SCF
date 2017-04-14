package com.scf.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

/**
 * web工具类
 * 
 * @author wub
 * @version [版本号, 2015年7月1日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class WebUtilies {

    /**
     * 获取请求中的参数
     * 
     * @author wubin
     * @param request
     * @param name
     * @param defaultValue
     * @return
     */

    public static String getString(HttpServletRequest request, String name, String defaultValue) {
        String value = getRequestValue(request, name);
        if (value == null) {
            value = defaultValue;
        }
        if (value != null) {
            value = value.trim();
        }
        return value;
    }

    /**
     * 获取请求中的boolean
     * 
     * @author wubin
     * @param request
     * @param name
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(HttpServletRequest request, String name, boolean defaultValue) {
        String value = getRequestValue(request, name);
        boolean r = false;
        if (value != null) {
            if ("true".equals(value)) {
                r = true;
            }
        } else {
            r = defaultValue;
        }
        return (r);
    }

    /**
     * 获取请求中的数值int
     * 
     * @author wubin
     * @param request
     * @param name
     * @param defaultValue
     * @return
     */
    public static int getInt(HttpServletRequest request, String name, int defaultValue) {

        String value = getRequestValue(request, name);
        int r = 0;
        if (value != null) {
            try {
                r = Integer.parseInt(value);

            } catch (Exception e) {
                r = defaultValue;
            }
        } else {
            r = defaultValue;
        }
        return r;
    }

    /**
     * 获取请求中的数值integer
     * 
     * @author wubin
     * @param request
     * @param name
     * @param defaultValue
     * @return
     */
    public static Integer getInteger(HttpServletRequest request, String name, Integer defaultValue) {

        String value = getRequestValue(request, name);
        Integer r = null;
        if (value != null) {
            try {
                r = Integer.parseInt(value);

            } catch (Exception e) {
                r = defaultValue;
            }
        } else {
            r = defaultValue;
        }
        return r;
    }

    /**
     * 获取请求中的数值long
     * 
     * @author wubin
     * @param request
     * @param name
     * @param defaultValue
     * @return
     */
    public static long getLong(HttpServletRequest request, String name, long defaultValue) {
        String value = getRequestValue(request, name);
        long r = 0;
        if (value != null) {
            try {
                r = Long.parseLong(value);
            } catch (Exception e) {
                r = defaultValue;
            }
        } else {
            r = defaultValue;
        }
        return r;
    }

    /**
     * 获取请求中的数值double
     * 
     * @author wubin
     * @param request
     * @param name
     * @param defaultValue
     * @return
     */
    public static double getDouble(HttpServletRequest request, String name, double defaultValue) {
        String value = getRequestValue(request, name);
        double r = 0.0;
        if (value != null) {
            try {
                r = Double.parseDouble(value);
            } catch (Exception e) {
                r = defaultValue;
            }
        } else {
            r = defaultValue;
        }
        return r;
    }

    /**
     * 获取请求中的数值float
     * 
     * @author wubin
     * @param request
     * @param name
     * @param defaultValue
     * @return
     */
    public static float getFloat(HttpServletRequest request, String name, float defaultValue) {
        String value = getRequestValue(request, name);
        float r = 0;
        if (value != null) {
            try {
                r = Float.parseFloat(value);
            } catch (Exception e) {
                r = defaultValue;
            }
        } else {
            r = defaultValue;
        }
        return r;
    }

    private static String getRequestValue(HttpServletRequest request, String name) {

        String value = null;
        try {
            value = request.getParameter(name);
        } catch (Exception e) {
            value = null;
        }
        return (value);
    }

    /**
     * 获取所有ip
     * 
     * @param request
     * @return
     */
    public final static String getIpAddr(HttpServletRequest request) {
        if (request == null)
            return "";
        String ip = request.getHeader("X-Requested-For");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    /**
     * 获取第一个ip
     * 
     * @param request
     * @return
     */
    public final static String getFirstIpAddr(HttpServletRequest request) {
        String ipAll = getIpAddr(request);
        String firstIp = null;
        if (StringUtils.isNotEmpty(ipAll)) {
            String[] ips = ipAll.split(",");
            firstIp = ips[0];
        }
        return firstIp;
    }

    // 打印header
    @SuppressWarnings("rawtypes")
    public static void printHeader(HttpServletRequest request) {
        Enumeration names = request.getHeaderNames();
        StringBuilder sb = new StringBuilder("headerInfo---");
        while (names.hasMoreElements()) {
            String name = names.nextElement().toString();
            Enumeration headers = request.getHeaders(name);
            sb.append(name).append(":");
            while (headers.hasMoreElements()) {
                sb.append(headers.nextElement()).append(" ");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    public static String getBasePath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + "/";
    }

    // 判断一个请求是否是Ajax请求
    public static boolean isAjaxRequest(HttpServletRequest request) {
        return request != null && "XMLHttpRequest".equals(request.getHeader("x-requested-with"));
    }

    /**
     * 取相对于 request.getContextPath() 后面的路径
     * 
     * @param request
     * @return
     */
    public static String getRelativeContextPath(HttpServletRequest request) {
        String uri = request.getServletPath();
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 0) {
            uri = uri + pathInfo;
        }
        return uri;
    }

    /**
     * 设置cookie
     * @author wubin
     * @param response
     * @param domain
     * @param cookieName
     * @param cookiePath
     * @param expiry
     */
    public static void setCookie(HttpServletResponse response, String domain, String cookieName,String cookieValue, String cookiePath,
            Integer expiry) {
        if (StringUtils.isBlank(cookieName)) {
            return;
        }
        
        Cookie cookie = new Cookie(cookieName, cookieValue);
        
        if(!StringUtils.isBlank(domain)){
            cookie.setDomain(domain);
        }
        
       if(!StringUtils.isBlank(cookiePath)){
           cookie.setPath(cookiePath);
       }

        if (expiry == null) {
            cookie.setMaxAge(-1);
        }else{
            cookie.setMaxAge(expiry);
        }
        
        response.addCookie(cookie);
    }
    
    /**
     * 获取cookiename对应的值
     * @author wubin
     * @param request
     * @param cookieName
     * @return
     */
    public static String getFromCookie(HttpServletRequest request,String cookieName){
        if(StringUtils.isEmpty(cookieName)){
            return null;
        }
        
        String cookieV = null;
        
          Cookie[] cookies = request.getCookies();
          if (null != cookies)
          {
              for (int i = 0; i < cookies.length; i++)
              {
                  if (cookieName.equals(cookies[i].getName()))
                  {
                      cookieV = cookies[i].getValue();
                      break;
                  }
              }
          }
          
          return cookieV;
    }
    
    
    /**
     * 从cookie中清空
     * @return
     */
    
    public static void clearCookie(HttpServletResponse response,String domain,String cookieName,String cookiePath){
          Cookie cookie = new Cookie(cookieName, null);
          cookie.setMaxAge(0);
          if(!StringUtils.isEmpty(cookiePath)){
              cookie.setPath(cookiePath);
          }else{
              cookie.setPath("/");
          }
          if(!StringUtils.isEmpty(domain)){
              cookie.setDomain(domain);
          }
          response.addCookie(cookie);
    }
    
  
    /**
     * 获取ip地址
     * @author wubin
     * @return
     */
    public static String getLocalHostIp(){  
        InetAddress netAddress = getInetAddress();
        if(null == netAddress){  
            return null;  
        }  
        String ip = netAddress.getHostAddress(); //get the ip address  
        return ip;  
    }  
    /**
     * 获取主机名
     * @author wubin
     * @return
     */
    public static String getLocalHostName(){
        InetAddress netAddress = getInetAddress();
        if(null == netAddress){  
            return null;  
        }  
        String name = netAddress.getHostName(); //get the host address  
        return name;  
    }
    

    private static InetAddress getInetAddress(){  
        
        try{  
            return InetAddress.getLocalHost();  
        }catch(UnknownHostException e){  
            System.out.println("unknown host!");  
        }  
        return null;  
  
    }  
    
}
