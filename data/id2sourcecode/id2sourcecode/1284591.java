    public Query rewrite(IndexReader reader) {
        if (termArrays.size() == 1) {
            Term[] terms = (Term[]) termArrays.get(0);
            BooleanQuery boq = new BooleanQuery(true);
            for (int i = 0; i < terms.length; i++) {
                boq.add(new TermQuery(terms[i]), BooleanClause.Occur.SHOULD);
            }
            boq.setBoost(getBoost());
            return boq;
        } else {
            return this;
        }
    }
