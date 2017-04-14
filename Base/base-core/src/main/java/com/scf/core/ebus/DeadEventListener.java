package com.scf.core.ebus;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wub
 *
 */
public class DeadEventListener {
    
    private static final Logger _logger = LoggerFactory.getLogger(DeadEventListener.class);

    @Subscribe
    public void handleEvent(DeadEvent deadEvent) {
        _logger.warn("No subscribers for " + deadEvent.getEvent());
    }
}
