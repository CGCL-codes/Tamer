    public Query rewrite(IndexReader reader) throws IOException {
        SpanFirstQuery clone = null;
        SpanQuery rewritten = (SpanQuery) match.rewrite(reader);
        if (rewritten != match) {
            clone = (SpanFirstQuery) this.clone();
            clone.match = rewritten;
        }
        if (clone != null) {
            return clone;
        } else {
            return this;
        }
    }
