    public boolean setChannel(String channelName) {
        Channel channel = rbnbController.getChannel(channelName);
        if (channel == null) {
            return false;
        }
        if (channels.size() == 1 && channels.contains(channelName)) {
            return false;
        }
        removeAllChannels();
        return addChannel(channelName);
    }
