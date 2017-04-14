/**
 * 
 */
package com.scf.web.manage.excel.read;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Excel文件读取器
 * 
 * @author LiCunjing
 * @version [版本号, 2016年2月23日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class ExcelReader
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelReader.class);
    
    protected String fileLocation;
    
    private XSSFSheet sheet;
    
    // 第一行为标题行，忽略
    private int curSheetIndex = 1;
    
    public ExcelReader(String fileLocation)
    {
        this.fileLocation = fileLocation;
        
        // 校验文件后缀
        checkFileSuffix();
        
        // 加载Excel文件
        loadExcel();
        
        // 校验文件行数是否合法
        checkMinRowNum(sheet.getLastRowNum());
    }
    
    /**
     * 加载Excel文件<br>
     * 1. 判断文件后缀是否合法<br>
     * 2. 判断文件是否存在<br>
     * 3.
     * 
     * @see [类、类#方法、类#成员]
     */
    private void loadExcel()
    {
        File excelFile = new File(fileLocation);
        
        checkFile(excelFile);
        
        BufferedInputStream fileReader = null;
        
        try
        {
            fileReader = new BufferedInputStream(new FileInputStream(excelFile));
            
            XSSFWorkbook excelBook = null;
            
            excelBook = new XSSFWorkbook(fileReader);
            
            // 暂时只支持一个sheet
            this.sheet = excelBook.getSheetAt(0);
        }
        catch (Exception e)
        {
            LOGGER.error("Create work book error.", e);
            
            throw new IllegalArgumentException("Create work book error. The fileLocation is:".concat(fileLocation));
        }
        finally
        {
            try
            {
                if (null != fileReader)
                {
                    fileReader.close();
                }
            }
            catch (IOException e)
            {
                LOGGER.error("Colse input stream error.", e);
            }
        }
    }
    
    /**
     * 判断是否还有下一行数据<br>
     * 
     * @return true 有，false 没有
     * @see [类、类#方法、类#成员]
     */
    public boolean hasNext()
    {
        if (curSheetIndex <= sheet.getLastRowNum())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 获取下一行数据<br>
     * 
     * @return 行数据
     */
    public IRowDto next()
    {
        XSSFRow row = sheet.getRow(curSheetIndex);
        
        checkColumnNum(row.getLastCellNum());
        
        IRowDto rowDto = convert(row);
        
        curSheetIndex++;
        
        return rowDto;
    }
    
    /**
     * 校验文件后缀
     * 
     * @see [类、类#方法、类#成员]
     */
    private void checkFileSuffix()
    {
        if (!fileLocation.endsWith(".xlsx"))
        {
            throw new IllegalArgumentException(
                "File location format error. The file location suffix must be '.xlsx'. current fileLocation is:".concat(fileLocation));
        }
    }
    
    /**
     * 校验文件是否存在<br>
     * 
     * @param excelFile excel文件对象
     */
    private void checkFile(File excelFile)
    {
        if (!excelFile.exists() || !excelFile.isFile())
        {
            throw new IllegalArgumentException("File location is not exit. The fileLocation is:".concat(fileLocation));
        }
    }
    
    /**
     * 校验文件列数是否合法<br>
     * 
     * @param columnNum
     * @param cellNum
     * @see [类、类#方法、类#成员]
     */
    private void checkColumnNum(short cellNum)
    {
        if (getColumnNum() > cellNum)
        {
            throw new IllegalArgumentException("Document template error. The columnNum is not legal.");
        }
    }
    
    /**
     * 校验文件行数是否合法
     * 
     * @param lastRowNum
     * @see [类、类#方法、类#成员]
     */
    private void checkMinRowNum(int lastRowNum)
    {
        if (getMinRowNum() - 1 > lastRowNum)
        {
            throw new IllegalArgumentException("Document template error. The rowNum is not legal.");
        }
    }
    
    /**
     * 获取文件模板设定的列数
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    protected abstract short getColumnNum();
    
    /**
     * 获取文件最小行数
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    protected abstract short getMinRowNum();
    
    /**
     * 将Row数据转换为自定义对象
     * 
     * @param row 行数据
     * @return 自定义数据对象
     */
    protected abstract IRowDto convert(XSSFRow row);
}
