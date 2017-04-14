
package com.scf.core.context.spring.redis.cleint;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import com.scf.core.context.spring.redis.exception.RedisException;
import com.scf.core.exception.ExCode;
import com.scf.utils.ExceptionsUtilies;

/**
 * reids操作类
 * @author wubin
 */
public class RedisOperation implements InitializingBean
{
    public static Logger logger = LoggerFactory.getLogger(RedisOperation.class);
    
    // 开启标志
    private static boolean enable ;
    
    // 操作模板
    private static RedisTemplate<String, String> stringRedisTemplate;
    
    // 重试时间，单位毫秒
    private static long timeout;
    
    @Override
    public void afterPropertiesSet()
        throws Exception
    {
        logger.info("is redis enable:{}", enable);
    }
    
    /**
     * spring dao异常转换为系统异常
     * 
     * @param ex DataAccessException
     * @return
     */
    protected static RedisException convertJedisAccessException(DataAccessException ex)
    {
        RedisException appException = new RedisException(ExCode.REDIS_001, String.valueOf(ExCode.REDIS_001), ex);
        return appException;
    }
    
 
    
    /**
     * 执行器 它保证在一定时间的重试自旋
     */
    static abstract class Executor<T>
    {
        public static Integer RETRY_INTERVAL = 200; // 每次重试间隔时间，单位毫秒
        
        /**
         * 业务执行方法
         * 
         * @return
         */
        abstract T execute();
        
        /**
         * 异常循环自旋执行，业务层容错
         * 
         * @return
         */
        T getResult()
        {
            T rs = null;
            Long beginTime = System.currentTimeMillis();
            Long recheckCostCopy = 0l;
            // 执行错误标志
            boolean timeout = false;
            RedisException ex = null;
            do
            {
                Long recheckCost = System.currentTimeMillis() - beginTime;
                recheckCostCopy = recheckCost;
                try
                {
                    if (recheckCost > RedisOperation.timeout)
                    {
                        timeout = true;
                        break;
                    }
                    rs = execute();
                    break;
                }
                catch (RedisException e)
                {
                    ex = e;
                    try
                    {
                        Thread.sleep(RETRY_INTERVAL);
                    }
                    catch (InterruptedException e1)
                    {
                        logger.warn("", e1);
                    }
                }
            } while (true);
            
            if (timeout)
            {
                logger.error("redis这次操作重试超时了，一共花费{}毫秒，准备放弃,错误原因:{}", recheckCostCopy, ExceptionsUtilies.exceptionToString(ex));
                throw ex;
            }
            
            return rs;
        }
        
    }
    
    /**
     * 增加
     * 
     * @param key
     * @param qty
     * @return 最终结果
     */
    public static long incr(final String key, final long qty)
    {
        long rs = 0l;
        rs = new Executor<Long>()
        {
            @Override
            Long execute()
            {
                long rs = 0l;
                try
                {
                    rs = getActiveStringRedisTemplate().opsForValue().increment(key, qty);
                }
                catch (DataAccessException e)
                {
                    throw convertJedisAccessException(e);
                }
                return rs;
            }
        }.getResult();
        return rs;
        
    }
    
    /**
     * 减少
     * 
     * @param key
     * @param qty
     * @return 最终结果
     */
    public static long decr(final String key, final long qty)
    {
        
        long rs = 0l;
        rs = new Executor<Long>()
        {
            @Override
            Long execute()
            {
                long rs = 0l;
                try
                {
                    rs = getActiveStringRedisTemplate().execute(new RedisCallback<Long>()
                    {
                        public Long doInRedis(RedisConnection connection)
                            throws DataAccessException
                        {
                            StringRedisConnection StringRedisConnection = (StringRedisConnection)connection;
                            return StringRedisConnection.decrBy(key, qty);
                        }
                    });
                }
                catch (DataAccessException e)
                {
                    throw convertJedisAccessException(e);
                }
                return rs;
            }
        }.getResult();
        return rs;
        
    }
    
