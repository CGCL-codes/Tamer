    private int quickSearchPosition(String inSortString, int inLowerBoundary, int inUpperBoundary) {
        int outPosition = 0;
        if (isEmpty()) {
            outPosition = 0;
        } else if (isMin(inSortString)) {
            outPosition = 0;
        } else if (isMax(inSortString)) {
            outPosition = sortedArr.length;
        } else if (isInMinimalIntervall(inLowerBoundary, inUpperBoundary)) {
            if (isLessThenLower(inSortString, inLowerBoundary)) {
                outPosition = inLowerBoundary;
            } else if (isGreaterThenUpper(inSortString, inUpperBoundary)) {
                outPosition = inUpperBoundary;
            } else {
                outPosition = inUpperBoundary;
            }
        } else {
            int lNewBoundary = (inLowerBoundary + inUpperBoundary) / 2;
            if (isLessThenPosition(inSortString, lNewBoundary)) {
                outPosition = quickSearchPosition(inSortString, inLowerBoundary, lNewBoundary);
            } else {
                outPosition = quickSearchPosition(inSortString, lNewBoundary, inUpperBoundary);
            }
        }
        return outPosition;
    }
