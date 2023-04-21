    public ChannelWrapper(final String name) {
        this(ChannelFactory.defaultFactory().getChannel(name));
    }
