    public Query rewrite(IndexReader reader) throws IOException {
        SpanNotQuery clone = null;
        SpanQuery rewrittenInclude = (SpanQuery) include.rewrite(reader);
        if (rewrittenInclude != include) {
            clone = (SpanNotQuery) this.clone();
            clone.include = rewrittenInclude;
        }
        SpanQuery rewrittenExclude = (SpanQuery) exclude.rewrite(reader);
        if (rewrittenExclude != exclude) {
            if (clone == null) clone = (SpanNotQuery) this.clone();
            clone.exclude = rewrittenExclude;
        }
        if (clone != null) {
            return clone;
        } else {
            return this;
        }
    }
