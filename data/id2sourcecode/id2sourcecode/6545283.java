    public static int[][] joinOverlapedRanges(int[][] ranges) {
        int numRanges = 1;
        for (int i = 1; i < ranges.length; i++) {
            if ((ranges[i][0] - ranges[i - 1][1]) > 1) {
                numRanges++;
            }
        }
        if (numRanges == ranges.length) return ranges;
        int[][] joinedRanges = new int[numRanges][2];
        int rangeIndex = 0;
        joinedRanges[0][0] = ranges[0][0];
        for (int i = 1; i < ranges.length; i++) {
            if ((ranges[i][0] - ranges[i - 1][1]) > 1) {
                joinedRanges[rangeIndex++][1] = ranges[i - 1][1];
                joinedRanges[rangeIndex][0] = ranges[i][0];
            }
        }
        joinedRanges[numRanges - 1][1] = ranges[ranges.length - 1][1];
        return joinedRanges;
    }
