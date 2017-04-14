package com.scf.core.context.spring.quartz;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.scf.core.context.app.cfg.initor.RootConfigInitor;
import com.scf.core.context.spring.memcache.cleint.MemCachedOperation;
import com.scf.utils.UUIDUtilies;
/**
 * 定时任务job切面类
 * 
 * @author wub
 *
 */
public class QuartzJobAspect implements  InitializingBean
{
    private static final Logger logger = LoggerFactory.getLogger(QuartzJobAspect.class);
    
    public static final String QUARTZ_LOCK = "quartz_lock_";
    
    /**
     * 集群时的项目标识，集群下的相同项目每个标识应该不同，否则无法实现互备
     */
    private String clusterIdentity;
    
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable
    {
        
      String  className= pjp.getSignature().getDeclaringType().getName();
  	  String methodName = pjp.getSignature().getName();
        
        boolean canExcute = chechAliveJobDetail(className,methodName);
        
        Object returnResult = null;
        
        if(canExcute){
        	logger.info("quartz jobDetail {}_{} 执行开始...",className,methodName);
        	returnResult =  pjp.proceed();
        	 logger.info("quartz jobDetail {}_{} 执行结束...",className,methodName);
        	 return returnResult;
        }else{
        	return null;
        }
      
    }
    /**
     * 判断是否能执行任务
     * @param pjp
     * @return
     */
    private boolean chechAliveJobDetail(String className,String methodName){
    	
    	
    	  String currentKey =QUARTZ_LOCK+className+"_"+methodName;
    	  logger.info("当前quartz jobDetail：{}_{}",className,methodName);
    	
			 boolean canExcute = true;
		   	 String enableQuartz = null;
		     try {
		   	  enableQuartz = (String)RootConfigInitor.PARAMS.get("task.quartz.backup.enable", null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		     
		     if(!"true".equals(enableQuartz)){
		   	    logger.info("当前quartz jobDetail：{}_{}  没有开启集群高可用",className,methodName);
		    	return canExcute;
		      }else{
		    	logger.info("当前quartz jobDetail：{}_{}  已经开启集群高可用",className,methodName);
		      }
    	 
    	  boolean addedSucc =  MemCachedOperation.setStringIfNotExists(currentKey,60*3, getClusterIdentity());
    	  if(!addedSucc){
    		  String clusterIdentity = (String)MemCachedOperation.get(currentKey);
    		  if(!getClusterIdentity().equals(clusterIdentity)){
    			  canExcute = false;
    			  logger.warn("当前quartz jobDetail："+className+"_"+methodName+" 无需执行,集群中的其他相同任务正在执行！");
    		  }
    	  }
          return canExcute;
    }
    
    
    @Override
    public void afterPropertiesSet() throws Exception {
    	 String enableQuartz = null;
	      try {
	    	  enableQuartz = (String)RootConfigInitor.PARAMS.get("task.quartz.backup.enable", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	      if("true".equals(enableQuartz)){
	    	  clusterIdentity = UUIDUtilies.base58Uuid();
	     	 logger.info("====当前项目的quartz标示符======"+clusterIdentity);
	      }else{
	    	  logger.info("====当前项目的quartz未开启容灾功能");
	      }
    	
    	
    }

	public String getClusterIdentity() {
		return clusterIdentity;
	}

	public void setClusterIdentity(String clusterIdentity) {
		this.clusterIdentity = clusterIdentity;
	}
    
}
