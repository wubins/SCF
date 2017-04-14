
package com.scf.core.concurrent;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scf.core.context.spring.memcache.cleint.MemCachedOperation;
/**
 * 并发控制类
 * 
 * @author  wub
 * @version  [版本号, 2015年7月14日]
 */
public class ConcurrentManager
{
    private static final Logger LOG = LoggerFactory.getLogger(ConcurrentManager.class);
    
    public static final Integer LOCK_EXPIRY = 2 * 60; // memCache加锁默认时间
    
    public static Integer CHECK_LOCK_INTERVAL = 10; // 如果被锁, 每隔多久再查一次（单位毫秒）
    
    public static Integer CHECK_LOCK_EXPIRY = 8 * 1000; // 如果被锁, 隔多久LOG超时异常（单位毫秒）
    
    public static Integer RELEASE_LOCK_INTERVAL = 10; // 尝试解锁的间隔时间（单位毫秒）
    
    public static Integer RELEASE_LOCK_EXPIRY = 2 * 1000; // 解锁的最长尝试时间（单位毫秒）
    
    /**
     * 带默认过期时间的超时锁(无前缀参数)
     * 
     * @param prefix
     * @param key
     * @param expireTime
     * @return
     */
    public static boolean lock(String key)
    {
        
        return lock("", key);
    }
    
    /**
     * 带默认过期时间的超时锁(有前缀参数)
     * 
     * @param prefix
     * @param key
     * @param expireTime
     * @return
     */
    public static boolean lock(String prefix, String key)
    {
        
        return lock(prefix, key, null,false);
    }
    
    /**
     * 带默认过期时间的超时锁(有前缀参数、带多系统共享标识)
     * 
     * @param prefix
     * @param key
     * @param expireTime
     * @return
     */
    public static boolean lock(String prefix, String key,boolean multiSysShared)
    {
        
        return lock(prefix, key, null,multiSysShared);
    }
    
    /**
     * 带过期时间的超时锁(无前缀参数)
     * 
     * @param prefix
     * @param key
     * @param expireTime
     * @return
     */
    public static boolean lock(String key, Integer expireTime)
    {
        return lock("", key, expireTime,false);
    }
    