    /**
     * 删除匹配的key（可以批量）
     * 
     * @param Key ...可以删除多个key
     * @return 删除成功的条数
     */
    public static long delKey(final String... keys)
    {
        long rs = 0l;
        rs = new Executor<Long>()
        {
            @Override
            Long execute()
            {
                long rs = 0l;
                try
                {
                    rs = getActiveStringRedisTemplate().execute(new RedisCallback<Long>()
                    {
                        public Long doInRedis(RedisConnection connection)
                            throws DataAccessException
                        {
                            StringRedisConnection StringRedisConnection = (StringRedisConnection)connection;
                            return StringRedisConnection.del(keys);
                        }
                    });
                }
                catch (DataAccessException e)
                {
                    logger.warn(e.getMessage());
                    throw convertJedisAccessException(e);
                }
                return rs;
            }
        }.getResult();
        return rs;
    }
    
    /* ======================================Strings====================================== */
    
    /**
     * 将字符串值 value 关联到 key 。 如果 key 已经持有其他值， setString 就覆写旧值，无视类型。 对于某个原本带有生存时间（TTL）的键来说， 当 setString 成功在这个键上执行时， 这个键原有的
     * TTL 将被清除。 时间复杂度：O(1)
     * 
     * @param key key
     * @param value string value
     * @return 在设置操作成功完成时，才返回 OK 。
     */
    public static void setString(final String key, final String value)
    {
        new Executor<Long>()
        {
            @Override
            Long execute()
            {
                try
                {
                    getActiveStringRedisTemplate().opsForValue().set(key, value);
                }
                catch (DataAccessException e)
                {
                    throw convertJedisAccessException(e);
                }
                return 1l;
            }
        }.getResult();
        
    }
    
    /**
     * 将值 value 关联到 key ，并将 key 的生存时间设为 expire (以秒为单位)。 如果 key 已经存在， 将覆写旧值。 类似于以下两个命令: SET key value EXPIRE key expire #
     * 设置生存时间 不同之处是这个方法是一个原子性(atomic)操作，关联值和设置生存时间两个动作会在同一时间内完成，在 Redis 用作缓存时，非常实用。 时间复杂度：O(1)
     * 
     * @param key key
     * @param value string value
     * @param expire 生命周期
     * @return 设置成功时返回 OK 。当 expire 参数不合法时，返回一个错误。
     */
    public static void setString(final String key, final String value, final int expire)
    {
        
        new Executor<Long>()
        {
            @Override
            Long execute()
            {
                long rs = 0l;
                try
                {
                    getActiveStringRedisTemplate().opsForValue().set(key, value, expire, TimeUnit.SECONDS);
                }
                catch (DataAccessException e)
                {
                    throw convertJedisAccessException(e);
                }
                return rs;
            }
        }.getResult();
        
    }
    
    /**
     * 将 key 的值设为 value ，当且仅当 key 不存在。若给定的 key 已经存在，则 setStringIfNotExists 不做任何动作。 时间复杂度：O(1)
     * 
     * @param key key
     * @param value string value
     * @return 设置成功，返回 1 。设置失败，返回 0 。
     */
    public static Long setStringIfNotExists(final String key, final String value)
    {
        
        long rs = 0l;
        rs = new Executor<Long>()
        {
            @Override
            Long execute()
            {
                long rs = 0l;
                boolean flag = false;
                try
                {
                    flag = getActiveStringRedisTemplate().opsForValue().setIfAbsent(key, value);
                }
                catch (DataAccessException e)
                {
                    throw convertJedisAccessException(e);
                }
                if (flag)
                {
                    rs = 1;
                }
                return rs;
            }
        }.getResult();
        
        return rs;
        
    }
    
    /**
     * 返回 key 所关联的字符串值。如果 key 不存在那么返回特殊值 nil 。 假如 key 储存的值不是字符串类型，返回一个错误，因为 getString 只能用于处理字符串值。 时间复杂度: O(1)
     * 
     * @param key key
     * @return 当 key 不存在时，返回 nil ，否则，返回 key 的值。如果 key 不是字符串类型，那么返回一个错误。
     */
    public static String getString(final String key)
    {
        
        String rs = null;
        rs = new Executor<String>()
        {
            @Override
            String execute()
            {
                String rs = null;
                try
                {
                    rs = getActiveStringRedisTemplate().opsForValue().get(key);
                }
                catch (DataAccessException e)
                {
                    throw convertJedisAccessException(e);
                }
                return rs;
            }
        }.getResult();
        
        return rs;
        
    }
    
