    public Channel getChannel(String name) {
        for (Channel c : getChannels()) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }
