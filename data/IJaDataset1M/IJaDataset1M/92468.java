package com.android.cts;

import com.android.ddmlib.log.LogReceiver.ILogListener;
import com.android.ddmlib.log.LogReceiver.LogEntry;
import java.util.ArrayList;

/**
 * This class allows multiplexing of log listeners onto a single device.
 */
public class MultiplexingLogListener implements ILogListener {

    private ArrayList<ILogListener> listeners = new ArrayList<ILogListener>();

    /**
     * Add a new listener.
     *
     * @param listener the listener
     */
    public void addListener(ILogListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove an existing listener.
     *
     * @param listener the listener to remove.
     */
    public void removeListener(ILogListener listener) {
        listeners.remove(listener);
    }

    /** {@inheritDoc} */
    public void newData(byte[] data, int offset, int length) {
        for (ILogListener listener : listeners) {
            listener.newData(data, offset, length);
        }
    }

    /** {@inheritDoc} */
    public void newEntry(LogEntry entry) {
        for (ILogListener listener : listeners) {
            listener.newEntry(entry);
        }
    }
}
