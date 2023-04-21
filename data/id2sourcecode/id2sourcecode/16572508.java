    private void mergeSortHelper(Sequence S, int start, int end, Comparator c) {
        if (start < end) {
            int middle = (start + end) / 2;
            mergeSortHelper(S, start, middle, c);
            mergeSortHelper(S, middle + 1, end, c);
            merge(S, start, middle, end, c);
        }
    }
