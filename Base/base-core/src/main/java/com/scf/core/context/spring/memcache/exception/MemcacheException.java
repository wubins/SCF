
/*   
 * @Title: MemcacheException.java 
 * @Package: com.scf.core.context.spring.memcache.exception 
 * @author wubin  
 * @date 2016年8月2日 上午11:17:23 
 * @version 1.3.1 
 */


package com.scf.core.context.spring.memcache.exception;

import com.scf.core.exception.ExCode;
import com.scf.core.exception.sys.SystemException;

/** 
 * @author wubin
 * @date 2016年8月2日 上午11:17:23 
 * @version V1.1.0
 */

public class MemcacheException extends SystemException {


    
    /** @Fields serialVersionUID: */
        
    private static final long serialVersionUID = 3123363884205879412L;

    
    public MemcacheException(int code,String message, Throwable cause) {
        super(code,message,cause);
    }
    
    
    
    public MemcacheException(int code,String message) {
        super(code,message);
    }
    /**
     * 默认配置异常
     * @author wubin
     * @param message 后台日志shortmessage
     * @param cause
     * @return
     */
    public static MemcacheException defaultException(Throwable cause){
        MemcacheException memcacheException = new MemcacheException(ExCode.MEMCACHED_001,ExCode.MEMCACHED_001+"",cause);
        return memcacheException;
    }
    
    /**
     * memcache未开启异常
     * @author wubin
     * @param message
     * @param cause
     * @return
     */
    public static MemcacheException notOpenException(){
        MemcacheException memcacheException = new MemcacheException(ExCode.MEMCACHED_002,"memcache未开启");
        return memcacheException;
    }
    
   

}
