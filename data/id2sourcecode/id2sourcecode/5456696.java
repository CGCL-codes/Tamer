    public NGramProbability findNGram(int nthWordID) {
        int mid, start = 0, end = getNumberNGrams();
        NGramProbability ngram = null;
        while ((end - start) > 0) {
            mid = (start + end) / 2;
            int midWordID = getWordID(mid);
            if (midWordID < nthWordID) {
                start = mid + 1;
            } else if (midWordID > nthWordID) {
                end = mid;
            } else {
                ngram = getNGramProbability(mid);
                break;
            }
        }
        return ngram;
    }
