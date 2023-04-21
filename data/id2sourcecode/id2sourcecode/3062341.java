    public void monitor(Call call) {
        if (logger.isDebugEnabled()) logger.debug("monitor " + call);
        String monitoredChannel = null;
        if (call instanceof SinglePartyCall) {
            SinglePartyCall spc = (SinglePartyCall) call;
            monitoredChannel = spc.getChannel().getDescriptor().getId();
            if (logger.isDebugEnabled()) logger.debug("spc monitoredChannel " + monitoredChannel);
        } else if (call instanceof TwoPartiesCall) {
            TwoPartiesCall tpc = (TwoPartiesCall) call;
            monitoredChannel = tpc.getCallerChannel().getDescriptor().getId();
            if (logger.isDebugEnabled()) logger.debug("tpc monitoredChannel " + monitoredChannel);
        } else if (call instanceof ConferenceCall) {
            ConferenceCall cc = (ConferenceCall) call;
            for (Channel channel : cc.getChannels()) {
                String channelId = channel.getDescriptor().getId();
                if (logger.isDebugEnabled()) logger.debug("cc channel " + channelId);
                monitoredChannel = channelId;
            }
        }
        if (logger.isDebugEnabled()) logger.debug("monitoredChannel:" + monitoredChannel);
        StringBuffer fileName = new StringBuffer();
        if (call instanceof SinglePartyCall) {
            SinglePartyCall spc = (SinglePartyCall) call;
            fileName.append("singleparty_").append(spc.getId());
        } else if (call instanceof TwoPartiesCall) {
            TwoPartiesCall tpc = (TwoPartiesCall) call;
            fileName.append("twoparties_").append(tpc.getId());
        } else if (call instanceof ConferenceCall) {
            ConferenceCall cc = (ConferenceCall) call;
            fileName.append("conference_").append(cc.getId());
        }
        MonitorAction monitorAction = new MonitorAction(monitoredChannel, "/tmp/" + fileName.toString());
        try {
            managerConnection.sendAction(monitorAction);
            if (logger.isDebugEnabled()) logger.debug("send " + monitorAction);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
