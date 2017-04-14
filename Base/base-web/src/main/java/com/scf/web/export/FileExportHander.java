package com.scf.web.export;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scf.utils.DatetimeUtilies;
import com.scf.utils.JacksonObjectMapper;

/**
 * 文件导出帮助类 <功能详细描述>
 * 
 * @author wub
 * @version [版本号, 2015年10月30日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class FileExportHander
{
    private static final Logger logger = LoggerFactory.getLogger(FileExportHander.class);
    
    /**
     * 导出文件 <功能详细描述>
     * 
     * @param list
     * @param keys
     * @param columnNames
     * @param request
     * @param response
     * @param fileName
     * @param fileType
     * @see [类、类#方法、类#成员]
     */
    public static void exportFile(HttpServletRequest request, HttpServletResponse response,
        List<Map<String, Object>> list, String[] keys, String columnNames[], String fileName, String fileType)
    {   
        logger.info("文件导出开始：数据对象---"+JacksonObjectMapper.toJsonString(list));
        logger.info("数据vo属性值列表---"+JacksonObjectMapper.toJsonString(keys));
        logger.info("数据vo标题名---"+JacksonObjectMapper.toJsonString(columnNames));
        logger.info("文件自定义名称---"+JacksonObjectMapper.toJsonString(fileName));
        logger.info("文件生成类型---"+JacksonObjectMapper.toJsonString(fileType));
        if (StringUtils.isEmpty(fileType))
        {
            createWorkBook(request, response, list, keys, columnNames, fileName);
        }
        else if (fileType.equalsIgnoreCase("excel"))
        {
            createWorkBook(request, response, list, keys, columnNames, fileName);
        }
        else if (fileType.equalsIgnoreCase("txt"))
        {
            createTxt(request, response, list, keys, columnNames, fileName);
        }
        else
        {
            createWorkBook(request, response, list, keys, columnNames, fileName);
        }
    }
    
    /**
     * 创建excel文档，
     * 
     * @param List<Map<String, Object>> 数据源 第一个map 为 sheetName
     * @param keys 展示的属性名
     * @param columnNames excel的列名
     * @param response httpResponse
     * @throws IOException
     * */
    private static void createWorkBook(HttpServletRequest request, HttpServletResponse response,
        List<Map<String, Object>> list, String[] keys, String columnNames[], String fileName)
    {   
        logger.info("导出excel文件开始：");
        HttpSession session = request.getSession();
        session.setAttribute("state", null);
        response.reset();
        // response.setContentType("text/plain;charset=utf-8");
        response.setContentType("APPLICATION/vnd.ms-excel;charset=utf-8");
        try
        {
            response.setHeader("Content-Disposition",
                "attachment;filename=" + java.net.URLEncoder.encode(fileName+".xls", "UTF-8"));
        }
        catch (UnsupportedEncodingException e1)
        {
            e1.printStackTrace();
        }
        // 创建excel工作簿
        Workbook wb = new HSSFWorkbook();
        // 创建第一个sheet（页），并命名
        Sheet sheet = wb.createSheet(list.get(0).get("sheetName").toString());
        // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
        for (int i = 0; i < keys.length; i++)
        {
            sheet.setColumnWidth(i, (int)(35.7 * 150));
        }
        
        // 创建第一行
        Row row = sheet.createRow(0);
        
        // 创建两种单元格格式
        CellStyle cs = wb.createCellStyle();
        CellStyle cs2 = wb.createCellStyle();
        
        // 创建两种字体
        Font f = wb.createFont();
        Font f2 = wb.createFont();
        
        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short)10);
        f.setColor(IndexedColors.BLACK.getIndex());
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);
        
        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short)10);
        f2.setColor(IndexedColors.BLACK.getIndex());
        
        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
        cs.setBorderLeft(CellStyle.BORDER_THIN);
        cs.setBorderRight(CellStyle.BORDER_THIN);
        cs.setBorderTop(CellStyle.BORDER_THIN);
        cs.setBorderBottom(CellStyle.BORDER_THIN);
        cs.setAlignment(CellStyle.ALIGN_CENTER);
        
        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs2.setBorderLeft(CellStyle.BORDER_THIN);
        cs2.setBorderRight(CellStyle.BORDER_THIN);
        cs2.setBorderTop(CellStyle.BORDER_THIN);
        cs2.setBorderBottom(CellStyle.BORDER_THIN);
        cs2.setAlignment(CellStyle.ALIGN_CENTER);
        // 设置列名
        for (int i = 0; i < columnNames.length; i++)
        {
            Cell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(cs);
        }
        // 设置每行每列的值
        for (short i = 1; i < list.size(); i++)
        {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            Row row1 = sheet.createRow((short)i);
            // 在row行上创建一个方格
            for (short j = 0; j < keys.length; j++)
            {
                Cell cell = row1.createCell(j);
                cell.setCellValue(list.get(i).get(keys[j]) == null ? " " : list.get(i).get(keys[j]).toString());
                cell.setCellStyle(cs2);
            }
        }
        try
        {   //logger.info("构造webwork对象为："+JacksonObjectMapper.toJsonString(wb));
            logger.info("文件开始写入:"+JacksonObjectMapper.toJsonString(DatetimeUtilies.currentTime(), "yyyy-MM-dd HH:mm:ss"));
            wb.write(response.getOutputStream());
            logger.info("文件结束写入:"+JacksonObjectMapper.toJsonString(DatetimeUtilies.currentTime(), "yyyy-MM-dd HH:mm:ss"));
        }
        catch (IOException e)
        {
            logger.error("导出文件异常:" + e.getMessage());
        }
        finally
        {
            OutputStream os = null;
            try
            {
                os = response.getOutputStream();
                os.flush();
                os.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            logger.info("设置session open 值:"+JacksonObjectMapper.toJsonString(DatetimeUtilies.currentTime(), "yyyy-MM-dd HH:mm:ss"));
            session.setAttribute("state", "open");
        }
    }
    
    private static void createTxt(HttpServletRequest request, HttpServletResponse response,
        List<Map<String, Object>> list, String[] keys, String columnNames[], String fileName)
    {   
        logger.info("导出txt文件开始：");
        HttpSession session = request.getSession();
        session.setAttribute("state", null);
        response.reset();
        // 导出txt文件
        response.setContentType("text/plain;charset=utf-8");
        try
        {
            response.setHeader("Content-Disposition", "attachment; filename=" +java.net.URLEncoder.encode(fileName+".txt", "UTF-8"));
        }
        catch (UnsupportedEncodingException e1)
        {
            e1.printStackTrace();
        }
        BufferedOutputStream buff = null;
        StringBuffer write = new StringBuffer();
        String enter = "\r\n";
        ServletOutputStream outSTr = null;
        try
        {
            outSTr = response.getOutputStream(); // 建立
            buff = new BufferedOutputStream(outSTr);
            // 把内容写入文件
            for (int i = 0; i < columnNames.length; i++)
            {
                write.append(formatStr(columnNames[i] + "", 25));
            }
            write.append(enter);
            for (short i = 0; i < list.size(); i++)
            {
                // 在row行上创建一个方格
                for (short j = 0; j < keys.length; j++)
                {
                    write.append(list.get(i).get(keys[j]) == null ? "" : formatStr(list.get(i).get(keys[j]) + "", 25));
                }
                write.append(enter);
            }
            logger.info("构造写入数据为："+JacksonObjectMapper.toJsonString(write));
            logger.info("文件开始写入:"+JacksonObjectMapper.toJsonString(DatetimeUtilies.currentTime(), "yyyy-MM-dd HH:mm:ss"));
            buff.write(write.toString().getBytes("gbk"));
            logger.info("文件结束写入:"+JacksonObjectMapper.toJsonString(DatetimeUtilies.currentTime(), "yyyy-MM-dd HH:mm:ss"));
            buff.flush();
            buff.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                buff.close();
                outSTr.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            logger.info("设置session open 值:"+JacksonObjectMapper.toJsonString(DatetimeUtilies.currentTime(), "yyyy-MM-dd HH:mm:ss"));
            session.setAttribute("state", "open");
        }
    }
    
    public static String formatStr(String str, int length)
    {
        StringBuffer sb = new StringBuffer(str);
        int plus = countChinese(str);
        length = length - str.length() - plus;
        
        for (int i = 0; i < length; i++)
        {
            sb.append(" ");
        }
        return sb.toString();
    }
    
    private static int countChinese(String express)
    {
        String regEx = "[\\u4e00-\\u9fa5]";
        int count = 0;
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(express);
        while (m.find())
        {
            for (int i = 0; i <= m.groupCount(); i++)
            {
                count = count + 1;
            }
        }
        return count;
    }
}
