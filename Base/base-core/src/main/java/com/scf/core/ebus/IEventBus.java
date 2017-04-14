package com.scf.core.ebus;

/**
 * 
 * @author wub
 *
 */
public interface IEventBus {

    public void dispatchEvent(Object event);

    public void addEventListener(Object listener);
    
    public void removeEventListener(Object listener);
}
