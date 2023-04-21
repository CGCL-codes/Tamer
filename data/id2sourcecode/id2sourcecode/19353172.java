    public IrcChannel getChannelByName(String channelName) {
        if (channelName == null) return null;
        for (IrcChannel ircChannel : getChannels()) {
            if (ircChannel == null) continue;
            if (channelName.equalsIgnoreCase(ircChannel.getName())) return ircChannel;
        }
        return null;
    }
