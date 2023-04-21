    private static void extractTermsFromFuzzy(FuzzyQuery query, Map<Object, Query> terms, IndexReader reader, boolean includeFields) throws IOException {
        extractTerms(query.rewrite(reader), terms, reader, includeFields);
    }
