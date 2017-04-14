package com.scf.core.ebus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wub
 *
 */
public class EventBusRepository {
    
    private static final Logger _logger = LoggerFactory.getLogger(EventBusRepository.class);
    /**
     *默认的bus名称
     */
    public static final String SCOPE_GLOBAL = "global";
    
    /**
     * 同步bus的名称
     */
    public static final String SCOPE_SYNC = "sync_global_bus_name";
    /**
     * 异步bus的名称
     */
    public static final String SCOPE_ASYNC = "async_global_bus_name";
    

    /**
     *
     */
    private static final Map<String, IEventBus> ebs = Collections.synchronizedMap(new HashMap<String, IEventBus>());

    /**
     *
     */
    private EventBusRepository() {
        
    }

    /**
     *
     * @param async
     */
    public static void regGlobalBus(boolean async) {
        regEventBus(SCOPE_GLOBAL, EventBusProvidor.TYPE_GUAVA, async);
    }
    
    /**
    *注册同步事件总线
    * @param async
    */
   public static void regGlobalSyncBus() {
       regEventBus(SCOPE_SYNC, EventBusProvidor.TYPE_GUAVA, false);
   }
   
   public static void regGlobalAsyncBus() {
       regEventBus(SCOPE_ASYNC, EventBusProvidor.TYPE_GUAVA, true);
   }
    
    public static void unRegGlobalBus(){
        ebs.remove(SCOPE_GLOBAL);
    }
    
    /**
     * 删除全局异步总线
     */
    public static void unRegGlobalAsyncBus(){
        ebs.remove(SCOPE_ASYNC);
    }
    /**
     * 删除全局同步总线
     */
    public static void unRegGlobalSyncBus(){
        ebs.remove(SCOPE_SYNC);
    }
    
    /**
     * 
     * @return 
     */
    public static IEventBus getGlobalBus(){
        return ebs.get(SCOPE_GLOBAL);
    }
    
    /**
     * 获取异步事件总线
     * @return 
     */
    public static IEventBus getGlobalAsyncBus(){
        return ebs.get(SCOPE_ASYNC);
    }
    
    
    /**
     * 获取同步事件总线
     * @return 
     */
    public static IEventBus getGlobalSyncBus(){
        return ebs.get(SCOPE_SYNC);
    }

    /**
     *
     * @param name
     * @return
     */
    public static boolean exists(String name) {
        return ebs.containsKey(name);
    }

    /**
     *
     * @param name
     * @param async
     */
    public static void regEventBus(String name, boolean async) {
        regEventBus(name, EventBusProvidor.TYPE_GUAVA, async);
    }

    /**
     *
     * @param name
     * @param type
     * @param async
     */
    public static void regEventBus(String name, String type, boolean async) {
        if (exists(name)) {
            throw new EventBusException("Event bus exists for name '" + name + "'.");
        }
        IEventBus eb = EventBusProvidor.create(name, type, async);
        eb.addEventListener(new DeadEventListener());
        regEventBus(name, eb);
    }

    /**
     *
     * @param name
     * @param eb
     */
    public static void regEventBus(String name, IEventBus eb) {
        if (exists(name)) {
            throw new EventBusException("Event bus exists for name '" + name + "'.");
        }
        ebs.put(name, eb);
        _logger.info("Registed event bus name: " + name);
    }

    /**
     *
     * @param name
     * @return
     */
    public static IEventBus getEventBus(String name) {
        return ebs.get(name);
    }
}
