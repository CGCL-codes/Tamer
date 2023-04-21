    public ChannelWrapper(String pv) {
        this(ChannelFactory.defaultFactory().getChannel(pv));
    }
