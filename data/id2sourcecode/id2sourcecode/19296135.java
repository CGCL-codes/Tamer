    public Channel getChannel(String name) {
        Channel channel = cmd.getChannel(name);
        if (channel == null) {
            channel = new Channel(name);
            cmd.setChannel(channel);
        }
        return channel;
    }
