/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.core.context;

/**
 * 唯一标示上下文
 * @author wub
 *
 */
public interface IdentityContext {

    public Identity getIdentity();

    public void setIdentity(Identity identity);

}
