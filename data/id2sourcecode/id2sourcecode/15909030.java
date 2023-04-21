    public static void sortFloatArray(Float[] a, int fromIndex, int toIndex) {
        int middle;
        if (a == null) return;
        if (fromIndex + 1 < toIndex) {
            middle = (fromIndex + toIndex) / 2;
            sortFloatArray(a, fromIndex, middle);
            sortFloatArray(a, middle, toIndex);
            mergeFloatArray(a, fromIndex, toIndex);
        }
    }
