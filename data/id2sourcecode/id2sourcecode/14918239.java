    private static Query rewrite(MultiTermQuery query, IndexReader reader) throws IOException {
        query.setRewriteMethod(PrefixQuery.SCORING_BOOLEAN_QUERY_REWRITE);
        return query.rewrite(reader);
    }
