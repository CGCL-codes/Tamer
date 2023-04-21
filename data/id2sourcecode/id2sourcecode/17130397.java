    public Interval findOverlap(Interval interval1) {
        int low = 0;
        int high = intervals.size() - 1;
        Interval interval2;
        Interval result = null;
        while (high >= low) {
            int middle = (high + low) / 2;
            interval2 = (Interval) intervals.elementAt(middle);
            if (interval1.start <= interval2.end) {
                if (interval1.end >= interval2.start) {
                    result = interval2;
                }
                high = middle - 1;
            } else {
                low = middle + 1;
            }
        }
        return result;
    }
