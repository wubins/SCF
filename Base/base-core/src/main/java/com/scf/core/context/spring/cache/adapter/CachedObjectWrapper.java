package com.scf.core.context.spring.cache.adapter;

import net.rubyeye.xmemcached.transcoders.CachedData;

import com.google.code.ssm.providers.CachedObject;

/**
 * 缓存数据适配器(可以适配JAVA，JSON，PROVIDER类型的缓存数据)，用于序列化和反序列化
 * @author wub
 *
 */
public class CachedObjectWrapper  implements CachedObject {

    private final CachedData cachedData;

    public  CachedObjectWrapper(final CachedData cachedData) {
        this.cachedData = cachedData;
    }

    @Override
    public byte[] getData() {
        return cachedData.getData();
    }

    @Override
    public int getFlags() {
        return cachedData.getFlag();
    }

    @Override
    public String toString() {
        return "CachedObjectWrapper [cachedData=" + cachedData + "]";
    }


}
