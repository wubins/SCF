package com.scf.core.context.app.cfg;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;

import com.scf.core.context.ClassScanner;
import com.scf.core.exception.sys.SystemException;
import com.scf.core.meta.InitResource;
import com.scf.utils.ClassUtilies;

public class ResourceInitor {

    private static final Logger _logger = LoggerFactory.getLogger(ResourceInitor.class);
    /**
     * root resource class param
     */
    public static final String ROOT_RESOURCE_CLASS_PARAM = "root_resource";
    /**
     * need monitor reload initors
     */
    private static final List<IBootstartupInitialization> initors = new ArrayList<IBootstartupInitialization>();
    private static final DefaultResourceLoader resourceLoader = new FileSystemResourceLoader();
    private static final Timer reloadTimer = new Timer();
    /**
     * first init status
     */
    private static boolean inited = false;

    /**
     *
     * @throws Exception
     */
    private static void reload() throws Exception {
        _logger.info("Checking resource change from " + initors.size() + " initors.");
        for (IBootstartupInitialization ii : initors) {
            ResourceMonitorable m = (ResourceMonitorable) ii;
            Resource[] resources = readResource(m.monitorResources());
            if (m.changed(resources)) {
                init(ii);
            }
        }
    }

    /**
     *
     */
    public static void load() {
        if (inited) {
            return;
        }
        List<IBootstartupInitialization> list = search();
        for (IBootstartupInitialization ii : list) {
            init(ii);
        }
        inited = true;
        //TODO  start monit server.
    }

    /**
     *
     * @param rs
     * @return
     */
    private static Resource[] readResource(String[] rs) {
        Resource[] res = new Resource[rs.length];
        int i = 0;
        for (String r : rs) {
            r = "classpath:" + r;
            Resource resource = resourceLoader.getResource(r);
            _logger.debug("Reading resource '" + r + "' using " + resource.getClass() + ".");
            res[i] = resource;
            i++;
        }
        return res;
    }

    /**
     * Unload resource
     */
    public static void unload() {
        _logger.info("Resource monit timer canceled.");
        reloadTimer.cancel();
    }

    /**
     *
     * @param initor
     * @throws ConfigException
     */
    public static void init(IBootstartupInitialization initor) throws SystemException {
        _logger.info("Loading/Reloading initor " + initor.getClass().getName() + ".");
        String[] rs = initor.getResource();
        Object[] config = new Object[rs.length];
        _logger.info("Will read " + rs.length + " files.");
        int i = 0;
        Resource[] res = readResource(rs);
        List<InputStream> iss = new ArrayList();
        try {
            for (Resource resource : res) {
                if (resource.exists()) {
                    InputStream is = resource.getInputStream();
                    iss.add(is);
                    String ext = FilenameUtils.getExtension(resource.getFilename());
                    if (ext.equals("properties")) {
                        _logger.info("Reading type of properties.");
                        config[i] = loadProperties(is);
                    } else if (ext.equals("xml")) {
                        config[i] = loadXMLStream(is);
                    }
                } else {
                    _logger.warn("Resource '" + resource + "' not exsits.");
                }
                i++;
            }
            initor.init(config);
        } catch (Exception ex) {
            _logger.error(ex.getMessage(), ex);
            throw SystemException.configException("scf加载properties文件出错！", ex);
        }
        if (initor instanceof ResourceMonitorable && !initors.contains(initor)) {
            initors.add(initor);
        }
        for (InputStream is : iss) {
            try {
                is.close();
            } catch (IOException ex) {
                _logger.warn("Close resource input stream faild.", ex);
            }
        }
    }

    /**
     *
     * @param is
     * @return
     * @throws Exception
     */
    private static Properties loadProperties(InputStream is) throws Exception {
        Properties p = new Properties();
        p.load(is);
        return p;
    }

    /**
     *
     * @param is
     * @return
     * @throws Exception
     */
    private static Document loadXML(InputStream is) throws Exception {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
    }

    /**
     *
     * @param is
     * @return
     */
    private static InputStream loadXMLStream(InputStream is) {
        return is;
    }

    /**
     *
     * @return
     */
    private static List<IBootstartupInitialization> search() {
        Set<Class> classes = ClassScanner.scan("com/xxxx/**/", InitResource.class);
        _logger.info("Searched " + classes.size() + " initors.");
        List<IBootstartupInitialization> list = new ArrayList();
        Iterator<Class> it = classes.iterator();
        while (it.hasNext()) {
            list.add((IBootstartupInitialization) ClassUtilies.newInstance(it.next()));
        }
        Collections.sort(list, new Comparator<IBootstartupInitialization>() {
            @Override
            public int compare(IBootstartupInitialization o1, IBootstartupInitialization o2) {
                if (o1.sort() < o2.sort()) {
                    return 1;
                } else if (o2.sort() < o1.sort()) {
                    return -1;
                }
                return 0;
            }
        });
        return list;
    }
}
