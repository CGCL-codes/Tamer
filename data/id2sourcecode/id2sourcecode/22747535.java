    public static Channel getInstance(final String signalName) {
        return ChannelFactory.defaultFactory().getChannel(signalName);
    }
