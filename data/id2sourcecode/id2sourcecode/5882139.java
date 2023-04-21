    public Query rewrite(IndexReader reader) throws IOException {
        RegexQuery orig = new RegexQuery(term);
        orig.setRegexImplementation(regexImpl);
        BooleanQuery bq = (BooleanQuery) orig.rewrite(reader);
        BooleanClause[] clauses = bq.getClauses();
        SpanQuery[] sqs = new SpanQuery[clauses.length];
        for (int i = 0; i < clauses.length; i++) {
            BooleanClause clause = clauses[i];
            TermQuery tq = (TermQuery) clause.query;
            sqs[i] = new SpanTermQuery(tq.getTerm());
            sqs[i].setBoost(tq.getBoost());
        }
        SpanOrQuery query = new SpanOrQuery(sqs);
        query.setBoost(orig.getBoost());
        return query;
    }
