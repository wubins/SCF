/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.core;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericCollectionTypeResolver;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.ResolvableType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.util.Log4jConfigurer;
import org.springframework.util.StringUtils;

import com.scf.core.exception.AppException;
import com.scf.core.exception.ExCode;
import com.scf.utils.DatetimeUtilies;

/**
 *
 * @author wub
 */
@ContextConfiguration(locations = {"classpath*:scf/spring-ctx.xml"})
public class EnvTest extends AbstractJUnit4SpringContextTests {
    
    public static int aaa =123;
    
    public List<String> t = new ArrayList<String>();
    
    
    
    
    public <T> T getBean(Class<T> type) {
    	return applicationContext.getBean(type);
    	}
    	 
    	public Object getBean(String beanName) {
    	return applicationContext.getBean(beanName);
    	}
    	 
    	protected ApplicationContext getContext() {
    	return applicationContext;
    	}
    
    @BeforeClass
    public static void setUpClass() {
        try {
            Log4jConfigurer.initLogging("classpath:log4j.properties");
        } catch (FileNotFoundException ex) {
        }
    }

    public static double halfUp(double val,int scale)
    {
	    if (scale < 0)
	    {
	        throw new IllegalArgumentException("The scale must be a positive integer or zero");
	    }
	    
	    BigDecimal   b   =   new   BigDecimal(val); 
	    //保留2位小数
	    double   f1   =   b.setScale(scale,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
	    return f1;
    }
    
    @Test
    public void testEnv() throws ParseException, UnsupportedEncodingException, NoSuchFieldException, SecurityException, NoSuchMethodException, ClassNotFoundException {
    	
    		ApplicationContext ctx = getContext();
    		
    	
            //StringRedisTemplate stringRedisTemplate = ctx.getBean("stringRedisTemplate", StringRedisTemplate.class);
            
     /*       RedisManager redisManager = ctx.getBean("redisManager", RedisManager.class);
            
            MemCachedManager.set("testba", "xxxxxx");
            System.out.println("---------------"+ MemCachedManager.get("testba"));*/

            // String读写
            /*RedisManager.delKey("myStr7777");
            RedisManager.setString("myStr7777", "http://7777.cnblogs.com/");
            System.out.println(RedisManager.getString("myStr7777"));
            System.out.println("---------------");
*/
            // List读写
            //RedisManager.delKey("myList");
            /*stringRedisTemplate.opsForList().rightPush("myList", "A");
            stringRedisTemplate.opsForList().rightPush("myList", "B");
            stringRedisTemplate.opsForList().leftPush("myList", "0");
            List<String> listCache = stringRedisTemplate.opsForList().range(
                    "myList", 0, -1);
            for (String s : listCache) {
                System.out.println(s);
            }*/
            System.out.println("---------------");

            // Set读写
           /* stringRedisTemplate.delete("mySet");
            stringRedisTemplate.opsForSet().add("mySet", "A");
            stringRedisTemplate.opsForSet().add("mySet", "B");
            stringRedisTemplate.opsForSet().add("mySet", "C");
            Set<String> setCache = stringRedisTemplate.opsForSet().members(
                    "mySet");
            for (String s : setCache) {
                System.out.println(s);
            }*/
            System.out.println("---------------");

            // Hash读写
/*            RedisManager.delKey("myHash");
            RedisManager.hashSet("myHash", "PEK", "北京7777");
            RedisManager.hashSet("myHash", "SHA", "上海虹桥7777");
            RedisManager.hashSet("myHash", "PVG", "浦东7777");*/
        /*    Map<Object, Object> hashCache = stringRedisTemplate.opsForHash()
                    .entries("myHash");
            for (Map.Entry<Object, Object> entry : hashCache.entrySet()) {
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }*/

//             HashMap<Integer,List<String>> myMap;
//            ResolvableType t1 = ResolvableType.forField(getClass().getDeclaredField("myMap"));
             Class<List<String>> targetListClass = (Class<List<String>>) Class.forName("java.util.ArrayList");
             List<String> aa = new ArrayList<String>();

            Class  abc = GenericCollectionTypeResolver.getCollectionType(targetListClass);
            t.add("123123");
            System.out.println("----------0----"+ResolvableType.forClass(t.getClass()).asCollection());
            
            System.out.println("----------0111----"+ResolvableType.forClass(t.getClass()).asCollection().resolveGenerics());
           
//            Class clas = GenericCollectionTypeResolver.getCollectionType(List.class);
//            System.out.println("----------1----"+clas2);

    }
    

    
    public <T> T get() {
    	Object aa = "132";
		return (T) aa;
    } 
    
    public static String formatMoney(BigDecimal money, int len)
    {
        NumberFormat formater = null;
        if (len == 0)
        {
            formater = new DecimalFormat("###,###");
        }
        else
        {
            StringBuffer buff = new StringBuffer();
            buff.append("###,##0.");
            for (int i = 0; i < len; i++)
            {
                buff.append("0");
            }
            formater = new DecimalFormat(buff.toString());
            formater.setRoundingMode(RoundingMode.HALF_UP);
        }
        return formater.format(money);
    }
    
    public static List<Date> getSelectableDateTimeList()
    {
        String deliveryCycle = "7";
        
        if (StringUtils.isEmpty(deliveryCycle))
        {
            throw new AppException(ExCode.SYS_002);
        }
        
        Date curDate = DatetimeUtilies.currentDay();
        
        List<Date> selectableDateList = new ArrayList<Date>(Integer.valueOf(deliveryCycle));
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.setTime(curDate);
        
        int i = 0;
        String[] d = {"07","16"};
        while (i < Integer.valueOf(deliveryCycle))
        {
           tomorrow.add(Calendar.DATE, 1);
            for(String deliveryHour : d){
            	 Calendar tomorrowDetail = Calendar.getInstance();
            	 tomorrowDetail.setTime(tomorrow.getTime());
            	 tomorrowDetail.set(Calendar.HOUR_OF_DAY, Integer.parseInt(deliveryHour));
            	  selectableDateList.add(tomorrowDetail.getTime());
            }
            i++;
        }
        
        
        return selectableDateList;
    }
    
    
    // 获取配送时间端数组
    private static Date[] getDeliveryMidHour()
    {
        String deliveryMidHour = "23:00:00,24:00:00";
        //系统配置异常
        if(StringUtils.isEmpty(deliveryMidHour)){
            throw new AppException(ExCode.SYS_002);
        }
        String[]  deliveryMidHourArr =  deliveryMidHour.split(",");
        if(deliveryMidHourArr==null || deliveryMidHourArr.length!=2){
            throw new AppException(ExCode.SYS_002);
        }
        Date[] dateArr = new Date[2];
        for(int i=0;i<deliveryMidHourArr.length;i++)
        {
        	try {
				dateArr[i] = DatetimeUtilies.parse(DatetimeUtilies.TIME,deliveryMidHourArr[i]);
			} catch (ParseException e) {
				 throw new AppException(ExCode.SYS_002);
			}
        }
        return dateArr;
    }
}

interface A{
    public List<String> a(List t) throws RuntimeException;
}

class B implements A{
    @Override
    public ArrayList<String> a( List t){
        return  null;
    }
    
    public static void main(String[] args) {
        B b1 = new B();
        List<String> ASFS= b1.a(new ArrayList<String>());
    }
}
