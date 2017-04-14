package com.scf.core.context.spring.memcache.cleint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.scf.core.constant.CommonConstants;
import com.scf.core.context.spring.memcache.exception.MemcacheException;
import com.scf.core.exception.AppException;
import com.scf.core.exception.ExCode;
import com.scf.utils.ExceptionsUtilies;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.transcoders.Transcoder;

/**
 * MemCached操作类
 * 
 * @author wubin
 * @date 2016年7月27日 下午3:11:12 
 * @version V1.1.0
 */
public class MemCachedOperation {

    private static Logger logger = LoggerFactory.getLogger(MemCachedOperation.class);

    private static MemcachedClient mcc;

    private static int MAX_KEY_LEN = 250;

    public static final Integer RETRY_COUNT = 6;
    
    private static boolean enable;

    @SuppressWarnings("rawtypes")
    public Transcoder getTranscoder() {
        return mcc.getTranscoder();
    }

    // 具有过期时间的缓存
    public static boolean set(String key, Object value, int seconds, boolean multiSysShared) {
        boolean flag = false;
        boolean exceptionFlag = false;
        Exception e1 = null;
        for (Integer index = 0; index < RETRY_COUNT; index++) {
            try {
                flag = setSub(key, value, seconds, multiSysShared);
                break;
            } catch (Exception e) {
                if ((RETRY_COUNT - 1) == index) {
                    exceptionFlag = true;
                    e1 = e;
                }
            }
        }
        if (exceptionFlag) {
            throw MemcacheException.defaultException(e1);
        }
        return flag;
    }

    // 具有过期时间的缓存
    public static boolean set(String key, Object value, int seconds) {
        boolean flag = false;
        boolean exceptionFlag = false;
        Exception e1 = null;
        for (Integer index = 0; index < RETRY_COUNT; index++) {
            try {
                flag = setSub(key, value, seconds, false);
                break;
            } catch (Exception e) {
                if ((RETRY_COUNT - 1) == index) {
                    exceptionFlag = true;
                    e1 = e;
                }
            }
        }
        if (exceptionFlag) {
            throw MemcacheException.defaultException(e1);
        }
        return flag;
    }

    // 具有过期时间的缓存
    public static boolean setSub(String key, Object value, int seconds, boolean multiSysShared) {

        key = getKey(key, multiSysShared);
        boolean flag = false;
        try {
            flag = getMemcachedClient().set(key, seconds, value);
        } catch (Exception e) {
            logger.error("缓存set报错,key:{},原因:{}", key, ExceptionsUtilies.exceptionToString(e));
            throw MemcacheException.defaultException(e);
        }
        return flag;

    }

    // 永不过期
    public static boolean set(String key, Object value) {
        return set(key, value, 0);
    }

    public static Object get(String key) {
        return get(key, false);
    }

    public static Object get(String key, boolean multiSysShared) {
        Object obj = null;
        boolean exceptionFlag = false;
        Exception e1 = null;
        for (Integer index = 0; index < RETRY_COUNT; index++) {
            try {
                obj = getSub(key, multiSysShared);
                break;
            } catch (Exception e) {
                if ((RETRY_COUNT - 1) == index) {
                    exceptionFlag = true;
                    e1 = e;
                }
            }
        }
        if (exceptionFlag) {
            throw MemcacheException.defaultException(e1);
        }
        return obj;
    }

    public static Object getSub(String key, boolean multiSysShared) {
        key = getKey(key, multiSysShared);
        Object obj = null;
        try {
            obj = getMemcachedClient().get(key);
        } catch (Exception e) {
            logger.error("缓存get报错,key:{},原因:{}", key, ExceptionsUtilies.exceptionToString(e));
            throw MemcacheException.defaultException(e);
        }
        return obj;
    }

    public static boolean del(String key) {
        return del(key, false);
    }

    public static boolean del(String key, boolean multiSysShared) {
        boolean flag = false;
        boolean exceptionFlag = false;
        Exception e1 = null;
        for (Integer index = 0; index < RETRY_COUNT; index++) {
            try {
                flag = delSub(key, multiSysShared);
                break;
            } catch (Exception e) {
                if ((RETRY_COUNT - 1) == index) {
                    exceptionFlag = true;
                    e1 = e;
                }
            }
        }
        if (exceptionFlag) {
            throw MemcacheException.defaultException(e1);
        }
        return flag;
    }

    public static boolean delSub(String key, boolean multiSysShared) {

        key = getKey(key, multiSysShared);
        boolean flag = false;
        try {
            flag = getMemcachedClient().delete(key);
        } catch (Exception e) {
            logger.error("缓存del报错,key:{},原因:{}", key, ExceptionsUtilies.exceptionToString(e));
            throw MemcacheException.defaultException(e);
        }
        return flag;

    }

    public static void flushAll() {
        try {
            getMemcachedClient().flushAll();
        } catch (Exception e) {
            logger.error("缓存flushAll报错,原因:{}", ExceptionsUtilies.exceptionToString(e));
            throw MemcacheException.defaultException(e);
        }
    }

