    public Query rewrite(IndexReader reader) throws IOException {
        if (disjuncts.size() == 1) {
            Query singleton = (Query) disjuncts.get(0);
            Query result = singleton.rewrite(reader);
            if (getBoost() != 1.0f) {
                if (result == singleton) result = (Query) result.clone();
                result.setBoost(getBoost() * result.getBoost());
            }
            return result;
        }
        DisjunctionMaxQuery clone = null;
        for (int i = 0; i < disjuncts.size(); i++) {
            Query clause = (Query) disjuncts.get(i);
            Query rewrite = clause.rewrite(reader);
            if (rewrite != clause) {
                if (clone == null) clone = (DisjunctionMaxQuery) this.clone();
                clone.disjuncts.set(i, rewrite);
            }
        }
        if (clone != null) return clone; else return this;
    }
