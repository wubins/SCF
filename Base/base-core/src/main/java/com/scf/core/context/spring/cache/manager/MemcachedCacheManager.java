package com.scf.core.context.spring.cache.manager;

import java.util.Collection;

import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;

/**
 * MemcachedCache管理器类
 * 具有事务感知
 * @author wubin
 *
 */
public class MemcachedCacheManager extends AbstractTransactionSupportingCacheManager {

	private Collection<? extends Cache> caches;
	
	public void setCaches(Collection<? extends Cache> caches) {
		this.caches = caches;
	}

	@Override
	protected Collection<? extends Cache> loadCaches() {
		return this.caches;
	}
	
	@Override
	public void afterPropertiesSet(){
		super.afterPropertiesSet();
		initCache();
	}
	/**
	 * 初始化缓存
	 */
	protected void initCache(){
		
	}

	  
}
	  
