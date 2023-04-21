    private static void extractTermsFromFuzzy(FuzzyQuery query, Map<String, Query> terms, IndexReader reader) throws IOException {
        extractTerms(query.rewrite(reader), terms, reader);
    }
