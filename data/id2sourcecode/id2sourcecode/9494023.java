    static int binarySearch(Vector<Integer> sorted, int first, int upto, int key) {
        int comparisonCount = 0;
        while (first < upto) {
            int mid = (first + upto) / 2;
            if (key < sorted.get(mid)) {
                upto = mid;
                comparisonCount++;
            } else if (key > sorted.get(mid)) {
                first = mid + 1;
                comparisonCount += 2;
            } else {
                comparisonCount += 2;
                return mid;
            }
        }
        return -(first + 1);
    }
