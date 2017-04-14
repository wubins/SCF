/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.core.persistence.db.dao;

import com.scf.core.exception.AppException;
import com.scf.core.exception.ExCode;

/**
 * dao异常
 * @author wub
 */
public class DaoException extends AppException {

    
    /** @Fields serialVersionUID: */
        
    private static final long serialVersionUID = 3123363884205879453L;

    public DaoException(int code,String message, Throwable cause) {
        super(code,message,cause);
    }
    
    public DaoException(int code, Throwable cause) {
        super(code,cause);
    }
    /**
     * 默认db异常
     * @author wubin
     * @param message
     * @param cause
     * @return
     */
    public static DaoException defaultDBException(Throwable cause){
        DaoException daoException = new DaoException(ExCode.DB_001,"database访问异常",cause);
        return daoException;
    }
    
    

}
