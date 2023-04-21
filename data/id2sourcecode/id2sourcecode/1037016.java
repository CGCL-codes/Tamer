    public Query rewrite(IndexReader reader) throws IOException {
        Query q = new ConstantScoreQuery(new PrefixFilter(prefix));
        q.setBoost(getBoost());
        return q;
    }
