    private static void mergeSortRecursive(Vector v, int first, int last, Object[] help) {
        if (first < last) {
            int mid = (first + last) / 2;
            mergeSortRecursive(v, first, mid, help);
            mergeSortRecursive(v, mid + 1, last, help);
            mergeRecursive(v, first, mid, last, help);
        }
        return;
    }
