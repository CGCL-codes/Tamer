    private void quickSortHelper(int lo, int hi) {
        for (; ; ) {
            int diff = hi - lo;
            if (diff <= QUICKSORT_THRESHOLD) {
                break;
            }
            int i = (hi + lo) / 2;
            if (compare(lo, i) > 0) {
                swap(lo, i);
            }
            if (compare(lo, hi) > 0) {
                swap(lo, hi);
            }
            if (compare(i, hi) > 0) {
                swap(i, hi);
            }
            int j = hi - 1;
            swap(i, j);
            i = lo;
            int v = j;
            for (; ; ) {
                while (compare(++i, v) < 0) {
                    ;
                }
                while (compare(--j, v) > 0) {
                    ;
                }
                if (j < i) {
                    break;
                }
                swap(i, j);
            }
            swap(i, hi - 1);
            if (j - lo <= hi - i + 1) {
                quickSortHelper(lo, j);
                lo = i + 1;
            } else {
                quickSortHelper(i + 1, hi);
                hi = j;
            }
        }
    }