    public static long getCounter(String key) {
        key = getKey(key);
        Object ob = getMemcachedClient().getCounter(key);
        if (null == ob) {
            return -1;
        } else if (ob instanceof String) {
            del(key);
            return -1;
        } else {
            return Long.valueOf(ob.toString());
        }
    }

    private static String getKey(String key) {
        return getKey(key, false);
    }

    /**
     * 加上当前系统的前缀区分各系统的缓存，如果multiSysShared为true表示多系统共享缓存，不加前缀！
     * 
     * key的格式：环境_系统_key
     * @param key
     * @param multiSysShared
     * @return
     */
    private static String getKey(String key, boolean multiSysShared) {
        if (!multiSysShared) {

            if (!StringUtils.isEmpty(CommonConstants.CURRENT_ENVIRONMENT)) {
                key = "_" + CommonConstants.CURRENT_ENVIRONMENT + "_" + key;
            }

            if (!StringUtils.isEmpty(CommonConstants.CURRENT_SYSTEM)) {
                key = "_" + CommonConstants.CURRENT_SYSTEM + "_" + key;
            }

        }

        if (key.length() >= MAX_KEY_LEN) {
            // 如果字符太大抛出异常
            throw new AppException(ExCode.CACHE_001);
            /*
             * try
             * {
             * MessageDigest mac = MessageDigest.getInstance("SHA-256");
             * mac.update(key.getBytes());
             * String origkey = key;
             * key = new String(Hex.encodeHex(mac.digest()));
             * logger.info("key too long. Using hash key=[{}], orig key=[{}]", key, origkey);
             * }
             * catch (NoSuchAlgorithmException e)
             * {
             * logger.error("can't find MessageDigest", e);
             * throw new AppException(ExCode.CACHE_001, e);
             * }
             */
        }
        return key;
    }

    public static boolean containsKey(String key) {
        Object obj = get(key);
        if (obj == null) {
            return false;
        }
        return true;
    }

    public static boolean setStringIfNotExists(String key, String value, boolean multiSysShared) {
        boolean flag = false;
        boolean exceptionFlag = false;
        Exception e1 = null;
        for (Integer index = 0; index < RETRY_COUNT; index++) {
            try {
                flag = setStringIfNotExistsSub(key, value, multiSysShared);
                break;
            } catch (Exception e) {
                if ((RETRY_COUNT - 1) == index) {
                    exceptionFlag = true;
                    e1 = e;
                }
            }
        }
        if (exceptionFlag) {
            throw MemcacheException.defaultException(e1);
        }
        return flag;
    }

    public static boolean setStringIfNotExists(String key, String value) {
        boolean flag = false;
        boolean exceptionFlag = false;
        Exception e1 = null;
        for (Integer index = 0; index < RETRY_COUNT; index++) {
            try {
                flag = setStringIfNotExistsSub(key, value, false);
                break;
            } catch (Exception e) {
                if ((RETRY_COUNT - 1) == index) {
                    exceptionFlag = true;
                    e1 = e;
                }
            }
        }
        if (exceptionFlag) {
            throw MemcacheException.defaultException(e1);
        }
        return flag;
    }

    /**
     * 设置key，如果这个key不存在的话；不带过期时间
     * 
     * @param key
     * @param expiry
     * @param value
     * @return
     */
    public static boolean setStringIfNotExistsSub(String key, String value, boolean multiSysShared) {
        boolean flag = false;
        key = getKey(key, multiSysShared);
        try {
            // 该方法是判断memcached中是否有某个key存在，如果存在则返回false,否则返回true,并添加该key值
            flag = getMemcachedClient().add(key, Integer.MAX_VALUE, value);
        } catch (Exception e) {
            logger.error("缓存setStringIfNotExists报错,key{},原因:{}", key, ExceptionsUtilies.exceptionToString(e));
            throw MemcacheException.defaultException(e);
        }
        return flag;
    }

    public static boolean setStringIfNotExists(String key, int expiry, Object value, boolean multiSysShared) {
        boolean flag = false;
        boolean exceptionFlag = false;
        Exception e1 = null;
        for (Integer index = 0; index < RETRY_COUNT; index++) {
            try {
                flag = setStringIfNotExistsSub(key, expiry, value, multiSysShared);
                break;
            } catch (Exception e) {
                if ((RETRY_COUNT - 1) == index) {
                    exceptionFlag = true;
                    e1 = e;
                }
            }
        }
        if (exceptionFlag) {
            throw MemcacheException.defaultException(e1);
        }
        return flag;
    }

    public static boolean setStringIfNotExists(String key, int expiry, Object value) {
        boolean flag = false;
        boolean exceptionFlag = false;
        Exception e1 = null;
        for (Integer index = 0; index < RETRY_COUNT; index++) {
            try {
                flag = setStringIfNotExistsSub(key, expiry, value, false);
                break;
            } catch (Exception e) {
                if ((RETRY_COUNT - 1) == index) {
                    exceptionFlag = true;
                    e1 = e;
                }
            }
        }
        if (exceptionFlag) {
            throw MemcacheException.defaultException(e1);
        }
        return flag;
    }

