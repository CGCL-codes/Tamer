    public String getChannelName(String channel) {
        for (int i = 0; i < mChannels.size(); i++) {
            if (((IrcChannel) mChannels.elementAt(i)).getName().equals(channel)) return ((IrcChannel) mChannels.elementAt(i)).getDescription();
        }
        return null;
    }
