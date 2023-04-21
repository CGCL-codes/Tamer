    private Tuple open(final IndexPolicy policy) throws IOException {
        final IndexWriter writer = createWriter(policy);
        final IndexReader reader = IndexReader.open(dir, false);
        final IndexSearcher searcher = new IndexSearcher(reader);
        return new Tuple(writer, reader, searcher);
    }
