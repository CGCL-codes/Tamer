    public Channel[] getChannels(String parentPath) throws Exception {
        List channelsList = getChannelsList(parentPath);
        Channel[] channels = new Channel[channelsList.size()];
        for (int i = 0; i < channels.length; i++) {
            channels[i] = (Channel) channelsList.get(i);
        }
        return channels;
    }
