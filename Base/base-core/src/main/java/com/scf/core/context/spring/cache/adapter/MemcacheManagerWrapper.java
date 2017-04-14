package com.scf.core.context.spring.cache.adapter;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.ssm.providers.AbstractMemcacheClientWrapper;
import com.google.code.ssm.providers.CacheException;
import com.google.code.ssm.providers.CacheTranscoder;
import com.google.code.ssm.providers.CachedObject;
import com.google.code.ssm.providers.CachedObjectImpl;
import com.scf.core.context.spring.memcache.cleint.MemCachedOperation;

import net.rubyeye.xmemcached.transcoders.CachedData;
import net.rubyeye.xmemcached.transcoders.Transcoder;

/**
 * MemcacheManager包装类，实现了CacheClient
 * @author wubin
 * @date 2016年7月27日 下午3:25:04 
 * @version V1.1.0
 */
public class MemcacheManagerWrapper extends AbstractMemcacheClientWrapper {


    private static final Logger LOGGER = LoggerFactory.getLogger(MemcacheManagerWrapper.class);

    private final Map<CacheTranscoder, Object> adapters = new HashMap<CacheTranscoder, Object>();

    private  MemCachedOperation memCachedManager;


    @Override
    public boolean add(final String key, final int exp, final Object value) throws TimeoutException, CacheException {
     /*   try {
            return memCachedManager.add(key, exp, value);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        } catch (InterruptedException e) {
            throw new CacheException(e);
        }*/
  	   return memCachedManager.setStringIfNotExists(key, exp, value,true);
    }

    @Override
    public <T> boolean add(final String key, final int exp, final T value, final CacheTranscoder transcoder) throws TimeoutException,
            CacheException {
      /*  try {
            return memCachedManager.add(key, exp, value, getTranscoder(transcoder));
        } catch (MemcachedException e) {
            throw new CacheException(e);
        } catch (InterruptedException e) {
            throw new CacheException(e);
        }*/
    	return memCachedManager.setStringIfNotExists(key, exp, value,true);
    }

    @Override
    public long decr(final String key, final int by) throws TimeoutException, CacheException {
      /*  try {
            return memCachedManager.decr(key, by);
        } catch (InterruptedException e) {
            throw new CacheException(e);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        }*/
    	throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public long decr(final String key, final int by, final long def) throws TimeoutException, CacheException {
 /*       try {
            return memCachedManager.decr(key, by, def);
        } catch (InterruptedException e) {
            throw new CacheException(e);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        }*/
    	throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public boolean delete(final String key) throws TimeoutException, CacheException {
       return memCachedManager.del(key,true);
    }

    @Override
    public void flush() throws TimeoutException, CacheException {
          memCachedManager.flushAll();
    }

    @Override
    public Object get(final String key) throws TimeoutException, CacheException {
            return memCachedManager.get(key,true);
    }

    @Override
    public <T> T get(final String key, final CacheTranscoder transcoder) throws TimeoutException, CacheException {
            //return memCachedManager.get(key, this.<T> getTranscoder(transcoder));
    	throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public <T> T get(final String key, final CacheTranscoder transcoder, final long timeout) throws TimeoutException, CacheException {
       /* try {
            return memCachedManager.get(key, timeout, this.<T> getTranscoder(transcoder));
        } catch (InterruptedException e) {
            throw new CacheException(e);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        }*/
    	throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Collection<SocketAddress> getAvailableServers() {
        /*List<SocketAddress> servers = new ArrayList<SocketAddress>();
        Collection<InetSocketAddress> inetSocketAddresses = memCachedManager.getAvailableServers();

        if (inetSocketAddresses != null && !inetSocketAddresses.isEmpty()) {
            servers.addAll(inetSocketAddresses);
        }

        return servers;*/
    	throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getBulk(final Collection<String> keys) throws TimeoutException, CacheException {
      /*  Map<String, Object> result = null;
        try {
            result = memCachedManager.get(keys);
            return (result == null) ? Collections.<String, Object> emptyMap() : result;
        } catch (InterruptedException e) {
            throw new CacheException(e);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        }*/
    	throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public <T> Map<String, T> getBulk(final Collection<String> keys, final CacheTranscoder transcoder) throws TimeoutException,
            CacheException {
       /* Map<String, T> result = null;
        try {
            result = memCachedManager.get(keys, this.<T> getTranscoder(transcoder));
            return (result == null) ? Collections.<String, T> emptyMap() : result;
        } catch (InterruptedException e) {
            throw new CacheException(e);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        }*/
    	throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public long incr(final String key, final int by) throws TimeoutException, CacheException {
     /*   try {
            return memCachedManager.incr(key, by);
        } catch (InterruptedException e) {
            throw new CacheException(e);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        }*/
    	throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public long incr(final String key, final int by, final long def) throws TimeoutException, CacheException {
       /* try {
            return memCachedManager.incr(key, by, def);
        } catch (InterruptedException e) {
            throw new CacheException(e);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        }*/
    	
    	throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public long incr(final String key, final int by, final long def, final int expiration) throws TimeoutException, CacheException {
      /*  try {
            return memCachedManager.incr(key, by, def, memCachedManager.getOpTimeout(), expiration);
        } catch (InterruptedException e) {
            throw new CacheException(e);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        }*/
    	
    	throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public boolean set(final String key, final int exp, final Object value) throws TimeoutException, CacheException {
      /*  try {
            return memCachedManager.set(key, exp, value);
        } catch (InterruptedException e) {
            throw new CacheException(e);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        }*/
    	return memCachedManager.set(key,value,exp,true);
    }

    @Override
    public <T> boolean set(final String key, final int exp, final T value, final CacheTranscoder transcoder) throws TimeoutException,
            CacheException {
       /* try {
            return memCachedManager.set(key, exp, value, getTranscoder(transcoder));
        } catch (InterruptedException e) {
            throw new CacheException(e);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        }*/
    	throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void shutdown() {
    /*    try {
            memCachedManager.shutdown();
        } catch (IOException e) {
            LOGGER.error("An error occurred when closing memcache", e);
        }*/
    }

    @Override
    @SuppressWarnings("unchecked")
    public CacheTranscoder getTranscoder() {
        return new TranscoderWrapper(memCachedManager.getTranscoder());
    }

    @Override
    public Object getNativeClient() {
        return memCachedManager;
    }

    @SuppressWarnings("unchecked")
    private <T> Transcoder<T> getTranscoder(final CacheTranscoder transcoder) {
        Transcoder<T> transcoderAdapter = (Transcoder<T>) adapters.get(transcoder);
        if (transcoderAdapter == null) {
            transcoderAdapter = (Transcoder<T>) new TranscoderAdapter(transcoder);
            adapters.put(transcoder, transcoderAdapter);
        }

        return transcoderAdapter;
    }

    private static class TranscoderWrapper implements CacheTranscoder {

        private final Transcoder<Object> transcoder;

        public TranscoderWrapper(final Transcoder<Object> transcoder) {
            this.transcoder = transcoder;
        }

        @Override
        public Object decode(final CachedObject data) {
            return transcoder.decode(new CachedData(data.getFlags(), data.getData()));
        }

        @Override
        public CachedObject encode(final Object o) {
            CachedData cachedData = transcoder.encode(o);
            return new CachedObjectImpl(cachedData.getFlag(), cachedData.getData());
        }
    }


}
