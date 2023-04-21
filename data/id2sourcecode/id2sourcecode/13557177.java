    private Hits searchIndex(String indexFieldSearch, String indexDir, String queryString) throws Exception {
        Analyzer analyzer = new StandardAnalyzer();
        IndexSearcher isearcher;
        Hits hits = null;
        isearcher = new IndexSearcher(indexDir);
        QueryParser parser = new QueryParser(indexFieldSearch, analyzer);
        IndexReader reader = IndexReader.open(indexDir);
        Query query = parser.parse(queryString);
        query = query.rewrite(reader);
        hits = isearcher.search(query);
        return hits;
    }
