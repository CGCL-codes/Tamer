    boolean getChannel(String url, ChannelInfo info, boolean relay) {
        ChannelManager channelManager = Singleton.getInstance(ChannelManager.class);
        Debug.println("url: " + url);
        procConnectArgs(url, info);
        Channel channel = channelManager.findChannelByNameID(info);
        if (channel != null) {
            if (!channel.isPlaying()) {
                if (relay) {
                    channel.info.lastPlayStart = 0;
                    channel.info.lastPlayEnd = 0;
                } else {
                    return false;
                }
            }
            info = channel.info;
            return true;
        } else {
            if (relay) {
                channel = channelManager.findAndRelay(info);
                if (channel != null) {
                    info = channel.info;
                    return true;
                }
            }
        }
        return false;
    }
