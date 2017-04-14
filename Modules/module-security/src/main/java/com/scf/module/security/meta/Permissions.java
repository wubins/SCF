package com.scf.module.security.meta;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 权限对象集合
 * @author wubin
 * @date 2016年8月3日 上午9:30:39 
 * @version V1.1.0
 */
@XStreamAlias("permissions")
public class Permissions {

    @XStreamImplicit(itemFieldName="permission")  
    private List<Permission> permissionList;

    /**
     * Getter method for property <tt>permissionList</tt>.
     * 
     * @return property value of permissionList
     */
    public List<Permission> getPermissionList() {
        return permissionList;
    }

    /**
     * Setter method for property <tt>permissionList</tt>.
     * 
     * @param permissionList value to be assigned to property permissionList
     */
    public void setPermissionList(List<Permission> permissionList) {
        this.permissionList = permissionList;
    }
    
    
}
