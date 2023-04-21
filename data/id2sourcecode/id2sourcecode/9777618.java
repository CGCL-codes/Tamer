    public BigramProbability findBigram(int secondWordID) {
        int mid, start = 0, end = getNumberNGrams() - 1;
        while ((end - start) > 0) {
            mid = (start + end) / 2;
            int midWordID = getWordID(mid);
            if (midWordID < secondWordID) {
                start = mid + 1;
            } else end = mid;
        }
        if (end != getNumberNGrams() - 1 && secondWordID == getWordID(end)) return getBigramProbability(end);
        return null;
    }
