/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.core.context;

import com.scf.core.exception.sys.SystemException;

/**
 * 上下文持有者
 * @author wub
 *
 */
public class ContextHolder {

    /**
     *
     */
    private static IdentityContext context;

    /**
     *
     * @param ctx
     */
    public synchronized static void initIdentiyContext(IdentityContext ctx) {
        if (context == null) {
            context = ctx;
        } else {
            throw SystemException.contextException("Identity context aleardy inited.", null);
        }
    }

    /**
     *
     * @return
     */
    public static IdentityContext getIdentityContext() {
        if (context == null) {
            throw SystemException.contextException("Identity dontent can not be null, please check.", null);
        }
        return context;
    }
}
