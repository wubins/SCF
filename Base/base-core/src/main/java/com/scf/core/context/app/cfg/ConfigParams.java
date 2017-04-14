/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.core.context.app.cfg;

import java.util.Properties;

import com.scf.utils.StringUtilies;
/**
 * 配置参数
 * @author wubin
 */
public class ConfigParams {

    /**
     *
     */
    private Properties prop = null;

    /**
     *
     * @param prop
     */
    public void setParams(Properties prop) {
        this.prop = prop;
    }

    /**
     *
     * @return
     */
    public Properties getParams() {
        return this.prop;
    }

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     * @throws java.lang.Exception
     */
    public Object get(String key, Object defaultValue)  {
        if (defaultValue == null) {
            return getString(key, null);
        }
        if (defaultValue instanceof String) {
            return getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            return getBoolean(key, (Boolean) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return getInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof Double) {
            return getDouble(key, (Double) defaultValue);
        }
        return defaultValue;
    }

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getString(String key, String defaultValue) {
        return prop.getProperty(key, defaultValue);
    }

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public int getInt(String key, int defaultValue) {
        String value = prop.getProperty(key);
        return StringUtilies.string2Int(value, defaultValue);
    }

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = prop.getProperty(key);
        return StringUtilies.string2Boolean(value, defaultValue);
    }

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public double getDouble(String key, double defaultValue) {
        String value = prop.getProperty(key);
        return StringUtilies.string2Number(value, defaultValue);
    }
    
}
