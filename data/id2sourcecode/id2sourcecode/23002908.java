    private Channel getSubChannelByName(String name) {
        for (int i = 0; i < subChannels.size(); i++) {
            Channel c = subChannels.getChannel(i);
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }
