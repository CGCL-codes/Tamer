    private static void mergeSort(int[][] array, int start, int end) {
        if (start < end) {
            int middle = (end + start) / 2;
            mergeSort(array, start, middle);
            mergeSort(array, middle + 1, end);
            merge(array, start, middle, end);
        }
    }
