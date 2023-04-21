    public ChannelMonitor(final String pv) {
        MESSAGE_CENTER = new MessageCenter("Channel Monitor");
        EVENT_PROXY = MESSAGE_CENTER.registerSource(this, ChannelEventListener.class);
        EVENT_LOCK = new Object();
        CHANNEL = ChannelFactory.defaultFactory().getChannel(pv);
        _lastRecord = null;
        _connectionListener = null;
        clearTripCount();
    }
