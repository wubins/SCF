package com.scf.utils;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

/**
 * excel 导出 
 * @date 2016年10月31日 下午3:33:56 
 * @version V1.1.0
 */



public class ExcelExportUtilies
{

    /**
     * 导出excel，只包含column名称和数据
     * @author jiangxia
     * @param os              response的输出流
     * @param dataList        数据list
     * @param headerCellList  excel的column名称
     * @throws Exception
     */
    public static void export(OutputStream os, List<?> dataList, List<ExcelHeaderCell> headerCellList) throws Exception {
        // 创建Excel的工作书册 Workbook,对应到一个excel文档
        HSSFWorkbook wb = new HSSFWorkbook();
        // 创建Excel的工作sheet,对应到一个excel文档的tab
        HSSFSheet sheet = wb.createSheet("sheet1");

        // 创建字体样式
        // HSSFFont font = wb.createFont();
        // font.setBoldweight((short) 100);
        // font.setFontHeight((short) 300);
        // 创建标题样式
        HSSFCellStyle headerStyle = wb.createCellStyle();
        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
        headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        // headerStyle.setFont(font);
        // 创建头
        createHerader(sheet, headerCellList, headerStyle, 0);
        // 创建数据
        createData(wb, sheet, headerCellList, dataList, 1);
        // 自动调整
        for (int j = 0; j < headerCellList.size(); j++) {
            sheet.autoSizeColumn(j);
            int curColWidth = sheet.getColumnWidth(j);
            sheet.setColumnWidth(j, curColWidth * 2);
            if (headerCellList.get(j).getCellName().equals("备注")) {
                if (curColWidth * 2 < 12.14 * 256) {
                    sheet.setColumnWidth(j, (int) Math.round(12.14 * 256));
                }
            }
        }
        wb.write(os);
        wb.close();
    }

    /**
     * 导出excel，以及title和生成时间, 还可以最末尾添加一条汇总信息(prependList)
     * @author jiangxia
     * @param startDate
     * @param endDate
     * @param title  标题
     * @param os
     * @param dataList
     * @param headerCellList
     * @param prependList
     * @throws Exception
     */
    public static void export(Date startDate, Date endDate, String title, OutputStream os, List<?> dataList,
            List<ExcelHeaderCell> headerCellList, List<?> prependList) throws Exception {
        // 创建Excel的工作书册 Workbook,对应到一个excel文档
        HSSFWorkbook wb = new HSSFWorkbook();
        // 创建Excel的工作sheet,对应到一个excel文档的tab
        HSSFSheet sheet = wb.createSheet("sheet1");

        // 创建字体样式
        // HSSFFont font = wb.createFont();
        // font.setBoldweight((short) 100);
        // font.setFontHeight((short) 300);
        // 创建标题样式
        HSSFCellStyle headerStyle = wb.createCellStyle();
        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
        headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        // headerStyle.setFont(font);
        if (startDate != null) {
            title += DatetimeUtilies.formatDateTime(DatetimeUtilies.DATE_TIME, startDate);
        }
        if (endDate != null) {
            title += "至" + DatetimeUtilies.formatDateTime(DatetimeUtilies.DATE_TIME, endDate);
        }
        createTitle(wb, sheet, headerCellList, title, 0);
        // 创建生成时间
        createGenerateTime(sheet, headerCellList, headerStyle, 1);
        // 创建头
        createHerader(sheet, headerCellList, headerStyle, 2);
        // 创建数据
        createData(wb, sheet, headerCellList, dataList, 3);
        int fromRow = 3;
        if (dataList != null && dataList.size() > 0) {
            fromRow = fromRow + dataList.size();
        }
        if (fromRow > 3) {
            ExcelHeaderCell headerCell = headerCellList.get(0);
            headerCell.setAutoIncrement(false);
            headerCell.setJavaFieldName("orderSerial");
            createData(wb, sheet, headerCellList, prependList, fromRow);
        }
        // 设置标题样式
        setHeaderStyle(wb, sheet, 0, headerCellList.size() - 1);
        // 时间设定样式
        HSSFCellStyle timeStyle = wb.createCellStyle();
        timeStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
        timeStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        timeStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        timeStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
        timeStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        timeStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        timeStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        // 合并
        CellRangeAddress cellRangeAddress = new CellRangeAddress(1, 1, 0, headerCellList.size() - 1);
        sheet.addMergedRegion(cellRangeAddress);
        // 再合并（省略不起作用）
        mergeBorder(wb, sheet, 1, headerCellList.size() - 1);
        sheet.getRow(1).getCell(0).setCellStyle(timeStyle);
        // 自动调整
        for (int j = 0; j < headerCellList.size(); j++) {
            sheet.autoSizeColumn(j);
            int curColWidth = sheet.getColumnWidth(j);
            sheet.setColumnWidth(j, curColWidth * 2);
            if (headerCellList.get(j).getCellName().equals("备注")) {
                if (curColWidth * 2 < 12.14 * 256) {
                    sheet.setColumnWidth(j, (int) Math.round(12.14 * 256));
                }
            }
        }
        wb.write(os);
        wb.close();
    }

