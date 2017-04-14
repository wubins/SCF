package com.scf.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.JSONPObject;

/**
 * 简单封装Jackson，实现JSON String<->Java Object的Mapper. 封装不同的输出风格, 使用不同的builder函数创建实例.
 * 
 * @author wub
 */
public class JacksonObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = 1L;

    private static Logger logger = LoggerFactory.getLogger(JacksonObjectMapper.class);

    private static JacksonObjectMapper mapper;

    public JacksonObjectMapper()
    {
        //属性为 空（“”） 或者为 NULL 都不序列化，建议在外部接口中使用
        this(Include.NON_EMPTY);
        //注册全局默认的jackson时间序列化、反序列化格式，注意，默认的时间格式只能设置一种，如果默认格式不能满足对应的格式，jackson会使用StdDateFormat 
        //来操作，它默认支持 yyyy-MM-dd等格式，如果它也不能满足，请自己手动添加jackson的序列化和反序列化注解:@JsonDeserialize(using = DateTimeJsonDeserializer.class)
        this.setDateFormat(new SimpleDateFormat(DatetimeUtilies.DATE_TIME));
        //this(Include.ALWAYS);
    }

    public JacksonObjectMapper(Include include)
    {
        //设置 序列化 bean的时候对于没有可以序列化的bean(一般是没有javabean常规的get方法同时没有jackson属性注解的情况)不会抛出异常
        //disable后返回empty对象：a对象里面有个类型为B的属性b，则返回{b:{}}
        this.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        //javabean 必须有get、set方法的属性才能被反序列化或者序列化
         this.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NONE);

        //map的某个entiry里的key对应的value为null的时候时候序列化这个entry
        this.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);

        // 设置输出时包含属性的风格
        if (include != null)
        {
            this.setSerializationInclusion(include);
        }
        // 允许单引号、允许不带引号的字段名称
        this.enableSimple();
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 空值处理为空串，和构造里的 Include.NON_EMPTY(“属性为 空（“”） 或者为 NULL 都不序列化”)是冲突的,这里不起作用
        this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>()
        {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException
            {
                jgen.writeString("");
            }
        });
        // 进行HTML解码。
        /*
         * this.registerModule(new SimpleModule().addSerializer(String.class, new JsonSerializer<String>(){
         * 
         * @Override public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws
         * IOException, JsonProcessingException { jgen.writeString(StringEscapeUtils.unescapeHtml4(value)); } }));
         */
        // 设置时区
        this.setTimeZone(TimeZone.getDefault());// getTimeZone("GMT+8:00")
    }

    /**
     * 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper,建议在外部接口中使用.
     */
    public static JacksonObjectMapper getInstance() {
        if (mapper == null) {
            mapper = new JacksonObjectMapper().enableSimple();
        }
        return mapper;
    }

    /**
     * 创建只输出初始值被改变的属性到Json字符串的Mapper, 最节约的存储方式，建议在内部接口中使用。
     */
    public static JacksonObjectMapper nonDefaultMapper() {
        if (mapper == null) {
            mapper = new JacksonObjectMapper(Include.NON_DEFAULT);
        }
        return mapper;
    }

    /**
     * Object可以是POJO，也可以是Collection或数组。 如果对象为Null, 返回"null". 如果集合为空集合, 返回"[]".
     */
    public String toJson(Object object, String dateFormat) {
        try {
            this.setDateFormat(new SimpleDateFormat(dateFormat));
            return this.writeValueAsString(object);
        } catch (IOException e) {
            logger.warn("write to json string error:" + object, e);
            return null;
        }
    }

    public  String toJson(Object object) {
        try {
            return this.writeValueAsString(object);
        } catch (IOException e) {
            logger.warn("write to json string error:" + object, e);
            return null;
        }
    }

    /**
     * 反序列化POJO或简单Collection如List<String>.
     * 
     * 如果JSON字符串为Null或"null"字符串, 返回Null. 如果JSON字符串为"[]", 返回空集合.
     * 
     * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String,JavaType)
     * 
     * @see #fromJson(String, JavaType)
     */
    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return this.readValue(jsonString, clazz);
        } catch (IOException e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }
   

    /**
     * 反序列化复杂Collection如List<Bean>, 先使用函數createCollectionType构造类型,然后调用本函数.
     * 
     * @see #createCollectionType(Class, Class...)
     */
    @SuppressWarnings("unchecked")
    public <T> T fromJson(String jsonString, JavaType javaType) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return (T) this.readValue(jsonString, javaType);
        } catch (IOException e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    public <T> T fromJson(String jsonString, TypeReference<T> valueTypeRef) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return (T) this.readValue(jsonString, valueTypeRef);
        } catch (IOException e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 构造泛型的Collection Type如：ArrayList<MyBean>, 则调用constructCollectionType(ArrayList.class,MyBean.class)
     * HashMap<String,MyBean>, 则调用(HashMap.class,String.class, MyBean.class)
     */
    public JavaType createCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return this.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * 当Json里只包含Bean的部分属性时，更新一个已经存在的Bean，只覆盖该部分的属性
     */
    @SuppressWarnings("unchecked")
    public <T> T update(String jsonString, T object) {
        try {
            return (T) this.readerForUpdating(object).readValue(jsonString);
        } catch (JsonProcessingException e) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        } catch (IOException e) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        }
        return null;
    }

    /**
     * 输出jsonp格式数据
     */
    public String toJsonP(String functionName, Object object) {
        return toJson(new JSONPObject(functionName, object));
    }

    /**
     * 设定是否使用Enum的toString函数来读写Enum，为False时使用Enum的name()函数来读写Enum，默认为False，注意本函数一定要在mapper创建后,所有的读写动作之前调用
     */
    public JacksonObjectMapper enableEnumUseToString() {
        this.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        this.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        return this;
    }

    /**
     * 支持使用Jaxb的Annotation，使得POJO上的annotation不用与Jackson耦合。 默认会先查找jaxb的annotation，如果找不到再找jackson的。
     */
    /*
     * public JsonMapper enableJaxbAnnotation() { JaxbAnnotationModule module = new JaxbAnnotationModule();
     * this.registerModule(module); return this; }
     */

    /**
     * 允许单引号 允许不带引号的字段名称
     */
    public JacksonObjectMapper enableSimple() {
        this.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        this.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        return this;
    }

    /**
     * 取出Mapper做进一步的设置或使用其他序列化API.
     */
    public ObjectMapper getMapper() {
        return this;
    }

    /**
     * 对象转换为JSON字符串
     * 
     * @param object
     * @return
     */
    public static String toJsonString(Object object) {
        String format = "yyyy-MM-dd HH:mm:ss";
        return toJsonString(object, format);
    }

    public static String toJsonString(Object object, String dateFormat) {
        return JacksonObjectMapper.getInstance().toJson(object, dateFormat);
    }

    /**
     * JSON字符串转换为对象
     * 
     * @param jsonString
     * @param clazz
     * @return
     */
    public static <T> T fromJsonString(String jsonString, Class<T> clazz) {
        return (T)JacksonObjectMapper.getInstance().fromJson(jsonString, clazz);
    }

    /**
     * <一句话功能简述>
     * <功能详细描述>
     * 
     * @param jsonString
     * @param javaType
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Object fromJsonString(String jsonString, JavaType javaType) {
        return JacksonObjectMapper.getInstance().fromJson(jsonString, javaType);
    }

    /**
     * 带泛型的json反序列化
     * @author wubin
     * @param jsonString
     * @param valueTypeRef
     * @return
     */
    public static <T> T fromJsonString(String jsonString, TypeReference<T> valueTypeRef) {
        return JacksonObjectMapper.getInstance().fromJson(jsonString, valueTypeRef);
    }
    
    /**
     * 获取复杂集合（包括map，比如：Map<List<String>）的JavaType
     * 
     * @param collectionClass
     * @param elementClasses
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return JacksonObjectMapper.getInstance().getTypeFactory().constructParametricType(collectionClass,
                elementClasses);
    }
}
