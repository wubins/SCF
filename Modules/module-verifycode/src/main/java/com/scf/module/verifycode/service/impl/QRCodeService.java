
/*
 * @Title: SimpleQRCodeService.java
 * 
 * @Package: com.scf.module.verifycode.service.impl
 * 
 * @author wubin
 * 
 * @date 2016年9月18日 下午6:45:15
 * 
 * @version 1.3.1
 */

package com.scf.module.verifycode.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.scf.core.exception.sys.SystemException;
import com.scf.module.verifycode.service.IQRCodeService;

/**
 * 二维码生成器
 * 
 * @author wubin
 * @date 2016年9月18日 下午6:45:15
 * @version V1.1.0
 */

public class QRCodeService implements IQRCodeService {

    private QRCodeService() {
    }

    private static class QRCodeServiceHolder {
        private static QRCodeService instance = new QRCodeService();
    }

    public static QRCodeService getInstance() {
        return QRCodeServiceHolder.instance;
    }

    /**
     * @param key
     * @param stream
     * @param width
     * @param height
     * @see com.scf.module.verifycode.service.IQRCodeService#GenerateQRPicToStream(java.lang.String,
     * java.io.OutputStream, java.lang.Integer, java.lang.Integer)
     */

    @Override
    public void GenerateQRPicToStream(String key, OutputStream stream, Integer width, Integer height) {

        // 文字编码
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(key, BarcodeFormat.QR_CODE, width, height, hints);

            BufferedImage m = toBufferedImage(bitMatrix);

            // 输出方式
            // 网页
            ImageIO.write(m, "png", stream);
        } catch (IOException | WriterException e) {
            throw SystemException.defaultSystemException("二维码生成错误", e);
        }

    }
    
    
    /**
     * @param key
     * @param width
     * @param height
     * @return 
     * @see com.scf.module.verifycode.service.IQRCodeService#GenerateQRPicToStream(java.lang.String, java.lang.Integer, java.lang.Integer) 
     */ 
        
    @Override
    public byte[] GenerateQRPicToBytes(String key, Integer width, Integer height) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GenerateQRPicToStream(key, baos,  width,  height);
        return baos.toByteArray();
    }

    /**
     * 转换成图片
     * 
     * @param matrix
     * @return
     */
    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    /**
     * @param key
     * @param file
     * @param width
     * @param height
     * @see com.scf.module.verifycode.service.IQRCodeService#GenerateQRPicToStream(java.lang.String, java.io.File,
     * java.lang.Integer, java.lang.Integer)
     */

    @Override
    public void GenerateQRPicToFile(String key, File file, Integer width, Integer height) {
    }

   

}
