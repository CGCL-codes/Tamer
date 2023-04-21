    protected ChannelStats getChannelStats(String channelId) {
        resetChannelStats(false);
        ChannelStats stats = channelStats.get(channelId);
        if (stats == null) {
            Node node = nodeService.getCachedIdentity();
            if (node != null) {
                stats = new ChannelStats(node.getNodeId(), AppUtils.getServerId(), new Date(), null, channelId);
                channelStats.put(channelId, stats);
            } else {
                stats = new ChannelStats(UNKNOWN, AppUtils.getServerId(), new Date(), null, channelId);
            }
        }
        return stats;
    }
