    @Override
    public Query rewrite(IndexReader reader) throws IOException {
        FieldMaskingSpanQuery clone = null;
        SpanQuery rewritten = (SpanQuery) maskedQuery.rewrite(reader);
        if (rewritten != maskedQuery) {
            clone = (FieldMaskingSpanQuery) this.clone();
            clone.maskedQuery = rewritten;
        }
        if (clone != null) {
            return clone;
        } else {
            return this;
        }
    }
