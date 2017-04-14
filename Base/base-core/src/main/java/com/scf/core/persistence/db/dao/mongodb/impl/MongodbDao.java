/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.core.persistence.db.dao.mongodb.impl;

import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import com.scf.core.persistence.db.dao.mongodb.IMongodbDao;
import com.scf.core.persistence.db.pagination.PaginationOrdersList;
public class MongodbDao<T> extends BasicDAO<T, String> implements IMongodbDao<T>  {

    public MongodbDao(Datastore ds) {
        super(ds);
    }

    @Override
    public PaginationOrdersList<T> findPagination(Query<T> query, PaginationOrdersList<T> paginationOrdersList) {
        long totalCount = count(query);
        List<T> list = find(query.offset(paginationOrdersList.getPagination().getOffset()).limit(paginationOrdersList.getPagination().getPageSize())).asList();
        paginationOrdersList.setDatas(list);
        paginationOrdersList.getPagination().setRowCount((int)totalCount);
        return paginationOrdersList;
    }

}