    /**
     * 导出excel，以及title和生成时间
     * @author jiangxia
     * @param startDate  起始时间
     * @param endDate    结束时间
     * @param username   用户名
     * @param title      表的标题
     * @param os         response的输出流
     * @param dataList   数据list
     * @param headerCellList  excel的column名称
     * @throws Exception
     */
    public static void export(Date startDate, Date endDate, String title, OutputStream os, List<?> dataList,
            List<ExcelHeaderCell> headerCellList) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        try {
            // 创建Excel的工作书册 Workbook,对应到一个excel文档

            // 创建Excel的工作sheet,对应到一个excel文档的tab
            HSSFSheet sheet = wb.createSheet("sheet1");

            // 创建字体样式
            // HSSFFont font = wb.createFont();
            // font.setBoldweight((short) 100);
            // font.setFontHeight((short) 300);
            // 创建标题样式
            HSSFCellStyle headerStyle = wb.createCellStyle();
            headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
            headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
            headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
            headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
            headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
            headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            // headerStyle.setFont(font);
            if (startDate != null) {
                title += DatetimeUtilies.formatDateTime(DatetimeUtilies.DATE_TIME, startDate);
            }
            if (endDate != null) {
                title += "至" + DatetimeUtilies.formatDateTime(DatetimeUtilies.DATE_TIME, endDate);
            }
            createTitle(wb, sheet, headerCellList, title, 0);
            // 创建生成时间
            createGenerateTime(sheet, headerCellList, headerStyle, 1);
            // 创建头
            createHerader(sheet, headerCellList, headerStyle, 2);
            // 创建数据
            createData(wb, sheet, headerCellList, dataList, 3);
            // 设置标题样式
            setHeaderStyle(wb, sheet, 0, headerCellList.size() - 1);
            // 时间设定样式
            HSSFCellStyle timeStyle = wb.createCellStyle();
            timeStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
            timeStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
            timeStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
            timeStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
            timeStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
            timeStyle.setAlignment(CellStyle.ALIGN_RIGHT);
            timeStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            // 合并
            CellRangeAddress cellRangeAddress = new CellRangeAddress(1, 1, 0, headerCellList.size() - 1);
            sheet.addMergedRegion(cellRangeAddress);
            // 再合并（省略不起作用）
            mergeBorder(wb, sheet, 1, headerCellList.size() - 1);
            sheet.getRow(1).getCell(0).setCellStyle(timeStyle);
            // 自动调整
            for (int j = 0; j < headerCellList.size(); j++) {
                sheet.autoSizeColumn(j);
                int curColWidth = sheet.getColumnWidth(j);
                sheet.setColumnWidth(j, curColWidth * 2);
                if (headerCellList.get(j).getCellName().equals("备注")) {
                    if (curColWidth * 2 < 12.14 * 256) {
                        sheet.setColumnWidth(j, (int) Math.round(12.14 * 256));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("导出excel失败");
        } finally {
            wb.write(os);
            wb.close();
        }
    }

    //生成“生成时间”
    private static void createGenerateTime(HSSFSheet sheet, List<ExcelHeaderCell> headerCellList, HSSFCellStyle headerStyle,
            int fromRow) {
        HSSFRow row = sheet.createRow(fromRow);
        Cell cell = row.createCell(0);
        
        cell.setCellValue("生成时间：" + DatetimeUtilies.formatDateTime(DatetimeUtilies.DATE_TIME, new Date()));
    }

    private static void setHeaderStyle(HSSFWorkbook wb, HSSFSheet sheet, int fromRow, int lastCol) {
        HSSFFont font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 14);// 设置字体大小
        font.setColor(HSSFFont.COLOR_NORMAL);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 创建标题样式
        HSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
        titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
        titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        titleStyle.setFont(font);
        HSSFRow row = sheet.getRow(fromRow);
        row.setHeightInPoints(30);
        HSSFCell cell = row.getCell(0);
        cell.setCellStyle(titleStyle);
        mergeBorder(wb, sheet, fromRow, lastCol);
    }

    /**
     * 合并单元格
     * 
     * @title: mergeBorder
     * @param wb
     * @param sheet
     * @param lastCol
     * @Author: zhaoweiyong
     * @Date: 2016年2月19日上午11:45:31
     */
    private static void mergeBorder(HSSFWorkbook wb, HSSFSheet sheet, int fromRow, int lastCol) {
        CellRangeAddress cellRangeAddress = new CellRangeAddress(fromRow, fromRow, 0, lastCol);
        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, wb);
        RegionUtil.setLeftBorderColor(IndexedColors.BLACK.getIndex(), cellRangeAddress, sheet, wb);
        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, wb);
        RegionUtil.setRightBorderColor(IndexedColors.BLACK.getIndex(), cellRangeAddress, sheet, wb);
        RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, wb);
        RegionUtil.setBottomBorderColor(IndexedColors.BLACK.getIndex(), cellRangeAddress, sheet, wb);
        RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, wb);
        RegionUtil.setTopBorderColor(IndexedColors.BLACK.getIndex(), cellRangeAddress, sheet, wb);
    }

    private static void createTitle(HSSFWorkbook wb, HSSFSheet sheet, List<ExcelHeaderCell> headerCellList, String title,
            int fromRow) {
        HSSFRow row = sheet.createRow(fromRow);
        Cell titleCell = row.createCell(0);
        titleCell.setCellValue(title);
        CellRangeAddress cra = new CellRangeAddress(0, 0, 0, headerCellList.size() - 1);
        // 在sheet里增加合并单元格
        sheet.addMergedRegion(cra);
    }

    private static void createData(HSSFWorkbook wb, HSSFSheet sheet, List<ExcelHeaderCell> headerCellList, List<?> dataList,
            int fromRow) throws Exception {
        // 居右
        HSSFCellStyle rightCellStyle = wb.createCellStyle();
        rightCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
        rightCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        rightCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        rightCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
        rightCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        rightCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        // 局左
        HSSFCellStyle leftCellStyle = wb.createCellStyle();
        leftCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
        leftCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        leftCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        leftCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
        leftCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        leftCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        // 居中
        HSSFCellStyle centerCellStyle = wb.createCellStyle();
        centerCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
        centerCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        centerCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        centerCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
        centerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        centerCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        
        Map<String, Method> methodMap = new HashMap<String, Method>();
        for (int i = 0; i < dataList.size(); i++) {
            Object o = dataList.get(i);
            HSSFRow row = sheet.createRow(fromRow + i);
            for (int j = 0; j < headerCellList.size(); j++) {
                Method cacheMethod = null;
                ExcelHeaderCell headerCell = headerCellList.get(j);
                HSSFCell cell = row.createCell(j);

                if (headerCell.isAutoIncrement()) {
                    cell.setCellValue(i + 1);// 序号
                    cell.setCellStyle(centerCellStyle);
                } else {
                    if((cacheMethod = methodMap.get(headerCell.getJavaFieldName())) == null)
                    {
                        cacheMethod = ClassUtilies.getGetMethodRecursive(o.getClass(), headerCell.getJavaFieldName());
                        if(cacheMethod == null)
                        {
                            throw new RuntimeException("没有找到"+headerCell.getJavaFieldName() + "对应的字段");
                        }
                        methodMap.put(headerCell.getJavaFieldName(), cacheMethod);
                    }
                    Object cellValue =   cacheMethod.invoke(o);   
                    if (cellValue == null) {
                        cellValue = "";
                    }
                    if (headerCell.getAppendSubLen() > 0) {// 从后截取
                        int len = (cellValue + "").length();
                        cellValue = len > 8 ? (cellValue + "").substring((cellValue + "").length() - 8) : cellValue;
                    }
                    if (headerCell.getPrependSubLen() > 0) {// 从前截取
                        int len = headerCell.getPrependSubLen();
                        cellValue = (cellValue + "").substring(len);
                    }

                    if (cellValue instanceof Date) {
                        Date dateCellValue = (Date) cellValue;
                        if (headerCell.getFormatDate() != null) {
                            String strCellValue = DatetimeUtilies.formatDateTime(headerCell.getFormatDate(), dateCellValue);
                            cell.setCellValue(strCellValue);
                        }
                    } else {

                        cell.setCellValue(cellValue + "");
                    }

                    if (headerCell.getDataDirection() == ExcelDirection.CENTER) {
                        cell.setCellStyle(centerCellStyle);
                    } else if (headerCell.getDataDirection() == ExcelDirection.LEFT) {
                        cell.setCellStyle(leftCellStyle);
                    } else if (headerCell.getDataDirection() == ExcelDirection.RIGHT) {
                        cell.setCellStyle(rightCellStyle);
                    }
                }
            }
        }
    }