    /**
     * 获取模糊匹配的所有key
     * 
     * @param keyLike
     * @return
     */
    public static Set<String> queryKeysLike(final String keyLike)
    {
        
        Set<String> keys = null;
        keys = new Executor<Set<String>>()
        {
            @Override
            Set<String> execute()
            {
                Set<String> keys = null;
                try
                {
                    keys = getActiveStringRedisTemplate().keys(keyLike + "*");
                }
                catch (DataAccessException e)
                {
                    throw convertJedisAccessException(e);
                }
                return keys;
            }
        }.getResult();
        
        return keys;
    }
    
    /* ======================================Hashes====================================== */
    
    /**
     * 将哈希表 key 中的域 field 的值设为 value 。 如果 key 不存在，一个新的哈希表被创建并进行 hashSet 操作。 如果域 field 已经存在于哈希表中，旧值将被覆盖。 时间复杂度: O(1)
     * 
     * @param key key
     * @param field 域
     * @param value string value
     * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1 。如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回 0 。
     */
    public static void hashSet(final String key, final String field, final String value)
    {
        new Executor<Long>()
        {
            @Override
            Long execute()
            {
                Long rs = 0l;
                try
                {
                    getActiveStringRedisTemplate().opsForHash().put(key, field, value);
                }
                catch (DataAccessException e)
                {
                    throw convertJedisAccessException(e);
                }
                return rs;
            }
        }.getResult();
    }
    
    /**
     * 将哈希表 key 中的域 field 的值设为 value 。 如果 key 不存在，一个新的哈希表被创建并进行 hashSet 操作。 如果域 field 已经存在于哈希表中，旧值将被覆盖。
     * 
     * @param key key
     * @param field 域
     * @param value string value
     * @param expire 生命周期，单位为秒
     * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1 。如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回 0 。
     */
    public static void hashSet(final String key, final String field, final String value, final int expire)
    {
        
        new Executor<Long>()
        {
            @Override
            Long execute()
            {
                Long rs = 0l;
                try
                {
                    getActiveStringRedisTemplate().execute(new RedisCallback<Boolean>()
                    {
                        public Boolean doInRedis(RedisConnection connection)
                            throws DataAccessException
                        {
                            StringRedisConnection StringRedisConnection = (StringRedisConnection)connection;
                            boolean rs = StringRedisConnection.hSet(key, field, value);
                            StringRedisConnection.expire(key, expire);
                            return rs;
                        }
                    });
                }
                catch (DataAccessException e)
                {
                    throw convertJedisAccessException(e);
                }
                return rs;
            }
        }.getResult();
        
    }
    
    /**
     * 返回哈希表 key 中给定域 field 的值。 时间复杂度:O(1)
     * 
     * @param key key
     * @param field 域
     * @return 给定域的值。当给定域不存在或是给定 key 不存在时，返回 nil 。
     */
    public static String hashGet(final String key, final String field)
    {
        
        String rs = null;
        
        rs = new Executor<String>()
        {
            @Override
            String execute()
            {
                String rs = null;
                try
                {
                    HashOperations<String, String, String> hashOperations = getActiveStringRedisTemplate().opsForHash();
                    rs = hashOperations.get(key, field);
                }
                catch (DataAccessException e)
                {
                    throw convertJedisAccessException(e);
                }
                return rs;
            }
        }.getResult();
        
        return rs;
    }
    
    /**
     * 返回哈希表 key 中，所有的域和值。在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。 时间复杂度: O(N)
     * 
     * @param key key
     * @return 以列表形式返回哈希表的域和域的值。若 key 不存在，返回空列表。
     */
    public static Map<String, String> hashGetAll(final String key)
    {
        Map<String, String> map = null;
        map = new Executor<Map<String, String>>()
        {
            @Override
            Map<String, String> execute()
            {
                Map<String, String> map = null;
                try
                {
                    HashOperations<String, String, String> hashOperations = getActiveStringRedisTemplate().opsForHash();
                    map = hashOperations.entries(key);
                }
                catch (DataAccessException e)
                {
                    throw convertJedisAccessException(e);
                }
                return map;
            }
        }.getResult();
        return map;
    }
    
