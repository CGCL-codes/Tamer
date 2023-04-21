    @Override
    public ChannelWorldInterface getChannelInterface(int channel) {
        ChannelWorldInterface cwi = WorldRegistryImpl.getInstance().getChannel(channel);
        return cwi;
    }
