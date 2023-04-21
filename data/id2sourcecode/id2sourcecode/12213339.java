    void quickSort(DefaultMutableTreeNode a[], int lo0, int hi0) {
        int lo = lo0;
        int hi = hi0;
        int mid;
        if (hi0 > lo0) {
            mid = (lo0 + hi0) / 2;
            while (lo <= hi) {
                while ((lo < hi0) && (compare(a[lo], a[mid]) > 0)) ++lo;
                while ((hi > lo0) && (compare(a[hi], a[mid]) < 0)) --hi;
                if (lo <= hi) {
                    swap(a, lo, hi);
                    ++lo;
                    --hi;
                }
            }
            if (lo0 < hi) quickSort(a, lo0, hi);
            if (lo < hi0) quickSort(a, lo, hi0);
        }
    }
