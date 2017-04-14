package com.scf.core.context.spring.cache.adapter;

import net.rubyeye.xmemcached.transcoders.CachedData;
import net.rubyeye.xmemcached.transcoders.CompressionMode;
import net.rubyeye.xmemcached.transcoders.Transcoder;

import com.google.code.ssm.providers.CacheTranscoder;
import com.google.code.ssm.providers.CachedObject;

/**
 * Transcoder序列化适配器，可以适配JAVA，JSON，PROVIDER
 * 
 * @author wubin
 *
 */
public class TranscoderAdapter implements Transcoder<Object> {

    private final CacheTranscoder transcoder;

    public TranscoderAdapter(final CacheTranscoder transcoder) {
        this.transcoder = transcoder;
    }

    @Override
    public Object decode(final CachedData d) {
        return transcoder.decode(new CachedObjectWrapper(d));
    }

    @Override
    public CachedData encode(final Object o) {
        CachedObject cachedObject = transcoder.encode(o);
        return new CachedData(cachedObject.getFlags(), cachedObject.getData());
    }

    @Override
    public boolean isPackZeros() {
        throw new UnsupportedOperationException("TranscoderAdapter doesn't support pack zeros");
    }

    @Override
    public boolean isPrimitiveAsString() {
        return false;
    }

    @Override
    public void setCompressionThreshold(final int compressionThreshold) {
        throw new UnsupportedOperationException("TranscoderAdapter doesn't support compression threshold");
    }

    @Override
    public void setPackZeros(final boolean packZeros) {
        throw new UnsupportedOperationException("TranscoderAdapter doesn't support pack zeros");
    }

    @Override
    public void setPrimitiveAsString(final boolean primitiveAsString) {
        throw new UnsupportedOperationException("TranscoderAdapter doesn't support primitive as string");
    }

    @Override
    public void setCompressionMode(final CompressionMode compressionMode) {
        throw new UnsupportedOperationException("TranscoderAdapter doesn't support compression mode");
    }


}
