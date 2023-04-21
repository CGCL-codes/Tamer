    private void mergeSort(int i, int j) {
        if (i < j) {
            int m = (i + j) / 2;
            mergeSort(i, m);
            mergeSort(m + 1, j);
            merge(i, m, j);
        }
    }
