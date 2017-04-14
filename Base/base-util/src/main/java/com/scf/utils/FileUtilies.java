/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.utils;

import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.springframework.util.StringUtils;

/**
 *  文件操作工具类
 * @author wubin
 * @date 2016年7月28日 下午3:42:28 
 * @version V1.1.0
 */
public class FileUtilies {
    
    /**
     * 获取下载文件的文件名称
    * @title: getDownloadEncodeFileName
    * @param fileName
    * @param userAgent
    * @return
    * @throws Exception 
    * @Author: jiangxia
    * @Date: 2016年2月18日下午7:49:39
     */
    public static String getDownloadEncodeFileName(String fileName, String userAgent) throws Exception{
        // 进行转码，使其支持中文文件名
        String codedFileName = null;
        // 解决中文文件名乱码问题
        if (userAgent.toLowerCase().indexOf("firefox") > 0) {
            codedFileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1"); // firefox浏览器
        } else if (userAgent.toUpperCase().indexOf("MSIE") > 0) {
            codedFileName = URLEncoder.encode(fileName, "UTF-8");// IE浏览器
        } else {
            codedFileName = URLEncoder.encode(fileName, "UTF-8");// IE浏览器
        }
        return codedFileName;
    }
    
    /**
     * 获取文件的扩展名
     * @author jiangxia
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {
        if (!StringUtils.isEmpty(filename)) {
            int dot = filename.lastIndexOf('.'); 
            if ((dot >-1) && (dot < (filename.length() - 1))) { 
                return filename.substring(dot + 1); 
            } 
        } 
        return filename; 
    } 
    
    
    /**
     * 获取项目路径的上一级完整路径
     * @author jiangxia
     * @param filename
     * @return
     */
    public static String getUpperPath(String filename) {
        if (!StringUtils.isEmpty(filename)) { 
            int dot = filename.lastIndexOf('/'); 
            if ((dot >-1) && (dot < (filename.length() - 1))) { 
                return filename.substring(0, dot); 
            } 
        } 
        return filename; 
    } 
    
    
    /**
     * 判断路径是否存在，不存在则创建
     * @author jiangxia
     * @param filename
     * @return
     */
    public static boolean checkPathExist(String inputPath)
    {
        if(StringUtils.isEmpty(inputPath)){
            return false;
        }
        File path = new File(inputPath);
        if(!path.exists())
        {
            return path.mkdirs();
        }
        return true;
    }
    
    
    
    /**
     * 获取classpath下单文件流
     * filepath: /a/b/c
     */
    
    public static InputStream getClassPathFile(String filepath){
        if(StringUtils.isEmpty(filepath)){
            return null;
        }
        filepath = fixPath(filepath);
        if("/".equals(filepath.substring(0, 1))){
            filepath = filepath.substring(1, filepath.length());
        }
        InputStream  inputStream = FileUtilies.class.getResourceAsStream(filepath);
        return inputStream;
    }
    
    
    /**
     * replace path '\\' to '/'.
     *
     * @param path
     * @return
     */
    public static String fixPath(String path) {
        return path.replace('\\', '/');
    }
    
    
    /**
    * jar包操作类，jar可以位于classpath之外，可以获取jar中的文件，比如propertiest
    * @param jarPath
    * @param entryFile
    * @return
    * @throws Exception
    */
   @SuppressWarnings("resource")
public static InputStream getInputStream(String jarPath, String entryFile) throws Exception {
       JarFile jarFile = new JarFile(jarPath);
       JarEntry entry = jarFile.getJarEntry(entryFile);
       InputStream input = jarFile.getInputStream(entry);
       return input;
   }

   /**
    *
    * @param jarPath
    * @param entryFile
    * @return
    * @throws Exception
    */
   public static Properties getProperties(String jarPath, String entryFile) throws Exception {
       InputStream is = getInputStream(jarPath, entryFile);
       Properties p = new Properties();
       p.load(is);
       is.close();
       return p;
   } 

    
}
