package com.scf.core.context.spring.cache;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.google.code.ssm.Cache;
import com.google.code.ssm.CacheProperties;
import com.google.code.ssm.api.format.SerializationType;
import com.google.code.ssm.providers.CacheClient;
import com.google.code.ssm.providers.CacheException;
import com.google.code.ssm.providers.CacheTranscoder;
import com.google.code.ssm.transcoders.JavaTranscoder;
import com.google.code.ssm.transcoders.JsonTranscoder;
import com.google.code.ssm.transcoders.LongToStringTranscoder;
/**
 *  memcache缓存实现
 * @author wubin
 *
 */
public class CacheImpl implements Cache {

    private static final Logger          LOGGER                 = LoggerFactory.getLogger(CacheImpl.class);

    public static final String           CACHE_SEPARATOR        = "_";

    private final String                 name;

    private final Collection<String>     aliases;

    private final CacheProperties        properties;

    private final SerializationType      defaultSerializationType;

    private final JsonTranscoder         jsonTranscoder;

    private final JavaTranscoder         javaTranscoder;

    private final LongToStringTranscoder longToStringTranscoder = new LongToStringTranscoder();

    private final CacheTranscoder        customTranscoder;

    private volatile CacheClient         cacheClient;

    /**
     * 缓存环境：dev\test，正式环境可以不配置
     */
    private String                       environment;

    public CacheImpl(final String name, final Collection<String> aliases, final CacheClient cacheClient, final SerializationType defaultSerializationType,
                     final JsonTranscoder jsonTranscoder, final JavaTranscoder javaTranscoder, final CacheTranscoder customTranscoder, final CacheProperties properties) {
        Assert.hasText(name, "'name' must not be null, empty, or blank");
        Assert.notNull(aliases, "'aliases' cannot be null");
        Assert.notNull(cacheClient, "'cacheClient' cannot be null");
        Assert.notNull(defaultSerializationType, "'defaultSerializationType' cannot be null");
        Assert.notNull(properties, "'cacheProperties' cannot be null");
        validateTranscoder(SerializationType.JSON, jsonTranscoder, "jsonTranscoder");
        validateTranscoder(SerializationType.JAVA, javaTranscoder, "javaTranscoder");
        validateTranscoder(SerializationType.CUSTOM, customTranscoder, "customTranscoder");

        this.name = name;
        this.aliases = aliases;
        this.cacheClient = cacheClient;
        this.defaultSerializationType = defaultSerializationType;
        this.jsonTranscoder = jsonTranscoder;
        this.javaTranscoder = javaTranscoder;
        this.customTranscoder = customTranscoder;
        this.properties = properties;
    }

    @Override
    public Collection<SocketAddress> getAvailableServers() {
        return cacheClient.getAvailableServers();
    }