//    @SuppressWarnings("rawtypes")
//    private static Object getValue(Object o, String javaFieldName) throws Exception {
//        if (o instanceof Map) {
//            return ((Map) o).get(javaFieldName);
//        }
//        Field field = o.getClass().getField(javaFieldName);
//        if (!field.isAccessible()) {
//            field.setAccessible(true);
//        }
//        return field.get(o);
//    }

    /**
     * 写excel的column名称
     * @author jiangxia
     * @param sheet
     * @param headerCellList
     * @param headerStyle
     * @param fromRow
     */
    private static void createHerader(HSSFSheet sheet, List<ExcelHeaderCell> headerCellList, HSSFCellStyle headerStyle,
            int fromRow) {
        HSSFRow row = sheet.createRow(fromRow);
        for (int i = 0; i < headerCellList.size(); i++) {
            ExcelHeaderCell headerCell = headerCellList.get(i);
            String headerName = headerCell.getCellName();
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(headerName);
            cell.setCellStyle(headerStyle);
        }
    }

    
    
    
    public static class ExcelHeaderCell {
    
        /**
         * 单元格名称
         */
        private String cellName;
        
        /**
         * 居中
         */
        private ExcelDirection headerDirection = ExcelDirection.CENTER;
        
        /**
         * 
         * if autoIncrement == false , javaFieldName != null
         * 
         */
        /**
         * 当前列对应的javaBean的字段名称
         */
        private String javaFieldName;
        
        /**
         * 自增长
         */
        private boolean autoIncrement;
        
        /**
         * 数据对齐方式
         */
        private ExcelDirection dataDirection;
        
        /**
         * 格式化
         */
        private String formatDate;
        
        /**
         * 从后截取
         */
        private int appendSubLen;
        /**
         * 从前截取
         */
        private int prependSubLen;
        public ExcelHeaderCell() { }
    
        public ExcelHeaderCell(String cellName, ExcelDirection headerDirection,
                String javaFieldName, boolean autoIncrement,
                ExcelDirection dataDirection) {
            super();
            this.cellName = cellName;
            this.headerDirection = headerDirection;
            this.javaFieldName = javaFieldName;
            this.autoIncrement = autoIncrement;
            this.dataDirection = dataDirection;
        }
    
        public String getCellName() {
            return cellName;
        }
    
        public void setCellName(String cellName) {
            this.cellName = cellName;
        }
    
        public ExcelDirection getHeaderDirection() {
            return headerDirection;
        }
    
        public void setHeaderDirection(ExcelDirection headerDirection) {
            this.headerDirection = headerDirection;
        }
    
        public String getJavaFieldName() {
            return javaFieldName;
        }
    
        public void setJavaFieldName(String javaFieldName) {
            this.javaFieldName = javaFieldName;
        }
    
        public boolean isAutoIncrement() {
            return autoIncrement;
        }
    
        public void setAutoIncrement(boolean autoIncrement) {
            this.autoIncrement = autoIncrement;
        }
    
        public ExcelDirection getDataDirection() {
            return dataDirection;
        }
    
        public void setDataDirection(ExcelDirection dataDirection) {
            this.dataDirection = dataDirection;
        }
    
        public String getFormatDate() {
            return formatDate;
        }
    
        public void setFormatDate(String formatDate) {
            this.formatDate = formatDate;
        }
    
        public int getAppendSubLen() {
            return appendSubLen;
        }
    
        public void setAppendSubLen(int appendSubLen) {
            this.appendSubLen = appendSubLen;
        }
    
        public int getPrependSubLen() {
            return prependSubLen;
        }
    
        public void setPrependSubLen(int prependSubLen) {
            this.prependSubLen = prependSubLen;
        }
    }

    
    /**
     * 
     * 单元格居中方式
    * @ClassName: ExcelDirection 
    * @Description: TODO
    * @author zhaoweiyong  kd.cc 
    * @date 2016年2月18日 上午10:25:18 *
    * 
     */
    public enum ExcelDirection {
        
        /**
         * 左
         */
        LEFT,
        
        /**
         * 右
         */
        RIGHT,
        
        /**
         * 居中
         */
        CENTER
    }

    public static void main(String[] args) throws Exception {
//        List<SellMoneyVO> dataList = new ArrayList<SellMoneyVO>();
//        // 现金
//        SellMoneyVO vo = new SellMoneyVO("现金", 5 + "", 50.02 + "", "预订现金收款+营业现金收款+清账现金收款");
//        dataList.add(vo);
//        // 银行卡
//        vo = new SellMoneyVO("银行卡", 6 + "", 60.02 + "", "预定银行卡收款+营业银行卡收款+清账银行卡收款");
//        dataList.add(vo);
//        // 优惠券
//        vo = new SellMoneyVO("优惠券", 7 + "", 70.02 + "", "--");
//        dataList.add(vo);
//        // 支付宝
//        vo = new SellMoneyVO("支付宝", 8 + "", 80.02 + "", "营业支付宝收款+清账支付宝收款");
//        dataList.add(vo);
//        // 挂账离店
//        vo = new SellMoneyVO("挂账离店", 10 + "", 100.02 + "", "即赊账，回头一起算钱");
//        dataList.add(vo);
//        // 免费结账
//        vo = new SellMoneyVO("免费结账", 9 + "", 90.02 + "", "--");
//        dataList.add(vo);
//        List<SellMoneyVO> prependList = new ArrayList<SellMoneyVO>();
//        vo = new SellMoneyVO("以上合计", null, null, "825.00", "--");
//        prependList.add(vo);
//        vo = new SellMoneyVO("实收合计", null, null, "825.00", "(不含挂账离店、免费结账)");
//        prependList.add(vo);
//        List<ExcelHeaderCell> headerCellList = new ArrayList<ExcelHeaderCell>();
//        ExcelHeaderCell excelHeaderCell = new ExcelHeaderCell("序号", ExcelDirection.CENTER, null, true,
//                ExcelDirection.CENTER);
//        headerCellList.add(excelHeaderCell);
//        excelHeaderCell = new ExcelHeaderCell("结算方式", ExcelDirection.CENTER, "sellType", false, ExcelDirection.LEFT);
//        headerCellList.add(excelHeaderCell);
//        excelHeaderCell = new ExcelHeaderCell("数量", ExcelDirection.RIGHT, "totalCount", false, ExcelDirection.RIGHT);
//        headerCellList.add(excelHeaderCell);
//        excelHeaderCell = new ExcelHeaderCell("金额", ExcelDirection.RIGHT, "totalAmount", false, ExcelDirection.RIGHT);
//        headerCellList.add(excelHeaderCell);
//        excelHeaderCell = new ExcelHeaderCell("备注", ExcelDirection.RIGHT, "remark", false, ExcelDirection.LEFT);
//        headerCellList.add(excelHeaderCell);
//        File file = new File("f:\\zwy\\1.xls");
//
//        OutputStream os = new FileOutputStream(file);
//        new ExcelWFacadeImpl().export(new Date(), new Date(), null, "营业状况汇总表", os, dataList, headerCellList,
//                prependList);
    }
}
