    private static int partition(Object A, int p, int r, Compare compare) {
        int z = (r + p) / 2;
        int i = p - 1;
        int j = r + 1;
        while (true) {
            while (compare.compare(A, z, --j) < 0) {
            }
            while (compare.compare(A, z, ++i) > 0) {
            }
            if (i >= j) {
                return j;
            }
            Object o = Array.get(A, i);
            Array.set(A, i, Array.get(A, j));
            Array.set(A, j, o);
            if (z == i) {
                z = j;
            } else if (z == j) {
                z = i;
            }
        }
    }
