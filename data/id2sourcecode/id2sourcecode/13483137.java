    public Query rewrite(IndexReader reader) throws IOException {
        FilteredTermEnum enumerator = getEnum(reader);
        BooleanQuery query = new BooleanQuery(true);
        try {
            do {
                Term t = enumerator.term();
                if (t != null) {
                    TermQuery tq = new TermQuery(t);
                    tq.setBoost(getBoost() * enumerator.difference());
                    query.add(tq, BooleanClause.Occur.SHOULD);
                }
            } while (enumerator.next());
        } finally {
            enumerator.close();
        }
        return query;
    }
