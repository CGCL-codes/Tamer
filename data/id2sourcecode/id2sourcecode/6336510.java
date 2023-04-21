    public StripChannel getChannel(String channelName) {
        Iterator<StripChannel> it = channelList.iterator();
        while (it.hasNext()) {
            StripChannel channel = it.next();
            if (channel.getChannelID().equals(channelName) || channel.getLabel().equals(channelName)) return channel;
        }
        return null;
    }
