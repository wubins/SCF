/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.core.persistence.db.dao;

import java.util.List;

/**
 *
 * @author wub
 */
public class Page {

    private long results;
    private int size;
    private int start;
    private List<?> rows;
    
    public Page(){}
    
    /** 
     * @param results - 总条数。
     * @param start - 当前分页开始序号。
     * @param size - 单页最大展示数量。
     * @param rows - 查询结果集。
     */
    public Page(long results, int start, int size, List<?> rows){
        this.results = results;
        this.size = size;
        this.start = start;
        this.rows = rows;
    }

    /**
     * @return the results
     */
    public long getResults() {
        return results;
    }

    /**
     * @param results the results to set
     */
    public void setResults(long results) {
        this.results = results;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return the start
     */
    public int getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * @return the rows
     */
    public List<?> getRows() {
        return rows;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
