    public void testDefaultOpenSearchRssReaderNoValueInOSSTag() throws Exception {
        DefaultOpenSearchRss osrss10 = new DefaultOpenSearchRss(getContents("sample-osrss10-3.xml").getBytes());
        assertEquals("Check default items per page", 10, osrss10.getChannel().getItemsPerPage());
        assertEquals("Check default total count.", 3, osrss10.getChannel().getTotalResult());
        assertEquals("Check start index if the value is not set.", 1, osrss10.getChannel().getStartIndex());
    }
