    public Query makeLuceneQueryFieldNoBoost(final String fieldName, final BasicQueryFactory qf) {
        return new Query() {

            public String toString(String fn) {
                return getClass().toString() + " " + fieldName + " (" + fn + "?)";
            }

            public Query rewrite(IndexReader reader) throws IOException {
                final ArrayList luceneSubQueries = new ArrayList();
                visitMatchingTerms(reader, fieldName, new MatchingTermVisitor() {

                    public void visitMatchingTerm(Term term) throws IOException {
                        luceneSubQueries.add(qf.newTermQuery(term));
                    }
                });
                return (luceneSubQueries.size() == 0) ? SrndQuery.theEmptyLcnQuery : (luceneSubQueries.size() == 1) ? (Query) luceneSubQueries.get(0) : SrndBooleanQuery.makeBooleanQuery(luceneSubQueries, false, false);
            }
        };
    }