    /**
     * 删除指定key下匹配的field（可以批量）
     * 
     * @param key key
     * @param fields 域
     * @return 删除成功的条数
     */
    public static long hashDel(final String key, final String... fields)
    {
        long rs = 0l;
        rs = new Executor<Long>()
        {
            @Override
            Long execute()
            {
                Long rs = 0l;
                try
                {
                    rs = getActiveStringRedisTemplate().execute(new RedisCallback<Long>()
                    {
                        public Long doInRedis(RedisConnection connection)
                            throws DataAccessException
                        {
                            StringRedisConnection StringRedisConnection = (StringRedisConnection)connection;
                            return StringRedisConnection.hDel(key, fields);
                        }
                    });
                }
                catch (DataAccessException e)
                {
                    throw convertJedisAccessException(e);
                }
                return rs;
            }
        }.getResult();
        return rs;
    }
    
    /**
     * 增加
     * 
     * @param key
     * @param qty
     * @return 最终结果
     */
    public static long hashIncr(final String key, final String field, final long qty)
    {
        long rs = 0l;
        rs = new Executor<Long>()
        {
            @Override
            Long execute()
            {
                long rs = 0l;
                try
                {
                    rs = getActiveStringRedisTemplate().opsForHash().increment(key, field, qty);
                }
                catch (DataAccessException e)
                {
                    throw convertJedisAccessException(e);
                }
                return rs;
            }
        }.getResult();
        return rs;
        
    }
    
    /**
     * 减少
     * 
     * @param key
     * @param qty
     * @return 最终结果
     */
    public static long hashDecr(final String key, final String field, final long qty)
    {
        return hashIncr(key, field, -1 * qty);
    }
    
    /* ======================================List====================================== */
    
    /**
     * 将一个或多个值 value 插入到列表 key 的表尾(最右边)。
     * 
     * @param key key
     * @param values value的数组
     * @return 执行 listPushTail 操作后，表的长度
     */
    public static Long listPushTail(final String key, final String... values)
    {
        Long rs = 0l;
        rs = new Executor<Long>()
        {
            @Override
            Long execute()
            {
                Long rs = 0l;
                try
                {
                    rs = getActiveStringRedisTemplate().opsForList().rightPushAll(key, values);
                }
                catch (DataAccessException e)
                {
                    throw convertJedisAccessException(e);
                }
                return rs;
            }
        }.getResult();
        return rs;
    }
    
    /**
     * 将一个或多个值 value 插入到列表 key 的表头
     * @param key key
     * @param value string value
     * @return 执行 listPushHead 命令后，列表的长度。
     */
    public static Long listPushHead(final String key, final String... values) {
    	Long rs = 0l;
    	rs = new Executor<Long>(){
    		@Override
    		Long execute() {
    			Long rs = 0l;
    			try{
            		rs = getActiveStringRedisTemplate().opsForList().leftPushAll(key, values);
    	       	}catch(DataAccessException e){
    	       		throw convertJedisAccessException(e);
    	       	}
    			return rs;
    		}
    	}.getResult();
    	return rs;
    }
    
    
    /**
     * 从列表右侧弹出一个元素
     * @param key key
     * @return 弹出的元素。
     */
    public static String rightPopTail(final String key) {
    	String rs = "";
    	rs = new Executor<String>(){
    		@Override
    		String execute() {
    			String rs = "";
    			try{
            		rs = getActiveStringRedisTemplate().opsForList().rightPop(key);
    	       	}catch(DataAccessException e){
    	       		throw convertJedisAccessException(e);
    	       	}
    			return rs;
    		}
    	}.getResult();
    	return rs;
    }
    
    
    /**
     * 从列表左侧弹出一个元素
     * @param key key
     * @return 弹出的元素。
     */
    public static String leftPopHead(final String key) {
    	String rs = "";
    	rs = new Executor<String>(){
    		@Override
    		String execute() {
    			String rs = "";
    			try{
            		rs = getActiveStringRedisTemplate().opsForList().leftPop(key);
    	       	}catch(DataAccessException e){
    	       		throw convertJedisAccessException(e);
    	       	}
    			return rs;
    		}
    	}.getResult();
    	return rs;
    }
    
