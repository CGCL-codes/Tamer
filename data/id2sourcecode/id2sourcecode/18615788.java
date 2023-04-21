    public void markItemRead(Item item, boolean read) {
        Channel channel = this.getChannel(item.getChannelID());
        channel.markItemRead(item, read);
        this.update(channel, item);
        this.update(channel);
    }
