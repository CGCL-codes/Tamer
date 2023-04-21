    protected int selectPivot(double[] a, int fromIndex, int toIndex) {
        final int first = fromIndex, last = toIndex - 1, mid = (fromIndex + toIndex) / 2;
        return median(a, first, mid, last);
    }
