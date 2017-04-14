package com.scf.core.ebus;

/**
 * @author wub
 *
 */
@SuppressWarnings("serial")
public class EventBusException extends RuntimeException {

    public EventBusException(String msg) {
        super(msg);
    }
}
