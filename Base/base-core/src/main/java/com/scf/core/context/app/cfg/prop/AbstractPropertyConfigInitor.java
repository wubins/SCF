/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.core.context.app.cfg.prop;

import java.util.Enumeration;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scf.core.context.app.cfg.AbstractResourceMonitor;
import com.scf.core.context.app.cfg.IBootstartupInitialization;

public abstract class AbstractPropertyConfigInitor extends AbstractResourceMonitor implements IBootstartupInitialization {
    
    private static final Logger _logger = LoggerFactory.getLogger(AbstractPropertyConfigInitor.class);

    /**
     *
     * @return
     */
    @Override
    public abstract String[] monitorResources();

    /**
     *
     * @param config
     * @return
     * @throws Exception
     */
    protected Properties _init(Object[] config) throws Exception {
        Properties p0 = (Properties) config[0];
        Properties p1 = (Properties) config[1];
        if (p0 == null) {
            _logger.warn("Can load config files for " + this.getClass().getName());
            return new Properties();
        }
        if (p1 != null) {
            Enumeration pks = p1.propertyNames();
            while (pks.hasMoreElements()) {
                String pk = (String) pks.nextElement();
                if (p0.containsKey(pk)) {
                    _logger.info("Config impl override " + pk + " = " + p1.getProperty(pk) + "[default: " + p0.getProperty(pk) + "]");
                }
                p0.setProperty(pk, p1.getProperty(pk));
            }
        }
        if (_logger.isInfoEnabled()) {
            Enumeration pks = p0.propertyNames();
            while (pks.hasMoreElements()) {
                String pk = (String) pks.nextElement();
                _logger.info(pk + " = " + p0.getProperty(pk));
            }
        }
        return p0;
    }

    /**
     *
     * @return
     */
    @Override
    public abstract String[] getResource();

    /**
     *
     * @return
     */
    @Override
    public abstract int sort();
}
