package com.scf.core.persistence.db.pagination;

import java.io.Serializable;

/**
 * 分页模型,需要设置每一页的数据量和总数据量后即可使用<br/>
 * 每一页的数据量可以设置也可以使用默认值<br/>
 * 
 * 如果rowCount 或 totalPage为-1时表示不考虑总页数和总数据量
 * 
 * @author wub
 *
 */
public class Pagination implements Serializable {
	private static final long serialVersionUID = -2479981776426836075L;

	/**
	 * 默认每一页的数据量
	 */
	public static final int DEFAULT_PAGESIZE = 10;
	
	/**
	 * 每一页的数据量
	 */
	private int pageSize;
	
	/**
	 * 总数据数量
	 */
	private int rowCount;
	
	/**
	 * 当前页数
	 */
	private int currentPage;

	/**
	 * 总页数
	 */
	private int totalPage;
	
	public Pagination() {
		this.pageSize = DEFAULT_PAGESIZE;
		this.currentPage = 1;
		this.totalPage = 0;
		this.rowCount = 0;
	}
	
	/**
	 * 获取当前页数,过滤越线的情况(如果rowCount 或 totalPage为-1时不过滤上界)
	 * 
	 * @return
	 */
	public int getCurrentPage() {
		if (this.currentPage < 1) {
			this.currentPage = 1;
		}
		int totalPage = getTotalPage();
		if (totalPage > 0 && this.currentPage > totalPage) {
			this.currentPage = totalPage;
		}
		return this.currentPage;
	}
	
	/**
	 * 每一页数据量
	 * @return
	 */
	public int getPageSize() {
		if (this.pageSize < 1) {
			this.pageSize = DEFAULT_PAGESIZE;
		}
		return pageSize;
	}

	/**
	 * 总数据条数
	 * @return
	 */
	public int getRowCount() {
		return this.rowCount;
	}
	
	/**
	 * 总页数
	 * @return -1 表示没有设置
	 */
	public int getTotalPage() {
		if (this.rowCount > 0) {
			totalPage = this.rowCount / this.getPageSize() + (this.rowCount % this.getPageSize() == 0 ? 0 : 1);
			totalPage = totalPage < 1 ? 1 : totalPage;
		}
		return this.totalPage;
	}
	
	/**
	 * 获取当前页数据库的偏移值
	 * 
	 * @return
	 */
	public int getOffset() {
		return (this.getCurrentPage() - 1) * this.getPageSize();
	}
	
	/**
	 * 获取当前页数据库的limit值
	 * 
	 * @return
	 */
	public int getLimit() {
		return this.getPageSize();
	}
	
	/**
	 * 如果rowCount 或 totalPage为-1时永远返回true
	 * @return the hasNext
	 */
	public boolean hasNext() {
		return this.getTotalPage() > 0 ? this.getCurrentPage() < this.getTotalPage() : true;
	}

	/**
	 * @return the hasPrev
	 */
	public boolean hasPrev() {
		return this.getCurrentPage() > 1;
	}

	// setting...
	
	/**
	 * 设置当前页数
	 * @param currentPage
	 * @return 
	 */
	public Pagination setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		return this;
	}

	/**
	 * 设置总数据量的时候，初始化总页数，如果每页数据量没有设定就取默认值10
	 * 
	 * @param rowCount
	 * @return 
	 */
	public Pagination setRowCount(int rowCount) {
		this.rowCount = rowCount;
		return this;
	}

	/**
	 * 每一页数据量
	 * 
	 * @param pageSize
	 * @return 
	 */
	public Pagination setPageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}
}
