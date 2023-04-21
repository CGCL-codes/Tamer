    private static void mergeSort(int src[], int dest[], int low, int high, int size, Comparator c) {
        int length = high - low;
        if (length < 7) {
            for (int i = low; i < high; i++) for (int j = i; j > low && c.compare(dest, (j - 1) * size, dest, j * size) > 0; j--) swap(dest, j, j - 1, size);
            return;
        }
        int mid = (low + high) / 2;
        mergeSort(dest, src, low, mid, size, c);
        mergeSort(dest, src, mid, high, size, c);
        if (c.compare(src, (mid - 1) * size, src, mid * size) <= 0) {
            System.arraycopy(src, low * size, dest, low * size, length * size);
            return;
        }
        for (int i = low, p = low, q = mid; i < high; i++) {
            if (q >= high || p < mid && c.compare(src, p * size, src, q * size) <= 0) {
                for (int s = size; --s >= 0; ) dest[i * size + s] = src[p * size + s];
                p++;
            } else {
                for (int s = size; --s >= 0; ) dest[i * size + s] = src[q * size + s];
                q++;
            }
        }
    }
