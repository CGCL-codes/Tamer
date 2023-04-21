    public static void sortStringArray(String[] a, int fromIndex, int toIndex) {
        int middle;
        if (a == null) return;
        if (fromIndex + 1 < toIndex) {
            middle = (fromIndex + toIndex) / 2;
            sortStringArray(a, fromIndex, middle);
            sortStringArray(a, middle, toIndex);
            mergeStringArray(a, fromIndex, toIndex);
        }
    }
