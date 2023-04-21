    private static void deleteItem(ItemIF item, Session session) throws PersistenceManagerException {
        final ChannelIF channel = item.getChannel();
        if (channel != null) {
            HibernateUtil.lock(channel, session);
            channel.removeItem(item);
        } else {
            LOG.severe("Item didn't belong to any channel: " + item);
        }
        HibernateUtil.deleteObject(item, session);
    }
