    public void testShowAllItemsNewItems() {
        clickLink("getChannelsWithNews");
        clickLink("readItems_" + channelId);
        assertLinkPresent("markItemAsRead_" + firstItemId);
        assertLinkPresent("markItemAsRead_" + secondItemId);
        assertLinkNotPresent("markItemAsNotRead_" + firstItemId);
        assertLinkNotPresent("markItemAsNotRead_" + secondItemId);
        clickLink("markItemAsRead_" + firstItemId);
        assertLinkNotPresent("markItemAsRead_" + firstItemId);
        assertLinkPresent("markItemAsRead_" + secondItemId);
        assertLinkPresent("toggleAllItems_" + channelId);
        assertTextPresent("Show new items");
        clickLink("toggleAllItems_" + channelId);
        assertLinkNotPresent("markItemAsRead_" + firstItemId);
        assertLinkNotPresent("markItemAsNotRead_" + firstItemId);
        assertTextNotPresent("Title item 1");
        assertTextPresent("Title item 2");
        assertLinkPresent("markItemAsRead_" + secondItemId);
        assertTextPresent("Show all items");
        assertLinkPresent("toggleAllItems_" + channelId);
        assertTextPresent("Show all items");
        clickLink("toggleAllItems_" + channelId);
        assertTextPresent("Title item 1");
        assertLinkPresent("markItemAsNotRead_" + firstItemId);
        assertTextPresent("Title item 2");
        assertLinkPresent("markItemAsRead_" + secondItemId);
    }
