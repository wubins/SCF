package com.scf.core.constant;

/**
 * 全局配置类
 * @author wubin
 */
public final class CommonConstants {
	
	/**
	 * 当前系统名称
	 */
    public static  String CURRENT_SYSTEM ;
    
    /**
     * 当前系统环境
     */
    public static  String CURRENT_ENVIRONMENT;
    
    /**
     * 通用是否状态-是（value=1）
     */
    public static final int COMMON_STATUS_VALUE_YES = 1;
    /**
     * 通用是否状态-否（value=0）
     */
    public static final int COMMON_STATUS_VALUE_NO = 0;
    
    /**
     * 逻辑删除标识-已删除（Value = 1）
     */
    public static final int FLAG_DELETE = 1;
    /**
     * 逻辑删除标识-未删除（Value = 0）
     */
    public static final int FLAG_UNDELETE = 0;
    /**
     * 字段排序-正序
     */
    public static final String ORDER_ASC = "asc";
    /**
     * 字段排序-倒序
     */
    public static final String ORDER_DESC = "desc";
    
    /**
     * session key - 用户对象
     */
    public static final String IDENTITY_KEY_USER = "user";
    /**
     * session key - 用户角色
     */
    public static final String IDENTITY_KEY_USER_ROLES = "user_roles";
    /**
     * session key - 用户模块权限
     */
    public static final String IDENTITY_KEY_USER_MODULES = "user_modules";
    
    /**
     * 通用缺省操作者
     */
    public static final String DEFAULT_OPER_USER_ID = "-1";
}
