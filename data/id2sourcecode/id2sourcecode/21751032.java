    public int offset2index(int offset) {
        try {
            if (index2offset(gapStart - 1) <= offset && index2offset(gapStart) > offset) {
                return gapStart - 1;
            }
        } catch (IndexOutOfBoundsException e) {
        }
        int intervalStart = 0;
        int intervalEnd = size();
        while (intervalEnd > intervalStart + 1) {
            int middle = (intervalStart + intervalEnd) / 2;
            if (index2offset(middle) <= offset) {
                intervalStart = middle;
            } else {
                intervalEnd = middle;
            }
        }
        return intervalStart;
    }
