    public IChannel[] getConnectedChannels() {
        final Collection<IChannel> values = getState().getChannels().values();
        return values.toArray(new IChannel[values.size()]);
    }
