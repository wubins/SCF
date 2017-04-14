package com.scf.core.context.spring.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.scf.core.exception.AppException;
import com.scf.core.exception.ExCode;
/**
 * 缓存上下文
 * 
 * @author wubin
 */
public class CacheContext {
	
	  private static Logger logger = LoggerFactory.getLogger(CacheContext.class);
	  /**
	   * 缓存名称
	   */
	   private static  String cacheName;
	    
	    /**
	     * 缓存管理器
	     */
	    private static CacheManager cacheManager;
	    
	    
	    /**
	     * 获取缓存
	     * @return
	     */
	    private static Cache getDefaultCache(){
	    	return getCache(cacheName);
	    }
	    
	    /**
	     * 往缓存中设置值
	     * @author wubin
	     * @param key
	     * @param value
	     */
	    public static void put(String key,Object value){
	        getDefaultCache().put(key, value);
	    }
	    
	    
	    /**
         * 从缓存中获取值
         * @author wubin
         * @param key
         * @param value
         */
	    @SuppressWarnings("unchecked")
        public static  <T> T get(String key){
	        return (T)getDefaultCache().get(key).get();
        }
	    
	    /**
	     * 通过缓存名称获取缓存
	     * @return
	     */
	    public static Cache getCache(String name){
	    	Cache cache =  (Cache)getCacheManager().getCache(cacheName);
	     	if(cache == null){
	    		logger.error("获取Cache缓存为空，请确定缓存\""+cacheName+"\"是否正确配置！！！");
	    		throw new AppException(ExCode.CACHE_003);
	    	}
	    	return cache;
	    }
	    
	    
	    

		private static CacheManager getCacheManager() {
		   	if(cacheManager == null){
	    		logger.error("获取CacheManager缓存管理器为空，请确定缓存管理器是否正确配置！！！");
	    		throw new AppException(ExCode.CACHE_003);
	    	}
			return cacheManager;
		}


		public static void setCacheManager(CacheManager cacheManager) {
			CacheContext.cacheManager = cacheManager;
		}


		public static String getCacheName() {
			return cacheName;
		}


		public static void setCacheName(String cacheName) {
			CacheContext.cacheName = cacheName;
		}

	
}
