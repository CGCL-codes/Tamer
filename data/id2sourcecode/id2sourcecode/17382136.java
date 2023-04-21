    private void mergesort(CubeFacelet[] a, int l, int r) {
        if (l < r) {
            int q = (l + r) / 2;
            mergesort(a, l, q);
            mergesort(a, q + 1, r);
            merge(a, l, q, r);
        }
    }
