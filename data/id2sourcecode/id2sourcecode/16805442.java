    public static void sort(final String[] array, final Comparator<String> c, final int lo, final int hi) {
        final int m = (lo + hi) / 2;
        final String x = array[m];
        int i = lo;
        int j = hi;
        do {
            while (c.compare(x, array[i]) > 0) {
                i++;
            }
            while (c.compare(x, array[j]) < 0) {
                j--;
            }
            if (i <= j) {
                final String s = array[i];
                array[i] = array[j];
                array[j] = s;
                i++;
                j--;
            }
        } while (i <= j);
        if (lo < j) {
            sort(array, c, lo, j);
        }
        if (i < hi) {
            sort(array, c, i, hi);
        }
    }
