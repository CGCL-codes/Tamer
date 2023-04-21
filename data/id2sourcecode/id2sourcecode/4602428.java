    protected static final void mergesort(Object[] a, int[] b, int p, int r, Comparator cmp) {
        if (p >= r) {
            return;
        }
        if (r - p + 1 < SORT_THRESHOLD) {
            insertionsort(a, b, p, r, cmp);
        } else {
            int q = (p + r) / 2;
            mergesort(a, b, p, q, cmp);
            mergesort(a, b, q + 1, r, cmp);
            merge(a, b, p, q, r, cmp);
        }
    }
