    private static void extractTermsFromPrefix(PrefixQuery query, Map<Object, Query> terms, IndexReader reader, boolean includeFields) throws IOException {
        extractTerms(rewrite(query, reader), terms, reader, includeFields);
    }
