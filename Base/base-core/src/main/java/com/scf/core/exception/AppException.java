package com.scf.core.exception;

/**
 * 应用运行时异常
 * 异常必须要有code
 * 
 * @author wubin
 */
public class AppException extends RuntimeException
{

    private static final long serialVersionUID = 5694277643293106939L;
    
    /**
     * 异常明细
     */
    private ExDetail detail;
    
    
    public AppException(int code) {
        this(new ExDetail(code, code+""));
    } 
    
    public AppException(int code,String message) {
    	 this(new ExDetail(code, message,null));
    }
    
    public AppException(int code, Throwable cause) {
        this(code, null,cause);
    }
    
    public AppException(int code,String message, Throwable cause) {
        this( code, message, null, cause);
    }
    
    public AppException(int code,String message,String[] placeholder) {
    	  this( code, message, placeholder, null);
    }
   
    
    public AppException(int code, String message, String[] placeholder, Throwable cause) {
        this(new ExDetail(code, message,placeholder), cause);
    }
    
    protected AppException(ExDetail detail) {
        super(detail.getMessage());
        this.detail = detail;
        
    }
    
    protected AppException(ExDetail detail, Throwable cause) {
        super(detail.getMessage(), cause);
        this.detail = detail;
    }
    
    /**
     * 获取异常明细
     * @return
     */
    public ExDetail getDetail() {
        return detail;
    }

    public void setDetail(ExDetail detail) {
        this.detail = detail;
    }
    
    /**
     * 异常编码
     * @return
     */
    public int getExCode() {
        return this.getDetail().getCode();
    }
    
    /**
     * 异常信息
     * @return
     */
    public String getExMessage() {
        return this.getDetail().getMessage();
    }

    /**
     * 异常信息包括被包装的异常
     */
    @Override
    public String getMessage() {
        if (this.getCause() != null) {
            StringBuffer buf = new StringBuffer();
            if (super.getMessage() != null) {
                buf.append(super.getMessage()).append("; ");
            }
            buf.append("nested exception is ").append(this.getCause());
            return buf.toString();
        }

        return super.getMessage();
    }
}
