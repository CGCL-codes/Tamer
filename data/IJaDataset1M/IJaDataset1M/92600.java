package net.sourceforge.ljm.consolidator;

import net.sourceforge.ljm.timeslice.TimeSliceType;

public interface ConsolidatorMBean {

    void clearMonitors();

    void add(TimeSliceType time, String monitor);

    void remove(TimeSliceType time, String monitor);

    void stop();

    String getMonitorsList(TimeSliceType time);

    void start();
}
