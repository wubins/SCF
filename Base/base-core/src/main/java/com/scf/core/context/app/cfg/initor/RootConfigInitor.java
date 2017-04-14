/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.core.context.app.cfg.initor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scf.core.context.app.cfg.ConfigConsts;
import com.scf.core.context.app.cfg.ConfigParams;
import com.scf.core.context.app.cfg.IBootstartupInitialization;
import com.scf.core.context.app.cfg.prop.AbstractPropertyConfigInitor;
import com.scf.utils.StringUtilies;

/**
 *  全局配置初始化，不需要设置ResourceInitor注解
 *  加载scf的project中的config.properties文件
 *  优先级：外链，scf，project
 */
public class RootConfigInitor extends AbstractPropertyConfigInitor implements IBootstartupInitialization {

    private static final Logger _logger = LoggerFactory.getLogger(RootConfigInitor.class);
    /**
     * source config files
     */
    private final String[] files = new String[]{"scf/config.properties", "project/config.properties"};
    /**
     *
     */
    public final static Params PARAMS = new Params();

    /**
     * @throws java.lang.Exception
     * @see IBootstartupInitialization
     * @param config
     */
    @Override
    public void init(Object[] config) throws Exception {
        Properties p0 = super._init(config);
        String outLink = p0.getProperty("out.link");
        if (!StringUtilies.isNullOrEmpty(outLink)) {
            _logger.info("Looked out link config file: " + outLink + ", now reading.");
            File _file = new File(outLink);
            if (_file.exists()) {
                _logger.info("Process out link config properties.");
                Properties _p = new Properties();
                InputStream _is = new FileInputStream(_file);
                _p.load(_is);
                _is.close();
                p0 = super._init(new Object[]{p0, _p});
            } else {
                _logger.warn("Out link config file not exists, continue.");
            }
        }
        PARAMS.setParams(p0);
        initConsts();
        super.prevModified = System.currentTimeMillis();
    }

    /**
     * init consts.
     *
     * @throws Exception
     */
    private void initConsts() throws Exception {
        ConfigConsts.init(PARAMS, CONSTS.class);
    }

    /**
     *
     * @return
     */
    @Override
    public String[] getResource() {
        return files;
    }

    /**
     *
     * @return
     */
    @Override
    public String[] monitorResources() {
        return files;
    }

    /**
     *
     * @return
     */
    @Override
    public int sort() {
        return Integer.MIN_VALUE;
    }

    /**
     *
     */
    public static class Params extends ConfigParams {
    }

    /**
     *
     */
    public static class CONSTS {

    }
}
