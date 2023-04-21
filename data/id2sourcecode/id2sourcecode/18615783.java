    public void deleteItems(List itemsToDelete) {
        Channel channel = null;
        for (int i = 0; i < itemsToDelete.size(); i++) {
            Item item = (Item) itemsToDelete.get(i);
            if (channel == null) {
                channel = this.getChannel(item.getChannelID());
            }
            channel.removeItem(item);
            this.update(channel, item);
        }
        if (channel != null) {
            this.update(channel);
        }
    }
