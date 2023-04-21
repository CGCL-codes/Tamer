    public void setActiveChannels(Set<NodeChannel> activeChannels) {
        this.activeChannels = activeChannels;
        activeChannelIds = new HashSet<String>();
        for (NodeChannel nodeChannel : activeChannels) {
            activeChannelIds.add(nodeChannel.getChannelId());
        }
    }
