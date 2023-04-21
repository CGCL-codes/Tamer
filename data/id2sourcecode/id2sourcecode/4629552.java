    public static int findEnd(Region[] regions, int p) {
        int lo = 0;
        int hi = regions.length;
        while (lo != hi) {
            int mid = (hi + lo) / 2;
            if (regions[mid].end < p) lo = mid + 1; else hi = mid;
        }
        return hi;
    }
