    Channel getChannelForKind(int chanid, String kind, List params) {
        ChannelFactory factory = (ChannelFactory) mChannelFactoryMap.get(kind);
        if (factory == null) {
            mLog.notice("Cannot find a ChannelFactory for the channel kind '" + kind + "'; using default Channel");
            factory = new Channel.Factory();
        }
        return factory.createChannel(kind, chanid, params);
    }
