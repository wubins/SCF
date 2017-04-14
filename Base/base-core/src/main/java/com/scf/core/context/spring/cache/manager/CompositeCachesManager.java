package com.scf.core.context.spring.cache.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
/**
 * MemcachedCache组合管理器类
 * 具有缓存穿透功能
 * @author wubin
 *
 */
public class CompositeCachesManager implements CacheManager, InitializingBean {

	private final List<CacheManager> cacheManagers = new ArrayList<CacheManager>();

	/**
	 * 穿透缓存开关:false表示穿透，true表示不穿透
	 */
	private boolean cacheEnable = false;


	public void setCacheManagers(Collection<CacheManager> cacheManagers) {
		this.cacheManagers.clear();  
		this.cacheManagers.addAll(cacheManagers);
	}

	public void setCacheEnable(boolean cacheEnable) {
		this.cacheEnable = cacheEnable;
	}

	public void afterPropertiesSet() {
		if (!this.cacheEnable) {
			this.cacheManagers.add(0,new NoOpCacheManager());
		}
	}


	public Cache getCache(String name) {
		for (CacheManager cacheManager : this.cacheManagers) {
			Cache cache = cacheManager.getCache(name);
			if (cache != null) {
				return cache;
			}
		}
		return null;
	}

	public Collection<String> getCacheNames() {
		Set<String> names = new LinkedHashSet<String>();
		for (CacheManager manager : this.cacheManagers) {
			names.addAll(manager.getCacheNames());
		}
		return Collections.unmodifiableSet(names);
	}

}
