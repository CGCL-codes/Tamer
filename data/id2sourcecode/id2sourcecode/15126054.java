    public ChannelParserImpl() {
        try {
            this.channels = Config.get().getChannelsConf();
        } catch (IOException e) {
            throw new IllegalArgumentException("channels.conf does not exist! " + channels, e);
        }
    }
