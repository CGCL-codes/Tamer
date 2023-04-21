    private static void extractTermsFromRegex(RegexQuery query, Map<String, Query> terms, IndexReader reader) throws IOException {
        extractTerms(rewrite(query, reader), terms, reader);
    }
