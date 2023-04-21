    public ChannelFactory getChannelFactory() throws ChannelException {
        if (channelFactory == null) {
            if (getChannelUrlProps() != null) {
                channelFactory = new JChannelFactory(getChannelUrlProps());
            } else if (getChannelStringProps() != null) {
                channelFactory = new JChannelFactory(getChannelStringProps());
            } else {
                channelFactory = new JChannelFactory(new JChannel().getProperties());
            }
        }
        return channelFactory;
    }
