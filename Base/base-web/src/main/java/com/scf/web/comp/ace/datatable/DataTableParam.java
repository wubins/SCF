package com.scf.web.comp.ace.datatable;

import org.apache.commons.lang.StringUtils;

import com.scf.core.constant.CommonConstants;

import java.util.LinkedHashMap;
import java.util.Map;

public class DataTableParam
{
    private String sEcho;
    
    private int iDisplayStart;
    
    private int iDisplayLength;
    
    private int iSortCol_0;
    
    private String sSortDir_0;
    
    private String[] columns = new String[15];
    
    public String getsEcho()
    {
        return sEcho;
    }
    
    public void setsEcho(String sEcho)
    {
        this.sEcho = sEcho;
    }
    
    public int getiDisplayStart()
    {
        return iDisplayStart;
    }
    
    public void setiDisplayStart(int iDisplayStart)
    {
        this.iDisplayStart = iDisplayStart;
    }
    
    public int getiDisplayLength()
    {
        return iDisplayLength;
    }
    
    public void setiDisplayLength(int iDisplayLength)
    {
        this.iDisplayLength = iDisplayLength;
    }
    
    public int getiSortCol_0()
    {
        return iSortCol_0;
    }
    
    public void setiSortCol_0(int iSortCol_0)
    {
        this.iSortCol_0 = iSortCol_0;
    }
    
    public String getSortDir()
    {
        return sSortDir_0;
    }
    
    public void setsSortDir_0(String sSortDir_0)
    {
        this.sSortDir_0 = sSortDir_0;
    }
    
    public void setmDataProp_0(String arg)
    {
        columns[0] = arg;
    }
    
    public void setmDataProp_1(String arg)
    {
        columns[1] = arg;
    }
    
    public void setmDataProp_2(String arg)
    {
        columns[2] = arg;
    }
    
    public void setmDataProp_3(String arg)
    {
        columns[3] = arg;
    }
    
    public void setmDataProp_4(String arg)
    {
        columns[4] = arg;
    }
    
    public void setmDataProp_5(String arg)
    {
        columns[5] = arg;
    }
    
    public void setmDataProp_6(String arg)
    {
        columns[6] = arg;
    }
    
    public void setmDataProp_7(String arg)
    {
        columns[7] = arg;
    }
    
    public void setmDataProp_8(String arg)
    {
        columns[8] = arg;
    }
    
    public void setmDataProp_9(String arg)
    {
        columns[9] = arg;
    }
    
    public void setmDataProp_10(String arg)
    {
        columns[10] = arg;
    }
    
    public void setmDataProp_11(String arg)
    {
        columns[11] = arg;
    }
    
    public void setmDataProp_12(String arg)
    {
        columns[12] = arg;
    }
    
    public void setmDataProp_13(String arg)
    {
        columns[13] = arg;
    }
    
    public void setmDataProp_14(String arg)
    {
        columns[14] = arg;
    }
    
    private String getProperty(int column)
    {
        return columns[column];
    }
    
    public String getSortProperty()
    {
        return getProperty(getiSortCol_0());
    }
    
    public Map<String, String> getSortMap()
    {
        String sortColumn = getSortProperty();
        String sortDir = getSortDir();
        Map<String, String> sortMap = new LinkedHashMap<String, String>();
        
        if (StringUtils.isEmpty(sortColumn) || StringUtils.equals("0", sortColumn))
        {
            return sortMap;
        }
        
        String sort = CommonConstants.ORDER_ASC;
        if (StringUtils.equalsIgnoreCase(CommonConstants.ORDER_DESC, sortDir))
        {
            sort = CommonConstants.ORDER_DESC;
        }
        sortMap.put(sortColumn, sort);
        return sortMap;
    }
    
}
