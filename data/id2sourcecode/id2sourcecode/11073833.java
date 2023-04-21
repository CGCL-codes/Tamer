    public SimpleChannelSource(final String pv) {
        this(ChannelFactory.defaultFactory().getChannel(pv));
    }
