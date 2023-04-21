    public MonitorController(final String pv, final int monitorMask) {
        this(ChannelFactory.defaultFactory().getChannel(pv), monitorMask);
    }
