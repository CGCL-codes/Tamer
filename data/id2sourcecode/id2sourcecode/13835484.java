    public static List<ChannelNotification> getChannels(final Element channels) throws ClassNotFoundException {
        return getChannels(channels, Thread.currentThread().getContextClassLoader());
    }
