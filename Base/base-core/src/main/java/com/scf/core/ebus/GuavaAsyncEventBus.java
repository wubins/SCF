package com.scf.core.ebus;

import com.google.common.eventbus.AsyncEventBus;
import java.util.concurrent.ExecutorService;

/**
 * @author wub
 *
 */
public class GuavaAsyncEventBus extends AsyncEventBus implements IEventBus {

    public GuavaAsyncEventBus(String name, ExecutorService pool) {
        super(name, pool);
    }

    @Override
    public void dispatchEvent(Object event) {
        super.post(event);
    }

    @Override
    public void addEventListener(Object listener) {
        super.register(listener);
    }

    @Override
    public void removeEventListener(Object listener) {
        super.unregister(listener);
    }

}
