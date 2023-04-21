    public int getMaxDiff(IntValIntervalSet ivis, int start, int end) {
        int ivisStart = ivis.getMin();
        int ivisEnd = ivis.getMax();
        int maxDiff = Integer.MIN_VALUE;
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
        int ivisMax = -(ivis.getMaxWorthOverRange(ivisStart, startVals[startIndex]));
        if ((startVals[startIndex] > ivisStart) && (ivisMax > maxDiff)) {
            maxDiff = ivisMax;
        }
        ivisMax = -(ivis.getMaxWorthOverRange(endVals[endIndex], ivisEnd));
        if ((endVals[endIndex] < ivisEnd) && (ivisMax > maxDiff)) {
            maxDiff = ivisMax;
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
            int minOfOther = ivis.getMinWorthOverRange(rangeStart, rangeEnd);
            int currentDiff = worth - minOfOther;
            if (currentDiff > maxDiff) {
                maxDiff = currentDiff;
            }
            if (nextIdxList[currentIndex] >= 0) {
                if (startVals[nextIdxList[currentIndex]] > (endVals[currentIndex] + 1)) {
                    int gapDiff = -(ivis.getMinWorthOverRange(endVals[currentIndex] + 1, startVals[nextIdxList[currentIndex]] - 1));
                    if (gapDiff > maxDiff) {
                        maxDiff = gapDiff;
                    }
                }
            }
            currentIndex = getNextIntervalIndex(currentIndex);
        }
        return maxDiff;
    }
