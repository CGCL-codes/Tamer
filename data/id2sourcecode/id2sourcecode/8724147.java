    public Query rewrite(IndexReader reader) throws IOException {
        Query rewritten = query.rewrite(reader);
        if (rewritten != query) {
            FilteredQuery clone = (FilteredQuery) this.clone();
            clone.query = rewritten;
            return clone;
        } else {
            return this;
        }
    }
