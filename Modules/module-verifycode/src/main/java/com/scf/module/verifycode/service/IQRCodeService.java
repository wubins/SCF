
/*   
 * @Title: IQRCodeService.java 
 * @Package: com.scf.module.verifycode.service 
 * @author wubin  
 * @date 2016年9月18日 下午6:42:00 
 * @version 1.3.1 
 */


package com.scf.module.verifycode.service;

import java.io.File;
import java.io.OutputStream;

/** 
 * 二维码生成工具
 * @author wubin
 * @date 2016年9月18日 下午6:42:00 
 * @version V1.1.0
 */

public interface IQRCodeService {
    
    public static final int BLACK = 0xff000000;

    public static final int WHITE = 0xFFFFFFFF;

    /**
     * 生成二维码图片,目标是流
     * @author wubin
     * @param key
     * @param stream
     * @param width
     * @param height
     */
      void GenerateQRPicToStream(String key, OutputStream stream,Integer width,Integer height);
      
      
      /**
       * 生成二维码图片,返回字节数组
       * @author wubin
       * @param key
       * @param stream
       * @param width
       * @param height
       */
        byte[] GenerateQRPicToBytes(String key,Integer width,Integer height);
      
      /**
       * 生成二维码图片,目标是文件
       * @author wubin
       * @param key
       * @param stream
       * @param width
       * @param height
       */
        void GenerateQRPicToFile(String key, File file,Integer width,Integer height);
        
    }
