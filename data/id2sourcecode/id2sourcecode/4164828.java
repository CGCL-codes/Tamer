    public void testMarkReadItem() {
        clickLink("getChannelsWithNews");
        StringBuffer link = new StringBuffer();
        link.append("readItems_").append(channelId);
        assertLinkPresent(link.toString());
        clickLink(link.toString());
        assertLinkPresent("markItemAsRead_" + firstItemId);
        assertLinkPresent("markItemAsRead_" + secondItemId);
        assertLinkNotPresent("markItemAsNotRead_" + firstItemId);
        assertLinkNotPresent("markItemAsNotRead_" + secondItemId);
        clickLink("markItemAsRead_" + firstItemId);
        assertLinkNotPresent("markItemAsRead_" + firstItemId);
        assertLinkPresent("markItemAsRead_" + secondItemId);
        assertLinkPresent("markItemAsNotRead_" + firstItemId);
        assertLinkNotPresent("markItemAsNotRead_" + secondItemId);
        clickLink("markItemAsNotRead_" + firstItemId);
        assertLinkPresent("markItemAsRead_" + firstItemId);
        assertLinkPresent("markItemAsRead_" + secondItemId);
        assertLinkNotPresent("markItemAsNotRead_" + firstItemId);
        assertLinkNotPresent("markItemAsNotRead_" + secondItemId);
    }
