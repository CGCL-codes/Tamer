    public Query rewrite(IndexReader reader) throws IOException {
        FilteredTermEnum enumerator = getEnum(reader);
        int maxClauseCount = BooleanQuery.getMaxClauseCount();
        ScoreTermQueue stQueue = new ScoreTermQueue(maxClauseCount);
        try {
            do {
                float minScore = 0.0f;
                float score = 0.0f;
                Term t = enumerator.term();
                if (t != null) {
                    score = enumerator.difference();
                    if (stQueue.size() < maxClauseCount || score > minScore) {
                        stQueue.insert(new ScoreTerm(t, score));
                        minScore = ((ScoreTerm) stQueue.top()).score;
                    }
                }
            } while (enumerator.next());
        } finally {
            enumerator.close();
        }
        BooleanQuery query = new BooleanQuery(true);
        int size = stQueue.size();
        for (int i = 0; i < size; i++) {
            ScoreTerm st = (ScoreTerm) stQueue.pop();
            TermQuery tq = new TermQuery(st.term);
            tq.setBoost(getBoost() * st.score);
            query.add(tq, BooleanClause.Occur.SHOULD);
        }
        return query;
    }
