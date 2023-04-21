    public static int search(int[] array, int offset, int length, int x) {
        if (length <= 0) return offset;
        int low = offset;
        int high = offset + length - 1;
        if (x <= array[low]) return low;
        if (x > array[high]) return high + 1;
        while (low + 1 < high) {
            int mid = (low + high) / 2;
            if (x <= array[mid]) high = mid; else low = mid;
        }
        debug.assertion(low + 1 == high);
        return high;
    }
