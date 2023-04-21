    public Channel getChannel(String channelId) {
        if (channelId == null) return null;
        TimeChannel timeChannel = channels.get(channelId);
        if (timeChannel != null) return timeChannel.getChannel();
        return null;
    }
