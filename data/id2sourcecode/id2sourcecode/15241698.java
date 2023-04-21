    public Query rewrite(IndexReader reader) throws IOException {
        Query newQ = q.rewrite(reader);
        if (newQ == q) return this;
        BoostedQuery bq = (BoostedQuery) this.clone();
        bq.q = newQ;
        return bq;
    }
