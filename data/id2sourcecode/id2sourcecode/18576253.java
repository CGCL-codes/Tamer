    private Channel getChannelByName(String name) {
        for (int i = 0; i < channels.size(); i++) {
            Channel c = channels.getChannel(i);
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }
