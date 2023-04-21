    public Query rewrite(IndexReader reader) throws IOException {
        MoreLikeThis mlt = new MoreLikeThis(reader);
        mlt.setFieldNames(moreLikeFields);
        mlt.setAnalyzer(analyzer);
        mlt.setMinTermFreq(minTermFrequency);
        if (minDocFreq >= 0) {
            mlt.setMinDocFreq(minDocFreq);
        }
        mlt.setMaxQueryTerms(maxQueryTerms);
        mlt.setStopWords(stopWords);
        BooleanQuery bq = (BooleanQuery) mlt.like(new ByteArrayInputStream(likeText.getBytes()));
        BooleanClause[] clauses = bq.getClauses();
        return bq;
    }
