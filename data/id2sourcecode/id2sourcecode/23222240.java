    public void deleteItem(Item item) {
        if (item != null) {
            Channel channel = (Channel) this.getHibernateTemplate().load(Channel.class, item.getChannelID());
            channel.getItems().remove(item);
            this.getHibernateTemplate().save(channel);
        }
    }
