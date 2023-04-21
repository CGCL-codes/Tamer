    private ValueListener addMonitor(String pvName) {
        Channel c1 = ChannelFactory.defaultFactory().getChannel(pvName);
        ValueListener l = new ValueListener(pvName);
        try {
            c1.addMonitorValTime(l, Monitor.VALUE);
        } catch (ConnectionException e1) {
            fail("ConnectionException on: " + pvName);
        } catch (MonitorException e1) {
            fail("MonitorException on: " + pvName);
        }
        return l;
    }
