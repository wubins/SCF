
/*   
 * @Title: DateJsonDeserializer.java 
 * @Package: com.scf.core.serializer.json 
 * @author wubin  
 * @date 2016年9月13日 下午5:15:55 
 * @version 1.3.1 
 */


package com.scf.core.serializer.support;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.scf.utils.DatetimeUtilies;
import com.scf.utils.ExceptionsUtilies;

/** 
 * json日期反序列化
 * @author wubin
 * @date 2016年9月13日 下午5:15:55 
 * @version V1.1.0
 */

public class DateTimeJsonDeserializer extends JsonDeserializer<Date> {
      public static final SimpleDateFormat format = new SimpleDateFormat(DatetimeUtilies.DATE_TIME);
         @Override
         public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            try {
                 return format.parse(jsonParser.getText());
             } catch (ParseException e) {
                 throw ExceptionsUtilies.unchecked(e);
             }
         }
}
