package com.scf.module.verifycode.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scf.module.verifycode.service.IVerifyCodeService;
import com.scf.module.verifycode.util.VerifyCodeUtil;

/**
 *图片验证码工具类
 * @author wubin
 */
public class SimpleVerifyCodeService implements IVerifyCodeService
{
    
    private static Logger _logger = LoggerFactory.getLogger(SimpleVerifyCodeService.class);
    
    // 使用到Algerian字体，字体只显示大写，去掉了1,0,i,o几个容易混淆的字符
    public static final String VERIFY_CODES = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    
    @Override
    public String outputVerifyImage(int w, int h, OutputStream os, int codeSize)
    {
        String verifyCode = this.generateVerifyCode(codeSize);
        try
        {
            outputImage(w, h, os, verifyCode);
        }
        catch (IOException e)
        {
            _logger.error("output verify image error : {} ", e);
        }
        return verifyCode;
    }
    
    @Override
    public String outputVerifyImage(int width, int height, File file, int codeSize)
    {
        String verifyCode = this.generateVerifyCode(codeSize);
        if (file == null)
        {
            return verifyCode;
        }
        File dir = file.getParentFile();
        if (!dir.exists())
        {
            dir.mkdirs();
        }
        try
        {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            outputImage(width, height, fos, verifyCode);
            fos.close();
        }
        catch (IOException e)
        {
            _logger.error("output verify image error : {} ", e);
        }
        return verifyCode;
    }
    
    /**
     * 输出指定验证码图片流
     * 
     * @param w
     * @param h
     * @param os
     * @param code
     * @throws IOException
     */
    public void outputImage(int w, int h, OutputStream os, String code)
        throws IOException
    {
        int verifySize = code.length();
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Random rand = new Random();
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color[] colors = new Color[5];
        Color[] colorSpaces =
            new Color[] {Color.WHITE, Color.CYAN, Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,
                Color.PINK, Color.YELLOW};
        float[] fractions = new float[colors.length];
        for (int i = 0; i < colors.length; i++)
        {
            colors[i] = colorSpaces[rand.nextInt(colorSpaces.length)];
            fractions[i] = rand.nextFloat();
        }
        Arrays.sort(fractions);
        // 设置边框色
        g2.setColor(Color.GRAY);
        g2.fillRect(0, 0, w, h);
        // 设置背景色
        Color c = VerifyCodeUtil.getRandomColor(200, 250);
        g2.setColor(c);
        g2.fillRect(0, 2, w, h - 4);
        // 绘制干扰线
        VerifyCodeUtil.drawRandomLines(w, h, g2, 20);
        // 添加噪点
        VerifyCodeUtil.addYawp(image, 0.05f);
        // 使图片扭曲
        VerifyCodeUtil.shear(g2, w, h, c);
        // 字体设置
        g2.setColor(VerifyCodeUtil.getRandomColor(100, 160));
        int fontSize = h - 4;
        Font font = new Font("Algerian", Font.ITALIC, fontSize);
        g2.setFont(font);
        char[] chars = code.toCharArray();
        for (int i = 0; i < verifySize; i++)
        {
            AffineTransform affine = new AffineTransform();
            affine.setToRotation(Math.PI / 4 * rand.nextDouble() * (rand.nextBoolean() ? 1 : -1), (w / verifySize) * i
                + fontSize / 2, h / 2);
            g2.setTransform(affine);
            g2.drawChars(chars, i, 1, ((w - 10) / verifySize) * i + 5, h / 2 + fontSize / 2 - 10);
        }
        g2.dispose();
        ImageIO.write(image, "jpg", os);
    }
    
    @Override
    public String generateVerifyCode(String source, int codeSize)
    {
        if (source == null || source.length() == 0)
        {
            source = VERIFY_CODES;
        }
        int codesLen = source.length();
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder verifyCode = new StringBuilder(codeSize);
        for (int i = 0; i < codeSize; i++)
        {
            verifyCode.append(source.charAt(rand.nextInt(codesLen - 1)));
        }
        return verifyCode.toString();
    }
    
    @Override
    public String generateVerifyCode(int codeSize)
    {
        return this.generateVerifyCode(VERIFY_CODES, codeSize);
    }
    
    @Override
    public String generate6Numbers()
    {
        return "" + (int)((Math.random() * 9 + 1) * 100000);
    }
    
    @Override
    public String generate4Numbers()
    {
        return "" + (int)((Math.random() * 9 + 1) * 1000);
    }
    
    
    public static void main(String[] args)
    {
        SimpleVerifyCodeService service = new SimpleVerifyCodeService();
        for (int i = 0; i < 100; i++)
        {
            System.out.println(service.generate4Numbers());
        }
    }
    
}
