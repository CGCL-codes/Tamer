    public Query rewrite(IndexReader reader) throws IOException {
        BooleanQuery query = new BooleanQuery(true);
        TermEnum enumerator = reader.terms(prefix);
        try {
            String prefixText = prefix.text();
            String prefixField = prefix.field();
            do {
                Term term = enumerator.term();
                if (term != null && term.text().startsWith(prefixText) && term.field() == prefixField) {
                    TermQuery tq = new TermQuery(term);
                    tq.setBoost(getBoost());
                    query.add(tq, BooleanClause.Occur.SHOULD);
                } else {
                    break;
                }
            } while (enumerator.next());
        } finally {
            enumerator.close();
        }
        return query;
    }
