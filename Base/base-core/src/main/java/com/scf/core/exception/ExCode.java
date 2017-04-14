package com.scf.core.exception;

/**
 * 异常编码
 * 所有异常编码由类型和编码组成，用下划线连接
 * 
 * @author wub
 * @version [版本号, 2015年6月23日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ExCode {
    // SYS ===================================

    /** 默认系统 异常 */
    public static final int SYS_001 = 10101;
    /** 系统配置异常 */
    public static final int SYS_002 = 10102;
    /** 系统加密异常 */
    public static final int SYS_003 = 10103;
    /** 系统解析文件异常 */
    public static final int SYS_004 = 10104;
    /** 与第三方系统交互异常 */
    public static final int SYS_005 = 10105;
    /** 系统上下文异常 */
    public static final int SYS_006 = 10106;

    // PARAM =================================

    /** 系统级别的请求参数异常，注意，不是业务级别的 */
    public static final int PARAM_001 = 111000;

    // DB =====================================
    /** 默认数据库异常 */
    public static final int DB_001 = 11101;

    // Cache===================================
    /** 默认缓存异常 */
    public static final int CACHE_001 = 12101;

    /** 缓存未开启异常 */
    public static final int CACHE_002 = 12102;

    /** 缓存配置异常 */
    public static final int CACHE_003 = 12103;


    // FILE===================================

    /**
     * 文件读写默认异常
     */
    public static final int FILE_000 = 13100;
    /**
     * 空文件
     */
    public static final int FILE_001 = 13101;
    /**
     * 文件找不到
     */
    public static final int FILE_002 = 13102;
    /**
     * 读取失败
     */
    public static final int FILE_003 = 13103;
    /**
     * 写入失败
     */
    public static final int FILE_004 = 13104;
    /**
     * 空文件名
     */
    public static final int FILE_005 = 13105;

    /**
     * 文件大小超过限制
     */
    public static final int FILE_006 = 13106;

    // FILE===================================

    // MQ=====================================
    /**
     * 发送消息为空
     */
    public static final int MQ_000 = 14100;
    /**
     * 发送主题为空
     */
    public static final int MQ_001 = 14101;
    // MQ=====================================
    

    // Cache===================================

    // memecache
    /** 默认memecache异常 */
    public static final int MEMCACHED_001 = 15101;

    /** memecache未开启异常 */
    public static final int MEMCACHED_002 = 15102;
    // memecache

    // redis
    /** 默认redis异常 */
    public static final int REDIS_001 = 16101;

    /** redis未开启异常 */
    public static final int REDIS_002 = 16102;
    // redis
    
    // Cache===================================
    
    
    // SMS=====================================
    /**
     * 默认短信发送异常
     */
    public static final int SMS_001 = 17101;
    /**
     * 手机发送验证码频繁，不准发送
     */
    public static final int SMS_002 = 17102;
    /**
     * 未到重发间隔，不准发送
     */
    public static final int SMS_003 = 17103;
    /**
     * IP地址请求验证码频繁，不准发送
     */
    public static final int SMS_004 = 17104;
    
    // SMS=====================================
    
    
    

}
