    public void testDefaultOpenSearchRssWithReader() throws Exception {
        DefaultOpenSearchRss osrss10 = new DefaultOpenSearchRss(getContents("sample-osrss10.xml").getBytes());
        OpenSearchRssChannel channel = osrss10.getChannel();
        assertNull(channel.getCopyright());
        assertEquals("Search results for License at Archtea", channel.getDescription());
        assertEquals(3, channel.getItemsPerPage());
        assertNull(channel.getLanguage());
        assertEquals("http://localhost:12345/archtea/search?q=License", channel.getLink().toExternalForm());
        assertEquals(1, channel.getStartIndex());
        assertEquals("Archtea - License", channel.getTitle());
        assertEquals(123, channel.getTotalResult());
        List items = channel.items();
        assertEquals(3, items.size());
        for (int i = 0; i < items.size(); i++) {
            OpenSearchRssItem item = (OpenSearchRssItem) items.get(i);
            assertEquals("desc" + (i + 1), item.getDescription());
            assertEquals("http://localhost:12345/link" + (i + 1), item.getLink().toExternalForm());
            assertEquals("title" + (i + 1), item.getTitle());
        }
        osrss10 = new DefaultOpenSearchRss(getContents("sample-osrss10-2.xml").getBytes());
        channel = osrss10.getChannel();
        assertEquals("unknown", channel.getCopyright());
        assertEquals("Search results for License at Archtea", channel.getDescription());
        assertEquals(3, channel.getItemsPerPage());
        assertEquals(new Locale("en", "US"), channel.getLanguage());
        assertEquals("http://localhost:12345/archtea/search?q=License", channel.getLink().toExternalForm());
        assertEquals(1, channel.getStartIndex());
        assertEquals("Archtea - License", channel.getTitle());
        assertEquals(123, channel.getTotalResult());
        items = channel.items();
        assertEquals(3, items.size());
        for (int i = 0; i < items.size(); i++) {
            OpenSearchRssItem item = (OpenSearchRssItem) items.get(i);
            assertEquals("desc" + (i + 1), item.getDescription());
            assertEquals("http://localhost:12345/link" + (i + 1), item.getLink().toExternalForm());
            assertEquals("title" + (i + 1), item.getTitle());
        }
    }
