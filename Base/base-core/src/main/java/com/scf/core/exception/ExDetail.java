package com.scf.core.exception;

/**
 * 异常明细，由异常编码和异常信息组成<br/>
 * 异常编码用于确定异常的类型,用于程序处理和用户提示信息<br/>
 * 异常信息一般用于调试和日志,而不是用于提示用户的信息，同Exception的message
 * 
 * @author  wub
 * @version  [版本号, 2015年6月23日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ExDetail
{

    /**
     * 异常编码
     */
    private int code;
    
    /**
     * 编码占位符
     */
	private String[] placeholders;
    
    /**
     * 异常信息,一般用于调试和日志,而不是用于提示用户的信息
     */
    private String message;
    
    
    public ExDetail(int code) {
        this(code,null);
    }
    
    public ExDetail(int code,String message) {
        this(code, message,null);
    }

    public ExDetail(int code, String message,String[] placeholder) {
        super();
        this.code = code;
        this.message = message;
        this.placeholders = placeholder;
    }
    
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    

    public String[] getPlaceholders() {
		return placeholders;
	}

	public void setPlaceholders(String[] placeholders) {
		this.placeholders = placeholders;
	}

	/**
     * 获取描述
     * 
     * @return
     */
    @Override
    public String toString() {
        return new StringBuffer().append("[code=").append(code).append(", message=").append(message).append("]").toString();
    }
}
