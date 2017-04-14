
package com.scf.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/** money 操作工具类
 * @author wubin
 * @date 2016年7月28日 下午1:39:06 
 * @version V1.1.0
 */

public class MoneyUtil {
    
    private static final char[] cnNumbers = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'};
    
    private static final char[] cnSeries = {'点', '拾', '百', '仟', '万', '拾', '百', '仟', '亿'};
    
    private static final char[] cnYuans = {'元', '角', '分', '厘'};
    /**
     * 金额转化为分为单位
     * 
     * @param money 单位:元
     * @return
     */
    public static Integer formatMoneyToCent(String money)
    {
        float sessionmoney = Float.parseFloat(money);
        String finalmoney = String.format("%.2f", sessionmoney);
        finalmoney = finalmoney.replace(".", "");
        return Integer.parseInt(finalmoney);
    }
    
    
    
   public static String number2str(double n, int scale, boolean up)
   {
       StringBuilder sb = new StringBuilder("0.");
       for (int i = 0; i < scale; i++)
       {
           sb.append("0");
       }
       DecimalFormat df = new DecimalFormat(sb.toString());
       if (up)
       {
           df.setRoundingMode(RoundingMode.HALF_UP);
       }
       return df.format(n);
   }

    /**
     * fromat 123.054 to 壹百贰拾叁点零伍肆元 或 壹百贰拾叁元零角伍分。
     *
     * 注意目前的友好格式仅支持到 9亿。
     *
     * @param n
     * @param scale 精度(如果想使用角分的方式，传入2)
     * @return
     */
    public static String cnYuan(double n, int scale)
    {
        if (n > 999999999)
        {
            throw new IllegalArgumentException("Input amount must be less than 1000000000. ");
        }
        String ns1 = number2str(n, scale, true);
        String ns2 = "";
        int _c = ns1.indexOf(".");
        if (_c > -1)
        {
            ns2 = ns1.substring(_c + 1);
            ns1 = ns1.substring(0, _c);
            while (ns2.endsWith("0"))
            {
                ns2 = ns2.substring(0, ns2.length() - 1);
            }
        }
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < ns1.length(); i++)
        {
            char c = ns1.charAt(i);
            int cc = Integer.parseInt(String.valueOf(c));
            if (cc == 0 && ns1.charAt(i - 1) != '0')
            {
                boolean b = false;
                int j = i;
                for (; j < ns1.length(); j++)
                {
                    if (ns1.charAt(j) != '0')
                    {
                        b = true;
                        break;
                    }
                }
                if (b)
                {
                    int l = ns1.length() - j;
                    if (l == 4)
                    {
                        str.append(cnSeries[l]);
                    }
                    str.append(cnNumbers[cc]);
                }
            }
            else if (cc > 0)
            {
                str.append(cnNumbers[cc]);
                int l = ns1.length() - 1 - i;
                if (l >= 1 && l < cnSeries.length)
                {
                    str.append(cnSeries[l]);
                }
            }
        }
        if (_c > -1)
        {
            if (ns2.length() > 2)
            {
                str.append(cnSeries[0]);
                for (int i = 0; i < ns2.length(); i++)
                {
                    char c = ns2.charAt(i);
                    int cc = Integer.parseInt(String.valueOf(c));
                    str.append(cnNumbers[cc]);
                }
                str.append(cnYuans[0]);
            }
            else
            {
                str.append(cnYuans[0]);
                for (int i = 0; i < ns2.length(); i++)
                {
                    char c = ns2.charAt(i);
                    int cc = Integer.parseInt(String.valueOf(c));
                    str.append(cnNumbers[cc]);
                    str.append(cnYuans[i + 1]);
                }
            }
        }
        return str.toString();
    }

}
