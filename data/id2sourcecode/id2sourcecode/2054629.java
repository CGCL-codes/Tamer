    public int getMinDiff(IntValIntervalSet ivis, int start, int end) {
        int ivisStart = ivis.getMin();
        int ivisEnd = ivis.getMax();
        int minDiff = Integer.MAX_VALUE;
        int startIndex = indexOfValue(start);
        int endIndex = indexOfValue(end);
        if (startIndex < 0) {
            startIndex = -(startIndex + 1);
        }
        if (endIndex < 0) {
            endIndex = -(endIndex + 1);
        }
        if (ivisStart < start) {
            ivisStart = start;
        }
        if (ivisEnd > end) {
            ivisEnd = end;
        }
        int ivisMin = -(ivis.getMaxWorthOverRange(ivisStart, startVals[startIndex]));
        if ((startVals[startIndex] > ivisStart) && (ivisMin < minDiff)) {
            minDiff = ivisMin;
        }
        ivisMin = -(ivis.getMaxWorthOverRange(endVals[endIndex], ivisEnd));
        if ((endVals[endIndex] < ivisEnd) && (ivisMin < minDiff)) {
            minDiff = ivisMin;
        }
        int currentIndex = startIndex;
        int postEndIndex = nextIdxList[endIndex];
        while (currentIndex != postEndIndex) {
            int worth = worthOfInterval.get(currentIndex);
            int rangeStart = startVals[currentIndex];
            int rangeEnd = endVals[currentIndex];
            if (rangeStart < start) {
                rangeStart = start;
            }
            if (rangeEnd > end) {
                rangeEnd = end;
            }
            int maxOfOther = ivis.getMaxWorthOverRange(rangeStart, rangeEnd);
            int currentDiff = worth - maxOfOther;
            if (currentDiff < minDiff) {
                minDiff = currentDiff;
            }
            if (nextIdxList[currentIndex] >= 0) {
                if (startVals[nextIdxList[currentIndex]] > (endVals[currentIndex] + 1)) {
                    int gapDiff = -(ivis.getMinWorthOverRange(endVals[currentIndex] + 1, startVals[nextIdxList[currentIndex]] - 1));
                    if (gapDiff < minDiff) {
                        minDiff = gapDiff;
                    }
                }
            }
            currentIndex = getNextIntervalIndex(currentIndex);
        }
        return minDiff;
    }
