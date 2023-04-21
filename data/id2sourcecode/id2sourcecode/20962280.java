    protected void resetChannelStats(boolean force) {
        if (force) {
            channelStats = null;
        }
        if (channelStats == null) {
            List<NodeChannel> channels = configurationService.getNodeChannels(false);
            channelStats = new HashMap<String, ChannelStats>(channels.size());
            for (NodeChannel nodeChannel : channels) {
                getChannelStats(nodeChannel.getChannelId());
            }
        }
    }
