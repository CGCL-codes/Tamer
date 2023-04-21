    public Query rewrite(IndexReader reader) throws IOException {
        if (clauses.size() == 1) {
            BooleanClause c = (BooleanClause) clauses.elementAt(0);
            if (!c.prohibited) {
                Query query = c.query.rewrite(reader);
                if (getBoost() != 1.0f) {
                    if (query == c.query) query = (Query) query.clone();
                    query.setBoost(getBoost() * query.getBoost());
                }
                return query;
            }
        }
        BooleanQuery clone = null;
        for (int i = 0; i < clauses.size(); i++) {
            BooleanClause c = (BooleanClause) clauses.elementAt(i);
            Query query = c.query.rewrite(reader);
            if (query != c.query) {
                if (clone == null) clone = (BooleanQuery) this.clone();
                clone.clauses.setElementAt(new BooleanClause(query, c.required, c.prohibited), i);
            }
        }
        if (clone != null) {
            return clone;
        } else return this;
    }
