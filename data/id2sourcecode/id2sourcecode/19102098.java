    private static void mergeSort(String src[], String dest[], int low, int high, boolean caseSensitive, boolean numeric) {
        int length = high - low;
        if (length < 7) {
            for (int i = low; i < high; i++) for (int j = i; j > low && compare(dest[j - 1], dest[j], caseSensitive, numeric) > 0; j--) swap(dest, j, j - 1);
            return;
        }
        int mid = (low + high) / 2;
        mergeSort(dest, src, low, mid, caseSensitive, numeric);
        mergeSort(dest, src, mid, high, caseSensitive, numeric);
        if (compare(src[mid - 1], src[mid], caseSensitive, numeric) <= 0) {
            System.arraycopy(src, low, dest, low, length);
            return;
        }
        for (int i = low, p = low, q = mid; i < high; i++) {
            if (q >= high || p < mid && compare(src[p], src[q], caseSensitive, numeric) <= 0) dest[i] = src[p++]; else dest[i] = src[q++];
        }
    }
