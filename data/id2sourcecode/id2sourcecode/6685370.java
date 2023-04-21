    public synchronized Channel newChannel() {
        Channel channel = engine.openChannel();
        getResponse().addCookie(new HttpCookie("channelId", channel.getId()));
        getServer().getChannels().put(channel.getId(), channel);
        this.channel = channel;
        return channel;
    }
