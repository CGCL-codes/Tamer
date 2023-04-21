package com.catarak.uwhscoretime.phidget;

import com.catarak.uwhscoretime.model.Match;
import com.phidgets.EncoderPhidget;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bouchard
 */
public abstract class DelayedControlState extends ControlState {

    public DelayedControlState(Match m, LCDConnector lcd, PhidgetConnector<EncoderPhidget> encoderManager) {
        super(m, lcd, encoderManager);
    }

    public synchronized void addPhidgetActionListener(PhidgetActionListener phidgetActionListener) {
        if (!listener.contains(phidgetActionListener)) {
            listener.add(phidgetActionListener);
        }
    }

    public synchronized void removePhidgetActionListener(PhidgetActionListener aThis) {
        listener.remove(aThis);
    }

    protected synchronized void fireDone(ControlState displayScoreState) {
        for (PhidgetActionListener phidgetActionListener : listener) {
            log.debug("notifying: " + phidgetActionListener + " count:" + listener.size());
            phidgetActionListener.done(displayScoreState);
        }
    }

    private final List<PhidgetActionListener> listener = new ArrayList<PhidgetActionListener>();
}
