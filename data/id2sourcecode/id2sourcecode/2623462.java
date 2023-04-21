    void mergeSort(Comparator comp, long a[], int lo, int hi) {
        if (lo == hi) {
            return;
        }
        int length = hi - lo + 1;
        int pivot = (lo + hi) / 2;
        mergeSort(comp, a, lo, pivot);
        mergeSort(comp, a, pivot + 1, hi);
        long working[] = new long[length];
        for (int i = 0; i < length; i++) {
            working[i] = a[lo + i];
        }
        int m1 = 0;
        int m2 = pivot - lo + 1;
        for (int i = 0; i < length; i++) {
            if (m2 <= hi - lo) {
                if (m1 <= pivot - lo) {
                    if (comp.gt(working[m1], working[m2])) {
                        a[i + lo] = working[m2++];
                    } else {
                        a[i + lo] = working[m1++];
                    }
                } else {
                    a[i + lo] = working[m2++];
                }
            } else {
                a[i + lo] = working[m1++];
            }
        }
    }
