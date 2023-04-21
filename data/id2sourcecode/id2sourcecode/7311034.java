    protected Channel createChannel() {
        Channel channel = gravityConfig.getChannelFactory().newChannel(UUIDUtil.randomUUID());
        TimeChannel timeChannel = new TimeChannel(channel);
        for (int i = 0; channels.putIfAbsent(channel.getId(), timeChannel) != null; i++) {
            if (i >= 10) throw new RuntimeException("Could not find random new clientId after 10 iterations");
            channel.destroy();
            channel = gravityConfig.getChannelFactory().newChannel(UUIDUtil.randomUUID());
            timeChannel = new TimeChannel(channel);
        }
        access(channel.getId());
        return channel;
    }
