    private static void mergeSort(Comparable[] a, Comparable[] tmpArray, int left, int right) {
        if (left < right) {
            int center = (left + right) / 2;
            Sort.mergeSort(a, tmpArray, left, center);
            Sort.mergeSort(a, tmpArray, center + 1, right);
            Sort.merge(a, tmpArray, left, center + 1, right);
        }
    }
