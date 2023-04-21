    @Override
    public List<SearchResult> search(String query, SortOrder order, int maxResults) throws Exception {
        RssParser parser = getRssParser(getUrl(query, order));
        parser.parse();
        List<Item> items = parser.getChannel().getItems();
        List<SearchResult> results = new ArrayList<SearchResult>();
        int i = 0;
        if (items != null) {
            for (Item item : items) {
                if (i >= maxResults) {
                    break;
                }
                results.add(fromRssItemToSearchResult(item));
                i++;
            }
        }
        return results;
    }
