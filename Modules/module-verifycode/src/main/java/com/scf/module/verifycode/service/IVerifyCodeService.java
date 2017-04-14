package com.scf.module.verifycode.service;

import java.io.File;
import java.io.OutputStream;

/**
 *图片验证码工具类
 * @author wubin
 */
public interface IVerifyCodeService
{
    /**
     * 生成六位随机验证码
     * 
     * @return
     */
    public String generate6Numbers();
    
    /**
     * 生成四位随机验证码
     * 
     * @return
     */
    public String generate4Numbers();
    
    /**
     * 使用默认字符源生成验证码
     * 
     * @param codeSize 验证码长度
     * @return 验证码
     */
    public String generateVerifyCode(int codeSize);
    
    /**
     * 使用指定源生成验证码
     * 
     * @param source 验证码字符源
     * @param codeSize 验证码长度
     * @return 验证码
     */
    public String generateVerifyCode(String source, int codeSize);
    
    /**
     * 输出验证码图片到输出流
     * 
     * @param width 图片宽度
     * @param height 图片高度
     * @param os 输出流
     * @param codeSize 验证码长度
     * @return 验证码字符
     */
    public String outputVerifyImage(int width, int height, OutputStream os, int codeSize);
    
    /**
     * 输出验证码图片到文件
     * 
     * @param width 图片宽度
     * @param height 图片高度
     * @param file 输出文件
     * @param codeSize 验证码长度
     * @return 验证码字符
     */
    public String outputVerifyImage(int width, int height, File file, int codeSize);
}
