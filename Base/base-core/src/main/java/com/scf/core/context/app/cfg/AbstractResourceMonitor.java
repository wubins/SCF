/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.core.context.app.cfg;

import java.io.IOException;
import java.util.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

public abstract class AbstractResourceMonitor extends Observable implements ResourceMonitorable {

    private static final Logger _logger = LoggerFactory.getLogger(AbstractResourceMonitor.class);
    /**
     * 上次变化时间
     */
    protected long prevModified = 0l;

    /**
     *
     * @return
     */
    @Override
    public abstract String[] monitorResources();

    /**
     *
     * @param resources
     * @return
     * @throws IOException
     */
    @Override
    public boolean changed(Resource[] resources) throws IOException {
        for (Resource r : resources) {
            if (r.exists() &&  r.lastModified() > prevModified) {
                prevModified = System.currentTimeMillis();
                _logger.info("Resource '" + r.getFilename() + " changed, reloading.");
                return true;
            }
        }
        return false;
    }
}
