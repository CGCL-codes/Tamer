    public InkChannel getChannel(ChannelName x) {
        for (InkChannel c : getChannels()) {
            if (c.getName() == x) {
                return c;
            }
        }
        return null;
    }