    @Override
    public <T> T get(final String cacheKey, final SerializationType serializationType) throws TimeoutException, CacheException {

        switch (getSerializationType(serializationType)) {
            case JAVA:
                return get(cacheKey, SerializationType.JAVA, javaTranscoder);
            case JSON:
                return get(cacheKey, SerializationType.JSON, jsonTranscoder);
            case PROVIDER:
                return get(cacheKey, SerializationType.PROVIDER, null);
            case CUSTOM:
                return get(cacheKey, SerializationType.CUSTOM, customTranscoder);
            default:
                throw new IllegalArgumentException(String.format("Serialization type %s is not supported", serializationType));
        }

    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void set(final String cacheKey, final int expiration, final Object value, final SerializationType serializationType) throws TimeoutException, CacheException {

        switch (getSerializationType(serializationType)) {
            case JAVA:
                set(cacheKey, expiration, (T) value, SerializationType.JAVA, javaTranscoder);
                break;
            case JSON:
                set(cacheKey, expiration, (T) value, SerializationType.JSON, jsonTranscoder);
                break;
            case PROVIDER:
                set(cacheKey, expiration, (T) value, SerializationType.PROVIDER, null);
                break;
            case CUSTOM:
                set(cacheKey, expiration, (T) value, SerializationType.CUSTOM, customTranscoder);
                break;
            default:
                throw new IllegalArgumentException(String.format("Serialization type %s is not supported", serializationType));
        }
    }

    @Override
    public <T> void setSilently(final String cacheKey, final int expiration, final Object value, final SerializationType serializationType) {
        try {
            set(cacheKey, expiration, value, serializationType);
        } catch (TimeoutException e) {
            warn(e, "Cannot set on key %s", cacheKey);
        } catch (CacheException e) {
            warn(e, "Cannot set on key %s", cacheKey);
        }
    }

    @Override
    public <T> void add(final String cacheKey, final int expiration, final Object value, final SerializationType serializationType) throws TimeoutException, CacheException {

        switch (getSerializationType(serializationType)) {
            case JAVA:
                add(cacheKey, expiration, value, SerializationType.JAVA, javaTranscoder);
                break;
            case JSON:
                add(cacheKey, expiration, value, SerializationType.JSON, jsonTranscoder);
                break;
            case PROVIDER:
                add(cacheKey, expiration, value, SerializationType.PROVIDER, null);
                break;
            case CUSTOM:
                add(cacheKey, expiration, value, SerializationType.CUSTOM, customTranscoder);
                break;
            default:
                throw new IllegalArgumentException(String.format("Serialization type %s is not supported", serializationType));
        }
    }

    @Override
    public <T> void addSilently(final String cacheKey, final int expiration, final Object value, final SerializationType serializationType) {
        try {
            add(cacheKey, expiration, value, serializationType);
        } catch (TimeoutException e) {
            warn(e, "Cannot add to key %s", cacheKey);
        } catch (CacheException e) {
            warn(e, "Cannot add to key %s", cacheKey);
        }
    }

    @Override
    public Map<String, Object> getBulk(final Collection<String> keys, final SerializationType serializationType) throws TimeoutException, CacheException {

        switch (getSerializationType(serializationType)) {
            case JAVA:
                return getBulk(keys, SerializationType.JAVA, javaTranscoder);
            case JSON:
                return getBulk(keys, SerializationType.JSON, jsonTranscoder);
            case PROVIDER:
                return getBulk(keys, SerializationType.PROVIDER, null);
            case CUSTOM:
                return getBulk(keys, SerializationType.CUSTOM, customTranscoder);
            default:
                throw new IllegalArgumentException(String.format("Serialization type %s is not supported", serializationType));
        }
    }

    @Override
    public long decr(final String key, final int by) throws TimeoutException, CacheException {
        return cacheClient.decr(getPrefixKey(key), by);
    }

    @Override
    public boolean delete(final String key) throws TimeoutException, CacheException {
        return cacheClient.delete(getPrefixKey(key));
    }

    @Override
    public void delete(final Collection<String> keys) throws TimeoutException, CacheException {
        cacheClient.delete(getPrefixKey(keys));
    }

    @Override
    public void flush() throws TimeoutException, CacheException {
        cacheClient.flush();
    }

    @Override
    public long incr(final String key, final int by, final long def) throws TimeoutException, CacheException {
        return cacheClient.incr(getPrefixKey(key), by, def);
    }

    @Override
    public long incr(final String key, final int by, final long def, final int exp) throws TimeoutException, CacheException {
        return cacheClient.incr(getPrefixKey(key), by, def, exp);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Long getCounter(final String cacheKey) throws TimeoutException, CacheException {
        return cacheClient.get(getPrefixKey(cacheKey), longToStringTranscoder);
    }

    @Override
    public void setCounter(final String cacheKey, final int expiration, final long value) throws TimeoutException, CacheException {
        cacheClient.set(getPrefixKey(cacheKey), expiration, value, longToStringTranscoder);
    }

    @Override
    public void shutdown() {
        cacheClient.shutdown();
    }

    @Override
    public Object getNativeClient() {
        return cacheClient.getNativeClient();
    }

    public void changeCacheClient(final CacheClient newCacheClient) {
        if (newCacheClient != null) {
            LOGGER.info("Replacing the cache client");
            CacheClient oldCacheClient = cacheClient;
            cacheClient = newCacheClient;
            LOGGER.info("Cache client replaced");
            LOGGER.info("Closing old cache client");
            oldCacheClient.shutdown();
            LOGGER.info("Old cache client closed");
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T get(final String cacheKey, final SerializationType serializationType, final CacheTranscoder cacheTranscoder) throws TimeoutException, CacheException {
        if (SerializationType.PROVIDER.equals(serializationType)) {
            return (T) cacheClient.get(getPrefixKey(cacheKey));
        }

        if (cacheTranscoder == null) {
            throw new IllegalArgumentException(String.format("Cannot use %s serialization because dedicated cache transcoder is null!", serializationType));
        }

        return (T) cacheClient.get(getPrefixKey(cacheKey), cacheTranscoder);
    }

    /**
     * 给缓存key加上前缀key:ECS_dev_orderCache_100000001
     * ECS和dev也就是项目和环境，这些是在scf初始化的时候指定的
     * @param cacheKey
     * @return
     */
    private String getPrefixKey(String cacheKey) {
        return CACHE_SEPARATOR + this.getName() + CACHE_SEPARATOR + cacheKey;
    }

    /**
     * 给数组缓存key加上前缀key:ECS_orderCache_100000001
     * @param cacheKey
     * @return
     */
    private Collection<String> getPrefixKey(Collection<String> keys) {
        Collection<String> prefixKeys = new ArrayList<String>();
        if (!CollectionUtils.isEmpty(keys)) {
            for (String key : keys) {
                prefixKeys.add(this.getName() + CACHE_SEPARATOR + key);
            }
        }

        return prefixKeys;
    }

    private <T> void set(final String cacheKey, final int expiration, final T value, final SerializationType serializationType, final CacheTranscoder cacheTranscoder)
                                                                                                                                                                      throws TimeoutException,
                                                                                                                                                                      CacheException {
        if (SerializationType.PROVIDER.equals(serializationType)) {
            cacheClient.set(getPrefixKey(cacheKey), expiration, value);
            return;
        }

        if (cacheTranscoder == null) {
            throw new IllegalArgumentException(String.format("Cannot use %s serialization because dedicated cache transcoder is null!", serializationType));
        }

        cacheClient.set(getPrefixKey(cacheKey), expiration, value, cacheTranscoder);
    }

    private <T> void add(final String cacheKey, final int expiration, final Object value, final SerializationType serializationType, final CacheTranscoder cacheTranscoder)
                                                                                                                                                                           throws TimeoutException,
                                                                                                                                                                           CacheException {
        if (SerializationType.PROVIDER.equals(serializationType)) {
            cacheClient.add(getPrefixKey(cacheKey), expiration, value);
            return;
        }

        if (cacheTranscoder == null) {
            throw new IllegalArgumentException(String.format("Cannot use %s serialization because dedicated cache transcoder is null!", serializationType));
        }

        cacheClient.add(getPrefixKey(cacheKey), expiration, value, cacheTranscoder);
    }

    private Map<String, Object> getBulk(final Collection<String> keys, final SerializationType serializationType, final CacheTranscoder cacheTranscoder) throws TimeoutException,
                                                                                                                                                        CacheException {
        if (SerializationType.PROVIDER.equals(serializationType)) {
            return cacheClient.getBulk(getPrefixKey(keys));
        }

        if (cacheTranscoder == null) {
            throw new IllegalArgumentException(String.format("Cannot use %s serialization because dedicated cache transcoder is null!", serializationType));
        }

        return cacheClient.getBulk(getPrefixKey(keys), cacheTranscoder);
    }

    private SerializationType getSerializationType(final SerializationType serializationType) {
        return (serializationType != null) ? serializationType : defaultSerializationType;
    }

    private void warn(final Exception e, final String format, final Object... args) {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn(String.format(format, args), e);
        }
    }

    private void validateTranscoder(final SerializationType serializationType, final CacheTranscoder cacheTranscoder, final String transcoderName) {
        if (defaultSerializationType == serializationType) {
            Assert.notNull(cacheTranscoder, String.format("'%s' cannot be null if default serialization type is set to %s", transcoderName, serializationType));
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<String> getAliases() {
        return aliases;
    }

    @Override
    public CacheProperties getProperties() {
        return properties;
    }

    public String getEnvironment() {
        return environment;
    }}
