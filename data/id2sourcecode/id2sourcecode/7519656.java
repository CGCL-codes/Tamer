    public Query rewrite(IndexReader reader) throws IOException {
        subQuery = subQuery.rewrite(reader);
        for (int i = 0; i < valSrcQueries.length; i++) {
            valSrcQueries[i] = (ValueSourceQuery) valSrcQueries[i].rewrite(reader);
        }
        return this;
    }
