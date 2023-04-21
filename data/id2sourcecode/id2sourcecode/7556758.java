    private boolean isChannelActive(String channel) {
        Channel chan = (Channel) getChannels().get(channel);
        if (chan != null) return true; else return false;
    }
