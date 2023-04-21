    private static void sortConfigurationArray(Configuration[] a, int fromIndex, int toIndex) {
        int middle;
        if (a == null) return;
        if (fromIndex + 1 < toIndex) {
            middle = (fromIndex + toIndex) / 2;
            sortConfigurationArray(a, fromIndex, middle);
            sortConfigurationArray(a, middle, toIndex);
            mergeConfigurationArray(a, fromIndex, toIndex);
        }
    }
