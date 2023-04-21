    public Channel getChannel(Channel channel) {
        if (channels.get(channel.address) != null) return new Channel(channel.address, value); else return new Channel(channel.address, (short) 0);
    }