    /**
     * 批量的{@link #listPushTail(String, String...)}，以锁的方式实现
     * 
     * @param key key
     * @param values value的数组
     * @param delOld 如果key存在，是否删除它。true 删除；false: 不删除，只是在行尾追加
     */
    /*
     * public void batchListPushTail(final String key, final String[] values, final boolean delOld) { new
     * Executor<Object>(shardedJedisPool) {
     * 
     * @Override Object execute() { if (delOld) { RedisLock lock = new RedisLock(key, shardedJedisPool); lock.lock();
     * try { Pipeline pipeline = jedis.getShard(key).pipelined(); pipeline.del(key); for (String value : values) {
     * pipeline.rpush(key, value); } pipeline.sync(); } finally { lock.unlock(); } } else { jedis.rpush(key, values); }
     * return null; } }.getResult(); }
     */
    
    /**
     * 同{@link #batchListPushTail(String, String[], boolean)},不同的是利用redis的事务特性来实现
     * 
     * @param key key
     * @param values value的数组
     * @return null
     */
    /*
     * public static Object updateListInTransaction(final String key, final List<String> values) { return new
     * Executor<Object>(shardedJedisPool) {
     * 
     * @Override Object execute() { Transaction transaction = jedis.getShard(key).multi(); transaction.del(key); for
     * (String value : values) { transaction.rpush(key, value); } transaction.exec(); return null; } }.getResult(); }
     */
    
    /**
     * 在key对应list的尾部部添加字符串元素,如果key存在，什么也不做
     * 
     * @param key key
     * @param values value的数组
     * @return 执行insertListIfNotExists后，表的长度
     */
    /*
     * public Long insertListIfNotExists(final String key, final String[] values) { return new
     * Executor<Long>(shardedJedisPool) {
     * 
     * @Override Long execute() { RedisLock lock = new RedisLock(key, shardedJedisPool); lock.lock(); try { if
     * (!jedis.exists(key)) { return jedis.rpush(key, values); } } finally { lock.unlock(); } return 0L; }
     * }.getResult(); }
     */
    
    /**
     * 返回list所有元素，下标从0开始，负值表示从后面计算，-1表示倒数第一个元素，key不存在返回空列表
     * 
     * @param key key
     * @return list所有元素
     */
    public static List<String> listGetAll(final String key)
    {
        List<String> list = null;
        list = new Executor<List<String>>()
        {
            @Override
            List<String> execute()
            {
                List<String> list = null;
                try
                {
                    list = getActiveStringRedisTemplate().opsForList().range(key, 0, -1);
                }
                catch (DataAccessException e)
                {
                    throw convertJedisAccessException(e);
                }
                return list;
            }
        }.getResult();
        return list;
    }
    
    /**
     * 返回指定区间内的元素，下标从0开始，负值表示从后面计算，-1表示倒数第一个元素，key不存在返回空列表
     * 
     * @param key key
     * @param beginIndex 下标开始索引（包含）
     * @param endIndex 下标结束索引（不包含）
     * @return 指定区间内的元素
     */
    public static List<String> listRange(final String key, final long beginIndex, final long endIndex)
    {
        List<String> list = null;
        list = new Executor<List<String>>()
        {
            @Override
            List<String> execute()
            {
                List<String> list = null;
                try
                {
                    list = getActiveStringRedisTemplate().opsForList().range(key, beginIndex, endIndex - 1);
                }
                catch (DataAccessException e)
                {
                    throw convertJedisAccessException(e);
                }
                return list;
            }
        }.getResult();
        return list;
    }
    
    /* ======================================Pub/Sub====================================== */
    
    /**
     * 将信息 message 发送到指定的频道 channel。 时间复杂度：O(N+M)，其中 N 是频道 channel 的订阅者数量，而 M 则是使用模式订阅(subscribed patterns)的客户端的数量。
     * 
     * @param channel 频道
     * @param message 信息
     * @return 接收到信息 message 的订阅者数量。
     */
    /*
     * public static Long publish(final String channel, final String message) { return new
     * Executor<Long>(shardedJedisPool) {
     * 
     * @Override Long execute() { Jedis _jedis = jedis.getShard(channel); return _jedis.publish(channel, message); }
     * 
     * }.getResult(); }
     */
    
