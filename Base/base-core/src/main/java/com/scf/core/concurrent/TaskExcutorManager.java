package com.scf.core.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.scf.core.context.spring.ContextAware;
import com.scf.core.exception.sys.SystemException;
/**
 * 多线程执行管理器
 * @author wub
 *
 */
@SuppressWarnings("static-access")
public class TaskExcutorManager {
	
private static Logger logger = LoggerFactory.getLogger(TaskExcutorManager.class);
    
    // spring 上下文存储器
    private static ContextAware applicationContextAware;
    
    //线程池对象
    private static ThreadPoolTaskExecutor poolTaskExecutor ;

	/**
	 * 初始化
	 */
    static{
    	poolTaskExecutor =  (ThreadPoolTaskExecutor)applicationContextAware.getBean("scf:taskExecutor");
    }
    
    /**
     *执行任务
     * @param runnable
     */
    public static void execute(Runnable runnable){
    	getPoolTaskExecutor().execute(runnable);
    }
    
    
    
    //获取poolTaskExecutor对象
    private  static ThreadPoolTaskExecutor getPoolTaskExecutor(){
    	if(poolTaskExecutor == null){
    		logger.warn("=====线程池taskExecutor配置异常，检查是否配置了ThreadPoolTaskExecutor bean=====");
    		throw SystemException.configException("线程池ThreadPoolTaskExecutor", null);
    	}
    	return poolTaskExecutor;
    }
    
    
}
