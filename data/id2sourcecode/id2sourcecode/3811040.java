    public void setChannelName(String chanName) {
        if (chanName != null && badChannelNames.containsKey(chanName) == false) {
            ch_ = ChannelFactory.defaultFactory().getChannel(chanName);
            callBack = new IEventSinkValue() {

                public void eventValue(ChannelRecord record, Channel chan) {
                    currValueMonitor = record.doubleValue();
                }
            };
            try {
                ch_.addMonitorValue(callBack, Monitor.VALUE);
            } catch (MonitorException e) {
                badChannelNames.put(chanName, null);
                ch_ = null;
            } catch (ConnectionException e) {
                badChannelNames.put(chanName, null);
                ch_ = null;
            }
        } else {
            ch_ = null;
        }
    }
