    private static void mergeSort(String src[], String dest[], int low, int high) {
        int length = high - low;
        if (length < 7) {
            for (int i = low; i < high; i++) for (int j = i; j > low && (dest[j - 1]).compareTo(dest[j]) > 0; j--) swap(dest, j, j - 1);
            return;
        }
        int mid = (low + high) / 2;
        mergeSort(dest, src, low, mid);
        mergeSort(dest, src, mid, high);
        if ((src[mid - 1]).compareTo(src[mid]) <= 0) {
            System.arraycopy(src, low, dest, low, length);
            return;
        }
        for (int i = low, p = low, q = mid; i < high; i++) {
            if (q >= high || p < mid && (src[p]).compareTo(src[q]) <= 0) dest[i] = src[p++]; else dest[i] = src[q++];
        }
    }
