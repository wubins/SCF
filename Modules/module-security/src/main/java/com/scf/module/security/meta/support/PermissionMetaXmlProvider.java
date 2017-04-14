package com.scf.module.security.meta.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.scf.module.security.matcher.AntPathRequestMatcher;
import com.scf.module.security.matcher.RequestMatcher;
import com.scf.module.security.meta.Permission;
import com.scf.module.security.meta.Permissions;
import com.scf.utils.DatetimeUtilies;
import com.scf.utils.XMLUtilies;
/**
 * 权限元数据提供者，xml实现
 * @author wubin
 * @date 2016年8月3日 上午10:20:58 
 * @version V1.1.0
 */
public class PermissionMetaXmlProvider implements PermissionMetaProvider,InitializingBean{

    /**
     * 固定xml存放路径
     */
    public static final String METADATAS = "xml/permission.xml";
    
    private List<Permission> permissions = new ArrayList<Permission>();
    
    /**
     * 获取权限
     * @see net.one_job.oper.web.security.meta.support.PermissionMetaProvider#getPermissionMeta()
     */
    public List<Permission> getPermissionMeta() {
        return permissions;
    }

    /** 
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<Permission>  permissions =  loadPermissionMeta();
        if(!CollectionUtils.isEmpty(permissions)){
            for (Permission permission : permissions) {
                RequestMatcher requestMatcher = new AntPathRequestMatcher(permission.getUrl());
                permission.setRequestMatcher(requestMatcher);
            }
        }
        this.permissions.addAll(permissions);
    }
    
    
    
    
    private List<Permission> loadPermissionMeta(){
        
        Permissions permissions =  XMLUtilies.getObjectFromXMLFile(METADATAS, Permissions.class, DatetimeUtilies.DATE_TIME);
        
        List<Permission> permissionList= new ArrayList<Permission>();
        if(permissions!=null){
            this.checkPermission(permissions);
            permissionList =  permissions.getPermissionList();
        }
         return permissionList;
    }
    
    /**
     * 校验配置是否正确
     * 
     * @param permissions
     */
    private void checkPermission(Permissions permissions){
        if(!CollectionUtils.isEmpty(permissions.getPermissionList())){
            for(Permission permission : permissions.getPermissionList()){
                Assert.hasText(permission.getCode());
                Assert.hasText(permission.getUrl());
            }
        }
    }
    
}
