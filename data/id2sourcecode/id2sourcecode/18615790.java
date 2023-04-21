    public boolean isUniqueUrl(IGeneralItem channel) {
        List channels = this.getChannelsByUrl(channel.getUrl());
        if (channels.size() == 0) {
            return true;
        }
        if (channels.size() == 1) {
            if (channel.getId() == null) {
                return false;
            }
            if (channel.getId().equals(((Channel) channels.get(0)).getId())) {
                return true;
            }
        }
        return false;
    }
