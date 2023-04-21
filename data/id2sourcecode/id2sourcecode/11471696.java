    private int getIndex(int c, int start, int stop) {
        int pivot = (start + stop) / 2;
        if (c == value[pivot]) return pivot;
        if (start >= stop) return -1;
        if (c < value[pivot]) return getIndex(c, start, pivot - 1);
        return getIndex(c, pivot + 1, stop);
    }
