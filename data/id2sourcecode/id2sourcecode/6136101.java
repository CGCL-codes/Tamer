    public static ChannelService getChannelService() {
        if (impl == null) {
            impl = getChannelService("com.langerra.server.channel.impl.AppEngineChannelServiceImpl");
        }
        return impl;
    }
