    protected static final void mergesort(int[] a, double[] b, int p, int r) {
        if (p >= r) {
            return;
        }
        if (r - p + 1 < SORT_THRESHOLD) {
            insertionsort(a, b, p, r);
        } else {
            int q = (p + r) / 2;
            mergesort(a, b, p, q);
            mergesort(a, b, q + 1, r);
            merge(a, b, p, q, r);
        }
    }
