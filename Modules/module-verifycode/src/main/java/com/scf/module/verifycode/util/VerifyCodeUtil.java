/**    
 * 文件名：VerifyCodeUtil.java    
 *    
 * 版本信息：    
 * 日期：2015年6月3日    
 * Copyright 足下 Corporation 2015     
 * 版权所有    
 *    
 */
package com.scf.module.verifycode.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 *
 * @author wubin
 */
public class VerifyCodeUtil
{
    
    private static Random random = new Random();
    
    /**
     * 添加噪点
     * 
     * @param image
     * @param yawpRate 噪声率
     */
    public static void addYawp(BufferedImage image, float yawpRate)
    {
        int area = (int)(yawpRate * image.getWidth() * image.getHeight());
        for (int i = 0; i < area; i++)
        {
            int x = random.nextInt(image.getWidth());
            int y = random.nextInt(image.getHeight());
            int rgb = VerifyCodeUtil.getRandomIntColor();
            image.setRGB(x, y, rgb);
        }
    }
    
    /**
     * 获取范围内的随机色
     * 
     * @param fc
     * @param bc
     * @return color
     */
    public static Color getRandomColor(int fc, int bc)
    {
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
    
    /**
     * 获取随机rgb
     * 
     * @return int
     */
    public static int getRandomIntColor()
    {
        int[] rgb = new int[3];
        for (int i = 0; i < 3; i++)
        {
            rgb[i] = random.nextInt(255);
        }
        int color = 0;
        for (int c : rgb)
        {
            color = color << 8;
            color = color | c;
        }
        return color;
    }
    
    /**
     * 绘制干扰线
     * 
     * @param width 图片宽度
     * @param height 图片高度
     * @param g Graphics2D对象，用来绘制图像
     * @param nums 干扰线的条数
     * 
     */
    public static void drawRandomLines(int width, int height, Graphics2D g, int nums)
    {
        g.setColor(getRandomColor(160, 200));
        for (int i = 0; i < nums; i++)
        {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(12);
            int y2 = random.nextInt(12);
            g.drawLine(x1, y1, x2, y2);
        }
    }
    
    /**
     * 扭曲图片
     * 
     * @param g Graphics对象
     * @param width 图片宽度
     * @param height 图片高度
     * @param color 背景色
     */
    public static void shear(Graphics g, int width, int height, Color color)
    {
        // shearX
        int period = random.nextInt(2);
        
        boolean borderGap = true;
        int frames = 1;
        int phase = random.nextInt(2);
        
        for (int i = 0; i < height; i++)
        {
            double d =
                (double)(period >> 1)
                    * Math.sin((double)i / (double)period + (6.2831853071795862D * (double)phase) / (double)frames);
            g.copyArea(0, i, width, 1, (int)d, 0);
            if (borderGap)
            {
                g.setColor(color);
                g.drawLine((int)d, i, 0, i);
                g.drawLine((int)d + width, i, width, i);
            }
        }
        // shearY
        period = random.nextInt(40) + 10; // 50;
        frames = 20;
        phase = 7;
        for (int i = 0; i < width; i++)
        {
            double d =
                (double)(period >> 1)
                    * Math.sin((double)i / (double)period + (6.2831853071795862D * (double)phase) / (double)frames);
            g.copyArea(i, 0, 1, height, 0, (int)d);
            if (borderGap)
            {
                g.setColor(color);
                g.drawLine(i, (int)d, i, 0);
                g.drawLine(i, (int)d + height, i, height);
            }
            
        }
    }
}
