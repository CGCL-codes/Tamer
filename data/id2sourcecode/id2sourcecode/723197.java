    private void mergeSort(T[] array, T[] temp, int lo, int hi) {
        if (hi <= lo) {
            return;
        }
        int mid = (hi + lo) / 2;
        mergeSort(array, temp, lo, mid);
        mergeSort(array, temp, mid + 1, hi);
        merge(array, temp, lo, mid, hi);
    }
