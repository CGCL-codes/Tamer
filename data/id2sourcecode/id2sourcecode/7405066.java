    @Override
    public Query rewrite(IndexReader reader) throws IOException {
        int docNum;
        Term idt = new Term(IndexConstants.FIELDNAME_IDENTIFIER, docIdentifier);
        TermDocs td = reader.termDocs(idt);
        try {
            if (td.next()) {
                docNum = td.doc();
            } else {
                log.warn("No document with identifier=" + docIdentifier + " found in index for 'More like this' query.");
                return new TermQuery(idt);
            }
            if (td.next()) log.warn("More than one document with unique identifier=" + docIdentifier + " found in index.");
        } finally {
            td.close();
        }
        TermFreqVector vector = reader.getTermFreqVector(docNum, matchingField);
        if (vector == null) throw new IllegalFieldConfigException("Field " + matchingField + " does not have term vectors.");
        String[] terms = vector.getTerms();
        int freqs[] = vector.getTermFrequencies();
        int numDocs = reader.numDocs();
        FreqQ q = new FreqQ(terms.length);
        Similarity similarity = new DefaultSimilarity();
        for (int i = 0; i < terms.length; i++) {
            if (minTermFreq > 0 && freqs[i] < minTermFreq) continue;
            int docFreq = reader.docFreq(new Term(matchingField, terms[i]));
            if (minDocFreq > 0 && docFreq < minDocFreq) continue;
            if (docFreq == 0) continue;
            q.insertWithOverflow(new Freq(terms[i], freqs[i] * similarity.idf(docFreq, numDocs)));
        }
        int qterms = 0;
        float bestScore = 0;
        BooleanQuery query = new BooleanQuery();
        for (Freq ar = q.pop(); ar != null; ) {
            TermQuery tq = new TermQuery(new Term(matchingField, ar.term));
            if (boostByScore) {
                if (qterms == 0) bestScore = ar.score;
                tq.setBoost(ar.score / bestScore);
            }
            try {
                query.add(tq, BooleanClause.Occur.SHOULD);
            } catch (BooleanQuery.TooManyClauses ignore) {
                break;
            }
            qterms++;
            if (maxQueryTerms > 0 && qterms >= maxQueryTerms) break;
        }
        query.setMinimumNumberShouldMatch((int) (qterms * fractionTermsToMatch));
        query.setBoost(this.getBoost());
        return query.rewrite(reader);
    }
