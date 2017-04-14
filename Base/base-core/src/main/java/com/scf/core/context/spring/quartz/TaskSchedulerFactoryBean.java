package com.scf.core.context.spring.quartz;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.MethodInvoker;

import com.scf.core.context.app.cfg.initor.RootConfigInitor;
import com.scf.core.meta.TaskIdentity;
import com.scf.utils.AssertUtilies;
/**
 * 检测所有的trigger对应的JobBean是否标注TaskIdentity注解
 * 
 * 继承SchedulerFactoryBean
 * @author wub
 *
 */
public class TaskSchedulerFactoryBean extends SchedulerFactoryBean{
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public void afterPropertiesSet() throws Exception {
		
		super.afterPropertiesSet();
		
	     String enableQuartz = null;
	      try {
	    	  enableQuartz = (String)RootConfigInitor.PARAMS.get("task.quartz.backup.enable", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	      if(!"true".equals(enableQuartz)){
	    	  return ;
	      }
	      
		Scheduler scheduler = super.getScheduler();
		String[] triggerGroupNames= scheduler.getTriggerGroupNames();
		List<String> triggerGroupNamesList= Arrays.asList(triggerGroupNames);
		if(CollectionUtils.isNotEmpty(triggerGroupNamesList)){
			for(String triggerGroupName : triggerGroupNamesList){
				 String[] triggerNames = scheduler.getTriggerNames(triggerGroupName);
					List<String> triggerNamesList= Arrays.asList(triggerNames);
					if(CollectionUtils.isNotEmpty(triggerNamesList)){
						triggers : for(String triggerName : triggerNamesList){
							Trigger trigger = scheduler.getTrigger(triggerName, triggerGroupName);
							JobDetail jobDetail = scheduler.getJobDetail(trigger.getJobName(), trigger.getJobGroup());
							if(jobDetail != null){
								MethodInvoker methodInvoker= (MethodInvoker)jobDetail.getJobDataMap().get("methodInvoker");
								AssertUtilies.notNull(methodInvoker, "quartz JobDetail' methodInvoker is null !!!");
								Class targetClass= methodInvoker.getTargetClass();
								String targetMethod = methodInvoker.getTargetMethod();
								//这个方法包含父类和接口的所有方法
								Method[] methods= targetClass.getMethods();
								List<Method> methodsList= Arrays.asList(methods);
								if(CollectionUtils.isNotEmpty(methodsList)){
									for(Method method : methodsList){
										Annotation taskIdentityAnnotation = null;
										// 需要判断目标方法是否配置TaskIdentity注解
										if(method.getName().equals(targetMethod)){
											taskIdentityAnnotation = method.getAnnotation(TaskIdentity.class);
											if(taskIdentityAnnotation != null){
												return ;
											}
											Class dec = method.getDeclaringClass();
											Class decSuper = dec.getSuperclass();
											//如果方法被重写了，再寻找父类的所有方法注解进行判断
											if(decSuper != null && !decSuper.getClass().equals(Object.class)){
												Method[]  superMethods =  decSuper.getMethods();
												List<Method> superMethodsList= Arrays.asList(superMethods);
												for(Method superMethod : superMethodsList){
													if(superMethod.getName().equals(targetMethod)){
														taskIdentityAnnotation = superMethod.getAnnotation(TaskIdentity.class);
														if(taskIdentityAnnotation != null){
															continue triggers;
														}
													}
												}
												
											}
											
											// 如果走到这里就说明没有注解，暂时不校验
											AssertUtilies.notNull(taskIdentityAnnotation,"quartz JobDetail'method must annotationed by  @TaskIdentity !!!   triggerName is "+triggerName);
										
										}
										
									
									}
									
								
								}
							
							}
							
						}
					}
					
					
			}
		}
	}
}
