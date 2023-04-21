    public void deleteItem(Long itemId) {
        Item item = this.getItem(itemId);
        Channel channel = this.getChannel(item.getChannelID());
        channel.removeItem(item);
        this.update(channel);
        if (item.isFetched()) {
            this.itemDAO.deleteItem(item);
        } else {
            this.update(channel, item);
        }
    }
