    public Query rewrite(IndexReader reader) throws IOException {
        if (rewrittenQuery != null) {
            return rewrittenQuery;
        }
        for (Iterator iter = fieldVals.iterator(); iter.hasNext(); ) {
            FieldVals f = (FieldVals) iter.next();
            addTerms(reader, f);
        }
        fieldVals.clear();
        BooleanQuery bq = new BooleanQuery();
        HashMap variantQueries = new HashMap();
        int size = q.size();
        for (int i = 0; i < size; i++) {
            ScoreTerm st = (ScoreTerm) q.pop();
            ArrayList l = (ArrayList) variantQueries.get(st.fuzziedSourceTerm);
            if (l == null) {
                l = new ArrayList();
                variantQueries.put(st.fuzziedSourceTerm, l);
            }
            l.add(st);
        }
        for (Iterator iter = variantQueries.values().iterator(); iter.hasNext(); ) {
            ArrayList variants = (ArrayList) iter.next();
            if (variants.size() == 1) {
                ScoreTerm st = (ScoreTerm) variants.get(0);
                TermQuery tq = new FuzzyTermQuery(st.term, ignoreTF);
                tq.setBoost(st.score);
                bq.add(tq, false, false);
            } else {
                BooleanQuery termVariants = new BooleanQuery();
                for (Iterator iterator2 = variants.iterator(); iterator2.hasNext(); ) {
                    ScoreTerm st = (ScoreTerm) iterator2.next();
                    TermQuery tq = new FuzzyTermQuery(st.term, ignoreTF);
                    tq.setBoost(st.score);
                    termVariants.add(tq, false, false);
                }
                bq.add(termVariants, false, false);
            }
        }
        bq.setBoost(getBoost());
        this.rewrittenQuery = bq;
        return bq;
    }
