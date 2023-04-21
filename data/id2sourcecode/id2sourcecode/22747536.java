    public static Channel getInstance(final String signalName, final ValueTransform transform) {
        return ChannelFactory.defaultFactory().getChannel(signalName, transform);
    }
