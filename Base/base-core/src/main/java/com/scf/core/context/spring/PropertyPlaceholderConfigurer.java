/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.core.context.spring;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scf.utils.StringUtilies;

/**
 * spring 占位符处理器，外链文件优先
 * @author wubin
 */
public class PropertyPlaceholderConfigurer extends org.springframework.beans.factory.config.PropertyPlaceholderConfigurer {

    private static final Logger _logger = LoggerFactory.getLogger(PropertyPlaceholderConfigurer.class);

    /**
     *
     * @param props
     * @throws IOException
     */
    @Override
    protected void loadProperties(Properties props) throws IOException {
        _logger.info("Loading place holder properties.");
        super.loadProperties(props);
        String outLink = props.getProperty("out.link");
        if (!StringUtilies.isNullOrEmpty(outLink)) {
            _logger.info("Loading out link propertie for '" + outLink + "'.");
            File _file = new File(outLink);
            if (_file.exists()) {
                _logger.info("Process out link config properties.");
                Properties _p = new Properties();
                InputStream _is = new FileInputStream(_file);
                _p.load(_is);
                _is.close();
                Enumeration enu = _p.keys();
                while (enu.hasMoreElements()) {
                    String k = (String) enu.nextElement();
                    String v = _p.getProperty(k);
                    _logger.info("Override properties " + k + " = " + v + "[old:" + props.getProperty(k) + "]");
                    props.put(k, v);
                }
            } else {
                _logger.warn("Out link config file not exists, continue.");
            }
        }
        _logger.info("---------------------------------------------------------");
        if (_logger.isInfoEnabled()) {
            Enumeration enu = props.keys();
            while (enu.hasMoreElements()) {
                String k = (String) enu.nextElement();
                _logger.info(k + " = " + props.getProperty(k));
            }
        }
        _logger.info("---------------------------------------------------------");
    }
}
