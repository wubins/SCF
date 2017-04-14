/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.core.persistence.db.dao.mongodb;

import org.mongodb.morphia.dao.DAO;
import org.mongodb.morphia.query.Query;

import com.scf.core.persistence.db.pagination.PaginationOrdersList;

public interface IMongodbDao<T> extends DAO<T, String> {
    
    /**
     * 分页查询
     * 
     * @param query 查询条件
     * @param start 当页其实条目
     * @param pageSize 每页最大展示数
     * @return 分页对象
     */
    PaginationOrdersList<T> findPagination(Query<T> query, PaginationOrdersList<T> paginationOrdersList);
    
}
