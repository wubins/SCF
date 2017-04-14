
package com.scf.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

/** 
 * class 操作工具类
 * @author wubin
 * @date 2016年7月28日 下午4:44:25 
 * @version V1.1.0
 */

public class ClassUtilies {
    
    private static final Logger _logger = LoggerFactory.getLogger(ClassUtilies.class);

    /**
     * 通过反射递归遍历本类至父类，获取指定fieldname的get方法
     * @author jiangxia
     * @param c
     * @param javaFieldName
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public static Method getGetMethodRecursive(Class c, String javaFieldName) throws Exception 
    {
        
        Field[] obj_fields = c.getDeclaredFields();
        
        for (Field field : obj_fields) 
        {
            if(field.getName().equals(javaFieldName))
            {
                PropertyDescriptor pd = new PropertyDescriptor(javaFieldName,  c); 
                Method getMethod = pd.getReadMethod();//获得get方法  
                return getMethod;
            }
        }
        if (c.getSuperclass() != null) 
        {
            return getGetMethodRecursive(c.getSuperclass(), javaFieldName);
        }
        else
        {
            return null;
        }
    }
    
    
    /**
     *
     * @param clazz
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Object newInstance(Class clazz) {
        Object o = null;
        try {
            o = clazz.newInstance();
        } catch (IllegalAccessException ex) {
            _logger.warn("Cant instance object for '" + clazz + "', " + ex.getMessage());
            ExceptionsUtilies.unchecked(ex);
        } catch (InstantiationException ex) {
            _logger.warn("Cant instance object for '" + clazz + "', " + ex.getMessage());
            ExceptionsUtilies.unchecked(ex);
        }
        return o;
    }

    /**
     *
     * @param clazz
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Object newInstance(String clazz) {
        Object o = null;
        Class c = null;
        try {
            c = Class.forName(clazz);
        } catch (ClassNotFoundException ex) {
            _logger.warn("Cant instance object for '" + clazz + "', " + ex.getMessage());
        }
        if (c != null) {
            o = newInstance(c);
        }
        return o;
    }

    /**
     * 取得一个对象的真实类名，主要处理cglib动态代理问题
     *
     * @param obj
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Class getRealClass(Object obj) {
        return AopUtils.getTargetClass(obj);
    }

    /**
     *
     * @param methodName
     * @return
     */
    public static String getMethodProperty(String methodName) {
        String f = methodName;
        if (methodName.startsWith("set") || methodName.startsWith("get")) {
            f = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
        }
        return f;
    }

    /**
     * check give class whether or not basic class type.
     *
     * @param c
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static boolean isBasicClassType(Class c) {
        return c == String.class
                || c == Integer.class
                || c == int.class
                || c == Long.class || c == long.class
                || c == Double.class || c == double.class
                || c == Date.class
                || c == Timestamp.class
                || c == Boolean.class
                || c == boolean.class;
    }

    /**
     * fix the class simple name to property style.
     *
     * @param c
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String getSimpleClassName(Class c) {
        String sname = c.getSimpleName();
        return sname.substring(0, 1).toLowerCase() + sname.substring(1);
    }

    /**
     *
     * @param t
     * @return
     */
    public static String getCurrentClassAndMethod(Throwable t) {
        String cName = t.getStackTrace()[0].getClassName();
        String cMethod = t.getStackTrace()[0].getMethodName();
        return cName + "." + cMethod;
    }
}
