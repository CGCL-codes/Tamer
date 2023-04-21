    public static void sort(Sortable a, int lo0, int hi0) {
        int lo = lo0;
        int hi = hi0;
        if (hi0 > lo0) {
            int mid = (lo0 + hi0) / 2;
            while (lo <= hi) {
                while ((lo < hi0) && (a.compare(lo, mid) < 0)) ++lo;
                while ((hi > lo0) && (a.compare(hi, mid) > 0)) --hi;
                if (lo <= hi) {
                    if (lo != hi) {
                        a.swap(lo, hi);
                        if (lo == mid) {
                            mid = hi;
                        } else if (hi == mid) {
                            mid = lo;
                        }
                    }
                    ++lo;
                    --hi;
                }
            }
            if (lo0 < hi) sort(a, lo0, hi);
            if (lo < hi0) sort(a, lo, hi0);
        }
    }
