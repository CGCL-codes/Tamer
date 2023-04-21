    private SearchResult getSearchResult(byte[] responseBodyBytes, long searchTimeMills) throws Rss20Exception {
        String searchName = getConfig().getName();
        Rss20Factory factory = new Rss20Factory();
        Rss20 rss20 = factory.newInstance(new ByteArrayInputStream(responseBodyBytes));
        Rss20Channel channel = rss20.getChannel();
        Rss20Item[] items = channel.getItems();
        if (channel.getTitle() != null) searchName = channel.getTitle();
        DefaultSearchResult searchResult = new DefaultSearchResult(searchName, searchTimeMills, items.length, DEFAULT_ITEMS_PER_PAGE);
        for (int i = 0; i < items.length; i++) {
            searchResult.addFoundItem(new Rss20FoundItem(DEFAULT_INDEX_OFFSET + i, items[i]));
        }
        return searchResult;
    }
