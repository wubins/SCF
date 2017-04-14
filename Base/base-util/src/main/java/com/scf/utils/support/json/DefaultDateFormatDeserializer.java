
/*
 * @Title: DateFormatDeserializer.java
 * 
 * @Package: com.scf.utils.support.json
 * 
 * @author wubin
 * 
 * @date 2016年8月22日 上午11:52:54
 * 
 * @version 1.3.1
 */

package com.scf.utils.support.json;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.DateFormatDeserializer;

/**
 * fast json 日期反序列化处理器
 * @author wubin
 * @date 2016年8月22日 上午11:52:54
 * @version V1.1.0
 */

public class DefaultDateFormatDeserializer extends DateFormatDeserializer {
    /** 日期格式 */
    private String format;

    /**
     * 
     * @param format
     */
    public DefaultDateFormatDeserializer(String format) {
        super();
        this.format = format;
    }

    /**
     * @see com.alibaba.fastjson.parser.deserializer.DateFormatDeserializer#cast(com.alibaba.fastjson.parser.DefaultJSONParser,
     * java.lang.reflect.Type, java.lang.Object, java.lang.Object)
     */

      /**
     * @param parser
     * @param clazz
     * @param fieldName
     * @param val
     * @return 
     * @see com.alibaba.fastjson.parser.deserializer.DateFormatDeserializer#cast(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.lang.Object, java.lang.Object) 
     */ 
    @SuppressWarnings({ "unchecked"})
    @Override
    protected <T> T cast(DefaultJSONParser parser, Type clazz, Object fieldName, Object val) {
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        if (val instanceof String) {
            String strVal = (String) val;
            if (strVal.length() == 0) {
                return null;
            }

            try {
                return (T) new SimpleDateFormat(format).parse((String) val);
            } catch (ParseException e) {
                throw new JSONException("parse error");
            }
        }
        throw new JSONException("parse error");
    }

    /**
     * Getter method for property <tt>myFormat</tt>.
     * 
     * @return property value of myFormat
     */
    public String getMyFormat() {
        return format;
    }

    /**
     * Setter method for property <tt>myFormat</tt>.
     * 
     * @param myFormat value to be assigned to property myFormat
     */
    public void setMyFormat(String myFormat) {
        this.format = myFormat;
    }

}
