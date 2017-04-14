package com.scf.core.ebus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * @author wub
 *
 */
public class EventBusProvidor {

    /**
     *
     */
    public static final String TYPE_GUAVA = "guava";

    /**
     *
     */
    private EventBusProvidor() {

    }

    /**
     *
     * @param name
     * @param type
     * @param async
     * @return
     */
    public static IEventBus create(String name, String type, boolean async) {
        IEventBus eb = null;
        if (type == null) {
            type = TYPE_GUAVA;
        }
        if (type.equalsIgnoreCase(TYPE_GUAVA)) {
            if (async) {
                ExecutorService pool = Executors.newCachedThreadPool();
                eb = new GuavaAsyncEventBus(name, pool);
            } else {
                eb = new GuavaEventBus(name);
            }
        }
        if (eb == null) {
            throw new EventBusException("Can not create event bus instance for type '" + type + "'");
        }
        return eb;
    }
}
