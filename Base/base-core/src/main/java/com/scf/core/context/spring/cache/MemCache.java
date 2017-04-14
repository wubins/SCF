package com.scf.core.context.spring.cache;

import java.util.concurrent.TimeoutException;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import com.google.code.ssm.api.format.SerializationType;
import com.google.code.ssm.providers.CacheException;
/**
 * spring缓存的memcache实现适配器
 * @author wub
 *
 */
public class MemCache implements Cache{  

		private com.google.code.ssm.Cache cache;
		
		/**
		 * 过期时间
		 */
		private int expire;
		
		public com.google.code.ssm.Cache getCache() {
			return cache;
		}

		public void setCache(com.google.code.ssm.Cache cache) {
			this.cache = cache;
		}

		@Override
		public String getName() {
			return this.cache.getName();
		}

		@Override
		public Object getNativeCache() {
			return this.cache;
		}

		@Override
		public ValueWrapper get(Object key) {
			Object object = null;
			try {
				object = this.cache.get((String)key, SerializationType.PROVIDER);
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (com.google.code.ssm.providers.CacheException e) {
				e.printStackTrace();
			}
			return (object != null ? new SimpleValueWrapper(object) : null);
		}

		@Override
		public void put(Object key, Object value) {
			try {
				this.cache.set((String)key, expire==0?86400:expire, value, SerializationType.PROVIDER);
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (com.google.code.ssm.providers.CacheException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void evict(Object key) {
			try {
				this.cache.delete((String)key);
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (com.google.code.ssm.providers.CacheException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void clear() {
			try {
				this.cache.flush();
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (com.google.code.ssm.providers.CacheException e) {
				e.printStackTrace();
			}
		}

		public int getExpire() {
			return expire;
		}

		public void setExpire(int expire) {
			this.expire = expire;
		}

		@SuppressWarnings("unchecked")
        @Override
        public <T> T get(Object key, Class<T> type) {
			Object object = null;
			try {
	            object = this.cache.get((String)key, SerializationType.PROVIDER);
            } catch (TimeoutException e) {
	            e.printStackTrace();
            } catch (CacheException e) {
	            e.printStackTrace();
            }
			if (object != null && type != null && !type.isInstance(object)) {
				throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + object);
			}
			return (T) object;
        }
		



}
