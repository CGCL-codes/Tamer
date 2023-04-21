    public void connectNoHangup() {
        this.reqChannels = sinkWrapper.getChannelNames();
        this.reqDatatypes = sinkWrapper.getChannelDataTypesVec();
        sink = new Sink();
        setClient(sink);
        this.connected = connect();
        reqPathIndicies = addChannels(reqChannels);
        switch(sinkWrapper.getRequestMode()) {
            case TurbineSinkConfig.MONITOR_MODE:
                handler = new TurbineSinkMonitorHandler(this, callbackHandler, sinkWrapper.getTimeout());
                break;
            case TurbineSinkConfig.POLL_MODE:
                this.reqPollIntervals = sinkWrapper.getPollIntervals();
                handler = new TurbineSinkPollHandler(this, callbackHandler);
                break;
            default:
                throw new IllegalStateException("Only monitor mode is currently " + "supported. More to come soon");
        }
        Debugger.debug(Debugger.TRACE, "RBNB sink=" + sink);
    }
