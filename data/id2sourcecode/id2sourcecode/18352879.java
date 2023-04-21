    public synchronized Channel getChannel(String channelName) {
        Channel curChannel = channelMap.get(channelName);
        if (curChannel == null) {
            curChannel = new Channel(channelName);
            channelMap.put(channelName, curChannel);
        }
        return curChannel;
    }
