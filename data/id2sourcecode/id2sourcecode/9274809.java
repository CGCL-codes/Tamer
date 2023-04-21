    public Query rewrite(IndexReader reader) throws IOException {
        if (clauses.size() == 1) {
            BooleanClause c = (BooleanClause) clauses.elementAt(0);
            if (!c.isProhibited()) {
                Query query = c.getQuery().rewrite(reader);
                if (getBoost() != 1.0f) {
                    if (query == c.getQuery()) query = (Query) query.clone();
                    query.setBoost(getBoost() * query.getBoost());
                }
                return query;
            }
        }
        BooleanQuery clone = null;
        for (int i = 0; i < clauses.size(); i++) {
            BooleanClause c = (BooleanClause) clauses.elementAt(i);
            Query query = c.getQuery().rewrite(reader);
            if (query != c.getQuery()) {
                if (clone == null) clone = (BooleanQuery) this.clone();
                clone.clauses.setElementAt(new BooleanClause(query, c.getOccur()), i);
            }
        }
        if (clone != null) {
            return clone;
        } else return this;
    }
