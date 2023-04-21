    public int findCriticalPoint(int lower, int upper) {
        int testPoint = 0;
        while (true) {
            if (processor.check(testPoint)) {
                lower = testPoint;
            } else {
                upper = testPoint;
            }
            if (upper == -1) {
                testPoint = (lower + 1) * 2;
            } else if (lower == -1) {
                break;
            } else if (upper - lower <= 1) {
                break;
            } else {
                testPoint = (upper + lower) / 2;
            }
        }
        return upper;
    }
