    public void testDeleteItems() {
        clickLink("getChannelsWithNews");
        clickLink("readItems_" + channelId);
        clickLink("deleteAllItems_" + channelId);
        IChannelDAO channelDao = (IChannelDAO) context.getBean("channelDAO");
        Channel channel = channelDao.getChannel(new Long(channelId));
        assertTrue(channel.getNumberOfItems() == 0);
        assertTrue(channel.getNumberOfRead() == 0);
        IItemDAO itemDao = (IItemDAO) context.getBean("itemDAO");
        Item firstItem = itemDao.getItem(new Long(firstItemId));
        assertTrue(firstItem.getDescription() == null);
        assertTrue(firstItem.isRemove());
        Item secondItem = itemDao.getItem(new Long(secondItemId));
        assertTrue(secondItem.getDescription() == null);
        assertTrue(secondItem.isRemove());
    }
