package com.scf.core.context.service;

public interface UserService {
	/**
	 * 
	 * @return
	 * @throws BmoException
	 */
	public long count() throws BmoException;
	
	/**
	 * 
	 * @throws BmoException
	 */
	public void deleteAll() throws BmoException;
	
	/**
	 * 
	 * @throws BmoException
	 */
	public void testTransationCommit() throws BmoException;
	
	/**
	 * 
	 * @throws BmoException
	 */
	public void testTransationRollback() throws BmoException;
}
