    @Override
    public Query rewrite(IndexReader reader) throws java.io.IOException {
        ConstantScoreQuery q = new ConstantScoreQuery(new TrieRangeFilter());
        q.setBoost(getBoost());
        return q.rewrite(reader);
    }
