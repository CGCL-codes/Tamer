    private static void sort(double[] A, int p, int r) {
        int q;
        if (p < r) {
            q = (p + r) / 2;
            sort(A, p, q);
            sort(A, q + 1, r);
            merge(A, p, q, r);
        }
    }
