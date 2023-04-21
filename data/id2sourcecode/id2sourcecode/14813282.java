    public void setChannelNameRB(String chanNameRB) {
        this.chanNameRB = chanNameRB;
        if (chanNameRB == null) {
            ch_RB_ = null;
            return;
        }
        callBackRB = new IEventSinkValue() {

            public void eventValue(ChannelRecord record, Channel chan) {
                currValueMonitor_RB = record.doubleValue();
            }
        };
        ch_RB_ = ChannelFactory.defaultFactory().getChannel(chanNameRB);
        if (ch_RB_ == null) {
            stopScanWithReport(null, getChannelNameRB(), "JCA can not create channel.");
            return;
        }
        try {
            ch_RB_.addMonitorValue(callBackRB, Monitor.VALUE);
        } catch (ConnectionException e) {
            stopScanWithReport(e, chanNameRB, "JCA can not create channel.");
        } catch (MonitorException e) {
            stopScanWithReport(e, chanNameRB, "JCA can not monitor channel.");
        }
    }
