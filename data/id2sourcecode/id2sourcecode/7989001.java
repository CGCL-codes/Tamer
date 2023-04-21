    private int getOffsetFromIndex(int[] offsets, int index) {
        int lowerBound = 0;
        int upperBound = offsets.length;
        int newBound = 0;
        while (upperBound - lowerBound > 1) {
            newBound = lowerBound + (upperBound - lowerBound) / 2;
            if (offsets[newBound] > index) {
                upperBound = newBound;
            } else {
                lowerBound = newBound;
            }
        }
        return lowerBound;
    }
