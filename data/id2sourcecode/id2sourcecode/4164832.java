    public void testDeleteToggledItems() {
        clickLink("getChannelsWithNews");
        clickLink("readItems_" + channelId);
        clickLink("markItemAsRead_" + firstItemId);
        clickLink("toggleAllItems_" + channelId);
        clickLink("deleteAllItems_" + channelId);
        clickLink("getChannelsWithNoNews");
        clickLink("readItems_" + channelId);
        clickLink("toggleAllItems_" + channelId);
        assertLinkPresent("markItemAsNotRead_" + firstItemId);
    }
