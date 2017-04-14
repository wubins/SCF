package com.scf.core.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scf.core.context.app.cfg.initor.RootConfigInitor;


/**
 * 系统开关上下文
 * @author wub
 *
 */
public class SwitchContext {
	
	 private static final Logger logger = LoggerFactory.getLogger(SwitchContext.class);
	
	/** 没有配置开关，是否默认关闭 */
    private static boolean defaultOpen = false;
    
    /** 开关分隔符 */
    private static String switchSplit = ",";
    
    /**
     * 判断开关是否打开
     * @param swc 开关名称
     */
    public static boolean isOpen(String swc) {
    	String val = null;
		try {
			val = (String)RootConfigInitor.PARAMS.get(swc,null);
		} catch (Exception e) {
			logger.error("SwitchHandler===isOpen", e);
		}
        return null != val ? parseSwitch(val) : defaultOpen;
    }
    
    /**
     * 判断开关是否关闭
     * @param swc 开关名称
     */
    public static boolean isClose(String swc) {
     	String val = null;
    		try {
    			val = (String)RootConfigInitor.PARAMS.get(swc,null);
    		} catch (Exception e) {
    			logger.error("SwitchHandler===isClose", e);
    		}
        return null != val ? !parseSwitch(val) : !defaultOpen;
    }
    
    /**
     * 判断开关数组中的开关是否全部打开
     * @param swcs 开关名称数组
     */
    public static boolean isOpenAll(String swcs) {
        String[] swcArr = swcs.split(switchSplit);

        for (String swc : swcArr) {
         	String val = null;
    		try {
    			val = (String)RootConfigInitor.PARAMS.get(swc,null);
    		} catch (Exception e) {
    			logger.error("SwitchHandler===isOpenAll", e);
    		}
            if (null != val ? !parseSwitch(val) : !defaultOpen) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 判断开关数组中的开关是否存在一个打开的
     * @param swcs 开关名称数组
     */
    public static boolean isOpenAny(String swcs) {
        String[] swcArr = swcs.split(switchSplit);

        for (String swc : swcArr) {
        	String val = null;
    		try {
    			val = (String)RootConfigInitor.PARAMS.get(swc,null);
    		} catch (Exception e) {
    			logger.error("SwitchHandler===isOpenAny", e);
    		}
            if (null != val ? parseSwitch(val) : defaultOpen) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 判断开关数组中的开关是否全部关闭
     * @param swcs 开关名称数组
     */
    public static boolean isCloseAll(String swcs) {
        String[] swcArr = swcs.split(switchSplit);

        for (String swc : swcArr) {
        	String val = null;
    		try {
    			val = (String)RootConfigInitor.PARAMS.get(swc,null);
    		} catch (Exception e) {
    			logger.error("SwitchHandler===isCloseAll", e);
    		}
            if (null != val ? parseSwitch(val) : defaultOpen) {
                return false;
            }
        }
        
        return true;
    }

    private static boolean parseSwitch(String val) {
        return "1".equals(val) || "open".equals(val) || "true".equals(val);
    }

    public void setDefaultOpen(boolean defaultOpen) {
        SwitchContext.defaultOpen = defaultOpen;
    }

    public void setSwitchSplit(String switchSplit) {
        SwitchContext.switchSplit = switchSplit;
    }

}
