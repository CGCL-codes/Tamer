    private SearchResult getRss20SearchResult(OpenSearchDescription desc, byte[] responseBodyBytes, long searchTimeMills) throws ArchteaSearchException {
        String searchName = getConfig().getName();
        if (desc instanceof OpenSearchDescription11) {
            searchName = ((OpenSearchDescription10) desc).getShortName();
        }
        Rss20Factory factory = new Rss20Factory();
        InputStream in = null;
        try {
            in = new ByteArrayInputStream(responseBodyBytes);
            OpenSearch11CommonModule opensearch = new OpenSearch11CommonModule();
            Rss20 rss20 = factory.newInstance(in, new Rss20Module[] { opensearch });
            Rss20Channel channel = rss20.getChannel();
            Rss20Item[] items = channel.getItems();
            if (channel.getTitle() != null) searchName = channel.getTitle();
            int hitCount = -1 < opensearch.getTotalResults() ? opensearch.getTotalResults() : items.length;
            int itemsPerPage = 0 < opensearch.getItemsPerPage() ? opensearch.getItemsPerPage() : items.length;
            if (itemsPerPage < OpenSearch11CommonModule.DEFAULT_ITEMS_PER_PAGE) itemsPerPage = OpenSearch11CommonModule.DEFAULT_ITEMS_PER_PAGE;
            int startIndex = 0 < opensearch.getStartIndex() ? opensearch.getStartIndex() : OpenSearch11CommonModule.DEFAULT_START_INDEX;
            DefaultSearchResult searchResult = new DefaultSearchResult(searchName, searchTimeMills, hitCount, itemsPerPage);
            for (int i = 0; i < items.length; i++) {
                searchResult.addFoundItem(new Rss20FoundItem(startIndex + i, items[i]));
            }
            return searchResult;
        } catch (Rss20Exception e) {
            log.error("Error happened while parsing response message.", e);
            throw new ArchteaSearchException(e);
        }
    }
