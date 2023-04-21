    public void testMarkChannelRead() {
        clickLink("getChannelsWithNoNews");
        assertLinkNotPresent("readItems_" + channelId);
        clickLink("getChannelsWithNews");
        clickLink("readItems_" + channelId);
        assertLinkPresent("markAsRead_" + channelId);
        clickLink("markAsRead_" + channelId);
        clickLink("getChannelsWithNews");
        assertLinkNotPresent("readItems_" + channelId);
        clickLink("getChannelsWithNoNews");
        assertLinkPresent("readItems_" + channelId);
        clickLink("readItems_" + channelId);
        assertLinkPresent("markItemAsNotRead_" + firstItemId);
        assertLinkPresent("markItemAsNotRead_" + secondItemId);
        clickLink("markItemAsNotRead_" + firstItemId);
        clickLink("getChannelsWithNoNews");
        assertLinkNotPresent("readItems_" + channelId);
        clickLink("getChannelsWithNews");
        clickLink("readItems_" + channelId);
        assertLinkPresent("markItemAsRead_" + firstItemId);
    }
