    protected int selectPivotIndex(final int left, final int right) {
        int midIndex = (left + right) / 2;
        int lowIndex = left;
        if (array[lowIndex].compareTo(array[midIndex]) >= 0) {
            lowIndex = midIndex;
            midIndex = left;
        }
        if (array[right].compareTo(array[lowIndex]) <= 0) {
            return lowIndex;
        } else if (array[right].compareTo(array[midIndex]) <= 0) {
            return midIndex;
        }
        return right;
    }
