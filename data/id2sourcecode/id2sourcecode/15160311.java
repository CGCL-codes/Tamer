    private SearchResult getSearchResult(OpenSearchDescription desc, byte[] responseBodyBytes, long searchTimeMills) throws OpenSearchException {
        String searchName = getConfig().getName();
        if (desc instanceof OpenSearchDescription10) {
            searchName = ((OpenSearchDescription10) desc).getShortName();
        }
        DefaultOpenSearchRss osRss = new DefaultOpenSearchRss(responseBodyBytes);
        OpenSearchRssChannel channel = osRss.getChannel();
        List items = channel.items();
        DefaultSearchResult searchResult = new DefaultSearchResult(searchName, searchTimeMills, channel.getTotalResult(), channel.getItemsPerPage());
        for (int i = 0; i < items.size(); i++) {
            searchResult.addFoundItem(new OpenSearchFoundItem(channel.getStartIndex() + i, (OpenSearchRssItem) items.get(i)));
        }
        return searchResult;
    }
