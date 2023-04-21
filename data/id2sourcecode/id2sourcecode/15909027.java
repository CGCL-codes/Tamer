    public static void sortFileArray(File[] a, int fromIndex, int toIndex) {
        int middle;
        if (a == null) return;
        if (fromIndex + 1 < toIndex) {
            middle = (fromIndex + toIndex) / 2;
            sortFileArray(a, fromIndex, middle);
            sortFileArray(a, middle, toIndex);
            mergeFileArray(a, fromIndex, toIndex);
        }
    }
