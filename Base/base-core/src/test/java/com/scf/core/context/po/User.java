package com.scf.core.context.po;

/**
 * 用户实体类
 * 
 *
 */
public class User {
	private int id;
	
	/** 用户名 */
	private String username;
	
	/** 用户密码 */
	private String password;
	
	/** 用户描述 */
	private String desc;

	public User() {
		super();
	}

	public User(String username, String password, String desc) {
		super();
		this.username = username;
		this.password = password;
		this.desc = desc;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return new StringBuilder("User{").append(id).append(", ").append(username).append(", ").append(password).append(", ").append(desc).append("}").toString();
	}
}
