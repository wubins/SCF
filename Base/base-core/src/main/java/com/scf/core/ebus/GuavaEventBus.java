package com.scf.core.ebus;

import com.google.common.eventbus.EventBus;

/**
 * @author wub
 *
 */
public class GuavaEventBus extends EventBus implements IEventBus {
    
    public GuavaEventBus(String name){
        super(name);
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
