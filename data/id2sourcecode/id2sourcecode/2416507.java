    public Query rewrite(IndexReader reader) throws IOException {
        BooleanQuery query = new BooleanQuery(true);
        TermEnum enumerator = reader.terms(lowerTerm);
        try {
            boolean checkLower = false;
            if (!inclusive) checkLower = true;
            String testField = getField();
            do {
                Term term = enumerator.term();
                if (term != null && term.field() == testField) {
                    if (!checkLower || term.text().compareTo(lowerTerm.text()) > 0) {
                        checkLower = false;
                        if (upperTerm != null) {
                            int compare = upperTerm.text().compareTo(term.text());
                            if ((compare < 0) || (!inclusive && compare == 0)) break;
                        }
                        TermQuery tq = new TermQuery(term);
                        tq.setBoost(getBoost());
                        query.add(tq, BooleanClause.Occur.SHOULD);
                    }
                } else {
                    break;
                }
            } while (enumerator.next());
        } finally {
            enumerator.close();
        }
        return query;
    }
