    @Override
    public void joinedChannel(Connection source, String channel) {
        append(source, "Joining channel " + channel + ".", cs.getChannelColor());
    }
