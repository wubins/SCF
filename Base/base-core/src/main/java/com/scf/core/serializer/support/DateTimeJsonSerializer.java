
/*   
 * @Title: DateJsonSerializer.java 
 * @Package: com.scf.core.serializer.json 
 * @author wubin  
 * @date 2016年9月13日 下午5:14:56 
 * @version 1.3.1 
 */


package com.scf.core.serializer.support;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.scf.utils.DatetimeUtilies;

/** 
 * json日期序列化
 * @author wubin
 * @date 2016年9月13日 下午5:14:56 
 * @version V1.1.0
 */

public class DateTimeJsonSerializer extends JsonSerializer<Date> {
      public static final SimpleDateFormat format = new SimpleDateFormat(DatetimeUtilies.DATE_TIME);
         @Override
         public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeString(format.format(date));
      }
}
