    public void removeChannel(Long id) {
        Channel channel = this.getChannel(id);
        channel.remove();
        this.remove(channel);
        this.itemDAO.deleteItemsFromChannel(channel);
    }
