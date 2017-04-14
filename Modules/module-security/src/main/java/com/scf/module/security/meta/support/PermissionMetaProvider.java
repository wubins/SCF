package com.scf.module.security.meta.support;

import java.util.List;

import com.scf.module.security.meta.Permission;

/**
 * 权限元数据提供者
 * @author wubin
 * @date 2016年8月3日 上午9:19:40 
 * @version V1.1.0
 */
public interface PermissionMetaProvider {
    
    /**
     * 获取权限
     * 
     * @return
     */
     List<Permission> getPermissionMeta() ;
}
