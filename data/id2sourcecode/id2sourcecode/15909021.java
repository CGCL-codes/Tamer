    public static void sortIntegerArray(Integer[] a, int fromIndex, int toIndex) {
        int middle;
        if (a == null) return;
        if (fromIndex + 1 < toIndex) {
            middle = (fromIndex + toIndex) / 2;
            sortIntegerArray(a, fromIndex, middle);
            sortIntegerArray(a, middle, toIndex);
            mergeIntegerArray(a, fromIndex, toIndex);
        }
    }
