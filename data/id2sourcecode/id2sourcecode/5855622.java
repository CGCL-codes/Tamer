    @Override
    public Query rewrite(final IndexReader reader) throws java.io.IOException {
        final ConstantScoreQuery q = new ConstantScoreQuery(new TrieRangeFilter());
        q.setBoost(getBoost());
        return q.rewrite(reader);
    }
