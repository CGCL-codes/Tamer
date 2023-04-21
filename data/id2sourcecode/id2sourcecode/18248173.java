    public BigramProbability findBigram(int secondWordID) {
        int mid, start = 0, end = getNumberNGrams() - 1;
        BigramProbability bigram = null;
        while ((end - start) > 0) {
            mid = (start + end) / 2;
            int midWordID = getWordID(mid);
            if (midWordID < secondWordID) {
                start = mid + 1;
            } else if (midWordID > secondWordID) {
                end = mid;
            } else {
                bigram = getBigramProbability(mid);
                break;
            }
        }
        return bigram;
    }
