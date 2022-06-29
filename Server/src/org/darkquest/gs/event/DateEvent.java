package org.darkquest.gs.event;

import java.util.Calendar;

/**
 * 
 * @author User
 *
 */
public abstract class DateEvent extends DelayedEvent {

    public DateEvent(Calendar d) {
    	super(null, (int)(d.getTimeInMillis() - System.currentTimeMillis()));
    }

    public abstract void action();

    public void run() {
		action();
		super.matchRunning = false;
    }

}