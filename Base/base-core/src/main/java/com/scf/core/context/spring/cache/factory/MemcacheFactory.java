package com.scf.core.context.spring.cache.factory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.google.code.ssm.Cache;
import com.google.code.ssm.CacheProperties;
import com.google.code.ssm.api.AnnotationConstants;
import com.google.code.ssm.api.format.SerializationType;
import com.google.code.ssm.mapper.JsonObjectMapper;
import com.google.code.ssm.providers.CacheClient;
import com.google.code.ssm.providers.CacheClientFactory;
import com.google.code.ssm.providers.CacheConfiguration;
import com.google.code.ssm.providers.CacheTranscoder;
import com.google.code.ssm.transcoders.JavaTranscoder;
import com.google.code.ssm.transcoders.JsonTranscoder;
import com.scf.core.context.spring.cache.CacheImpl;
import com.scf.core.context.spring.cache.adapter.MemcacheManagerWrapper;
/**
 * MemcacheFactoryBean 工厂bean
 * @author wubin
 *
 */
public class MemcacheFactory implements  FactoryBean<Cache>, InitializingBean {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(MemcacheFactory.class);

    private CacheConfiguration configuration = new CacheConfiguration();

    private CacheClientFactory cacheClientFactory;

    private String cacheName = AnnotationConstants.DEFAULT_CACHE_NAME;

    private Collection<String> cacheAliases = Collections.emptyList();

    private Cache cache;

    private SerializationType defaultSerializationType = SerializationType.PROVIDER;

    private JsonTranscoder jsonTranscoder;

    private JavaTranscoder javaTranscoder;

    private CacheTranscoder customTranscoder;

    private boolean initializeTranscoders = true;
    

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(configuration, "'configuration' cannot be null");
        Assert.notNull(cacheName, "'cacheName' cannot be null");
        Assert.notNull(defaultSerializationType, "'defaultSerializationType' cannot be null");

        if (initializeTranscoders) {
            if (jsonTranscoder == null) {
                jsonTranscoder = new JsonTranscoder(new JsonObjectMapper());
            }
            if (javaTranscoder == null) {
                javaTranscoder = new JavaTranscoder();
            }
        }

        validateTranscoder(SerializationType.JSON, jsonTranscoder, "jsonTranscoder");
        validateTranscoder(SerializationType.JAVA, javaTranscoder, "javaTranscoder");
        validateTranscoder(SerializationType.CUSTOM, customTranscoder, "customTranscoder");

    }
    @Override
    public Cache getObject() throws Exception {
    	Cache cache = createCache();
        return  cache;
    }

    @Override
    public Class<?> getObjectType() {
        return Cache.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * 创建缓存
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
	protected Cache createCache() throws IOException {
    	
        if (cache != null) {
            throw new IllegalStateException(String.format("This factory has already created memcached client for cache %s", cacheName));
        }

        if (configuration == null) {
            throw new RuntimeException(String.format("The MemcachedConnectionBean for cache %s must be defined!", cacheName));
        }

        List<InetSocketAddress> addrs  = Collections.EMPTY_LIST;
        cache = new CacheImpl(cacheName, cacheAliases, createClient(addrs), defaultSerializationType, jsonTranscoder, javaTranscoder,
                customTranscoder, new CacheProperties(configuration.isUseNameAsKeyPrefix(), configuration.getKeyPrefixSeparator()));
        LOGGER.info("====创建缓存成功====");
        return cache;
    }
    


    private CacheClient createClient(final List<InetSocketAddress> addrs) throws IOException {
    	
    	return new MemcacheManagerWrapper();
    }

    private void validateTranscoder(final SerializationType serializationType, final CacheTranscoder cacheTranscoder,
            final String transcoderName) {
        if (defaultSerializationType == serializationType) {
            Assert.notNull(cacheTranscoder,
                    String.format("'%s' cannot be null if default serialization type is set to %s", transcoderName, serializationType));
        }
    }

	public CacheConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(CacheConfiguration configuration) {
		this.configuration = configuration;
	}

	public CacheClientFactory getCacheClientFactory() {
		return cacheClientFactory;
	}

	public void setCacheClientFactory(CacheClientFactory cacheClientFactory) {
		this.cacheClientFactory = cacheClientFactory;
	}

	public String getCacheName() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	public Collection<String> getCacheAliases() {
		return cacheAliases;
	}

	public void setCacheAliases(Collection<String> cacheAliases) {
		this.cacheAliases = cacheAliases;
	}

	public Cache getCache() {
		return cache;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public SerializationType getDefaultSerializationType() {
		return defaultSerializationType;
	}

	public void setDefaultSerializationType(
			SerializationType defaultSerializationType) {
		this.defaultSerializationType = defaultSerializationType;
	}

	public JsonTranscoder getJsonTranscoder() {
		return jsonTranscoder;
	}

	public void setJsonTranscoder(JsonTranscoder jsonTranscoder) {
		this.jsonTranscoder = jsonTranscoder;
	}

	public JavaTranscoder getJavaTranscoder() {
		return javaTranscoder;
	}

	public void setJavaTranscoder(JavaTranscoder javaTranscoder) {
		this.javaTranscoder = javaTranscoder;
	}

	public CacheTranscoder getCustomTranscoder() {
		return customTranscoder;
	}

	public void setCustomTranscoder(CacheTranscoder customTranscoder) {
		this.customTranscoder = customTranscoder;
	}

	public boolean isInitializeTranscoders() {
		return initializeTranscoders;
	}

	public void setInitializeTranscoders(boolean initializeTranscoders) {
		this.initializeTranscoders = initializeTranscoders;
	}


}
