    public static int[] mergeSort(int[] s, int f, int l) {
        if (f < l) {
            int m = (f + l) / 2;
            Sort.mergeSort(s, f, m);
            Sort.mergeSort(s, m + 1, l);
            merge(s, f, m, l);
        }
        return s;
    }
