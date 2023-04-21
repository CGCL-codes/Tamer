    private Channel getChannel(String channel, boolean create) {
        channel = channel.toLowerCase();
        if (channels.containsKey(channel)) {
            return (Channel) channels.get(channel);
        } else if (create) {
            channels.put(channel, new Channel(this, channel));
            return (Channel) channels.get(channel);
        } else {
            return null;
        }
    }
