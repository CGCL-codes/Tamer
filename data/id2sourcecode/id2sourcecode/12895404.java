    public Channel[] getChannels(Channel[] channels, int source) {
        IValueSet set = channelSets.getSet(source);
        Channel[] results = new Channel[channels.length];
        for (int i = 0; i < channels.length; i++) results[i] = set.getChannel(channels[i]);
        return results;
    }