    /**
     * 订阅给定的一个频道的信息。
     * 
     * @param jedisPubSub 监听器
     * @param channel 频道
     */
    /*
     * public static void subscribe(final JedisPubSub jedisPubSub, final String channel) { new
     * Executor<Object>(shardedJedisPool) {
     * 
     * @Override Object execute() { Jedis _jedis = jedis.getShard(channel); //
     * 注意subscribe是一个阻塞操作，因为当前线程要轮询Redis的响应然后调用subscribe _jedis.subscribe(jedisPubSub, channel); return null; }
     * }.getResult(); }
     */
    
    /**
     * 取消订阅
     * 
     * @param jedisPubSub 监听器
     */
    /*
     * public static void unSubscribe(final JedisPubSub jedisPubSub) { jedisPubSub.unsubscribe(); }
     */
    
    /* ======================================Sorted set================================= */
    
    /**
     * 将一个 member 元素及其 score 值加入到有序集 key 当中。
     * 
     * @param key key
     * @param score score 值可以是整数值或双精度浮点数。
     * @param member 有序集的成员
     * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
     */
    /*
     * public static Long addWithSortedSet(final String key, final double score, final String member) { return new
     * Executor<Long>(shardedJedisPool) {
     * 
     * @Override Long execute() { return jedis.zadd(key, score, member); } }.getResult(); }
     */
    
    /**
     * 将多个 member 元素及其 score 值加入到有序集 key 当中。
     * 
     * @param key key
     * @param scoreMembers score、member的pair
     * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
     */
    /*
     * public Long addWithSortedSet(final String key, final Map<Double, String> scoreMembers) { return new
     * Executor<Long>(shardedJedisPool) {
     * 
     * @Override Long execute() { return jedis.zadd(key, scoreMembers); } }.getResult(); }
     */
    
    /**
     * 返回有序集 key 中， score 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员。 有序集成员按 score 值递减(从大到小)的次序排列。
     * 
     * @param key key
     * @param max score最大值
     * @param min score最小值
     * @return 指定区间内，带有 score 值(可选)的有序集成员的列表
     */
    /*
     * public static Set<String> revrangeByScoreWithSortedSet(final String key, final double max, final double min) {
     * return new Executor<Set<String>>(shardedJedisPool) {
     * 
     * @Override Set<String> execute() { return jedis.zrevrangeByScore(key, max, min); } }.getResult(); }
     */
    
    /* ======================================Other====================================== */
    
    /**
     * 构造Pair键值对
     * 
     * @param key key
     * @param value value
     * @return 键值对
     */
    public <K, V> Pair<K, V> makePair(K key, V value)
    {
        return new Pair<K, V>(key, value);
    }
    
    /**
     * 键值对
     * 
     * @version V1.0
     * @param <K> key
     * @param <V> value
     */
    public class Pair<K, V>
    {
        
        private K key;
        
        private V value;
        
        public Pair(K key, V value)
        {
            this.key = key;
            this.value = value;
        }
        
        public K getKey()
        {
            return key;
        }
        
        public void setKey(K key)
        {
            this.key = key;
        }
        
        public V getValue()
        {
            return value;
        }
        
        public void setValue(V value)
        {
            this.value = value;
        }
    }
    
   
    
    /**
     * @return enable
     */
    
    public  boolean isEnable() {
        return enable;
    }

    
    /** 
     * @param enable 设置 enable 
     */
    
    public  void setEnable(boolean enable) {
        RedisOperation.enable = enable;
    }

    public static long getTimeout()
    {
        return timeout;
    }
    
    public static void setTimeout(long timeout)
    {
        RedisOperation.timeout = timeout;
    }
    
    public RedisTemplate<String, String> getStringRedisTemplate()
    {
        return stringRedisTemplate;
    }
    
    public void setStringRedisTemplate(RedisTemplate<String, String> stringRedisTemplate)
    {
        RedisOperation.stringRedisTemplate = stringRedisTemplate;
    }
    
    /**
     * 获取RedisTemplate
     * 
     * @return
     */
    public static RedisTemplate<String, String> getActiveStringRedisTemplate()
    {
        if (!enable || RedisOperation.stringRedisTemplate == null)
        {
            throw RedisException.notOpenException();
        }
        
        return RedisOperation.stringRedisTemplate;
    }
    
}
