    public TrigramProbability findTrigram(int thirdWordID) {
        int mid, start = 0, end = getNumberNGrams() - 1;
        while ((end - start) > 0) {
            mid = (start + end) / 2;
            int midWordID = getWordID(mid);
            if (midWordID < thirdWordID) {
                start = mid + 1;
            } else end = mid;
        }
        if (end != getNumberNGrams() - 1 && thirdWordID == getWordID(end)) return getTrigramProbability(end);
        return null;
    }
