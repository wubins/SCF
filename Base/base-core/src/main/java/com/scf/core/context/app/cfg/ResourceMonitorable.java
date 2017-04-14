/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.core.context.app.cfg;

import java.io.IOException;
import org.springframework.core.io.Resource;

public interface ResourceMonitorable {

    /**
     * Need monitor resource.
     *
     * @return
     */
    public String[] monitorResources();

    /**
     * check resource changed, if you dont want monit change, can return 'false'
     * also.
     *
     * @param resources
     * @return
     * @throws IOException
     */
    public boolean changed(Resource[] resources) throws IOException;
}
