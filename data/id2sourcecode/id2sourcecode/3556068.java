    public boolean isInList(Collection<? extends NodeChannel> channels) {
        if (channels != null) {
            for (NodeChannel channel : channels) {
                if (channel.getChannelId().equals(channelId)) {
                    return true;
                }
            }
        }
        return false;
    }
