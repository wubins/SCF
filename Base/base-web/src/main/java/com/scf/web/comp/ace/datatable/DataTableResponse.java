package com.scf.web.comp.ace.datatable;

/**
 * datatable 响应
 * @author wubin
 * @date 2016年7月28日 下午7:13:56 
 * @version V1.1.0
 */
public class DataTableResponse {
    
    private long recordsTotal;
    private long recordsFiltered;
    private Object data;
    
    /**
     * 创建响应
     * @author wubin
     * @param recordsTotal
     * @param recordsFiltered
     * @param obj
     * @return
     */
    public static DataTableResponse createResponse(long recordsTotal,long recordsFiltered , Object obj){
        DataTableResponse response = new DataTableResponse();
        response.setRecordsTotal(recordsTotal);
        response.setRecordsFiltered(recordsFiltered);
        response.setData(obj);
        return response;
    }

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
