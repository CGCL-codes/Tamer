    public int findProbabilityID(int thirdWordID) {
        int mid, start = 0, end = getNumberNGrams();
        while ((end - start) > 0) {
            mid = (start + end) / 2;
            int midWordID = getWordID(mid);
            if (midWordID < thirdWordID) {
                start = mid + 1;
            } else end = mid;
        }
        if (end != getNumberNGrams() && thirdWordID == getWordID(end)) return getProbabilityID(end);
        return -1;
    }
