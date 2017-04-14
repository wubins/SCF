/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.core.context.app.cfg.module;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.scf.core.context.ClassScanner;
import com.scf.core.context.app.cfg.ConfigParams;
import com.scf.utils.JacksonObjectMapper;

/**
 * 模块参数处理器
 * 
 * @author wubin
 * 
 */
public class ModuleConfigHandler {

    private static final Log _logger = LogFactory.getLog(ModuleConfigHandler.class);
    /**
     *
     */
    private final static Map<String, ConfigParams> map = new HashMap<String, ConfigParams>();


    /**
     *  获取复杂json数组结构属性：{a:xx,..}
     * @author wubin
     * @param moduleName
     * @param propertyName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, String>> getJsonArrProp(String moduleName, String propertyName) {
        String cofigValue = (String) ModuleConfigHandler.getModuleConfig(moduleName).get(propertyName, null);

        List<Map<String, String>> configParamList = (List<Map<String, String>>) JacksonObjectMapper
                .fromJsonString(cofigValue, JacksonObjectMapper.getCollectionType(List.class, Map.class));

        return configParamList;
    }

    /**
     * 获取复杂json对象性：{a:xx,b:yy..}
     * @author wubin
     * @param moduleName
     * @param propertyName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getJsonProp(String moduleName, String propertyName) {
        String cofigValue = (String) ModuleConfigHandler.getModuleConfig(moduleName).get(propertyName, null);

        Map<String, String> configParamMap = (Map<String, String>) JacksonObjectMapper.fromJsonString(cofigValue,
                Map.class);
        return configParamMap;
    }
    
    /**
     * 获取复杂json数组结构属性：[{a:xx,..},{a:yy,..}]
     * @author wubin
     * @param moduleName
     * @param propertyName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, String>> getJsonPropArr(String moduleName, String propertyName) {
        String cofigValue = (String) ModuleConfigHandler.getModuleConfig(moduleName).get(propertyName, null);

        List<Map<String, String>> configParamList = (List<Map<String, String>>)JacksonObjectMapper.fromJsonString(cofigValue, JacksonObjectMapper.getCollectionType(List.class, Map.class));

        return configParamList;
    }
    


    /**
     * 快速获取普通string prop
     * @author wubin
     * @param moduleName
     * @param propertyName
     */
    public static String getSimpleProp(String moduleName, String propertyName) {
        String proValue = null;
        ConfigParams configParams = ModuleConfigHandler.getModuleConfig(moduleName);
        if (configParams != null) {
            proValue = configParams.getString(propertyName, null);
        }
        return proValue;
    }

    /**
     * 获取ConfigParams，里面包含properties
     * 
     * @param moduleName 模块的名称，其实是project包名下的包名：project.file,file也就是moduleName
     * @return
     */
    public static ConfigParams getModuleConfig(String moduleName) {
        if (map.containsKey(moduleName)) {
            return map.get(moduleName);
        }
        return null;
    }

    /**
     * marges 覆盖 props中的属性
     * 
     * @param marges
     * @param moduleName
     * @param props
     * @return
     */
    @SuppressWarnings("rawtypes")
    private static ConfigParams loadModuleConfig(Properties marges, String moduleName, Properties props) {
        if (marges != null) {
            Enumeration pks = marges.propertyNames();
            while (pks.hasMoreElements()) {
                String pk = (String) pks.nextElement();
                String _pk = "module_" + moduleName + ".";
                if (pk.startsWith(_pk)) {
                    _pk = pk.substring(_pk.length());
                    _logger.info("Module config override " + pk + " = " + marges.getProperty(pk) + "[default: "
                            + props.getProperty(_pk) + "]");
                    props.setProperty(_pk, marges.getProperty(pk));
                }
            }
        }
        ConfigParams cp = new ConfigParams();
        cp.setParams(props);
        return cp;
    }

    /**
     *
     * @param classPackage
     * @param marges
     */
    private static void loadModuleConfigs(String classPackage, Properties marges) {
        Resource[] resources = ClassScanner.scan(classPackage, "config.properties");
        for (Resource resource : resources) {
            try {
                if (!resource.exists()) {
                    continue;
                }
                Properties props = new Properties();
                // 获取相对路径的file名称
                String moduleName = resource.createRelative("/").getFilename();
                PropertiesLoaderUtils.fillProperties(props, new EncodedResource(resource, "utf-8"));
                ConfigParams cp = loadModuleConfig(marges, moduleName, props);
                _logger.info("Loaded module config for " + moduleName + " has " + cp.getParams().keySet().size()
                        + " properties.");
                map.put(moduleName, cp);
            } catch (IOException ex) {
                _logger.warn("Can not load module config for " + resource, ex);
            }
        }
    }

    private static void loadScfModuleConfigs(Properties marges) {
        loadModuleConfigs("scf/module/**/", marges);
    }

    private static void loadProjectModuleConfig(Properties marges) {
        loadModuleConfigs("project/module/**/", marges);
    }

    public static void loadModuleConfigs(Properties marges) {
        loadScfModuleConfigs(marges);
        loadProjectModuleConfig(marges);
    }

}
