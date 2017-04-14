package com.scf.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * 数值工具类
 * @author wubin
 * @date 2016年7月28日 下午1:55:09 
 * @version V1.1.0
 */
public class NumberUtilies
{
    
    // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;
    //数字转换
    public static final String[] hanziArr = new String[] { "个", "十", "百", "千", "万", "十", "百", "千", "亿" };
    public static final String[] numberArr = new String[] { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "点" };
    
    /**
     * 四舍五入，保留两位小数
     * 
     * @param val，要格式化的值
     * @return
     */
    public static String halfUp2Scale(double val)
    {
        
        // 保留2位小数
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(val);
    }
    
    /**
     * 四舍五入，保留两位小数
     * 
     * @param val，要格式化的值  BigDecimal
     * @return
     */
    public static String halfUp2Scale(BigDecimal val)
    {
        
        // 保留2位小数
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(val);
    }
    
    /**
     * 四舍五入，保留四位小数
     * 
     * @param val，要格式化的值
     * @return
     */
    public static String halfUp4Scale(double val)
    {
        
        // 保留2位小数
        DecimalFormat df = new DecimalFormat("0.0000");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(val);
    }
    
    /**
     * 四舍五入，保留四位小数
     * 
     * @param val，要格式化的值 BigDecimal
     * @return
     */
    public static String halfUp4Scale(BigDecimal val)
    {
        
        // 保留2位小数
        DecimalFormat df = new DecimalFormat("0.0000");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(val);
    }
    
    /**
     * 加法计算BigDecimal
     * 
     * @param v1 BigDecimal
     * @param v2 BigDecimal
     * @return
     */
    public static BigDecimal add(BigDecimal v1, BigDecimal v2)
    {
        return v1.add(v2);
    }
    
    /**
     * 加法计算
     * 
     * @param v1
     * @param v2
     * @return
     */
    public static double add(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }
    
    /**
     * 提供精确的减法运算。
     * 
     * @param v1 被减数 BigDecimal
     * @param v2 减数 BigDecimal
     * @return 两个参数的差
     */
    public static BigDecimal sub(BigDecimal v1, BigDecimal v2)
    {
        return v1.subtract(v2);
    }
    
    /**
     * 提供精确的减法运算。
     * 
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }
    
    /**
     * 提供精确的乘法运算。
     * 
     * @param v1 被乘数 BigDecimal
     * @param v2 乘数 BigDecimal
     * @return 两个参数的积
     */
    public static BigDecimal mul(BigDecimal v1, BigDecimal v2)
    {
        return v1.multiply(v2);
    }
    
