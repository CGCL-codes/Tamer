    private int find(int first) {
        int s = 0;
        int e = ranges.size() - 1;
        while (s <= e) {
            int m = (s + e) / 2;
            Range r = ranges.get(m);
            if (r.first < first) {
                s = m + 1;
            } else if (r.first > first) {
                e = m - 1;
            } else {
                return m;
            }
        }
        return s;
    }
