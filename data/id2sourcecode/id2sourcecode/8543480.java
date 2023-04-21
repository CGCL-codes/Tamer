    public boolean isChannelNameInUse(String channelName) {
        for (int i = 0; i < size(); i++) {
            Channel c = getChannel(i);
            if (c.getName().equals(channelName)) {
                return true;
            }
        }
        return false;
    }
