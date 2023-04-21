    private static int binarySearch(final int[] a, int len, int n) {
        if (n < 0) {
            return -1;
        }
        int left = 0, right = len;
        int mid = (left + right) / 2;
        while (left <= right) {
            if (n >= a[mid]) {
                left = mid + 1;
            } else if (n < a[mid]) {
                right = mid - 1;
            }
            mid = (left + right) / 2;
        }
        return left;
    }
