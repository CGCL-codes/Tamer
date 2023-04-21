    @Override
    public Query rewrite(IndexReader reader) throws java.io.IOException {
        Query q = query.rewrite(reader);
        q.extractTerms(new TermCheckerSet(field));
        return q;
    }
