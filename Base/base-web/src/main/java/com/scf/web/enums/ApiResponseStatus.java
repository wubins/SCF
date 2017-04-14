package com.scf.web.enums;

/**
 * 
 * 接口访问返回提示码
 * 
 * @author wub
 * @version [版本号, 2015年7月1日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public enum ApiResponseStatus {
    
    
    //========简单校验错误返回码========
    /**
     * 接口鉴权失败
     */
    IP_AUTH_FAIL(1002, "非授信IP"),

    /**
     * 接口鉴权失败
     */
    AUTH_FAIL(1003, "非授信接口"),
    /**
     * 参数未知
     */
    PARAM_UNKNOWN(1004, "未知参数错误"),
    /**
     * 参数为空
     */
    PARAM_NULL(1005, "必填参数为空"),
    /**
     * 参数范围错误
     */
    PARAM_ERROR(1006, "参数范围错误"),
    
    /**
     * 接口不合法
     */
    INTERFACE_ILLEGAL(1007, "接口不合法"),

    /**
     * 参数不合法
     */
    PARAM_ILLEGAL(1008, "参数不合法"),

    /**
     * 默认未知异常
     */
    UNKNOWN_ERROR(9999, "默认未知异常");

    private Integer code;

    private String msg;

    // 通过订单code查询对应的中文
    public static String getValueByCode(Integer codeTemp) {
        ApiResponseStatus[] arr = ApiResponseStatus.values();
        for (ApiResponseStatus apiResponseStatus : arr) {
            if (codeTemp.equals(apiResponseStatus.getCode())) {
                return apiResponseStatus.getMsg();
            }
        }
        return null;
    }

    private ApiResponseStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
