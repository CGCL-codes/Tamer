    public Channel[] getChannels(Channel[] channels) {
        Channel[] newChannels = new Channel[channels.length];
        for (int i = 0; i < channels.length; i++) newChannels[i] = new Channel(channels[i].address, getChannelValue(channels[i]));
        return newChannels;
    }
