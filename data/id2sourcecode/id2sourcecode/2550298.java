    public static void mergeSort(int fromIndex, int toIndex, IntComparator c, Swapper swapper) {
        int length = toIndex - fromIndex;
        if (length < SMALL) {
            for (int i = fromIndex; i < toIndex; i++) {
                for (int j = i; j > fromIndex && (c.compare(j - 1, j) > 0); j--) {
                    swapper.swap(j, j - 1);
                }
            }
            return;
        }
        int mid = (fromIndex + toIndex) / 2;
        mergeSort(fromIndex, mid, c, swapper);
        mergeSort(mid, toIndex, c, swapper);
        if (c.compare(mid - 1, mid) <= 0) return;
        inplace_merge(fromIndex, mid, toIndex, c, swapper);
    }