    /**
     * 提供精确的乘法运算。
     * 
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }
    
    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     * 
     * @param v1 被除数 BigDecimal
     * @param v2 除数 BigDecimal
     * @return 两个参数的商
     */
    public static BigDecimal div(BigDecimal v1, BigDecimal v2)
    {
        return div(v1, v2, DEF_DIV_SCALE);
    }
    
    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     * 
     * @param v1 被除数 BigDecimal
     * @param v2 除数 BigDecimal
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale)
    {
        if (scale < 0)
        {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        return v1.divide(v2, scale, BigDecimal.ROUND_HALF_UP);
    }
    
    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     * 
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2)
    {
        return div(v1, v2, DEF_DIV_SCALE);
    }
    
    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     * 
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale)
    {
        if (scale < 0)
        {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    /**
     * 获取指定范围内的随机数
     *
     * @param begin 起始数
     * @param end 最大数
     * @return 随机数
     */
    public static int getRandom(int begin, int end)
    {
        if (end <= begin)
        {
            throw new IllegalArgumentException("'end' must be greater than 'begin'.");
        }
        double random = Math.random();
        return (int)(random * (end - begin) + begin);
    }
    
    /**
     * 将double转为BigDecimal，不进行四舍五入
     *
     * @param value
     * @param scale 小数位数
     * @return
     */
    public static BigDecimal toBigDecimalDown(double value, int scale)
    {
        return toBigDecimalDown(Double.toString(value), scale);
    }
    
    /**
     *
     * @param value
     * @param scale
     * @return
     */
    public static BigDecimal toBigDecimalDown(String value, int scale)
    {
        return new BigDecimal(value).setScale(scale, BigDecimal.ROUND_HALF_DOWN);
    }
    
    /**
     * 四舍五入，格式化double为保留两位小数，不足2位补0
     * 
     * @param value
     * @param scale
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String format2DoubleUp(double value)
    {
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(value);
    }
    
    /**
     * 四舍五入，格式化double为保留两位小数，不足2位补0
     * 
     * @param value
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String format2BigDecimalUp(BigDecimal value)
    {
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(value);
    }
    
    /**
     * 四舍五入，格式化double为保留四位小数，不足2位补0
     * 
     * @param value
     * @param scale
     * @return
     * @see [类、类#方法、类#成员]
     */
    
    public static String format4DoubleUp(double value)
    {
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.0000");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(value);
    }
    
    /**
     * 四舍五入，格式化double为保留两位小数，不足2位补0
     * 
     * @param value
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String format4BigDecimalUp(BigDecimal value)
    {
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.0000");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(value);
    }
    
    
    /**
     * 将double转为BigDecimal，进行四舍五入
     *
     * @param value BigDecimal
     * @param scale
     * @return
     */
    public static BigDecimal toBigDecimalUp(BigDecimal value, int scale)
    {
        return toBigDecimalUp(value.toString(), scale);
    }
    
    /**
     * 将double转为BigDecimal，进行四舍五入
     *
     * @param value
     * @param scale
     * @return
     */
    public static BigDecimal toBigDecimalUp(double value, int scale)
    {
        return toBigDecimalUp(Double.toString(value), scale);
    }
    
    /**
     *
     * @param value
     * @param scale
     * @return
     */
    public static BigDecimal toBigDecimalUp(String value, int scale)
    {
        return new BigDecimal(value).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }
    
    /**
     * 判断是否为整数
     *
     * @param value
     * @return
     */
    public static boolean isInt(String value)
    {
        try
        {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException ex)
        {
            return false;
        }
    }
    
    /**
     * 判断是否为double
     *
     * @param value
     * @return
     */
    public static boolean isDouble(String value)
    {
        try
        {
            Double.parseDouble(value);
            return true;
        }
        catch (NumberFormatException ex)
        {
            return false;
        }
    }
    
    
    /**
     * double千分号处理
     * 
     * @param number  (double)
     * @param len 小数位数
     * @return
     */
    public static String formatNumberForDB(Double number, int len)
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
        return formater.format(number);
    }
    
    /**
     * bigdecimal千分号处理
     * 
     * @param number  (BigDecimal)
     * @param len 小数位数
     * @return
     */
    public static String formatThousandForBD(BigDecimal number, int len)
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
        return formater.format(number);
    }
    
    /**
     *
     * @param d1
     * @param d2
     * @return
     */
    public static boolean numberEquals(Double d1, Double d2)
    {
        boolean eq = true;
        if ((d1 != null && d2 == null) || (d2 != null && d1 == null))
        {
            eq = false;
        }
        else if (d1 != null && d2 != null)
        {
            eq = d1.equals(d2);
        }
        return eq;
    }
    
    public static boolean isBalance(String str)
    {
        java.util.regex.Pattern pattern =
            java.util.regex.Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        java.util.regex.Matcher match = pattern.matcher(str);
        if (match.matches() == false)
        {
            return false;
        }
        else
        {
            double balance = Double.parseDouble(str);
            if (balance <= 0)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }
    
    /**
     * 获取固定长度的随机数字
     * 
     * @param length 数字长度
     * @return
     */
    public static String getRandomNumber(int length)
    {
        Random rm = new Random();
        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, length);
        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);
        // 返回固定的长度的随机数
        return fixLenthString.substring(1, length + 1);
    }
    
    public static void main(String[] args) {

		Integer quantity =30;
		int[] aa =NumberUtilies.randomArray(0, quantity-1 ,quantity);
	    for (int e : aa){
	    	  System.out.println(e);
	     }
    
    }
    
    /**
     * 判断参数是不是正整数
     * 
     * @param str
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean isSignlessInteger(String str)
    {
        Pattern pattern = Pattern.compile("^[0-9]*[1-9][0-9]*$");
        return pattern.matcher(str).matches();
    }
    
    
       
       /**
   	 * 随机指定范围内N个不重复的数
   	 * 在初始化的无重复待选数组中随机产生一个数放入结果中，
   	 * 将待选数组被随机到的数，用待选数组(len-1)下标对应的数替换
   	 * 然后从len-2里随机产生下一个随机数，如此类推
   	 * @param max  指定范围最大值
   	 * @param min  指定范围最小值
   	 * @param n  随机数个数
   	 * @return int[] 随机数结果集
   	 */
   	public static int[] randomArray(int min,int max,int n){
   		int len = max-min+1;
   		
   		if(max < min || n > len){
   			return null;
   		}
   		
   		//初始化给定范围的待选数组
   		int[] source = new int[len];
           for (int i = min; i < min+len; i++){
           	source[i-min] = i;
           }
           
           int[] result = new int[n];
           Random rd = new Random();
           int index = 0;
           for (int i = 0; i < result.length; i++) {
           	//待选数组0到(len-2)随机一个下标
               index = Math.abs(rd.nextInt() % len--);
               //将随机到的数放入结果集
               result[i] = source[index];
               //将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
               source[index] = source[len];
           }
           return result;
   	}
   	
   	
   	
   	/**
     * 数字转化：   10－>十   1->一    11->十一
     * @author xiaotao
     * @param integer
     * @return
     */
    public static String toHanzi(String integer) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < integer.length(); i++) {
            char c = integer.charAt(i);
            if(i==integer.length()-1 && c == '0'){
               
            }else{
                if(c == '0'){
                    result.append(numberArr[0]);
                } else {
                    result.append(numberArr[Integer.parseInt(String.valueOf(c))]);
                    if (i != integer.length() - 1)
                        result.append(hanziArr[integer.length() - 1 - i]);
                }
            }
        }
        if(result.length()>1 && "一".equals(result.substring(0, 1))){
            return result.substring(1, result.length());
        }else{
            return result.toString();
        }
        
    }
   	
   	
   	
}
