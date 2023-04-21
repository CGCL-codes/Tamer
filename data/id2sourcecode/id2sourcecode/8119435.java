    public static String getChannelDesc(String channel) {
        Channels channels = getChannels();
        if (channels != null) {
            if (m_channelMap == null) m_channelMap = channels.getItems();
            Map data = (Map) m_channelMap.get(channel);
            if (data != null) return (String) data.get(ChannelData.DISPLAYNAME);
        }
        return channel;
    }