    /**
     * 设置key，如果这个key不存在的话；带过期时间
     * 
     * @param key
     * @param expiry
     * @param value
     * @return
     */
    public static boolean setStringIfNotExistsSub(String key, int expiry, Object value, boolean multiSysShared) {
        boolean flag = false;
        key = getKey(key, multiSysShared);
        try {
            // 该方法是判断memcached中是否有某个key存在，如果存在则返回false,否则返回true,并添加该key值
            flag = getMemcachedClient().add(key, expiry, value);
        } catch (Exception e) {
            logger.error("缓存setStringIfNotExists报错,key{},原因:{}", key, ExceptionsUtilies.exceptionToString(e));
            throw MemcacheException.defaultException(e);
        }
        return flag;
    }

    /**
     * 锁定，默认为单系统锁
     * 
     * @param lock
     * @param expiry
     * @return
     */
    public static boolean lockWithoutWait(String lock, int expiry) {
        return lockWithoutWait(lock, expiry, false);
    }

    /**
     * 锁定（带自旋锁），multiSysShared为true表示多系统锁，为false表示单系统锁
     * 
     * @param lock
     * @param expiry
     * @return
     */
    public static boolean lockWithoutWait(String lock, int expiry, boolean multiSysShared) {
        boolean flag = false;
        boolean exceptionFlag = false;
        Exception e1 = null;
        for (Integer index = 0; index < RETRY_COUNT; index++) {
            try {
                flag = lockWithoutWaitSub(lock, expiry, multiSysShared);
                break;
            } catch (Exception e) {
                if ((RETRY_COUNT - 1) == index) {
                    exceptionFlag = true;
                    e1 = e;
                }
            }
        }
        if (exceptionFlag) {
            logger.error("缓存lockWithoutWait重试{}次后依然报错,lock{},原因:{}",
                    new Object[] { RETRY_COUNT, lock, ExceptionsUtilies.exceptionToString(e1) });
            throw MemcacheException.defaultException(e1);
        }
        return flag;
    }

    /**
     * 
     * @param lock
     * @param expiry
     * @param multiSysShared multiSysShared为true表示多系统锁，为false表示单系统锁
     * @return
     */
    public static boolean lockWithoutWaitSub(String lock, int expiry, boolean multiSysShared) {
        long now = System.currentTimeMillis();
        Object value = now + "";
        boolean flag = false;
        lock = getKey(lock, multiSysShared);
        try {
            // add方法是实现该功能的关键，该方法是判断memcached中是否有某个key存在，如果存在则返回false,否则返回true,并添加该key值
            flag = getMemcachedClient().add(lock, expiry, value);
        } catch (Exception e) {
            logger.error("缓存lockWithoutWait报错,lock{},原因:{}", lock, ExceptionsUtilies.exceptionToString(e));
            throw MemcacheException.defaultException(e);
        }

        return flag;
    }

    public static boolean unlock(String lock, boolean multiSysShared) {
        return del(lock, multiSysShared);
    }

    public static boolean unlock(String lock) {
        return del(lock, false);
    }

    // 自增，带超时时间
    public static long incr(String key, long delta, long initValue, long timeout, int exp) {
        long result = 0l;
        key = getKey(key);
        try {
            getMemcachedClient().incr(key, delta, initValue, timeout, exp);
        } catch (Exception e) {
            throw MemcacheException.defaultException(e);
        }
        return result;
    }

    // 自增，不过期
    public static long incr(String key, long delta, long initValue, long timeout) {
        long result = 0l;
        key = getKey(key);
        try {
            getMemcachedClient().incr(key, delta, initValue, timeout);
        } catch (Exception e) {
            throw MemcacheException.defaultException(e);
        }
        return result;
    }

    // 自减，带过期时间
    public static long decr(String key, long delta, long initValue, long timeout, int exp) {
        long result = 0l;
        key = getKey(key);
        try {
            getMemcachedClient().decr(key, delta, initValue, timeout, exp);
        } catch (Exception e) {
            throw MemcacheException.defaultException(e);
        }
        return result;
    }

    // 自减，不过期
    public static long decr(String key, long delta, long initValue, long timeout) {
        long result = 0l;
        key = getKey(key);
        try {
            getMemcachedClient().decr(key, delta, initValue, timeout);
        } catch (Exception e) {
            throw MemcacheException.defaultException(e);
        }
        return result;
    }

    // 获取mcc对象
    private static MemcachedClient getMemcachedClient() {
        if(!isEnable()){
            throw MemcacheException.notOpenException();
        }
        if (mcc == null) {
            throw MemcacheException.configException("Memcache");
            
        }
        return mcc;
    }

    public void setMcc(MemcachedClient mcc) {
        MemCachedOperation.mcc = mcc;
    }

    
    /**
     * @return enable
     */
    
    public static boolean isEnable() {
        return enable;
    }

    
    /** 
     * @param enable 设置 enable 
     */
    
    public  void setEnable(boolean enable) {
        MemCachedOperation.enable = enable;
    }
    
    
}
