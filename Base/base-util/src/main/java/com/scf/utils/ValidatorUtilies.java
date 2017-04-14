package com.scf.utils;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
/**
 * 通用校验工具类
 * @author wubin
 * @date 2016年7月28日 下午1:28:38 
 * @version V1.1.0
 */
public class ValidatorUtilies
{
    /**
     * 校验手机号
     * 
     * @param mobile
     * @return
     */
    public static boolean validatorMobile(String mobile)
    {
        String reg = "^\\s*|\\s*$";
        mobile = mobile.replaceAll(reg, "");
        // 电信
        String dReg = "^1[3578][01379]\\d{8}$";
        // 联通
        String lReg = "^1[34578][01256]\\d{8}$";
        // 移动
        String yReg = "^(134[012345678]\\d{7}|1[34578][012356789]\\d{8})$";
        if (StringUtils.isEmpty(mobile))
        {
            return false;
        }
        if (Pattern.matches(dReg, mobile))
        {
            return true;
        }
        else if (Pattern.matches(lReg, mobile))
        {
            return true;
        }
        else if (Pattern.matches(yReg, mobile))
        {
            return true;
        }
        return false;
    }
    
    /**
     * 请输入英文字母和数字,下划线,横线
     * 
     * @param input
     * @return
     */
    public static boolean validatorLetterOrNumOrUnderlineOrDash(String input)
    {
        String reg = "^[a-zA-Z0-9_-]+$";
        if (Pattern.matches(reg, input))
        {
            return true;
        }
        return false;
    }
    
    /**
     * 请输入英文字母或数字
     * 
     * @param input
     * @return
     */
    public static boolean validatorLetterOrNum(String input)
    {
        String reg = "^[a-zA-Z0-9]+$";
        if (Pattern.matches(reg, input))
        {
            return true;
        }
        return false;
    }
    
    /**
     * 请输入中文
     * 
     * @param input
     * @return
     */
    public static boolean validatorChinese(String input)
    {
        String reg = "^[\u4E00-\u9FA5]+$";
        if (Pattern.matches(reg, input))
        {
            return true;
        }
        return false;
    }
    /**
     * 校验数字
     * @author wubin
     * @param number
     * @return
     */
    public static boolean validatorNum(String number)
    {
        String reg = "^[0-9]*[1-9][0-9]*$";
        if (Pattern.matches(reg, number))
        {
            return true;
        }
        return false;
    }
    
    /**
     * 校验邮箱
     * 
     * @param email
     * @return
     */
    public static boolean validatorEmail(String email)
    {
        String reg = "^[A-Za-zd]+([-_.][A-Za-zd]+)*@([A-Za-zd]+[-.])+[A-Za-zd]{2,5}$";
        if (Pattern.matches(reg, email))
        {
            return true;
        }
        return false;
    }
    
    /**
     * 请输入密码，长度8~25位，最少要有1个数字，1个大写字母，1个特殊字符（!@#$%*()_+^&}{:;?.），及若干小写字母
     * 
     * @param password
     * @return
     */
    public static boolean validatorPassword(String password)
    {
        String reg =
            "(?=^.{8,25}$)(?=(?:.*?\\d){1})(?=.*[a-z])(?=(?:.*?[A-Z]){1})(?=(?:.*?[!@#$%*()_+^&}{:;?.]){1})(?!.*\\s)[0-9a-zA-Z!@#$%*()_+^&]*$";
        if (Pattern.matches(reg, password))
        {
            return true;
        }
        return false;
    }
    
    /**
     * 金额校验，正数，最多两位小数
     * 
     * @param money
     * @return
     */
    public static boolean validatorMoney(String money)
    {
        String reg = "^[+]?\\d+(\\.\\d{1,2})?$";
        if (Pattern.matches(reg, money))
        {
            return true;
        }
        return false;
    }
    
    /**
     * 常规输入校验：中英文、数字、中划线、下划线
     * 
     * @param input
     * @return
     */
    public static boolean validatorInput(String input)
    {
        String reg = "^[\u4E00-\u9FA5a-zA-Z0-9_-]+$";
        if (Pattern.matches(reg, input))
        {
            return true;
        }
        return false;
    }
    
    /**
     * 非中文字符校验，可包含特殊字符
     * 
     * @param input
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean validatorNonChinese(String input)
    {
        String reg = "^[\u4E00-\u9FA5]$";
        
        if (StringUtils.isEmpty(input))
        {
            return false;
        }
        
        String temp;
        
        for (int i = 0; i < input.length(); i++)
        {
            temp = String.valueOf(input.charAt(i));
            
            if (Pattern.matches(reg, temp))
            {
                return false;
            }
        }
        
        return true;
    }
    
}
