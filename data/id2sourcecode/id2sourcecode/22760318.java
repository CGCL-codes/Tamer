    private synchronized void mergeSort(int[] src, int[] dest, int low, int high) {
        int length = high - low;
        if (length < 7) {
            for (int i = low; i < high; i++) {
                for (int j = i; j > low && compare(dest[j - 1], dest[j]) > 0; j--) {
                    swap(dest, j, j - 1);
                }
            }
            return;
        }
        int mid = (low + high) / 2;
        mergeSort(dest, src, low, mid);
        mergeSort(dest, src, mid, high);
        if (compare(src[mid - 1], src[mid]) <= 0) {
            System.arraycopy(src, low, dest, low, length);
            return;
        }
        for (int i = low, p = low, q = mid; i < high; i++) {
            if (q >= high || (p < mid && compare(src[p], src[q]) <= 0)) {
                dest[i] = src[p++];
            } else {
                dest[i] = src[q++];
            }
        }
    }
