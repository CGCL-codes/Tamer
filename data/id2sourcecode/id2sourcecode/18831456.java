    private void mergesort(int[] r, int lo, int hi) {
        if (lo < hi) {
            int m = (hi + lo) / 2;
            mergesort(r, lo, m);
            mergesort(r, m + 1, hi);
            merge(r, lo, m, hi);
        }
    }