    /**
     * 带过期时间、前缀的超时锁(带自旋)
     * 
     * @param prefix
     * @param key
     * @param expireTime
     * @param multiSysShared true表示多系统共享，false表示不共享
     * @return
     */
    public static boolean lock(String prefix, String key, Integer expireTime,boolean multiSysShared)
    {
        Integer expireTimeTemp = null;
        if (expireTime == null)
        {
            expireTimeTemp = LOCK_EXPIRY;
        }
        else
        {
            expireTimeTemp = expireTime;
        }
        
        if (StringUtils.isBlank(key))
            return false;
        String memKey = getKey(prefix, key);
        LOG.info("并发机制加锁:{}", memKey);
        
        Long beginTime = System.currentTimeMillis();
        
        boolean lockSuccess = false;
        do{
        	lockSuccess = MemCachedOperation.lockWithoutWait(memKey, expireTimeTemp,multiSysShared);
        	 try
             {
	        		 if(lockSuccess){
	        			 break;
	        		 }
	        		 // 如果加锁未成功, 继续尝试, 直到超时
                     Long recheckCost = System.currentTimeMillis() - beginTime;
                     if (recheckCost > CHECK_LOCK_EXPIRY)
                     {
                         LOG.warn("并发机制加锁: {} 时超时, 已等待 {} ms. 加锁不成功.", memKey, recheckCost);
                         break;
                     }
                     Thread.sleep(CHECK_LOCK_INTERVAL);
             }
             catch (InterruptedException e)
             {
                 LOG.warn("并发机制加锁:{} 等待时出错", memKey);
                 LOG.warn(e.getMessage(), e);
             }
        	
        }while(!lockSuccess);
        
        LOG.info("并发机制加锁:{}, 结果:{}", memKey, lockSuccess);
        return lockSuccess;
        
    }
    
    
    /**
     * 不带过期时间、不带前缀的超时锁(不带自旋)
     * 
     * @param prefix
     * @param key
     * @return
     */
    public static boolean lockWithoutWait(String prefix, String key)
    {
        Integer expireTimeTemp = LOCK_EXPIRY;
        if (StringUtils.isBlank(key))
            return false;
        String memKey = getKey(prefix, key);
        LOG.info("并发机制加锁:{}", memKey);
        
        
        boolean lockSuccess = MemCachedOperation.lockWithoutWait(memKey, expireTimeTemp);
        
        LOG.info("并发机制加锁:{}, 结果:{}", memKey, lockSuccess);
        return lockSuccess;
        
    }
    
/*    public static boolean lock(String prifex, Long key)
    {
        if (StringUtils.isEmpty(prifex) || key == null)
        {
            return false;
        }
        return lock(prifex, String.valueOf(key));
    }*/
     
    
    /**
     * Lock传入的List中的所有key. 由于Java的泛型擦除问题, 无法重载实现参数为List<String>和List<Long>的方法
     *
     * @param scene
     * @param keys List中的元素需实现toString()方法.
     * @return 是否全部成功加锁
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static boolean lock(String prefix, List keys)
    {
        if (StringUtils.isEmpty(prefix) || CollectionUtils.isEmpty(keys))
        {
            return false;
        }
        // 去掉null
        List nullList = new ArrayList();
        if (!CollectionUtils.isEmpty(keys))
        {
            for (Object item : keys)
            {
                if (item == null)
                {
                    nullList.add(item);
                }
            }
        }
        keys.removeAll(nullList);
        Object[] keysArray = keys.toArray();
        String keyString = ArrayUtils.toString(keysArray, "");
        LOG.info("并发机制加锁:{}", keyString);
        Long beginTime = System.currentTimeMillis();
        
        // 先检查现有的memCache中是否存在keys中的锁,如果有则等待, 直至超时.
        boolean hasLockedKey = checkExistLockedKey(prefix, keys);
        while (hasLockedKey)
        {
            Long recheckCost = System.currentTimeMillis() - beginTime;
            if (recheckCost > CHECK_LOCK_EXPIRY)
            {
                LOG.warn("并发机制加锁: {} 时超时, 已等待 {} ms. 加锁不成功.", keyString, recheckCost);
                return false;
            }
            try
            {
                Thread.sleep(CHECK_LOCK_INTERVAL);
            }
            catch (InterruptedException e)
            {
                LOG.warn("并发机制加锁:{} 等待时出错", keyString);
            }
            hasLockedKey = checkExistLockedKey(prefix, keys);
        }
        
        boolean allLocked = true;
        for (int i = 0; i < keys.size(); i++)
        {
            Object key = keys.get(i);
            if (key != null && !lock(prefix, key.toString()))
            {
                allLocked = false;
                break;
            }
        }
        // 如果加锁失败, 解除List中加过的锁.
        if (!allLocked)
        {
            for (int i = 0; i < keys.size(); i++)
            {
                Object key = keys.get(i);
                if (key != null)
                    release(prefix, key.toString());
            }
        }
        LOG.info("并发机制加锁:{}, 结果:{}", keyString, allLocked);
        return allLocked;
    }
    
    @SuppressWarnings("rawtypes")
    private static boolean checkExistLockedKey(String prefix, List keys)
    {
        boolean hasLockedKey = false;
        for (int i = 0; i < keys.size(); i++)
        {
            Object key = keys.get(i);
            if (key != null && MemCachedOperation.get(getKey(prefix, String.valueOf(key))) != null)
            {
                hasLockedKey = true;
                break;
            }
        }
        return hasLockedKey;
    }
    
    public static boolean release(String key)
    {
        return release("", key);
    }
    
    /**
     * 释放锁（带业务前缀，默认为单系统内缓存）
     * @param prefix
     * @param key
     * @param multiSysShared
     * @return
     */
    public static boolean release(String prefix, String key)
    {
    	return release(prefix, key, false);
    }
    /**
     * 释放锁（带多系统标识和业务前缀）
     * @param prefix
     * @param key
     * @param multiSysShared
     * @return
     */
    public static boolean release(String prefix, String key,boolean multiSysShared)
    {
        if (StringUtils.isBlank(key))
        {
            return false;
        }
        String memKey = getKey(prefix, key);
        LOG.info("并发机制解锁:{}", memKey);
        Long beginTime = System.currentTimeMillis();
        Object lock = MemCachedOperation.get(memKey,multiSysShared);
        if (lock == null)
        {
            return true;
        }
        boolean releaseSuccess = MemCachedOperation.unlock(memKey,multiSysShared);
        try
        {
            while (!releaseSuccess)
            {
                Long recheckCost = System.currentTimeMillis() - beginTime;
                if (recheckCost > RELEASE_LOCK_EXPIRY)
                {
                    LOG.warn("并发机制解锁: {} 时超时, 已等待 {} ms.", memKey, recheckCost);
                    releaseSuccess = false;
                    return releaseSuccess;
                }
                Thread.sleep(RELEASE_LOCK_INTERVAL);
                releaseSuccess = MemCachedOperation.unlock(memKey,multiSysShared);
            }
        }
        catch (InterruptedException e)
        {
            LOG.warn("并发机制解锁: {} 时出错", memKey);
            LOG.warn(e.getMessage(), e);
        }
        LOG.info("并发机制解锁:{}, 结果:{}", memKey, releaseSuccess);
        return releaseSuccess;
    }
    
    public static boolean release(String prefix, Long key)
    {
        if (StringUtils.isEmpty(prefix) || key == null)
        {
            return false;
        }
        return release(prefix, String.valueOf(key));
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static boolean release(String prefix, List keys)
    {
        if (StringUtils.isEmpty(prefix) || CollectionUtils.isEmpty(keys))
        {
            return false;
        }
        // 去掉null
        List nullList = new ArrayList();
        if (!CollectionUtils.isEmpty(keys))
        {
            for (Object item : keys)
            {
                if (item == null)
                {
                    nullList.add(item);
                }
            }
        }
        keys.removeAll(nullList);
        
        boolean allReleased = true;
        for (int i = 0; i < keys.size(); i++)
        {
            Object key = keys.get(i);
            if (key != null && !release(prefix, key.toString()))
            { // key存在 且解锁不成功的情况下不阻止其他Key解锁
                allReleased = false;
            }
        }
        return allReleased;
    }
    //concurrent_lock_goods_stock_000001，如果不是多系统共享锁（默认不是），那么key：环境_系统_concurrent_lock_goods_stock_000001
    private static String getKey(String name, String key)
    {
        return "concurrent_lock_" + StringUtils.trim(name) + StringUtils.trim(key);
    }
    
}