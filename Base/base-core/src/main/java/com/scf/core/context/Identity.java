/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.core.context;

import java.util.Map;


/**
 * 唯一标示
 * @author wub
 *
 */
public interface Identity  extends java.io.Serializable{

	public String getId();

	public void setId(String id);

	public Map<String, Object> getDatas();

	public void put(String key, Object value);

	public Object getData(String key);

}
