    private void quickSortChan(Part[] a, int lo0, int hi0) {
        int lo = lo0;
        int hi = hi0;
        Part mid;
        if (hi0 > lo0) {
            mid = a[(lo0 + hi0) / 2];
            while (lo <= hi) {
                while ((lo < hi0) && (a[lo].getChannel() < mid.getChannel())) ++lo;
                while ((hi > lo0) && (a[hi].getChannel() > mid.getChannel())) --hi;
                if (lo <= hi) {
                    swap(a, lo, hi);
                    ++lo;
                    --hi;
                }
            }
            if (lo0 < hi) quickSortChan(a, lo0, hi);
            if (lo < hi0) quickSortChan(a, lo, hi0);
        }
    }
