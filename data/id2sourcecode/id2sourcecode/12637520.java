    private Object getChannelNode(ChannelIF channel) {
        Object channelsList = main.find("channels");
        Object[] channels = main.getItems(channelsList);
        for (int i = 0; i < channels.length; i++) {
            long id = ((Long) main.getProperty(channels[i], "id")).longValue();
            if (id == channel.getId()) return channels[i];
        }
        return null;
    }
