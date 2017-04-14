package com.scf.core.context;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.SystemPropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 资源文件加载器
 * @author wubin
 */
public class ClassScanner implements ResourceLoaderAware {

    /**
     * 
     */
    private static final Logger _logger = LoggerFactory.getLogger(ClassScanner.class);

    /**
     * 
     */
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    /**
     * 
     */
    private final List<TypeFilter> includeFilters = new LinkedList<TypeFilter>();
    /**
     * 
     */
    private final List<TypeFilter> excludeFilters = new LinkedList<TypeFilter>();
    /**
     * 
     */
    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(
            this.resourcePatternResolver);

    
    private ClassScanner() {
    }

    /**
     * 
     * @param resourceLoader 
     */
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils
                .getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(
                resourceLoader);
    }

    /**
     * 
     * @return 
     */
    public final ResourceLoader getResourceLoader() {
        return this.resourcePatternResolver;
    }

    /**
     * 
     * @param includeFilter 
     */
    public void addIncludeFilter(TypeFilter includeFilter) {
        this.includeFilters.add(includeFilter);
    }

    /**
     * 
     * @param excludeFilter 
     */
    public void addExcludeFilter(TypeFilter excludeFilter) {
        this.excludeFilters.add(0, excludeFilter);
    }

    /**
     * 
     * @param useDefaultFilters 
     */
    public void resetFilters(boolean useDefaultFilters) {
        this.includeFilters.clear();
        this.excludeFilters.clear();
    }

    /**
     * 
     * @param basePackage
     * @param resName
     * @return 
     */
    public static Resource[] scan(String basePackage, String resName) {
        ClassScanner cs = new ClassScanner();
        return cs.doScanResource(basePackage, resName);
    }

    /**
     * 
     * @param basePackage
     * @param classes
     * @return 
     */
    public static Set<Class> scan(String basePackage, Class... classes) {
        ClassScanner cs = new ClassScanner();
        for (Class anno : classes) {
            if (anno.isAnnotation()) {
                cs.addIncludeFilter(new AnnotationTypeFilter(anno));
            } else {
                cs.addIncludeFilter(new AssignableTypeFilter(anno));
            }
        }
        return cs.doScanClass(basePackage);
    }

    /**
     * 
     * @param basePackages
     * @param classes
     * @return 
     */
    public static Set<Class> scan(String[] basePackages, Class... classes) {
        ClassScanner cs = new ClassScanner();
        for (Class anno : classes) {
            if (anno.isAnnotation()) {
                cs.addIncludeFilter(new AnnotationTypeFilter(anno));
            } else {
                cs.addIncludeFilter(new AssignableTypeFilter(anno));
            }
        }
        Set<Class> cls = new HashSet<Class>();
        for (String s : basePackages) {
            cls.addAll(cs.doScanClass(s));
        }
        return cls;
    }

    /**
     * 
     * @param basePackage
     * @param resName
     * @return 
     */
    public Resource[] doScanResource(String basePackage, String resName) {
        Resource[] reses = null;
        try {
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + org.springframework.util.ClassUtils
                    .convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage))
                    + resName;
            _logger.info("Searching " + packageSearchPath + " for " + resName);
            reses = this.resourcePatternResolver.getResources(packageSearchPath);

        } catch (IOException ex) {
            throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
        }
        return reses;
    }

    /**
     * 
     * @param basePackage
     * @return 
     */
    public Set<Class> doScanClass(String basePackage) {
        Set<Class> classes = new HashSet<Class>();
        try {
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + org.springframework.util.ClassUtils
                    .convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage))
                    + "/**/*.class";
            _logger.info("Searching " + packageSearchPath);
            Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);

            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
                    if ((includeFilters.isEmpty() && excludeFilters.isEmpty()) || matches(metadataReader)) {
                        try {
                            classes.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
                        } catch (ClassNotFoundException e) {
                        } catch (NoClassDefFoundError e) {
                        }
                    }
                }
            }
        } catch (IOException ex) {
            throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
        }
        return classes;
    }

    /**
     * 
     * @param metadataReader
     * @return
     * @throws IOException 
     */
    protected boolean matches(MetadataReader metadataReader) throws IOException {
        for (TypeFilter tf : this.excludeFilters) {
            if (tf.match(metadataReader, this.metadataReaderFactory)) {
                return false;
            }
        }
        for (TypeFilter tf : this.includeFilters) {
            if (tf.match(metadataReader, this.metadataReaderFactory)) {
                return true;
            }
        }
        return false;
    }
}
