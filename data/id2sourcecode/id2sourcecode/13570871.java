    public ChannelBuilderIF getChannelBuilder() {
        if (channelBuilder == null) {
            return DEFAULT_BUILDER;
        } else {
            return channelBuilder;
        }
    }
