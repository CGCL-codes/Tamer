    private static void sortr(int from, int to, Comparator<Integer> comparator, Swapable swapper) {
        if (to - from < 8) {
            InsertionSort.sort(from, to + 1, comparator, swapper);
            return;
        }
        int middle = (from + to) / 2;
        sortr(from, middle, comparator, swapper);
        sortr(middle, to, comparator, swapper);
        merge(from, middle, to, middle - from, to - middle, comparator, swapper);
    }
