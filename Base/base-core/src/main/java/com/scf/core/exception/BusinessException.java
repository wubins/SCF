package com.scf.core.exception;


/**
 * 业务异常
 * 
 * @author  wub
 * @version  [版本号, 2015年6月24日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class BusinessException extends Exception {
	private static final long serialVersionUID = -1;
	
	//一般为code（Integer或者String）
	private Object obj;
	//占位符
	private String[] placeholders;

	public BusinessException() {
		super();
	}

	public BusinessException(Object value) {
	    super();
		this.obj = value;
	}
	
	public BusinessException(Object value,String[] placeholder) {
	    this.placeholders=placeholder;
        this.obj = value;
    }

	public void setValue(Object value) {
		this.obj = value;
	}
	
    /**
     * @return 返回 placeholders
     */
    public String[] getPlaceholders()
    {
        return placeholders;
    }

    /**
     * @param 对placeholders进行赋值
     */
    public void setPlaceholders(String[] placeholders)
    {
        this.placeholders = placeholders;
    }

    @SuppressWarnings("unchecked")
	public <T> T getValue() {
		return (T) this.obj;
	}
}
