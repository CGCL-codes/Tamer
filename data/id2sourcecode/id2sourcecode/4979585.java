    private static void split(final int first, final int last) {
        if (first < last) {
            int middle = (first + last) / 2;
            split(first, middle);
            split(middle + 1, last);
            merge(first, middle, middle + 1, last);
        }
    }
