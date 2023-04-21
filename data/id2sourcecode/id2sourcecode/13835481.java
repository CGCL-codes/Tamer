    public static List<ChannelNotification> getChannels(final String resourceName) throws ClassNotFoundException {
        return getChannels(resourceName, Thread.currentThread().getContextClassLoader());
    }
