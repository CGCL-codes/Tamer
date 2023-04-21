    @Deprecated
    public NSArray<Term> findTerms(Query q) {
        NSMutableArray<Term> terms = new NSMutableArray<Term>();
        try {
            IndexReader reader = indexReader();
            HashSet<Term> suggestedTerms = new HashSet<Term>();
            q.rewrite(reader).extractTerms(suggestedTerms);
            for (Iterator<Term> iter = suggestedTerms.iterator(); iter.hasNext(); ) {
                Term term = iter.next();
                terms.addObject(term);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return terms.immutableClone();
    }
