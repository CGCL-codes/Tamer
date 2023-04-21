    public Query rewrite(Query original) throws IOException {
        Query query = original;
        for (Query rewrittenQuery = query.rewrite(reader); rewrittenQuery != query; rewrittenQuery = query.rewrite(reader)) {
            query = rewrittenQuery;
        }
        return query;
    }
