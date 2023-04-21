    public Query rewrite(IndexReader reader) throws IOException {
        SpanOrQuery clone = null;
        for (int i = 0; i < clauses.size(); i++) {
            SpanQuery c = (SpanQuery) clauses.get(i);
            SpanQuery query = (SpanQuery) c.rewrite(reader);
            if (query != c) {
                if (clone == null) clone = (SpanOrQuery) this.clone();
                clone.clauses.set(i, query);
            }
        }
        if (clone != null) {
            return clone;
        } else {
            return this;
        }
    }
