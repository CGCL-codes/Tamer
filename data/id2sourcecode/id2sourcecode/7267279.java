    private static void extractTermsFromRegex(RegexQuery query, Map<Object, Query> terms, IndexReader reader, boolean includeFields) throws IOException {
        extractTerms(rewrite(query, reader), terms, reader, includeFields);
    }
