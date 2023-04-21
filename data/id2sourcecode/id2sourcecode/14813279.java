    public void setChannelName(String chanName) {
        this.chanName = chanName;
        if (chanName == null) {
            ch_ = null;
            return;
        }
        callBack = new IEventSinkValue() {

            public void eventValue(ChannelRecord record, Channel chan) {
                currValueMonitor = record.doubleValue();
            }
        };
        ch_ = ChannelFactory.defaultFactory().getChannel(chanName);
        if (ch_ == null) {
            stopScanWithReport(null, getChannelName(), "JCA can not create channel.");
            return;
        }
        try {
            ch_.addMonitorValue(callBack, Monitor.VALUE);
        } catch (ConnectionException e) {
            stopScanWithReport(e, chanName, "JCA can not create channel.");
        } catch (MonitorException e) {
            stopScanWithReport(e, chanName, "JCA can not monitor channel.");
        }
    }
