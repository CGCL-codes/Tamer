    @SuppressWarnings({ "unchecked" })
    public static int selectPivotIndex(Comparable ar[], int left, int right) {
        int midIndex = (left + right) / 2;
        int lowIndex = left;
        if (ar[lowIndex].compareTo(ar[midIndex]) >= 0) {
            lowIndex = midIndex;
            midIndex = left;
        }
        if (ar[right].compareTo(ar[lowIndex]) <= 0) {
            return lowIndex;
        } else if (ar[right].compareTo(ar[midIndex]) <= 0) {
            return midIndex;
        }
        return right;
    }
