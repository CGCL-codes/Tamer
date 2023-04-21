    @Deprecated
    public NSArray<String> findTermStrings(Query q) {
        NSMutableArray<String> terms = new NSMutableArray<String>();
        try {
            IndexReader reader = indexReader();
            HashSet<Term> suggestedTerms = new HashSet<Term>();
            q.rewrite(reader).extractTerms(suggestedTerms);
            for (Iterator<Term> iter = suggestedTerms.iterator(); iter.hasNext(); ) {
                Term term = iter.next();
                terms.addObject(term.text());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return terms.immutableClone();
    }
