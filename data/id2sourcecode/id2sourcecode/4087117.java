    static void mergeSort(Object src[], Object dest[], int low, int high) {
        int length = high - low;
        if (length < 7) {
            for (int i = low; i < high; i++) for (int j = i; j > low && dest[j - 1].hashCode() > dest[j].hashCode(); j--) swap(dest, j, j - 1);
            return;
        }
        int mid = (low + high) / 2;
        mergeSort(dest, src, low, mid);
        mergeSort(dest, src, mid, high);
        if (src[mid - 1].hashCode() <= src[mid].hashCode()) {
            System.arraycopy(src, low, dest, low, length);
            return;
        }
        for (int i = low, p = low, q = mid; i < high; i++) {
            if (q >= high || p < mid && src[p].hashCode() <= src[q].hashCode()) dest[i] = src[p++]; else dest[i] = src[q++];
        }
    }
