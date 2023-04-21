    public void markAsRead(Long id) {
        this.channelDAO.markAsRead(id);
        this.itemDAO.markItemsAsRead(id);
        this.channelDAO.update(getChannel(id));
    }
