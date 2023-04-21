    private static void quickSort(Vector matches, StorableComparator comparator, int startIndex, int endIndex) {
        if (comparator == null) return;
        if (startIndex < 0 || startIndex >= endIndex || endIndex >= matches.size()) return;
        int middleIndex = (startIndex + endIndex) / 2;
        Storable pivot = (Storable) matches.elementAt(middleIndex);
        swap(matches, middleIndex, endIndex);
        int pivotIndex = startIndex;
        for (int i = startIndex; i <= endIndex - 1; i++) {
            if (comparator.terminate()) return;
            int result = comparator.compare(pivot, ((Storable) matches.elementAt(i)));
            if (result >= 0) {
                swap(matches, pivotIndex, i);
                pivotIndex++;
            }
        }
        swap(matches, pivotIndex, endIndex);
        quickSort(matches, comparator, startIndex, pivotIndex - 1);
        quickSort(matches, comparator, pivotIndex + 1, endIndex);
    }
