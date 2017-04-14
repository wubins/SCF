package com.scf.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * String工具类
 * @author wubin
 * @date 2016年7月28日 下午1:36:20 
 * @version V1.1.0
 */
public class StringUtilies {
	
	/**
     * 获取一定长度的随机字符串
     * 
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomStringByLength(int length)
    {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++)
        {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
    
   
    

    /**
     * 字符串是否为NULL或空
     *
     * @param s
     * @return
     */
    public static boolean isNullOrEmpty(String s) {
        boolean y = false;
        if (s == null) {
            y = true;
        } else if (s.trim().equals("")) {
            y = true;
        }
        return y;
    }


    /**
     * 将一个字符串转为整型
     *
     * @param str
     * @param defaultValue
     * @return
     */
    public static int string2Int(String str, int defaultValue) {
        int v = defaultValue;
        try {
            if (!StringUtilies.isNullOrEmpty(str)) {
                v = Integer.parseInt(str.trim());
            }
        } catch (NumberFormatException ex) {
            v = defaultValue;
        }
        return v;
    }

    /**
     *
     * @param str
     * @param defaultValue
     * @return
     */
    public static double string2Number(String str, double defaultValue) {
        double v = defaultValue;
        try {
            if (!StringUtilies.isNullOrEmpty(str)) {
                v = Double.parseDouble(str.trim());
            }
        } catch (NumberFormatException ex) {
            v = defaultValue;
        }
        return v;
    }

    /**
     *
     * @param str
     * @param defaultValue
     * @return
     */
    public static boolean string2Boolean(String str, boolean defaultValue) {
        try {
            return Boolean.parseBoolean(str.trim());
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * 连接数组中的字符串
     *
     * @param array
     * @param split
     * @return
     */
    public static String conArray(String[] array, String split) {
        StringBuilder str = new StringBuilder();
        if (array != null) {
            for (String a : array) {
                str.append(",").append(a);
            }
        }
        return str.deleteCharAt(0).toString();
    }

    /**
     * compress give string use zip format.
     *
     * @param str
     * @return
     * @throws IOException
     */
    public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes("utf-8"));
        gzip.close();
        return out.toString("ISO-8859-1");
    }

    /**
     * uncompress give string use zip format.
     *
     * @param str
     * @return
     * @throws IOException
     */
    public static String uncompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        return out.toString("utf-8");
    }

    /**
     * format a string by give parameters.
     *
     * @param msg for example, 'come on, ${user}, let's ${act}.'
     * @param params for example, {user='john', act='moving'}
     * @return formatted string.
     */
    public static String format(String msg, Map<String, Object> params) {
        StringBuffer sb = new StringBuffer();
        Pattern p = Pattern.compile("\\$\\{\\w+\\}");
        Matcher matcher = p.matcher(msg);
        while (matcher.find()) {
            String ms = matcher.group();
            String _ms = ms.replaceAll("\\{", "").replaceAll("\\}", "");
            matcher.appendReplacement(sb, params.containsKey(_ms) ? params.get(_ms).toString() : ms.substring(1));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
    
    /**
     * string的格式化
     * format:你好，%s
     * args:jack
     * 结果：你好，jack
     * @author wubin
     * @param format
     * @param args
     * @return
     */
    @SuppressWarnings("resource")
    public static String format(String format, String... args) {
        Formatter fmt = new Formatter();
        return fmt.format(format, args).toString();
    }
    

  
    /**
     * 将list转换为逗号分隔的字符串
     * @param list
     * @return
     */
    public static String listToString(List<String> list) {  
        StringBuilder sb = new StringBuilder();  
        if (list != null && list.size() > 0) {  
            for (int i = 0; i < list.size(); i++) {  
                if (i < list.size() - 1) {  
                    sb.append(list.get(i) + ",");  
                } else {  
                    sb.append(list.get(i));  
                }  
            }  
        }  
        return sb.toString();  
    }  
}
