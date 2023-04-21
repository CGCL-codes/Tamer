    @SuppressWarnings({ "unchecked" })
    public static int selectPivotIndex(Object ar[], int left, int right, Comparator comparator) {
        int midIndex = (left + right) / 2;
        int lowIndex = left;
        if (comparator.compare(ar[lowIndex], ar[midIndex]) >= 0) {
            lowIndex = midIndex;
            midIndex = left;
        }
        if (comparator.compare(ar[right], ar[lowIndex]) <= 0) {
            return lowIndex;
        } else if (comparator.compare(ar[right], ar[midIndex]) <= 0) {
            return right;
        }
        return midIndex;
    }
