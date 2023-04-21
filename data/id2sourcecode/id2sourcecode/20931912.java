    private Channel getChannel(long channelId) throws IOException {
        Long l = new Long(channelId);
        if (!activeChannels.containsKey(l)) {
            throw new IOException("Non existent channel " + l.toString() + " requested");
        }
        return (Channel) activeChannels.get(l);
    }
